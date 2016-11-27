package com.danze.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.danze.utils.PropUtils;

public class OpenIdFilter {
	
	public static String getOpenId() {
		HttpServletRequest request = RequestFilter.threadLocalRequest.get();
		HttpServletResponse response = RequestFilter.threadLocalResponse.get();
		Object obj = request.getSession().getAttribute("openid");
		obj = "oRo78s1dW1paF0_7vGVrdNHBOARQ";
		if (obj == null) {
			try {
				System.out.println("OpenIdFilter openid is null");
				String urlStr = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
						+ PropUtils.getProp("wechat.appid")
						+ "&redirect_uri="
						+ PropUtils.getProp("wechat.domain")
						+ "/getPower&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
				response.sendRedirect(urlStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return obj == null?"":obj.toString();
	}
	
	public static void setOpenId(String openid) {
		HttpServletRequest request = RequestFilter.threadLocalRequest.get();
		request.getSession().setAttribute("openid", openid);
	}
}
