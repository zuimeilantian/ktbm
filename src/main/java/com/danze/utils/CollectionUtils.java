package com.danze.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public class CollectionUtils {

	
	public static boolean notEmpty(Collection con){
		return !(con == null || con.isEmpty());
	}
	
	public static boolean notEmpty(Map<?, ?> map){
		return !(map == null || map.isEmpty());
	}
	
	public static boolean isEmpty(Collection con){
		return (con == null || con.isEmpty());
	}
	
	public static boolean isEmpty(Map<?, ?> map){
		return (map == null || map.isEmpty());
	}
	
	public static String list2String(Object list){
		String resutl="";
		if(list!=null){
			try {
				List ls = (List)list;
				if(notEmpty(ls)){
					for(int i=0;i<ls.size();i++){
						resutl +=(ls.get(i).toString() +",");
					}
				}
				if(!resutl.equals("")){
					resutl = resutl.substring(0,resutl.length()-1);
				}
			} catch (Exception e) {
				System.out.println("utils.CollectionUtils：数据类型不一致");
			}
		}else{
			return resutl;
		}
		return resutl;
	}
	
	
}
