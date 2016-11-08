package com.danze.utils;

import java.util.Random;


public class CodeUtils {

	
	public static String getCode(){
		String code = "";
		for (int i = 0; i < 6; i++) {
			code += new Random().nextInt(10);
		}
		System.out.println(code);
		return code;
	}
}
