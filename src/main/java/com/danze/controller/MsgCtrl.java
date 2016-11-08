package com.danze.controller;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danze.service.MsgService;
import com.danze.utils.JsonResult;
import com.danze.utils.MapUtils;

@Controller
@RequestMapping("/msg")
public class MsgCtrl {

	@Resource
	private MsgService msgService;

	
	/**
	 * 发送短信
	 * JZY
	 * @param map
	 * @return
	 * 2016年11月8日
	 */
	@RequestMapping("/sendMsg")
	@ResponseBody
	public JsonResult sendMsg(@RequestBody Map<String, Object> map) {
		JsonResult jsonResult = new JsonResult(true);
		String user = "123";
		try {
			String phone = MapUtils.getString(map, "phone", "");
			String regExp = "^[1][3,4,5,8][0-9]{9}$";  
			Pattern p = Pattern.compile(regExp);  
			Matcher m = p.matcher(phone);  
			if (m.find()) {
				//msgService.sendMsg(phone, user);
			}else{
				jsonResult.setFail("手机号不合法");
				return jsonResult;
			}
		} catch (Exception e) {
			jsonResult.setFail("系统异常，请稍后再试。");
			e.printStackTrace();
		}
		return jsonResult;
	}
	
	
	/**
	 * 短信注册
	 * JZY
	 * @param map
	 * @return
	 * 2016年11月8日
	 */
	@RequestMapping("/register")
	@ResponseBody
	public JsonResult register(@RequestBody Map<String, Object> map) {
		JsonResult jsonResult = new JsonResult(true);
		try {
			String phone = MapUtils.getString(map, "phone", "");
			String regExp = "^[1][3,4,5,8][0-9]{9}$";  
			Pattern p = Pattern.compile(regExp);  
			Matcher m = p.matcher(phone);  
			if (!m.find()) {
				jsonResult.setFail("手机号不合法");
				return jsonResult;
			}
			String user = "123";
			//
			String code = MapUtils.getString(map, "code", "");
			if(code.equals("")){
				jsonResult.setFail("没有验证码");
				return jsonResult;
			}
			jsonResult = msgService.register(phone,code,user);
		} catch (Exception e) {
			jsonResult.setFail("系统异常，请稍后再试。");
			e.printStackTrace();
		}
		return jsonResult;
	}
	
	/**
	 * 校验是否绑定手机号
	 * JZY
	 * @param map
	 * @return
	 * 2016年11月8日
	 */
	@RequestMapping("/getPhone")
	@ResponseBody
	public JsonResult getPhone(@RequestBody Map<String, Object> map) {
		JsonResult jsonResult = new JsonResult(true);
		try {
			String wecharId = MapUtils.getString(map, "wecharId", "");
			if(wecharId.equals("")){
				jsonResult.setFail("？？？");
				return jsonResult;
			}
			jsonResult = msgService.getPhone(wecharId);
		} catch (Exception e) {
			jsonResult.setFail("系统异常，请稍后再试。");
			e.printStackTrace();
		}
		
		return jsonResult;
	}
	
	
	
}
