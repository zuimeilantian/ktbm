package com.danze.service;

import java.util.List;
import java.util.Map;

public interface AddressService {
	
	/**
	 * 获取地址列表
	 * JZY
	 * @return
	 * 2016年11月14日
	 */
	public Map<String, Object> getAddress(String userId) throws RuntimeException;

	/**
	 * 添加地址列表
	 * JZY
	 * @return
	 * 2016年11月14日
	 */
	public boolean addAddress(Map<String, Object> map) throws RuntimeException;
	
	/**
	 * 添加地址列表
	 * JZY
	 * @return
	 * 2016年11月14日
	 */
	public boolean addAddress2(Map<String, Object> map) throws RuntimeException;


	/**
	 * 删除地址
	 * JZY
	 * @param map
	 * @return
	 * 2016年11月14日
	 */
	public boolean delAddress(Map<String, Object> map);

	/**
	 * 设置默认地址
	 * JZY
	 * @param map
	 * @return
	 * 2016年11月14日
	 */
	public boolean setDefaultAddress(Map<String, Object> map);

	/**
	 * 省，市，区
	 * JZY
	 * @param level
	 * @param parent_id
	 * @return
	 * 2016年11月16日
	 */
	public List<Map<String, Object>> getAddressList(String level, String parent_id);

	/**
	 * 修改地址
	 * JZY
	 * @param map
	 * @return
	 * 2016年11月23日
	 */
	public boolean updateAddress2(Map<String, Object> map);
	
}
