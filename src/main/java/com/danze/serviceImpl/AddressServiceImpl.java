package com.danze.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.danze.dao.CircleDAO;
import com.danze.service.AddressService;

public class AddressServiceImpl implements AddressService{

	@Resource
	private CircleDAO dao;
	
	@Override
	public Map<String, Object> getAddress(String userId) throws RuntimeException {
		Map<String, Object> res = new HashMap<String, Object>();
		String sql = "select * from ktbm_address where cusId = ?";
		dao.getList(sql, new Object[]{userId});
		List<Map<String, Object>> lists = dao.getList(sql, null);
		String sqlCount = "select distinct * from ktbm_good";
		Integer count = dao.getCount(sqlCount, null);
		res.put("list", lists);
		res.put("count", count==null?0:count);
		return res;
	}

	
}
