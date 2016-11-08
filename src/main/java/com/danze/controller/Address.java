package com.danze.controller;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.danze.service.AddressService;


@Controller
@RequestMapping("/address")
public class Address {

	@Resource
	private AddressService addressService;
	
	@RequestMapping("/getAddress/userId")
	public Map<String, Object> getAddress(@PathVariable("userId") String userId){
		Map<String, Object> map = new HashMap<String, Object>();
		map = addressService.getAddress(userId);
		return map;
	}
}
