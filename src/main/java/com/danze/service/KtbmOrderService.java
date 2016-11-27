package com.danze.service;

import java.util.List;
import java.util.Map;

/**
 * 空调保姆订单管理
 * @author ws
 */
public interface KtbmOrderService{
	
	Map<String, Object> getKtbmOrderPageList(Map<String, Object> ktbmOrdersch);

	Map<String, Object> saveKtbmOrder(Map<String, Object> dataMap);

	Map<String, Object> dealKtbmOrder(Map<String, Object> dataMap);

	Map<String, Object> getKtbmOrderMap(String id);

	Map<String, Object> delKtbmOrder(String id);

	Map<String, Object> saveEvaluation(Map<String, Object> dataMap);

	/**
	 * 获取品牌或机型
	 * JZY
	 * @return
	 * 2016年11月16日
	 */
	List<Map<String, Object>> getBoundType(String key);
	
}
