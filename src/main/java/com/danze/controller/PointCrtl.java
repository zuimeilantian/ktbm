package com.danze.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danze.service.PointService;

@Controller
@RequestMapping("/ktbm/pointCrtl")
public class PointCrtl {
	
	@Resource
	private PointService pointService;

	/**
	 * 获得积分策略信息List(分页)
	 * @return
	 */
	@RequestMapping("/getPointsMethodPageList")
	public @ResponseBody Map<String,Object> getPointsMethodPageList(@RequestBody Map<String, Object> pointsmethodsch){
		return pointService.getPointsMethodPageList(pointsmethodsch);
	}
	
	/**
	 * 保存、修改积分策略信息List(分页)
	 * @return
	 */
	@RequestMapping("/savePointsMethod")
	public @ResponseBody Map<String,Object> savePointsMethod(@RequestBody Map<String, Object> dataMap){
		return pointService.savePointsMethod(dataMap);
	}
	
	/**
	 * 根据订单id获得积分策略信息
	 * @return
	 */
	@RequestMapping("/getPointsMethodMap")
	public @ResponseBody Map<String,Object> getPointsMethodMap(@RequestParam(value = "id", required = true)String id){
		return pointService.getPointsMethodMap(id);
	}
	
	/**
	 * 获得积分
	 * @return
	 */
	@RequestMapping("/getPoints")
	public @ResponseBody Integer getPoints(@RequestBody Map<String, Object> dataMap){
		return pointService.getPoints(dataMap);
	}
	
	/**
	 * 根据订单id作废积分策略信息
	 * @return
	 */
	@RequestMapping("/delPointsMethod")
	public @ResponseBody Map<String,Object> delPointsMethod(@RequestParam(value = "id", required = true)String id){
		return pointService.delPointsMethod(id);
	}
	
	/**
	 * 获得积分记录信息List(分页)
	 * @return
	 */
	@RequestMapping("/getPointsPageList")
	public @ResponseBody Map<String,Object> getPointsPageList(@RequestBody Map<String, Object> pointssch){
		return pointService.getPointsPageList(pointssch);
	}
	
	/**
	 * 统计积分信息List(分页)
	 * @return
	 */
	@RequestMapping("/getTjPointsPageList")
	public @ResponseBody Map<String,Object> getTjPointsPageList(@RequestBody Map<String, Object> pointssch){
		return pointService.getTjPointsPageList(pointssch);
	}
	
	/**
	 * 操作积分
	 * @return
	 */
	@RequestMapping("/savePoints")
	public @ResponseBody Map<String,Object> savePoints(@RequestBody Map<String, Object> pointsMap){
		return pointService.savePoints(pointsMap);
	}
}
