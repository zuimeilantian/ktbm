package com.danze.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;
import com.danze.service.PointService;

@Service
public class PointServiceImpl implements PointService{

	@Resource
	private CircleDAO dao;
	
	@Override
	public Map<String, Object> getPoints() throws RuntimeException {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select * ktbm_points_method where type = 1";
		
		
		return map;
	}

	
}
