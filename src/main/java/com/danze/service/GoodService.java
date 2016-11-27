package com.danze.service;

import java.util.Map;

import com.danze.utils.JsonResult;

public interface GoodService {
	
	/**
	 * 获取所有商品
	 * JZY
	 * @return
	 * @throws RuntimeException
	 * 2016年11月8日
	 */
	public Map<String, Object> getGoods(Map<String, Object> map)throws RuntimeException;

	/**
	 * 兑换商品
	 * JZY
	 * @param openid
	 * @param count
	 * @param id
	 * @return
	 * 2016年11月10日
	 */
	public JsonResult byGoods(Map<String, Object> list) throws RuntimeException;

	/**
	 * 兑换记录
	 * JZY
	 * @return
	 * 2016年11月10日
	 */
	public Map<String, Object> getGoodsRecord(Map<String, Object> map);

}
