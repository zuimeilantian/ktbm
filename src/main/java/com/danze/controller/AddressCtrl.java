package com.danze.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danze.filter.OpenIdFilter;
import com.danze.service.AddressService;

@Controller
@RequestMapping("/address")
public class AddressCtrl {

	@Resource
	private AddressService addressService;

	/**
	 * 获取地址列表
	 * JZY
	 * @return
	 * 2016年11月14日
	 */
	@RequestMapping("/getAddress")
	@ResponseBody
	public Map<String, Object> getAddress() {
		String openid = OpenIdFilter.getOpenId();
		Map<String, Object> map = new HashMap<String, Object>();
		map = addressService.getAddress(openid);
		return map;
	}

	
	/**
	 * 添加地址列表
	 * JZY
	 * @return
	 * 2016年11月14日
	 */
	@RequestMapping("addAddress")
	@ResponseBody
	public boolean addAddress(@RequestBody Map<String, Object> map) {
		boolean flag = true;
		try {
			flag = addressService.addAddress(map);
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 添加地址列表
	 * JZY
	 * @return
	 * 2016年11月14日
	 */
	@RequestMapping("addAddress2")
	@ResponseBody
	public boolean addAddress2(@RequestBody Map<String, Object> map) {
		boolean flag = true;
		try {
			flag = addressService.addAddress2(map);
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	
	/**
	 * 修改地址列表
	 * JZY
	 * @return
	 * 2016年11月14日
	 */
	@RequestMapping("updateAddress2")
	@ResponseBody
	public boolean updateAddress2(@RequestBody Map<String, Object> map) {
		boolean flag = true;
		try {
			flag = addressService.updateAddress2(map);
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	
	

	/**
	 * 删除地址
	 * JZY
	 * @param map
	 * @return
	 * 2016年11月14日
	 */
	@RequestMapping("delAddress")
	@ResponseBody
	public boolean delAddress(@RequestBody Map<String, Object> map) {
		boolean flag = true;
		try {
			flag = addressService.delAddress(map);
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 设置默认地址
	 * JZY
	 * @param map
	 * @return
	 * 2016年11月14日
	 */
	@RequestMapping("setDefaultAddress")
	@ResponseBody
	public boolean setDefaultAddress(@RequestBody Map<String, Object> map) {
		boolean flag = true;
		try {
			flag = addressService.setDefaultAddress(map);
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	
	@RequestMapping("/getAddressList")
	@ResponseBody
	public List<Map<String,Object>> getAddressList(@RequestParam(value = "level", required = true)String level,@RequestParam(value = "parent_id", required = true)String parent_id){
		List<Map<String, Object>> list = addressService.getAddressList(level,parent_id);
		return list;
	}

}
