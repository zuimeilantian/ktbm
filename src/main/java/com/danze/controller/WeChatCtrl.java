package com.danze.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danze.service.WeChatService;

@Controller
@RequestMapping("/wechat")
public class WeChatCtrl {

	@Resource
	private WeChatService weChatService;
	
	@ResponseBody()
	@RequestMapping("/attention")
	public String attention(HttpServletRequest req){
		String value = "";
		value = "";
		return value;
	}
}
