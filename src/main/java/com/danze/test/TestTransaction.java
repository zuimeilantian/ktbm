package com.danze.test;

import javax.annotation.Resource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danze.dao.CircleDAO;
import com.danze.service.testService;

@Controller
public class TestTransaction {

	@Resource
	CircleDAO dao;
	
	@Resource
	testService t;
	
	@RequestMapping("tstTransaction")
	@ResponseBody
	public String test(){
		t.test();
			return "3123";
	}
}
