package com.danze.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapUtils {

	//日期格式 yyyy-MM-dd
	public static final String DATE = "yyyy-MM-dd";
	//日期格式 yyyy-MM-dd HH:mm:ss
	public static final String TIME = "yyyy-MM-dd HH:mm:ss";
	//开始时间
	public static final boolean START = true;
	//结束时间
	public static final boolean END = false;
	
	/**
	 * 获取字符串
	 * jzy
	 * 2016年9月22日
	 * @param map
	 * @param key
	 * @return
	 * String
	 */
	public static String getString(Map<String, Object> map,String key){
		String value = map.get(key)==null?"":map.get(key).toString();
		return value;
	}
	
	/**
	 * 
	 * jzy
	 * 2016年9月29日
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return
	 * String
	 */
	public static String getString(Map<String, Object> map,String key,String defaultValue){
		String value = map.get(key)==null?defaultValue:map.get(key).toString();
		return value;
	}
	
	/**
	 * 获取
	 * jzy
	 * 2016年9月22日
	 * @param map
	 * @param key
	 * @param integer 默认值
	 * @return
	 * Integer
	 */
	public static Integer getInteger(Map<String, Object> map,String key,Integer integer){
		if(map.get(key)!=null){
			try {
				integer = Integer.parseInt(map.get(key).toString());
			} catch (Exception e) {
				
			}
		}
		return integer;
	}
	
	public static List<Integer> getIntesgerList(Map<String, Object> map,String key){
		List<Integer> s = new ArrayList<Integer>();
		
		
		return s;
	}
	
	
	/**
	 * 
	 * jzy
	 * 2016年9月22日
	 * @param map
	 * @param key
	 * @param type 日期格式
	 * @param flag true 开始 false 结束
	 * @return
	 * String
	 */
	public static String getDate(Map<String, Object> map,String key,String type,boolean flag){
		String time = getString(map, key);
		// 没值
		if (time.equals("")) {
			if (flag) {
				time = "1870-01-01 00:00:00";
			} else {
				time = "2870-01-01 23:59:59";
			}
		}
		// 有
		else {
			if (type.equals(DATE)) {
				if (time.length() > 10) {
					time = time.substring(0, 10);
				}
			} else if (type.equals(TIME)) {
				if (time.length() == 10) {
					if (flag) {
						time = time + " 00:00:00";
					} else {
						time = time + "23:59:59";
					}
				}
			}
		}
		System.out.println(key + ":" + time);
		return time;
	}
	
	public  static String getAmbiguousString(Map<String, Object> map,String key){
		String value = getString(map, key); 
		value = "%" + value + "%";
		return value;
	}
	
	
}
