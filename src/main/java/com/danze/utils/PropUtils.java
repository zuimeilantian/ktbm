package com.danze.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropUtils {

	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String getProp(String key){
		String value = "";
		InputStream is = PropUtils.class.getClassLoader().getResourceAsStream("application.properties");
		Properties prop = new Properties();
		try {
			prop.load(is);
			value = prop.getProperty(key);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}
}
