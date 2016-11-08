package com.danze.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.danze.service.CustomerService;

@Controller
@RequestMapping("/cus")
public class CustomerCtrl {

	@Resource
	private CustomerService customerService;
	
	@RequestMapping("/getCusInfo")
	public Map<String, Object> getCusInfo(){
		Map<String, Object> m = new HashMap<String, Object>();
		String userId = "";
		m = customerService.getCusInfo(userId);
		return m;
	}
	
	@RequestMapping("/saveCusInfo")
	public Map<String, Object> saveCusInfo(){
		Map<String, Object> m = new HashMap<String, Object>();
		String userId = "";
		m = customerService.getCusInfo(userId);
		return m;
	}
	
}
