package com.danze.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danze.service.GoodService;
import com.danze.utils.JsonResult;

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
	@ResponseBody
	public Map<String, Object> getGoods(@RequestBody Map<String, Object> map){
		Map<String, Object> m = new HashMap<String, Object>();
		m = goodService.getGoods(map);
		return m;
	}
	
	/**
	 * 兑换商品
	 * JZY
	 * @param map
	 * @return
	 * 2016年11月8日
	 */
	@RequestMapping("/byGoods")
	@ResponseBody
	public JsonResult byGoods(@RequestBody Map<String, Object> list){
		JsonResult jsonResult = new JsonResult(true);
		jsonResult= goodService.byGoods(list);
		return jsonResult;
	}
	
	
	/**
	 * 获取商品 兑换记录
	 * JZY
	 * @param map
	 * @return
	 * 2016年11月8日
	 */
	@RequestMapping("/getGoodsRecord")
	@ResponseBody
	public Map<String, Object> getGoodsRecord(@RequestBody Map<String,Object> map){
		Map<String, Object> rep = new HashMap<String, Object>();
		rep = goodService.getGoodsRecord(map);
		return rep;
	}
	
	
}
