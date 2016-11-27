package com.danze.wechat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danze.dao.CircleDAO;
import com.danze.filter.OpenIdFilter;
import com.danze.filter.RequestFilter;
import com.danze.service.CustomerService;
import com.danze.service.WeChatService;
import com.danze.utils.CollectionUtils;
import com.danze.utils.HttpUtils;
import com.danze.utils.MapUtils;

@Controller
public class WXController{


	@Value("#{configProperties['wechat.appid']}")
	private String appid;
	@Value("#{configProperties['wechat.secret']}")
	private String secret;
	@Value("#{configProperties['wechat.token']}")
	private String token;
	
	@Resource
	private CircleDAO dao;
	
	@Resource
	private CustomerService customerService;
	
	@Resource
	private WeChatService weService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("wechat/check")
	@ResponseBody
	public synchronized void sign(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		weService.processRequest(request);
		
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		String[] str = { token, timestamp, nonce };
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
	public synchronized void getOpenId(HttpServletRequest request,HttpServletResponse response) throws IOException {
		try {
			String state = request.getParameter("state");
			HttpServletRequest req = RequestFilter.threadLocalRequest.get();
			Object obj = req.getSession().getAttribute("openid");
			String openid = "";
			if(obj==null){
				System.out.println("/openid: openid is null"  ); 
			}else {
				openid = obj.toString();
			}
			if(openid!=null&&openid.equals("")){
				System.out.println("/openid:" + "getopenid");
				String code = request.getParameter("code");
				Map<String, Object> map = HttpUtils.getToken(code, appid, secret);
				openid = MapUtils.getString(map, "openid");
			}
			if(!openid.equals("")){
				OpenIdFilter.setOpenId(openid);
			}
			if(!customerService.isRegister()){
				state = "index.html#/register/"+state;
			}
			else {
				state = "index.html#/"+state;
			}
			response.sendRedirect(state);
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/getPower")
	@ResponseBody
	public synchronized void getPower(HttpServletRequest request,HttpServletResponse response) throws IOException {
		try {
			String code = request.getParameter("code");
			Map<String, Object> map = HttpUtils.getToken(code, appid, secret);
			String openid = MapUtils.getString(map, "openid");
			String state = "index.html#/register/personal";
			if(!openid.equals("")){
				System.out.println("getBack:openid:" + openid);
				request.getSession().setAttribute("openid", openid);
				OpenIdFilter.setOpenId(openid);
				String sql = "select * from ktbm_customer where openid = ?";
				List<Map<String, Object>> lists = dao.getList(sql, new Object[]{openid});
				if(CollectionUtils.isEmpty(lists)){
					sql = "insert into ktbm_customer(wechatId,openid,points) values(?,?,?)";
					dao.CUD(sql, new Object[]{openid,openid,0});
				}
				if(customerService.isRegister()){
					state = "index.html#/personal";
				}
			}
			response.sendRedirect(state);
		}
		catch(Exception e){
			log.error(e.toString());
			e.printStackTrace();
		}
	}
	
}
