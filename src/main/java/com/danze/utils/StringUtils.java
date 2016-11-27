package com.danze.utils;

import java.util.ArrayList;
import java.util.List;


public class StringUtils {

	/**
	 * jzy
	 * 是否为空
	 * @param source
	 * @return
	 * 2016年7月28日
	 */
	public static boolean notEmpty(Object source){
		return !(source == null || "".equals(source.toString()) || "".equals(source.toString().trim()));
	}

	public static List<String> string2list(Object obj) {
		List<String> ls = new ArrayList<String>();
		if(obj!=null){
			String[] strings = obj.toString().split(",");
			if(strings!=null){
				for(String s:strings){
					ls.add(s.toString());
				}
			}
		}
		return ls;
	}
}
