package com.danze.service;

import com.danze.utils.JsonResult;

public interface MsgService {

	
	/**
	 * 发送短信
	 * JZY
	 * @param phone
	 * @param userId
	 * @return
	 * @throws RuntimeException
	 * 2016年11月8日
	 */
	public String sendMsg(String phone,String userId) throws RuntimeException;

	/**
	 * 短信注册
	 * JZY
	 * @param phone
	 * @param code
	 * @param user
	 * @return
	 * @throws RuntimeException
	 * 2016年11月8日
	 */
	public JsonResult register(String phone, String code, String user) throws RuntimeException;

	/**
	 * 校验是否绑定手机号
	 * JZY
	 * @param wecharId
	 * @return
	 * @throws RuntimeException
	 * 2016年11月8日
	 */
	public JsonResult getPhone(String wecharId) throws RuntimeException;
}
