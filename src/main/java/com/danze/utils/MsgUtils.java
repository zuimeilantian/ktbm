package com.danze.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.danze.dao.CircleDAO;

public class MsgUtils{
	
	private static Logger log = LoggerFactory.getLogger(MsgUtils.class);
	
	/**
	 * 获取手机验证码
	 * JZY
	 * @return
	 * 2016年11月11日
	 */
	public static String getCode(){
		String code = "";
		for (int i = 0; i < 6; i++) {
			code += new Random().nextInt(10);
		}
		//System.out.println(code);
		return code;
	}

	
	/**
	 * 验证手机号是否有效
	 * JZY
	 * @param phone
	 * @return
	 * 2016年11月16日
	 */
	public static boolean validPhone(String phone){
		String regExp = "^[1][3,4,5,7,8][0-9]{9}$";  
		Pattern p = Pattern.compile(regExp);  
		Matcher m = p.matcher(phone);  
		return m.find();
	}
	
	public static String getCodePic(String openid,String appid,String secret,CircleDAO dao) {
		String urlpic = "";
		
		String ticket = getTicket(openid, appid, secret,dao);
		urlpic = ticket.equals("")? null:( "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket);
		return urlpic;
	}
	
	
	public static String  getTicket(String openid,String appid,String secret,CircleDAO dao){
		String ticket = "";
		BufferedReader br =null;
		HttpURLConnection httpURLConnection = null;
		try {
			
			String token = getAccessToken( openid, appid, secret,dao);
			String urlStr = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + token;
			URL url = new URL(urlStr);
			httpURLConnection = (HttpURLConnection) url.openConnection();
		    httpURLConnection.setDoInput(true);
		    httpURLConnection.setDoOutput(true);        // 设置该连接是可以输出的
		    httpURLConnection.setRequestMethod("POST"); // 设置请求方式
		    httpURLConnection.setRequestProperty("charset", "utf-8");
		    httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		    httpURLConnection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            //{"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": "123"}}}
			out.writeBytes("{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"" + openid + "\"}}}");
            out.flush();
            out.close();
		    br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
		    String line = null;
		    StringBuilder sb = new StringBuilder();
		    while ((line = br.readLine()) != null) {    // 读取数据
		        sb.append(line + "\n");
		    }
		    //System.out.println(sb);
		    @SuppressWarnings("unchecked")
			Map<String, Object> m = (Map<String, Object>) JSON.parse(sb.toString());
		    //System.out.println(m);
		    ticket = MapUtils.getString(m, "ticket", "");
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
		finally{
			HttpUtils.close(br, httpURLConnection);
		}
		return  ticket;
	}
	
	
	public static String getAccessToken(String openid,String appid,String secret,CircleDAO dao){
		boolean flag = false;
		String token = "";
		String sql = "select * from ktbm_token order by requireTime desc";
		List<Map<String, Object>> list = dao.getList(sql, null);
		if(CollectionUtils.notEmpty(list)){
			Map<String, Object> m = list.get(0);
			String requireTime = MapUtils.getString(m, "requireTime");
			String time = DateUtils.getNextDate(requireTime, "分", "90");
			if(DateUtils.beforeNow(time)){
				flag = true;
			}else{
				token = MapUtils.getString(m, "token");
			}
		}
		else{
			flag = true;
		}
		if(flag){
			BufferedReader br = null;
			HttpURLConnection httpURLConnection = null;
			try {
				String urlStr = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
				//System.out.println(urlStr);
				URL url = new URL(urlStr);
			    httpURLConnection = (HttpURLConnection) url.openConnection();

			    httpURLConnection.setDoInput(true);
			    httpURLConnection.setDoOutput(true);        // 设置该连接是可以输出的
			    httpURLConnection.setRequestMethod("POST"); // 设置请求方式
			    httpURLConnection.setRequestProperty("charset", "utf-8");

			    br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
			    String line = null;
			    StringBuilder sb = new StringBuilder();
			    while ((line = br.readLine()) != null) {    // 读取数据
			        sb.append(line + "\n");
			    }
			    @SuppressWarnings("unchecked")
				Map<String, Object> m = (Map<String, Object>) JSON.parse(sb.toString());
			    //System.out.println(m);
			    token = m.get("access_token").toString();
			    Map<String, Object > param = new HashMap<String, Object>();
			    param.put("token", token);
			    param.put("requireTime", DateUtils.getNowDate("yyyy-MM-dd HH:mm:ss"));
			    dao.NamedCUDHoldId("ktbm_token", param);
			} catch (Exception e) {
				log.error(e.toString());
				e.printStackTrace();
			}finally{
				HttpUtils.close(br, httpURLConnection);
			}
		}
		return token;
	}
	
	
	
	public static String getIMCode(){
		String code = "";
		for (int i = 0; i < 4; i++) {
			code +=(char) (new Random().nextInt(26) + 97);
		}
		//System.out.println(code);
		return code;
	}
	
	
	public static String validCodeTime(String codeTime){
		String time = "10";
		try {
			Integer t = Integer.parseInt(codeTime);
			t = Math.abs(t);
			if (t == 0) {
				t = 10;
			}
			time = t + "";
		} catch (Exception e) {

		}
		return time;
	}
}