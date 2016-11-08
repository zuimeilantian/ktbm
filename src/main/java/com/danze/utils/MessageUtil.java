package com.danze.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class MessageUtil {

	public static Map<String, String> parseXml(HttpServletRequest request) {
		
		Map<String, String> map = new HashMap<String, String>();
		//BufferedInputStream bis = new BufferedInputStream();
		BufferedReader bf = null;
		String result = "";
		try {
			bf = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = "";
			while ((line = bf.readLine()) != null) {
				result += line + "\r\n";
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		finally {
			try {
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(result);
		return map;
	}

}
