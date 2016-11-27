package com.danze.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;
import com.danze.filter.OpenIdFilter;
import com.danze.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService{

	@Resource
	private CircleDAO dao;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Map<String, Object> getAddress(String userId) throws RuntimeException {
		Map<String, Object> res = new HashMap<String, Object>();
//		String sql = "select * from ktbm_address where cusId = ? and statue =0 order by isDefault desc ";
		String sql = "select a.id as id,a.province as provinceId,a.city as cityId,a.district as districtId ,ifnull(t1.name,'') as province,ifnull(t2.name,'') as city,ifnull(t3.name,'') as district ,ifnull(detail,'') as detail ,isDefault,statue from ktbm_address a " +
					"left join t_address t1 on a.province = t1.id " +
					"left join t_address t2 on a.city = t2.id " +
					"left join t_address t3 on a.district = t3.id " +
					"where cusId = ? and statue =0 order by isDefault desc ";
		List<Map<String, Object>> lists = dao.getList(sql, new Object[]{userId});
//		String sqlCount = "select count(*) from (select distinct * from ktbm_address  where cusId = ? and statue =0 )a";
		String sqlCount = "select count(*) from (select a.id as id,ifnull(t1.name,'') as province,ifnull(t2.name,'') as city,ifnull(t3.name,'') as district ,ifnull(detail,'') as detail ,isDefault,statue from ktbm_address a " +
					"left join t_address t1 on a.province = t1.id " +
					"left join t_address t2 on a.city = t2.id " +
					"left join t_address t3 on a.district = t3.id " +
					"where cusId = ? and statue =0 order by isDefault desc ) a ";
		Integer count = dao.getCount(sqlCount, new Object[]{userId});
		res.put("list", lists);
		res.put("count", count==null?0:count);
		return res;
	}

	@Override
	public boolean addAddress(Map<String, Object> map) throws RuntimeException {
		try {
			String openid = OpenIdFilter.getOpenId();
			map.put("cusId", openid);
			map.put("statue", 0);
			Object id = map.get("id");
			if(id==null){
				dao.NamedCUDHoldId("ktbm_address", map);
			}
			else {
				String sql = "update ktbm_address set province = :province,city=:city,district=:district,detail =:detail where id = :id";
				dao.NamedCUD(sql, map);
			}
			return true;
		} catch (Exception e) {
			log.error(e.toString());
		}
//		String del = "delete from ktbm_address where cusId = ?";
//		dao.CUD(del, new Object[]{openid});
//		if(CollectionUtils.notEmpty(list)){
//			for (int i = 0; i < list.size(); i++) {
//				Map<String, Object> m = list.get(i);
//				String def = MapUtils.getString(m, "isDefault", "0");
//				if(def.equals("1")){
//					String sql = "update ktbm_address set isDefault = 0 where cusId =  ? ";
//					dao.CUD(sql, new Object[]{openid});
//					break;
//				}
//			}
//  			for (int i = 0; i < list.size(); i++) {
//				Map<String, Object> m = list.get(i);
//				m.put("cusId", openid);
//				dao.NamedCUDHoldId("ktbm_address", m);
//			}
//		}
		return false;
	}
	
	@Override
	public boolean addAddress2(Map<String, Object> map) throws RuntimeException {
		try {
			String openid = OpenIdFilter.getOpenId();
			map.put("cusId", openid);
			map.put("statue", 0);
			Object id = map.get("id");
			if(id==null){
				dao.NamedCUDHoldId("ktbm_address", map);
			}
			else {
				String sql = "update ktbm_address set province = :province,city=:city,district=:district,detail =:detail where id = :id";
				dao.NamedCUD(sql, map);
			}
			return true;
		} catch (Exception e) {
			log.error(e.toString());
		}
		return false;
	}
	

	@Override
	public boolean delAddress(Map<String, Object> map) {
		String id = map.get("id").toString();
		try {
			String sql = "update ktbm_address set statue = 1 where id = ?";
			dao.CUD(sql, new Object[]{id});
			return true;
		} catch (Exception e) {
			log.error(e.toString());
			return false;
		}
	}

	@Override
	public boolean setDefaultAddress(Map<String, Object> map) {
		try {
			String id = map.get("id").toString();
			String sql = "update ktbm_address set isDefault = 0 where statue = 0 and cusId = ?  ";
			dao.CUD(sql, new Object[]{OpenIdFilter.getOpenId()});
			sql = "update ktbm_address set isDefault = 1 where id = ?  ";
			dao.CUD(sql, new Object[]{id});
			return true;
		} catch (Exception e) {
			log.error(e.toString());
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> getAddressList(String level,String parent_id) {
		String sql = "select a.* from t_address a where a.status=1 ";
		if(level!=null||!"".equals(level)){
			sql += " and a.level='"+level+"' ";
		}
		if(parent_id!=null||!"".equals(parent_id)){
			sql += " and a.parent_id='"+parent_id+"' ";
		}
		sql += " order by a.ordernum ";
		return dao.getList(sql, null);
	}

	@Override
	public boolean updateAddress2(Map<String, Object> map) {
		try {
			String sql = "update ktbm_address set province = :provinceId,city=:cityId,district=:districtId,detail =:detail where id = :id";
			dao.NamedCUD(sql, map);
			return true;
		} catch (Exception e) {
			log.error(e.toString());
		}
		return false;
	}

	
}
