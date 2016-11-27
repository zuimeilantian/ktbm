package com.danze.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.danze.utils.PropUtils;

public class RequestFilter implements Filter {
	
	public static ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal<HttpServletRequest>();
	public static ThreadLocal<HttpServletResponse> threadLocalResponse = new ThreadLocal<HttpServletResponse>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) resp;
	    
	    // 不过滤的uri
 		String[] notFilterUris = new String[] {"/css","/doc","/images","/img","/js","/lib","/templates","/upload", "/wechat/check","/openid","/getPower"};
 		// 获取请求的url
 		String uri = request.getRequestURI();
 		System.out.println("RequestFilter.uri:"+uri);
 		// true表示过滤,false表示不过滤
 		boolean doFilter = true;
 		for (String notFilterUri : notFilterUris) {
 			if (uri.contains(notFilterUri)) {
 				doFilter = false;
 				break;
 			}
 		}
	    if(doFilter){
	    	Object obj = request.getSession().getAttribute("openid");
	    	obj = "oRo78s1dW1paF0_7vGVrdNHBOARQ";
			if (obj == null) {
				try {
			    	System.out.println("RequestFilter openid is null");
					String urlStr = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
							+ PropUtils.getProp("wechat.appid")
							+ "&redirect_uri="
							+ PropUtils.getProp("wechat.domain")
							+ "/getPower&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
					System.out.println(urlStr);
					response.sendRedirect(urlStr);
					return ;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
	    }
	    threadLocalRequest.set(request);
    	threadLocalResponse.set(response);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}
