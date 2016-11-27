package com.danze.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.danze.dao.CircleDAO;

@Service
@Lazy(false)
public class TokenUtils implements InitializingBean{

	@Resource
	CircleDAO dao;
	
	@Value("#{configProperties['wechat.appid']}")
	private String appid;
	@Value("#{configProperties['wechat.secret']}")
	private String secret;
	@Value("#{configProperties['wechat.token']}")
	private String token;
	
	
	//IM
	@Value("#{configProperties['im.clientId']}")
	private String clientId;
	@Value("#{configProperties['im.clientSecret']}")
	private String clientSecret;
	@Value("#{configProperties['im.application']}")
	private String application;
	
	private static Logger log = LoggerFactory.getLogger(TokenUtils.class);
	
	@Scheduled(cron = "0 0/90 * * * ?")
	public void getAccessToken(){
		System.out.println("getAccessToken:" + DateUtils.getNowDate(DateUtils.TIME));
		String token = "";
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
		}
		finally{
			HttpUtils.close(br, httpURLConnection);
		}
		
	}
	
	
//	@Scheduled(cron = "0 0/90 * * * ?")
	public void getIMToken(String application){
		try {
			String urlStr = application+"/token";
			String method = "POST";
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("grant_type", "client_credentials");
			param.put("client_id",clientId);
			param.put("client_secret", clientSecret);
			String paramJSON = JSON.toJSONString(param);
		    HttpURLConnection connection = HttpUtils.getConnection(urlStr, method, paramJSON);
			Map<String, Object> m = HttpUtils.closeConnection(connection);
			System.out.println(m);
			if(m.get("access_token")!=null){
				String token = MapUtils.getString(m, "");
				String sql = "insert into ktbm_imtoken(token,requireTime) values(?,?)";
				dao.CUD(sql, new Object[]{token,DateUtils.getNowDate("yyyy-MM-dd HH:mm:ss")});
			}
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
	}
	
	public static boolean getIM(String username,String password,String application){
		try {
			String urlStr = application + "/users";
			String method = "POST";
			String param = "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";
			HttpURLConnection connection = HttpUtils.getConnection(urlStr, method,param);
			Map<String, Object> m = HttpUtils.closeConnection(connection);
			System.out.println(m.toString());
			return true;
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
			return false;
		}
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		//getAccessToken();
		//getIMToken(application);
	}
}
