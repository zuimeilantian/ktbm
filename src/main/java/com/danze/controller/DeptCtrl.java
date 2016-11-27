package com.danze.controller;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danze.service.DeptService;

@Controller
@RequestMapping("/dept")
public class DeptCtrl {

	@Resource
	private DeptService deptService;
	
	/**
	 * 获取分公司
	 * JZY
	 * @return
	 * 2016年11月23日
	 */
	@RequestMapping("getDept")
	@ResponseBody
	public List<Map<String, Object>> getDept(){
		List<Map<String, Object>> list = deptService.getDept();
		return list;
	}
}
