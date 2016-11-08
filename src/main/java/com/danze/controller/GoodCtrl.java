package com.danze.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.danze.service.GoodService;

@Controller
@RequestMapping("/good")
public class GoodCtrl {

	@Resource
	private GoodService goodService;
	
	/**
	 * 商品列表
	 * JZY
	 * @param map
	 * @return
	 * 2016年11月8日
	 */
	@RequestMapping("/getGoods")
	public Map<String, Object> getGoods(@RequestBody Map<String, Object> map){
		Map<String, Object> m = new HashMap<String, Object>();
		m = goodService.getGoods(map);
		return m;
	}
	
	/**
	 * 购买
	 * JZY
	 * @param map
	 * @return
	 * 2016年11月8日
	 */
	@RequestMapping("/byGoods")
	public Map<String, Object> byGoods(@RequestBody Map<String, Object> map){
		return new HashMap<String, Object>();
	}
	
}
