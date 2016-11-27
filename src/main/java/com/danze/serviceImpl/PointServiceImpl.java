package com.danze.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;
import com.danze.filter.OpenIdFilter;
import com.danze.service.PointService;
import com.danze.utils.BaseService;
import com.danze.utils.MapUtils;

@Service
public class PointServiceImpl extends BaseService implements PointService{

	@Resource
	private CircleDAO dao;
	
	/* 获得积分策略信息List(分页)
	 * @see com.danze.service.PointService#getPointsMethodPageList(java.util.Map)
	 */
	@Override
	public Map<String, Object> getPointsMethodPageList(Map<String, Object> pointsmethodsch) {
		Map<String, Object> map = new HashMap<String, Object>();
		String methodType = this.getStringParam(pointsmethodsch,"methodType");//积分方式 1:扫码 2：评价
		String methodObject = this.getStringParam(pointsmethodsch,"methodObject");//积分对象 1：员工 2：客户
		Integer page = getIntegerParam(pointsmethodsch,"page");
		Integer size = getIntegerParam(pointsmethodsch,"size");
		Integer offset = page*size;
		String sql = "select a.*,ifnull(b.name,'') lastModifiedName,getdicyvalue(a.methodType,'methodType') methodTypeName,"
				       + "          getdicyvalue(a.methodObject,'methodObject') methodObjectName "
				       + " from ktbm_points_method a,user b where a.lastModifiedBy=b.id and a.status=1 ";
		if(!"".equals(methodType)){
			sql += " and a.methodType='"+methodType+"' ";
		}
		if(!"".equals(methodObject)){
			sql += " and a.methodObject='"+methodObject+"' ";
		}
		map.put("count", dao.getList(sql, null).size());//总记录数
		sql += " order by a.lastModifiedTime desc limit "+size+" offset "+offset;
		map.put("datalist",  dao.getList(sql, null));//订单列表数据
		return map;
	}

	/* 保存、修改积分策略信息List(分页)
	 * @see com.danze.service.PointService#savePointsMethod(java.util.Map)
	 */
	@Override
	public Map<String, Object> savePointsMethod(Map<String, Object> dataMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			String id = this.getStringParam(dataMap, "id");//id
			String methodType = this.getStringParam(dataMap, "methodType");//积分方式 1:扫码 2：评价
			String methodObject = this.getStringParam(dataMap, "methodObject");//积分对象 1：员工 2：客户
			Integer scanFirst = this.getIntegerParam(dataMap, "scanFirst");//一级扫码积分
			Integer scanSecond = this.getIntegerParam(dataMap, "scanSecond");//二级扫码积分
			Integer evaluate = this.getIntegerParam(dataMap, "evaluate");//评价积分
			String operId  = "";//操作人
			String operTime  = this.getToday("yyyy-MM-dd HH:mm:ss");//操作时间
			String sql = "";
			if(!"".equals(id)){//修改
				sql = "update ktbm_points_method a "
					 + "       set methodType=?,methodObject=?,scanFirst=?,scanSecond=?,evaluate=?,lastModifiedBy=?,lastModifiedTime=? "
			         + " where a.id=? ";
				dao.CUD(sql, new Object[]{methodType,methodObject,scanFirst,scanSecond,evaluate,operId,operTime,id});
			}else{//新增
				int cnt = 0;
				//验证积分规则是否存在
				sql = "select count(1) from ktbm_points_method a where a.methodType=? and a.methodObject=? and a.status=1 ";
				cnt= dao.getCount(sql, new Object[]{methodType,methodObject});
				if(cnt>0){
					map.put("success", false);
					map.put("msg", "此积分策略已存在，无法新增！");
					return map;
				}
				//保存数据
				sql = "insert into ktbm_points_method(methodType,methodObject,scanFirst,scanSecond,evaluate,status,"
					 + "createdBy,createdTime,lastModifiedBy,lastModifiedTime) "
			         + "values(?,?,?,?,?,1,?,?,?,?)";
				dao.CUD(sql, new Object[]{methodType,methodObject,scanFirst,scanSecond,evaluate,operId,operTime,operId,operTime});
			}
			map.put("success", true);
			map.put("msg", "保存成功！");
		}catch(Exception e){
			map.put("success", false);
			map.put("msg", "保存失败！");
		}
		return map;
	}

	/* 根据订单id获得积分策略信息
	 * @see com.danze.service.PointService#getPointsMethodMap(java.lang.String)
	 */
	@Override
	public Map<String, Object> getPointsMethodMap(String id) {
		String sql = "select a.*,getdicyvalue(a.methodType,'methodType') methodTypeName,"
				       + "          getdicyvalue(a.methodObject,'methodObject') methodObjectName "
				       + "  from ktbm_points_method a where a.id=? ";
		return dao.getMap(sql, new Object[]{id});
	}
	
	/* 获得积分
	 * @see com.danze.service.PointService#getPointsMethodMap(java.lang.String)
	 */
	@Override
	public Integer getPoints(Map<String,Object> dataMap) {
		String id = this.getStringParam(dataMap, "id");//id
		String methodType = this.getStringParam(dataMap, "methodType");//积分方式 1:扫码 2：评价
		String columnName = this.getStringParam(dataMap, "columnName");//scanFirst:一级扫码积分、scanSecond:二级扫码积分、evaluate:评价积分
		String sql = "select ifnull(a."+columnName+",'0') points from ktbm_points_method a where a.status=1 ";
		if(!"".equals(id)){
			sql += " and a.id='"+id+"' ";
		}
		if(!"".equals(methodType)){
			sql += " and a.methodType='"+methodType+"' ";
		}
		return dao.getCount(sql, null);
	}

	/* 根据订单id作废积分策略信息
	 * @see com.danze.service.PointService#delPointsMethod(java.lang.String)
	 */
	@Override
	public Map<String, Object> delPointsMethod(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			String sql = "";
			//获取订单信息
			sql = "update ktbm_points_method a set a.status=0 where a.id=? ";
			dao.CUD(sql, new Object[]{id});
			map.put("success", true);
			map.put("msg", "删除成功！");
		}catch(Exception e){
			map.put("success", false);
			map.put("msg", "删除失败！");
		}
		return map;
	}

	/* 获得积分记录信息List(分页)
	 * @see com.danze.service.PointService#getPointsPageList(java.util.Map)
	 */
	@Override
	public Map<String, Object> getPointsPageList(Map<String, Object> pointssch) {
		Map<String, Object> map = new HashMap<String, Object>();
		String flag = this.getStringParam(pointssch,"flag");//1：个体 2:其他
		String type = this.getStringParam(pointssch,"type");//积分类型  1：员工 2 客户
		String source = this.getStringParam(pointssch,"source");//积分来源  1：评价 2：一级分销扫码 3：二级分销扫码
		String objectName = this.getStringParam(pointssch,"objectName");//对象名称
		String userId = this.getStringParam(pointssch,"userId");//员工id
		String cusId = "";//客户openid
		if("1".equals(flag)){
			cusId =  OpenIdFilter.getOpenId();//客户idopenid
		}else{
			cusId =  this.getStringParam(pointssch,"cusId");//客户openid
		}
		String beginDate = this.getStringParam(pointssch,"beginDate");//开始日期 yyyy-MM-dd
		String endDate = this.getStringParam(pointssch,"endDate");//结束日期 yyyy-MM-dd
		Integer page = getIntegerParam(pointssch,"page");
		Integer size = getIntegerParam(pointssch,"size");
		Integer offset = page*size;
		String sql = "select * from ("
				   + "select a.*,getdicyvalue(a.type,'points_type') typeName,getdicyvalue(a.source,'points_source') sourceName,"
				   + "         getusername(a.userId) userName,getcusnameopenid(a.cusId) cusName,"
				   + "         (case when a.userId is null or a.userId='' then getcusnameopenid(a.cusId) else getusername(a.userId) end) objectName,"
				   + "         getcusnameopenid(a.wechatId) wechatName "
				   + "  from ktbm_points a) t where 1=1 ";
		if(!"".equals(type)){
			sql += " and t.type='"+type+"' ";
		}
		if(!"".equals(source)){
			sql += " and t.source='"+source+"' ";
		}
		if(!"".equals(userId)){
			sql += " and t.userId='"+userId+"' ";
		}
		if(!"".equals(cusId)){
			sql += " and t.cusId='"+cusId+"' ";
		}
		if(!"".equals(objectName)){
			sql += " and t.objectName like '%"+objectName+"%' ";
		}
		if(!"".equals(beginDate)){
			sql += " and DATE_FORMAT(a.createTime,'%Y-%m-%d')>='"+beginDate+"' ";
		}
		if(!"".equals(endDate)){
			sql += " and DATE_FORMAT(a.createTime,'%Y-%m-%d')<='"+endDate+"' ";
		}
		map.put("count", dao.getList(sql, null).size());//总记录数
		sql += " order by t.createTime desc limit "+size+" offset "+offset;
		map.put("datalist",  dao.getList(sql, null));//订单列表数据
		return map;
	}

	/* 统计积分信息List(分页)
	 * @see com.danze.service.PointService#getTjPointsPageList(java.util.Map)
	 */
	@Override
	public Map<String, Object> getTjPointsPageList(Map<String, Object> pointssch) {
		Map<String, Object> map = new HashMap<String, Object>();
		String objectName = this.getStringParam(pointssch,"objectName");//对象名称
		String beginDate = this.getStringParam(pointssch,"beginDate");//开始日期 yyyy-MM-dd
		String endDate = this.getStringParam(pointssch,"endDate");//结束日期 yyyy-MM-dd
		Integer page = getIntegerParam(pointssch,"page");
		Integer size = getIntegerParam(pointssch,"size");
		Integer offset = page*size;
		String sql = "select t.objectName,t.typeName,sum((case when t.source=4 then -1*t.point else t.point end)) points from ("
				   + "select a.*,getdicyvalue(a.type,'points_type') typeName,getdicyvalue(a.source,'points_source') sourceName,"
				   + "       getusername(a.userId) userName,getcusnameopenid(a.cusId) cusName,"
				   + "       (case when a.userId is null or a.userId='' then getcusnameopenid(a.cusId) else getusername(a.userId) end) objectName,"
				   + "       getcusnameopenid(a.wechatId) wechatName "
				   + "  from ktbm_points a) t ";
		if(!"".equals(objectName)){
			sql += " and t.objectName like '%"+objectName+"%' ";
		}
		if(!"".equals(beginDate)){
			sql += " and DATE_FORMAT(a.createTime,'%Y-%m-%d')>='"+beginDate+"' ";
		}
		if(!"".equals(endDate)){
			sql += " and DATE_FORMAT(a.createTime,'%Y-%m-%d')<='"+endDate+"' ";
		}
		sql += " group by t.objectName,t.typeName ";
		map.put("count", dao.getList(sql, null).size());//总记录数
		sql += " order by t.typeName,t.objectName limit "+size+" offset "+offset;
		map.put("datalist",  dao.getList(sql, null));//订单列表数据
		return map;
	}

	/* 操作积分
	 * @see com.danze.service.PointService#savePoints(java.util.Map)
	 */
	@Override
	public Map<String, Object> savePoints(Map<String, Object> pointsMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			String type = this.getStringParam(pointsMap, "type");//积分类型  1：员工 2 客户
			String source = this.getStringParam(pointsMap, "source");//积分来源  1：评价 2：一级分销扫码 3：二级分销扫码 4:兑换
			Integer point = this.getIntegerParam(pointsMap, "point");//积分数量
			String userId = this.getStringParam(pointsMap, "userId");//员工id
			String cusId = this.getStringParam(pointsMap, "cusId");//客户Id
			String wechatId = this.getStringParam(pointsMap, "wechatId");//扫码人或评价人openId
			String orderId = this.getStringParam(pointsMap, "orderId");//订单id
			String operId  ="";//操作人
			String operTime  = this.getToday("yyyy-MM-dd HH:mm:ss");//操作时间
			String sql = "";
			List<String> sqls = new ArrayList<String>();
			//保存数据
			if("1".equals(type)){
				//插入员工获得的积分记录信息
				sql = "insert into ktbm_points(type,source,point,userId,createTime,wechatId) "
				    + "values('"+type+"','"+source+"','"+point+"','"+userId+"','"+operTime+"','"+wechatId+"')";
				sqls.add(sql);
				//更新员工总积分
				if("4".equals(source)){
					sql = "update user a set a.points=(a.points-"+point+") where a.id='"+userId+"' ";
				}else{
					sql = "update user a set a.points=(a.points+"+point+") where a.id='"+userId+"' ";
				}
				sqls.add(sql);
			}else{
				//插入客户获得的积分记录信息
				sql = "insert into ktbm_points(type,source,point,cusId,createTime,wechatId,orderId) "
				    + "values('"+type+"','"+source+"','"+point+"','"+cusId+"','"+operTime+"','"+wechatId+"','"+orderId+"')";
				sqls.add(sql);
				//更新客户总积分
				if("4".equals(source)){
					sql = "update ktbm_customer a set a.points=(a.points-"+point+") where a.openId='"+cusId+"' ";
				}else{
					sql = "update ktbm_customer a set a.points=(a.points+"+point+") where a.openId='"+cusId+"' ";
				}
				sqls.add(sql);
			}
			dao.batchUpdate(ListZhArray(sqls));
			map.put("success", true);
			map.put("msg", "保存成功！");
		}catch(Exception e){
			map.put("success", false);
			map.put("msg", "保存失败！");
		}
		return map;
	}

	@Override
	public Map<String, Object> getPointRecord(Map<String, Object> map) {
		String openid = OpenIdFilter.getOpenId();
		int limit = MapUtils.getInteger(map, "limit", 10);
		int offset = MapUtils.getInteger(map, "offset", 0);
		offset = limit * offset;
		String sqlCount = "select count(*) from (select r.amount,r.phone,r.address,r.exchangeTime,g.name,CONCAT(\"/upload/files/\",f.name)" +
					 " as url from ktbm_record r left join ktbm_goods g on r.goodId = g.id " +
					 "left JOIN _file f on g.fileid = f.id  where cusId = ? )a";
		Integer count = dao.getCount(sqlCount, new Object[]{openid});
		
		String sql = "select r.amount,r.phone,r.address,r.exchangeTime,g.name,CONCAT(\"/upload/files/\",f.name)" +
				 " as url from ktbm_record r left join ktbm_goods g on r.goodId = g.id " +
				 "left JOIN _file f on g.fileid = f.id  where cusId = ? limit ? offset ?";
		List<Map<String, Object>> list = dao.getList(sql, new Object[]{openid , limit ,offset});
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("count", count ==null?0:count);
		m.put("list", list);
		return m;
	}
	
}
