package com.danze.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class WXController {

	private static String TOKEN = "wuzhong";
//	private static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx4ecdc814aa5b1fca&secret=510be9685fa5d47e4ffd61d56adff804&code=#CODE#&grant_type=authorization_code";

	@Value("#{configProperties['wechat.appid']}")
	private String appid;
	@Value("#{configProperties['wechat.secret']}")
	private String secret;
	
	
	@RequestMapping("/wechat/check")
	@ResponseBody
	public void sign(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		System.out.println(signature);
		String[] str = { TOKEN, timestamp, nonce };
		Arrays.sort(str); // 字典序排序
		String bigStr = str[0] + str[1] + str[2];
		// SHA1加密
		String digest = new SHA1().getDigestOfString(bigStr.getBytes()).toLowerCase();
		// 确认请求来至微信
		if (digest.equals(signature)) {
			response.getWriter().print(echostr);
		}
	}

	@RequestMapping("/openid")
	@ResponseBody
	public String getOpenId(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String v = "";
		try {
			String code = request.getParameter("code");
			String urlStr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
			
			URL url = new URL(urlStr);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

		    httpURLConnection.setDoInput(true);
		    httpURLConnection.setDoOutput(true);        // 设置该连接是可以输出的
		    httpURLConnection.setRequestMethod("POST"); // 设置请求方式
		    httpURLConnection.setRequestProperty("charset", "utf-8");

		    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
		    String line = null;
		    StringBuilder sb = new StringBuilder();
		    while ((line = br.readLine()) != null) {    // 读取数据
		        sb.append(line + "\n");
		    }
		    
		    v = sb.toString();
		    System.out.println(sb.toString());
		    
//		    Map<String, Object> m = (Map<String, Object>) JSON.parse(v);
//		    m.get("openid");
		    
		    request.getRequestDispatcher("#/order-form").forward(request, response);
		    
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}

}
