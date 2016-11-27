package com.danze.service;

import javax.servlet.http.HttpServletRequest;

public interface WeChatService {

	/**
	 * 请求消息类型：事件
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * 事件类型：subscribe(关注)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消关注)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";
	
	/**
	 * 事件类型：VIEW(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_VIEW = "VIEW";

	public String processRequest(HttpServletRequest request);
}
