package com.danze.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;
import com.danze.filter.OpenIdFilter;
import com.danze.service.WeChatService;
import com.danze.utils.CollectionUtils;
import com.danze.utils.DateUtils;
import com.danze.utils.MapUtils;
import com.danze.utils.MessageUtil;

@Service
public class weChatServiceImpl implements WeChatService {

	@Resource
	CircleDAO dao;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public synchronized String processRequest(HttpServletRequest request) {

		try {
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			//System.out.println(requestMap.toString());
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			request.getSession().setAttribute("openid", fromUserName);
			OpenIdFilter.setOpenId(fromUserName);
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
					//respContent = "感谢您关注,这里会给您提供最新的公司资讯和公告！\n";
					//StringBuffer contentMsg = new StringBuffer();
					//respContent = respContent + contentMsg.toString();
					String sql = "select * from ktbm_customer where openid = ?";
					
					List<Map<String, Object>> list = dao.getList(sql, new Object[]{fromUserName});
					if(CollectionUtils.isEmpty(list)){
						Map<String, Object> m = new HashMap<String, Object>();
						m.put("wechatId", fromUserName);
						m.put("openid", fromUserName);
						m.put("points", 0);
						list = dao.getList(sql, new Object[]{fromUserName});
						if(CollectionUtils.isEmpty(list)){
							sql = "insert into ktbm_customer(wechatId,openid,points) values(?,?,?)";
							dao.CUD(sql, new Object[]{fromUserName,fromUserName,0});
							//dao.NamedCUD("ktbm_customer", m);
						}
						
						try {
							if(requestMap.get("EventKey")!=null&&requestMap.get("Ticket")!=null){
								String eventKey = requestMap.get("EventKey").toString();
								String sourceId = eventKey.substring(8);
								sql = "select distinct scanFirst,scanSecond from ktbm_points_method where methodType = 1 and methodObject = 2";
								Map<String, Object> scanMap = dao.getMap(sql, null);
								sql = "insert into ktbm_points(type,source,point,cusId,createTime,wechatId) values(?,?,?,?,?,?)";
								dao.CUD(sql, new Object[]{2,2,MapUtils.getString(scanMap, "scanFirst","0"),sourceId,DateUtils.getNowDate("yyyy-MM-dd HH:mm:ss"),fromUserName});
								sql = "update ktbm_customer set points = ifnull(points,0) + ? where openid = ?";
								dao.CUD(sql, new Object[]{MapUtils.getString(scanMap, "scanFirst","0"),sourceId});
								sql = "select cusId from ktbm_points where wechatId  = ? and source = 2";
								Object t =dao.getObject(sql, new Object[]{sourceId});
								if(t!=null){
									sql = "insert into ktbm_points(type,source,point,cusId,createTime,wechatId) values(?,?,?,?,?,?)";
									dao.CUD(sql, new Object[]{2,3,MapUtils.getString(scanMap, "secondFirst","0"),t,DateUtils.getNowDate("yyyy-MM-dd HH:mm:ss"),fromUserName});
									sql = "update ktbm_customer set points = ifnull(points,0) + ? where openid = ?";
									dao.CUD(sql, new Object[]{MapUtils.getString(scanMap, "secondFirst","0"),t});
								}
							}
						} catch (Exception e) {
							log.error(e.toString());
							e.printStackTrace();
						}
					}
				} else if (eventType.equals(EVENT_TYPE_UNSUBSCRIBE)) {
					// 取消关注,用户接受不到我们发送的消息了，可以在这里记录用户取消关注的日志信息

				} else if (eventType.equals(EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");
				} else if (eventType.equals(EVENT_TYPE_VIEW)) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");
				}
				//return respContent;
			}
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
		return "";
	}
}
