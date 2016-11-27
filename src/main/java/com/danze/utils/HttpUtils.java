package com.danze.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class HttpUtils {

	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);
	
	public static HttpURLConnection getConnection(String urlStr,String method,String paramJSON){
		URL url;
		try {
			url = new URL(urlStr);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		    httpURLConnection.setDoInput(true);
		    httpURLConnection.setDoOutput(true);        // 设置该连接是可以输出的
		    httpURLConnection.setRequestMethod(method); // 设置请求方式
		    httpURLConnection.setRequestProperty("charset", "utf-8");
		    httpURLConnection.setRequestProperty("Content-Type","application/json");
		    httpURLConnection.connect();
            if(paramJSON!=null){
            	//POST请求
                DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
                //{"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": "123"}}}
    			out.writeBytes(paramJSON);
                out.flush();
                out.close();
            }
		    return httpURLConnection;
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> closeConnection(HttpURLConnection httpURLConnection){
		Map<String, Object> m = new HashMap<String, Object>();
		if(httpURLConnection!=null){
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
			    String line = null;
			    StringBuilder sb = new StringBuilder();
			    while ((line = br.readLine()) != null) {    // 读取数据
			        sb.append(line + "\n");
			    }
			    m = (Map<String, Object>) JSON.parse(sb.toString());
			} catch (Exception e) {
				log.error(e.toString());
				e.printStackTrace();
			}
			finally{
				close(br, httpURLConnection);
			}
		}
		return m;
	}
	
	
	//acctoken openid 
	public static Map<String, Object> getToken(String code,String appid,String secret){
		Map<String, Object> m = new HashMap<String, Object>();
		BufferedReader br = null;
		HttpURLConnection httpURLConnection = null;
		try {
			String urlStr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
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
		    m = (Map<String, Object>) JSON.parse(sb.toString());
		    //System.out.println(m);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
		}finally{
			close(br, httpURLConnection);
		}
	    return m;
	}

	public static void close(BufferedReader br , HttpURLConnection httpURLConnection){
		try {
			if (br != null) {
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
	}
	
	
	public static String getOpenId(String token, String appid, String secret) {
		
		return null;
	}
}
