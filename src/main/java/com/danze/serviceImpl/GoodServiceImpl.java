package com.danze.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;
import com.danze.filter.OpenIdFilter;
import com.danze.service.GoodService;
import com.danze.utils.DateUtils;
import com.danze.utils.JsonResult;
import com.danze.utils.MapUtils;
import com.danze.utils.MsgUtils;

@Service
public class GoodServiceImpl implements GoodService{

	@Resource
	private CircleDAO dao;
	
	@Value("#{configProperties['oa.fileurl']}")
	private String url;

	
	@Override
	public Map<String, Object> getGoods(Map<String, Object> map) throws RuntimeException {
		Map<String, Object> res = new HashMap<String, Object>();
		int limit = MapUtils.getInteger(map, "limit",10);
		int offset = MapUtils.getInteger(map, "offset",0);
		offset = limit * offset;
		String sql = "select kg.fileid, kg.id,kg.name,kg.amount,kg.point,kg.des,kg.type,CONCAT(SUBSTR(beginTime,1,13),'点') as beginTime,CONCAT(SUBSTR(endTime,1,13),'点') as endTime,CONCAT('"+(url==null?"":url)+"/',f.name) as url " + 
				" from ktbm_goods kg left join _file f on kg.fileid = f.id where statue = 0 limit " + limit + " offset " + offset;
		List<Map<String, Object>> lists = dao.getList(sql, null);
		String sqlCount = "select count(*) from (select kg.fileid,kg.id,kg.name,kg.amount,kg.point,kg.des,kg.type, beginTime, endTime,CONCAT('/upload/files/',f.name) as url " + 
					" from ktbm_goods kg left join _file f on kg.fileid = f.id where statue = 0 ) a";
		Integer count = dao.getCount(sqlCount, null);
		res.put("list", lists);
		res.put("count", count==null?0:count);
		return res;
	}

	@Override
	public synchronized JsonResult byGoods(Map<String, Object> mapTotal) {
		JsonResult jsonResult = new JsonResult();
		String phone = MapUtils.getString(mapTotal, "phone");
		if (!MsgUtils.validPhone(phone)) {
			jsonResult.setFail("请填写合法手机号");
			return jsonResult;
		}
		if (MapUtils.getString(mapTotal, "address").trim().equals("")) {
			jsonResult.setFail("收获地址不能为空");
			return jsonResult;
		}
		String openid = OpenIdFilter.getOpenId();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) mapTotal.get("goods");

		String sqlPoint = "select points from ktbm_customer where openid = ?";
		Object c = dao.getObject(sqlPoint, new Object[] { openid });
		int total = Integer.parseInt(c == null ? "0" : c.toString());
		int tempPoint = 0;
		String err = "";

		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String sql = "select amount,point,name,statue,beginTime,endTime from ktbm_goods where id = ?";
			Map<String, Object> m = dao.getMap(sql,new Object[] { map.get("id") });
			String statue = m.get("statue").toString();
			if (statue.equals("1")) {
				err += (m.get("name") + "已被删除，不能兑换，");
				continue;
			}
			String type = MapUtils.getString(m, "type", "1");
			if (type.equals("2")) {
				String beginTime = MapUtils.getString(m, "beginTime");
				String endTime = MapUtils.getString(m, "endTime");
				if (!(DateUtils.beforeNow(beginTime) && DateUtils.afterNow(endTime))) {
					err += (m.get("name") + "不在有效期内，不能兑换，");
					continue;
				}
			}
			int count = 0;
			Object obj = map.get("count");
			if (obj == null) {
				err += (m.get("name") + "没有兑换数量");
				continue;
			} else {
				try {
					count = Integer.parseInt(obj.toString());
				} catch (Exception e) {
					err += (m.get("name") + "一次兑换不能超过10个");
					continue;
				}
			}
			int amount = MapUtils.getInteger(m, "amount", 0);
			int point = MapUtils.getInteger(m, "point", 0);
			if (amount < count) {
				err += (m.get("name") + "数量不足，");
			}
			tempPoint += count * point;
		}
		if (err.length() == 0) {
			if (total < tempPoint) {
				err += "您的积分不足";
			}
		}
		if (err.length() != 0) {
			jsonResult.setFail(err);
			return jsonResult;
		} else {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				int count = MapUtils.getInteger(map, "count", 0);
				String id = MapUtils.getString(map, "id", "");
				String sql = "select amount,point,name from ktbm_goods where id = ?";
				Map<String, Object> m = dao.getMap(sql,new Object[] { map.get("id") });
				int point = MapUtils.getInteger(m, "point", 0);
				sql = "update ktbm_goods set amount = amount - ? where id = ?";
				dao.CUD(sql, new Object[] { count, id });
				sql = "insert into ktbm_points(type,source,point,cusId,createTime) values(?,?,?,?,?)";
				dao.CUD(sql,new Object[] { 2, 4, point, openid,DateUtils.getNowDate("yyyy-MM-dd HH:mm:ss") });
				sql = "insert into ktbm_record(goodId,type,cusId,address,phone,exchangeTime,amount,point) values(?,?,?,?,?,?,?,?)";
				dao.CUD(sql,new Object[] { id, 2, openid, mapTotal.get("address"),mapTotal.get("phone"),DateUtils.getNowDate("yyyy-MM-dd HH:mm:ss"),count ,point});
				sql = "update ktbm_customer set points = points - ? where openid = ?";
				dao.CUD(sql, new Object[] { count * point, openid });
			}
		}
		jsonResult.setSuccess("兑换成功");
		return jsonResult;
	}

	@Override
	public Map<String, Object> getGoodsRecord(Map<String, Object> map) {
		String openid = OpenIdFilter.getOpenId();
		int limit = MapUtils.getInteger(map, "limit", 10);
		int offset = MapUtils.getInteger(map, "offset", 0);
		offset = limit * offset;
		String sqlCount = "select count(*) from (select r.amount,r.phone,r.point,r.address,r.exchangeTime,g.name,CONCAT(\"/upload/files/\",f.name)" +
					 " as url from ktbm_record r left join ktbm_goods g on r.goodId = g.id " +
					 "left JOIN _file f on g.fileid = f.id  where cusId = ? )a";
		Integer count = dao.getCount(sqlCount, new Object[]{openid});
		
		String sql = "select r.amount,r.phone,r.address,r.point,r.exchangeTime,g.name,CONCAT(\"/upload/files/\",f.name)" +
				 " as url from ktbm_record r left join ktbm_goods g on r.goodId = g.id " +
				 "left JOIN _file f on g.fileid = f.id  where cusId = ? limit ? offset ?";
		List<Map<String, Object>> list = dao.getList(sql, new Object[]{openid , limit ,offset});
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("count", count ==null?0:count);
		m.put("list", list);
		return m;
	}

	
}
