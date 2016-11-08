package com.danze.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;
import com.danze.exception.MsgException;
import com.danze.service.MsgService;
import com.danze.utils.CodeUtils;
import com.danze.utils.CollectionUtils;
import com.danze.utils.DateUtils;
import com.danze.utils.JsonResult;
import com.danze.utils.MapUtils;
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

	@Resource
	private CircleDAO dao;
	
	@Override
	public String sendMsg(String phone, String userId) throws MsgException {
		String status = "101";
		String code = CodeUtils.getCode();
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend(extend);
		req.setSmsType(type);
		req.setSmsFreeSignName(signName);
		req.setSmsTemplateCode(templateCode);
		// json 参数，根据模板来
		String json = "{\"name\":" + code + "}";
		req.setSmsParamString(json);
		req.setRecNum(phone);
		try {
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			System.out.println(req);
			if (rsp.isSuccess()) {
				status = "102";
			} else {
				status = rsp.getErrorCode();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(phone, phone);
		map.put(status, status);
		map.put("createTime", DateUtils.getNowDate("yyyy-MM-dd HH:mm:ss"));
		map.put("code", code);
		map.put(status, status);
		try {
			dao.NamedCUDHoldId("ktmb_message", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public JsonResult register(String phone, String code, String user) throws RuntimeException {
		JsonResult jsonResult = new JsonResult(true);
		String sql = "select code from ktbm_message where phone = ? and wechatId = ? order by createTime desc";
		List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();
		
		lists = dao.getList(sql, new Object[]{phone,user});
		String sendCode = "";
		if(CollectionUtils.notEmpty(lists)){
			Map<String, Object> m = lists.get(0);
			sendCode = MapUtils.getString(m, "code","");
		}
		if(code.toLowerCase().equals(sendCode.toLowerCase())){
			jsonResult.setFail("无效的验证码");
		}else{
			String update = "update ktbm set phone = ? where wechatId = ?";
			dao.CUD(update, new Object[]{phone,user});
		}
		return jsonResult;
	}

	@Override
	public JsonResult getPhone(String wecharId) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

}
