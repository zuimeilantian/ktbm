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
}
