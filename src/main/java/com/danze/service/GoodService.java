package com.danze.service;

import java.util.List;
import java.util.Map;

public interface GoodService {
	
	/**
	 * 获取所有商品
	 * JZY
	 * @return
	 * @throws RuntimeException
	 * 2016年11月8日
	 */
	public Map<String, Object> getGoods(Map<String, Object> map)throws RuntimeException;
}
