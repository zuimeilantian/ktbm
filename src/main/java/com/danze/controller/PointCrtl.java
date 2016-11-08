package com.danze.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("point")
public class PointCrtl {

	@RequestMapping("getPoints")
	public Map<String, Object> getPoints(){
		Map<String, Object> m= new HashMap<String, Object>();
		return m;
	}
}
