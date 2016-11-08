package com.danze.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;
import com.danze.service.GoodService;
import com.danze.utils.MapUtils;

@Service
public class GoodServiceImpl implements GoodService{

	@Resource
	private CircleDAO dao;
	
	@Override
	public Map<String, Object> getGoods(Map<String, Object> map) throws RuntimeException {
		Map<String, Object> res = new HashMap<String, Object>();
		String limit = MapUtils.getString(map, "limit","10");
		String offset = MapUtils.getString(map, "offset","0");
		String sql = "select distinct * from ktbm_good limit " + limit + " offset " + offset;
		List<Map<String, Object>> lists = dao.getList(sql, null);
		String sqlCount = "select distinct * from ktbm_good";
		Integer count = dao.getCount(sqlCount, null);
		res.put("list", lists);
		res.put("count", count==null?0:count);
		return res;
	}

	
}
