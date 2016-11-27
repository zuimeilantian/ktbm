package com.danze.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;
import com.danze.exception.MsgException;
import com.danze.service.MsgService;
import com.danze.utils.MsgUtils;
import com.danze.utils.CollectionUtils;
import com.danze.utils.DateUtils;
import com.danze.utils.JsonResult;
import com.danze.utils.MapUtils;
import com.danze.utils.TokenUtils;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

@Service
public class MsgServiceImpl implements MsgService {

	@Value("#{configProperties['msg.url']}")
	private String url;
	@Value("#{configProperties['msg.appkey']}")
	private String appkey;
	@Value("#{configProperties['msg.secret']}")
	private String secret;
	@Value("#{configProperties['msg.extend']}")
	private String extend;
	@Value("#{configProperties['msg.type']}")
	private String type;
	@Value("#{configProperties['msg.signName']}")
	private String signName;
	@Value("#{configProperties['msg.templateCode']}")
	private String templateCode;

	@Value("#{configProperties['wechat.appid']}")
	private String appid;
	@Value("#{configProperties['wechat.secret']}")
	private String wesecret;
	
	@Value("#{configProperties['code.time']}")
	private String codeTime;
	@Value("#{configProperties['code.amount']}")
	private String amountStr;
	
	@Value("#{configProperties['im.application']}")
	private String application;
	
	@Resource
	private CircleDAO dao;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public String sendMsg(String phone, String userId) throws MsgException {
		String sql = "select * from ktbm_customer where phone = ? and openid != ?";
//		List<Map<String, Object>> obj = dao.getList(sql, new Object[]{phone,userId});
//		if(obj!=null&&obj.size() > 0){
//			return "手机号已被使用";
//		}
		sql = "select sendCount from ktbm_message_count where openid = ? and sendDate = ?";
		Object sendCountStr = dao.getObject(sql, new Object[]{userId,DateUtils.getNowDate(DateUtils.DATE)});
		//当日已发送数量
		int sendCount = 0;
		try {
			sendCount = Integer.parseInt(sendCountStr==null?"0":sendCountStr.toString());
		} catch (NumberFormatException e1) {
			sendCount = 0;
		}
		//每日发送数量
		int amount = 0;
		try {
			amount = Integer.parseInt(amountStr==null?"10":amountStr.toString());
		} catch (NumberFormatException e1) {
			amount = 10;
		}
		if(sendCount>=amount){
			return "每个用户每天最多发送" + amount + "条验证码";
		}
		String status = "101";
		String code = MsgUtils.getCode();
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend(extend);
		req.setSmsType(type);
		req.setSmsFreeSignName(signName);
		req.setSmsTemplateCode(templateCode);
		// json 参数，根据模板来
		String json = "{\"name\":\""+ code + "\"}";
		req.setSmsParamString(json);
		req.setRecNum(phone);
		try {
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			if (rsp.isSuccess()) {
				status = "102";
			} else {
				status = rsp.getErrorCode();
			}

		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
			return "系统异常,发送失败";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		map.put("status", status);
		map.put("createTime", DateUtils.getNowDate("yyyy-MM-dd HH:mm:ss"));
		map.put("code", code);
		map.put("openid", userId);
		try {
			dao.NamedCUDHoldId("ktbm_message", map);
			if(sendCount==0){
				sql = "insert into ktbm_message_count(openid,phone,sendCount,sendDate) values(?,?,?,?)";
				dao.CUD(sql, new Object[]{userId,phone,1,DateUtils.getNowDate(DateUtils.DATE)});
			}else {
				sql = "update ktbm_message_count set sendCount = sendCount + 1 where openid = ? and sendDate = ?";
				dao.CUD(sql, new Object[]{userId,DateUtils.getNowDate(DateUtils.DATE)});
			}
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
			return "系统异常";
		}
		return "ok";
	}

	@Override
	public JsonResult register(String phone, String code, String user) throws RuntimeException {
		JsonResult jsonResult = new JsonResult(true);
		String sql = "select phone,code,createTime from ktbm_message where openid = ? order by createTime desc";
		List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();
		lists = dao.getList(sql, new Object[]{user});
		String sendCode = "-1";
		String sendPhone = "-1";
		if(CollectionUtils.notEmpty(lists)){
			Map<String, Object> m = lists.get(0);
			sendCode = MapUtils.getString(m, "code","");
			sendPhone = MapUtils.getString(m, "phone","-1");
			if(!phone.equals(sendPhone)){
				jsonResult.setFail("不是发送验证码的手机号");
				return jsonResult;
			}
			String createTime = m.get("createTime").toString();
			String time = DateUtils.getNextDate(createTime, "分", MsgUtils.validCodeTime(codeTime));
			if(DateUtils.beforeNow(time)){
				jsonResult.setFail("验证码已过期，请重新获取");
				return jsonResult;
			}
		}
		if(!code.toLowerCase().equals(sendCode.toLowerCase())){
			jsonResult.setFail("无效的验证码");
		}else{
			try {
				sql = "select * from ktbm_customer where openid = ?";
				Map<String, Object> m = dao.getMap(sql, new Object[]{user});
				String codePic = "";
				if(MapUtils.getString(m, "url").equals("")){
					codePic = MsgUtils.getCodePic(user,appid,wesecret,dao);
				}else {
					codePic = MapUtils.getString(m, "url").toString();
				}
				String userName = "";
				if(MapUtils.getString(m, "imname").equals("")){
					String rCode = MsgUtils.getIMCode();
					userName = phone.substring(0,3) + rCode + phone.substring(7);
					String password = userName;
					if(!TokenUtils.getIM(userName, password,application)){
						userName = "";
					}	
				}else{
					userName = codePic = MapUtils.getString(m, "imname").toString();
				}
				String update = "update ktbm_customer set phone = null where phone =  ?";
				dao.CUD(update, new Object[]{phone});
				update = "update ktbm_customer set phone = ? ,url=?,imname = ? where openid = ?";
				dao.CUD(update, new Object[]{phone,codePic,userName,user});
				jsonResult.setSuccess("注册成功");
			} catch (Exception e) {
				log.error(e.toString());
				jsonResult.setSuccess("系统异常");
			}
		}
		return jsonResult;
	}

	@Override
	public JsonResult getPhone(String wecharId) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

}
