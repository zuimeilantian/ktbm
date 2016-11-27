package com.danze.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;
import com.danze.exception.MsgException;
import com.danze.filter.OpenIdFilter;
import com.danze.service.CustomerService;
import com.danze.utils.MsgUtils;
import com.danze.utils.CollectionUtils;
import com.danze.utils.JsonResult;
import com.danze.utils.MapUtils;
import com.danze.utils.StringUtils;

@Service
public class CustomerServiceImpl implements CustomerService{

	@Resource
	CircleDAO dao;
	
	@Value("#{configProperties['wechat.appid']}")
	private String appid;
	@Value("#{configProperties['wechat.secret']}")
	private String wesecret;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public Map<String, Object> getCusInfo(String userId) throws MsgException{
		Map<String, Object> m = new HashMap<String, Object>();
		String sql = "select c.*,g.name as defaultGroupName, CONCAT('upload/files/',ifnull(f.name,'defaltIcon.png')) as iconUrl from ktbm_customer c left join _file f on f.id = iconId left join groups g on c.defaultGroup = g.id  where openId = ? ";
		m = dao.getMap(sql, new Object[]{userId});
		if(m.get("url")==null||m.get("url").toString().equals("")){
			String url = MsgUtils.getCodePic(userId,appid,wesecret,dao);
			m.put("url", url);
		}
		String city = MapUtils.getString(m, "localCity", "");
		List<String> localCity = StringUtils.string2list(city);
		m.put("localCity", localCity);
		sql = "select distinct *, CONCAT(ifnull(province,''),ifnull(city,''),ifnull(district,''),ifnull(detail,'')) as address from ( " +
				"select a.id as id,cusId,a.province as provinceId,a.city as cityId ,a.district as districtId,ifnull(t1.name,'') as province,ifnull(t2.name,'') as city,ifnull(t3.name,'') as district ,ifnull(detail,'') as detail ,isDefault,statue from ktbm_address a " +
				"left join t_address t1 on a.province = t1.id " +
				"left join t_address t2 on a.city = t2.id "+
				"left join t_address t3 on a.district = t3.id)b  where cusId = ? and statue = 0 order by isDefault desc";
		List<Map<String, Object>> address = dao.getList(sql, new Object[]{userId});
		m.put("address", address);
		return m;
	}


	@Override
	public JsonResult saveCusInfo(Map<String, Object> m) throws RuntimeException {
		JsonResult jsonResult = new JsonResult(true);
		try {
			dao.NamedCUDHoldId("ktbm_customer", m);
		} catch (Exception e) {
			log.error(e.toString());
			jsonResult.setFail("系统异常，请稍后再试");
		}
		return jsonResult;
	}


	@Override
	public boolean isRegister() {
		String sql = "select phone from ktbm_customer where openId = ?";
		String  openid = OpenIdFilter.getOpenId();
		System.out.println("验证是否注册:" + openid);
		Object obj =dao.getObject(sql, new Object[]{openid});
		if(obj==null){
			return false;
		}
		else{
			String phone = obj.toString();
			if (!MsgUtils.validPhone(phone)) {
				return false;
			}
		}
		return true;
	}


	@Override
	public JsonResult updateCusInfo(Map<String, Object> m) {
		String openid = OpenIdFilter.getOpenId();
		JsonResult js = new JsonResult(true);
//		String phone = MapUtils.getString(m, "phone", "");
//		if(phone.equals("")){
//			js.setFail("手机号不能为空");
//			return js;
//		}else{
//			String sql = "select phone from ktbm_customer where cusId = ?";
//			Object obj = dao.getObject(sql,new Object[]{openid});
//			if(!phone.toString().equals(obj==null?"":obj.toString())){
//				sql = "select phone from ktbm_message where openid = ?";
//				obj = dao.getObject(sql,new Object[]{openid});
//				if(!phone.toString().equals(obj==null?"":obj.toString())){
//					js.setFail("手机号不是注册的手机号");
//					return js;
//				}
//			}
//		}
		OpenIdFilter.getOpenId();
		
		Set<String> set = m.keySet();
		List<String> keys = new ArrayList<String>(set);
		for(int i=0;i<keys.size();i++){
			try {
				String key = keys.get(i);
				if(key.equals("name")){
					String sql = "update ktbm_customer set name = ? where openid = ?";
					dao.CUD(sql, new Object[]{m.get(key),openid});
				}else if(key.equals("localCity")){
					@SuppressWarnings("unchecked")
					List<Map<String, Object>> list = (List<Map<String, Object>>) m.get("localCity");
					String localCity = CollectionUtils.list2String(list,",");
					String sql = "update ktbm_customer set localCity = ? where openid = ?";
					dao.CUD(sql, new Object[]{localCity,openid});
				}else if(key.equals("company")){
					String sql = "update ktbm_customer set company = ? where openid = ?";
					dao.CUD(sql, new Object[]{m.get(key),openid});
				}else if(key.equals("defaultGroup")){
					if(m.get(key)==null){
						String sql = "update ktbm_customer set defaultGroup = null where openid = ?";
						dao.CUD(sql, new Object[]{openid});
					}else{
						String sql = "update ktbm_customer set defaultGroup = ? where openid = ?";
						dao.CUD(sql, new Object[]{m.get(key),openid});
					}
				}
			} catch (Exception e) {
				log.error(e.toString());
				e.printStackTrace();
			}
		}
		return js;
	}


	@Override
	public void saveOrUpdateIcon(String iconId) throws Exception{
		String openid = OpenIdFilter.getOpenId();
		String sql = "update ktbm_customer set iconId = ? where openid = ?";
		dao.CUD(sql, new Object[]{iconId,openid});
	}

}
