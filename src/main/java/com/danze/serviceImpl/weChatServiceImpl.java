package com.danze.serviceImpl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.stereotype.Service;

import com.danze.service.WeChatService;
import com.danze.utils.MessageUtil;

@Service
public class weChatServiceImpl implements WeChatService {

	@Override
	public String processRequest(HttpServletRequest request) {

		try {
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");

			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			String respContent = "";

			// 事件处理开始
			if (msgType.equals(REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");

				if (eventType.equals(EVENT_TYPE_SUBSCRIBE)) {
					// 关注
					respContent = "感谢您关注,这里会给您提供最新的公司资讯和公告！\n";
					StringBuffer contentMsg = new StringBuffer();
					respContent = respContent + contentMsg.toString();

				} else if (eventType.equals(EVENT_TYPE_UNSUBSCRIBE)) {
					// 取消关注,用户接受不到我们发送的消息了，可以在这里记录用户取消关注的日志信息

				} else if (eventType.equals(EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");
				}
				return respContent;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
