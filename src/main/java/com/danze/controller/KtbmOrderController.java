package com.danze.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danze.serviceImpl.KtbmOrderImpl;

/**
 * 空调保姆订单管理
 * @author ws
 */
@Controller
@RequestMapping("/ktbm/ktbmOrderController")
public class KtbmOrderController {
	private KtbmOrderImpl ktbmOrderImpl;
	
	/**
	 * 获得订单信息List(分页)
	 * @return
	 */
	@RequestMapping("/getKtbmOrderPageList")
	public @ResponseBody Map<String,Object> getKtbmOrderPageList(@RequestBody Map<String, Object> ktbmOrdersch){
		return ktbmOrderImpl.getKtbmOrderPageList(ktbmOrdersch);
	}
	
	/**
	 * 保存订单信息
	 * @return
	 */
	@RequestMapping("/saveKtbmOrder")
	public @ResponseBody Map<String,Object> saveKtbmOrder(@RequestBody Map<String, Object> dataMap){
		return ktbmOrderImpl.saveKtbmOrder(dataMap);
	}
	
	/**
	 * 处理订单信息
	 * @return
	 */
	@RequestMapping("/dealKtbmOrder")
	public @ResponseBody Map<String,Object> dealKtbmOrder(@RequestBody Map<String, Object> dataMap){
		return ktbmOrderImpl.dealKtbmOrder(dataMap);
	}
	
	/**
	 * 根据订单id获得订单信息
	 * @return
	 */
	@RequestMapping("/getKtbmOrderMap")
	public @ResponseBody Map<String,Object> getKtbmOrderMap(@RequestParam(value = "id", required = true)String id){
		return ktbmOrderImpl.getKtbmOrderMap(id);
	}
	
	/**
	 * 根据订单id作废订单信息
	 * @return
	 */
	@RequestMapping("/delKtbmOrder")
	public @ResponseBody Map<String,Object> delKtbmOrder(@RequestParam(value = "id", required = true)String id){
		return ktbmOrderImpl.delKtbmOrder(id);
	}
	
}
