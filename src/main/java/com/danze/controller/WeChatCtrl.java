package com.danze.controller;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.danze.service.WeChatService;
import com.danze.utils.HttpUtils;

@Controller
@RequestMapping("/wechat")
public class WeChatCtrl {

	@Resource
	private WeChatService weChatService;
	
	/*
	{
	    "target_type" : "users", // users 给用户发消息。chatgroups: 给群发消息，chatrooms: 给聊天室发消息
	    "target" : ["u1", "u2", "u3"], // 注意这里需要用数组，数组长度建议不大于20，即使只有一个用户，
	                                   // 也要用数组 ['u1']，给用户发送时数组元素是用户名，给群组发送时  
	                                   // 数组元素是groupid
	    "msg" : {
	        "type" : "txt",
	        "msg" : "hello from rest" //消息内容，参考[[start:100serverintegration:30chatlog|聊天记录]]里的bodies内容
	        },
	    "from" : "jma2" //表示消息发送者。无此字段Server会默认设置为"from":"admin"，有from字段但值为空串("")时请求失败
	}
	*/
	
	
	@ResponseBody
	@RequestMapping("/sendMsg")
	public String sendMsg(String msgs){
		String urlStr = "https://a1.easemob.com/33oa/oa/messages";
		String method = "POST";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("target_type", "users");
		List<String> target = new ArrayList<String>();
		target.add("jzy");
		map.put("target", target);
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("type", "txt");
		msg.put("msg", "12332313131312");
		map.put("msg", msg);
		String paramJSON = JSON.toJSONString(map);
		System.out.println(paramJSON);
		
		URL url;
		HttpURLConnection httpURLConnection = null;
		try {
			url = new URL(urlStr);
			httpURLConnection = (HttpURLConnection) url.openConnection();
		    httpURLConnection.setDoInput(true);
		    httpURLConnection.setDoOutput(true);        // 设置该连接是可以输出的
		    httpURLConnection.setRequestMethod(method); // 设置请求方式
		    httpURLConnection.setRequestProperty("charset", "utf-8");
		    httpURLConnection.setRequestProperty("Content-Type","application/json");
		    httpURLConnection.setRequestProperty("Authorization","Bearer YWMtOrgHJKTDEeav7o1L9o_ZPAAAAAAAAAAAAAAAAAAAAAFiKe4gensR5qENMej_g0cAAgMAAAFYPeEOGQBPGgBlQpeuwRfBL2rIctvd21JElQhf7pFYJ8sn8FRmU8NbmQ");
		    httpURLConnection.connect();
		    
	        //POST请求
	        DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
	        //{"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": "123"}}}
			out.writeBytes(paramJSON);
	        out.flush();
	        out.close();
		} catch (Exception e) {
			
		}
		Map<String, Object> res = HttpUtils.closeConnection(httpURLConnection);
		return res.toString();
	}
}
