package com.danze.serviceImpl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;
import com.danze.service.testService;

@Service
public class testServiceImpl implements testService{

	
	@Resource
	CircleDAO dao;
	
	@Override
	public String test() {
		String sql = "insert into ktbm_test(name,age) values('name','123')";
		dao.CUD(sql,null);
		
		sql = "insert into ktbm_test(names,age) values('names','123')";
		dao.CUD(sql,null);
		return "tstTransaction";
	}

}
