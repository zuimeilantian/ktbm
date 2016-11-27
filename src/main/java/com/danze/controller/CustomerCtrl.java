package com.danze.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danze.filter.OpenIdFilter;
import com.danze.service.CustomerService;
import com.danze.utils.JsonResult;

@Controller
@RequestMapping("/cus")
public class CustomerCtrl {

	@Resource
	private CustomerService customerService;
	
	
	/**
	 * 获取客户信息
	 * JZY
	 * @return
	 * 2016年11月14日
	 */
	@RequestMapping("/getCusInfo")
	@ResponseBody
	public Map<String, Object> getCusInfo(){
		Map<String, Object> m = new HashMap<String, Object>();
		String userId = OpenIdFilter.getOpenId();
		m = customerService.getCusInfo(userId);
		return m;
	}
	
	/**
	 * 
	 * JZY
	 * @return
	 * 2016年11月14日
	 */
	@RequestMapping("/saveCusInfo")
	public Map<String, Object> saveCusInfo(){
		Map<String, Object> m = new HashMap<String, Object>();
		String userId = "";
		m = customerService.getCusInfo(userId);
		return m;
	}
	
	/**
	 * 判断是否注册
	 * JZY
	 * @return
	 * 2016年11月14日
	 */
	@RequestMapping("/isRegister")
	@ResponseBody
	public boolean isRegister(){
		return customerService.isRegister();
	}
	
	
	/**
	 * 修改用户信息
	 * JZY
	 * @param m
	 * @return
	 * 2016年11月14日
	 */
	@RequestMapping("/updateCusInfo")
	@ResponseBody
	public JsonResult updateCusInfo(@RequestBody Map<String,Object> m){
		JsonResult js =  customerService.updateCusInfo(m);
		return js;
	}
	
	/**
	 * 修改头像
	 * JZY
	 * @param file
	 * @param request
	 * @return
	 * 2016年11月22日
	 */
	@RequestMapping("/saveOrUpdateIcon/{iconId}")
	@ResponseBody
	public JsonResult saveOrUpdateIcon(@PathVariable("iconId") String iconId){
		JsonResult js = new JsonResult();
		js.setSuccess("修改成功");
		try {
			customerService.saveOrUpdateIcon(iconId);
		} catch (Exception e) {
			js.setFail("系统异常，修改失败");
		}
		return js;
	}
}
