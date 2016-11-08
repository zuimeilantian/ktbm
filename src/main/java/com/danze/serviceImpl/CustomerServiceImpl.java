package com.danze.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;
import com.danze.exception.MsgException;
import com.danze.service.CustomerService;
import com.danze.utils.JsonResult;

@Service
public class CustomerServiceImpl implements CustomerService{

	@Resource
	CircleDAO dao;
	
	
	public Map<String, Object> getCusInfo(String userId) throws MsgException{
		Map<String, Object> m = new HashMap<String, Object>();
		String sql = "select * from ktbm_customer where wechatId = ?";
		m = dao.getMap(sql, new Object[]{userId});
		return m;
	}


	@Override
	public JsonResult saveCusInfo(Map<String, Object> m) throws RuntimeException {
		JsonResult jsonResult = new JsonResult(true);
		try {
			dao.NamedCUDHoldId("ktbm_customer", m);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setFail("系统异常，请稍后再试");
		}
		return jsonResult;
	}
	
}
