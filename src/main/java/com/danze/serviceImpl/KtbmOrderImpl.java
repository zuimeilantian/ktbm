package com.danze.serviceImpl;

import java.util.Map;

public interface KtbmOrderImpl {

	Map<String, Object> getKtbmOrderPageList(Map<String, Object> ktbmOrdersch);

	Map<String, Object> saveKtbmOrder(Map<String, Object> dataMap);

	Map<String, Object> dealKtbmOrder(Map<String, Object> dataMap);

	Map<String, Object> getKtbmOrderMap(String id);

	Map<String, Object> delKtbmOrder(String id);

}
