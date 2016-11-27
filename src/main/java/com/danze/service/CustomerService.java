package com.danze.service;

import java.util.Map;

import com.danze.utils.JsonResult;

public interface CustomerService {

	/**
	 * 获取用户信息
	 * JZY
	 * @param userId
	 * @return
	 * @throws RuntimeException
	 * 2016年11月8日
	 */
	public Map<String, Object> getCusInfo(String userId) throws RuntimeException;
	
	/**
	 * 修改更新用户信息
	 * JZY
	 * @return
	 * 2016年11月8日
	 */
	public JsonResult saveCusInfo(Map<String, Object> m) throws RuntimeException;

	/**
	 * 是否注册
	 * JZY
	 * @return
	 * 2016年11月10日
	 */
	public boolean isRegister();

	/**
	 * 修改用户信息
	 * JZY
	 * @param m
	 * @return
	 * 2016年11月10日
	 */
	public JsonResult updateCusInfo(Map<String, Object> m);

	/**
	 * 修改头像
	 * JZY
	 * @param file
	 * @param request
	 * @return
	 * 2016年11月22日
	 */
	public void saveOrUpdateIcon(String iconId) throws Exception;

}
