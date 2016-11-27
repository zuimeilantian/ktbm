package com.danze.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danze.service.KtbmOrderService;
import com.danze.utils.MapUtils;

/**
 * 空调保姆订单管理
 * @author ws
 */
@Controller
@RequestMapping("/ktbm/ktbmOrderController")
public class KtbmOrderController {
	@Resource
	private KtbmOrderService ktbmOrderService;
	
	/**
	 * 获得订单信息List(分页)
	 * @return
	 */
	@RequestMapping("/getKtbmOrderPageList")
	public @ResponseBody Map<String,Object> getKtbmOrderPageList(@RequestBody Map<String, Object> ktbmOrdersch){
		return ktbmOrderService.getKtbmOrderPageList(ktbmOrdersch);
	}
	
	/**
	 * 保存订单信息
	 * @return
	 */
	@RequestMapping("/saveKtbmOrder")
	public @ResponseBody Map<String,Object> saveKtbmOrder(@RequestBody Map<String, Object> dataMap){
		return ktbmOrderService.saveKtbmOrder(dataMap);
	}
	
	/**
	 * 处理订单信息
	 * @return
	 */
	@RequestMapping("/dealKtbmOrder")
	public @ResponseBody Map<String,Object> dealKtbmOrder(@RequestBody Map<String, Object> dataMap){
		return ktbmOrderService.dealKtbmOrder(dataMap);
	}
	
	/**
	 * 根据订单id获得订单信息
	 * @return
	 */
	@RequestMapping("/getKtbmOrderMap")
	public @ResponseBody Map<String,Object> getKtbmOrderMap(@RequestParam(value = "id", required = true)String id){
		return ktbmOrderService.getKtbmOrderMap(id);
	}
	
	/**
	 * 根据订单id作废订单信息
	 * @return
	 */
	@RequestMapping("/delKtbmOrder")
	public @ResponseBody Map<String,Object> delKtbmOrder(@RequestParam(value = "id", required = true)String id){
		return ktbmOrderService.delKtbmOrder(id);
	}
	
	/**
	 * 保存评论信息
	 * @return
	 */
	@RequestMapping("/saveEvaluation")
	public @ResponseBody Map<String,Object> saveEvaluation(@RequestBody Map<String, Object> dataMap){
		return ktbmOrderService.saveEvaluation(dataMap);
	}
	
	/**
	 * 获取品牌或机型
	 * JZY
	 * @return
	 * 2016年11月16日
	 */
	@RequestMapping("/getBoundType")
	@ResponseBody
	public List<Map<String, Object>> getBoundType(@RequestBody Map<String,Object> map){
		String key = MapUtils.getString(map, "key");
		List<Map<String, Object>> list = ktbmOrderService.getBoundType(key);
		return list;
	}
	
}
