/**
 * DateUtils.java
 * 董春滔 (mailto://dct@njdanze.com)
 * 2016年3月28日 上午9:03:11
 */
package com.danze.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public final class DateUtils {
	
	/**
	 * 格式化日期  yyyy-MM-dd
	 */
	public static final String DATE = "yyyy-MM-dd";
	
	/**
	 * 格式化日期  yyyy-MM-dd HH:mm:ss
	 */
	public static final String TIME = "yyyy-MM-dd HH:mm:ss";

	private DateUtils(){}
	
	public static String date2String(String pattern,Date date){
		String sDate = null;
		if(null == pattern || "".equals(pattern)){
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sDate = sdf.format(date);
		return sDate;
	}
	
	public static List<String> getMonthsOfCurrentYear(){
		List<String> months = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		for(int i = 1;i <= 12;i++){
			String tMon = String.valueOf(i);
			if(10 > i){
				tMon = "0" + tMon;
			}
			String fullMon = year + "-" + tMon;
			months.add(fullMon);
		}
		return months;
	}
	
	public static String getCurrentFullMonth(){
		String fullMonth = null;
		Calendar calendar = Calendar.getInstance();
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		fullMonth = year + "-";
		int month = calendar.get(Calendar.MONTH) + 1;
		if(10 > month){
			fullMonth += "0";
		}
		fullMonth += month;
		return fullMonth;
	}
	
	public static Long getDaysBetween(Date start,Date end){
		Long days = 0L;
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(start);
		endCal.setTime(end);
		Long startTimeMillis = startCal.getTimeInMillis();
		Long endTimeMillis = endCal.getTimeInMillis();
		days = Math.abs(startTimeMillis - endTimeMillis) / (3600 * 24 * 1000);
		return days;
	}
	
	
	public static String calendar2String(String pattern,Calendar  calendar){
		return date2String(pattern, calendar.getTime());
	}
	
	public static Date string2Date(String pattern,String source){
		if(null == pattern || "".equals(pattern)){
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = format.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getNextDate(String now, String unit,String count) {
		
		//System.out.println(now);
		Date date = DateUtils.string2Date("yyyy-MM-dd HH:mm:ss", now); 
		int type = Calendar.DAY_OF_MONTH;
		if(unit.equals("秒")){
			type = Calendar.SECOND;
		}else if(unit.equals("分")){
			type = Calendar.MINUTE;
		}else if(unit.equals("时")){
			type = Calendar.HOUR_OF_DAY;
		}else if(unit.equals("天")){
			type = Calendar.DAY_OF_MONTH;
		}else if(unit.equals("周")){
			type = Calendar.WEEK_OF_YEAR;
		}else if(unit.equals("月")){
			type = Calendar.MONTH;
		}else if(unit.equals("年")){
			type = Calendar.YEAR;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int c = 0;
		try {
			c = Integer.parseInt(count);
		} catch (Exception e) {
		}
		calendar.add(type, c);
		return calendar2String("yyyy-MM-dd HH:mm:ss", calendar);
	}
	
	public static String getTime(String time,String way,String beforeday,String unit) {
		if(!beforeday.startsWith("-")){
			beforeday = "-" + beforeday;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = (String) time;
		//到期提醒
		try {
			long t = Long.parseLong(time);
			Date date = new Date(t);
			result = format.format(date);
		} catch (Exception e) {
			if (time.length()==10) {
				result =time + " 03:00:00";
			}
			try {
				format.parse(time);
			} catch (ParseException e1) {
				result = "";
			}
		}
		//无效的时间格式
		if(result.equals("")){
			return result;
		}
		if(way.equals("0")){
			result = DateUtils.getNextDate(result, "天", beforeday);
		}
		//周期提醒
		else if(way.equals("1")){
			if(unit.equals("")){
				return "";
			}
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH);
			int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			if(unit.equals("年")){
				result = result.substring(4); // -03-03
				result = year + result;
			}else if(unit.equals("月")){
				result = result.substring(7); // -03
				result = year + "-" + month + result;
			}else if(unit.equals("天")){
				result = year + "-" + month + "-" + day; //
			}
			if(result.length()==10){
				result += " 03:00:00";
			}
			result = DateUtils.getNextDate(result, "天", beforeday);
		}
		return result;
	}
	
	public static boolean beforeNow(String time){
		boolean flag = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			long t = format.parse(time).getTime();
			if(t>new Date().getTime()){
				flag = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public static boolean afterNow(String time){
		return !beforeNow(time);
	}
	
	/**
	 * 
	 * jzy
	 * 2016年9月29日
	 * @param oldDate
	 * @param newDate
	 * @return
	 * boolean
	 */
	public static boolean beforeDate(String oldDate,String newDate){
		boolean flag = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			long t1 = format.parse(oldDate).getTime();
			long t2 = format.parse(newDate).getTime();
			if(t1>t2){
				flag = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
	
	/**
	 * 时间是否在同一天
	 * jzy
	 * 2016年10月20日
	 * @param day
	 * @return
	 * boolean
	 */
	public static boolean sameDay(String day){
		boolean flag = true;
		if(day.length()==10){
			day = day + " 00:00:00";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar now = Calendar.getInstance();
		try {
			Date d = format.parse(day);
			now.setTime(d);
		} catch (ParseException e) {
			flag = false;
			e.printStackTrace();
		}
		Calendar c1 =Calendar.getInstance();
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.add(Calendar.SECOND, -1);
		
		Calendar c2 =Calendar.getInstance();
		c2.set(Calendar.SECOND, 59);
		c2.set(Calendar.MINUTE, 59);
		c2.set(Calendar.HOUR_OF_DAY, 23);
		
		if(!(c1.before(now)&&now.before(c2))){
			flag = false;
		}
		return flag;
	}
	
	
	/**
	 * 时间是否在同一天
	 * jzy
	 * 2016年10月20日
	 * @param day
	 * @return
	 * boolean
	 */
	public static boolean remindDay(String start,String end){
		boolean flag = true;
		if(start.length()>=10){
			start = start.substring(0,10) + " 00:00:00";
		}
		if(end.length()>=10){
			end = end.substring(0,10) + " 23:59:59";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar now = Calendar.getInstance();
		Date sDate = null;
		Date eDate = null;
		try {
			sDate = format.parse(start);
			eDate = format.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar c1 =Calendar.getInstance();
		c1.setTime(sDate);
		c1.add(Calendar.SECOND, -1);
		
		Calendar c2 =Calendar.getInstance();
		c2.setTime(eDate);
		
		if(!(c1.before(now)&&now.before(c2))){
			flag = false;
		}
		return flag;
	}

	public static String getNowDate(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String v = format.format(new Date());
		return v;
	}

	public static String formatDate(String time, String pattern) {
		String value = "";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		if(pattern.equals(DATE)){
			if(time.length()>10){
				time = time.substring(0,10);
			}
		}
		else if(pattern.equals(TIME)){
			if(time.length() == 10){
				time = time + " 05:00:00";
			}
		}
		try {
			Date d = format.parse(time);
			value = format.format(d);
		} catch (ParseException e) {
			value = "";
		}
		
		return value;
	}
}
