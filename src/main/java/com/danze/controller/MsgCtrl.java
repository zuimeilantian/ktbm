package com.danze.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danze.filter.OpenIdFilter;
import com.danze.service.MsgService;
import com.danze.utils.JsonResult;
import com.danze.utils.MapUtils;
import com.danze.utils.MsgUtils;

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
		String openid = OpenIdFilter.getOpenId();
		try {
			String phone = MapUtils.getString(map, "phone", "");
			if (MsgUtils.validPhone(phone)) {
				String msg = msgService.sendMsg(phone, openid);
				if(!msg.equals("ok")){
					jsonResult.setFail(msg);
				}
			}else{
				jsonResult.setFail("手机号不合法");
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
	public JsonResult register(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult(true);
		try {
			String phone = MapUtils.getString(map, "phone", "");
			if (!MsgUtils.validPhone(phone)) {
				jsonResult.setFail("手机号不合法");
				return jsonResult;
			}
			String openId = OpenIdFilter.getOpenId();
			String code = MapUtils.getString(map, "code", "");
			if(code.equals("")){
				jsonResult.setFail("没有验证码");
				return jsonResult;
			}
			jsonResult = msgService.register(phone,code,openId);
		} catch (Exception e) {
			jsonResult.setFail("系统异常，请稍后再试。");
			e.printStackTrace();
		}
		return jsonResult;
	}
	
}
