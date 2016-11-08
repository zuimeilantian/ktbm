package com.danze.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;
import com.danze.serviceImpl.KtbmOrderImpl;
import com.danze.utils.BaseService;

/**
 * 空调保姆订单管理
 * @author ws
 */
@Service(value="ktbmOrderService")
public class KtbmOrderService extends BaseService implements KtbmOrderImpl {
	
	@Resource
	CircleDAO dao;

	/**
	 * 获得订单信息List
	 * @param ktbmOrdersch
	 * @return
	 */
	public Map<String, Object> getKtbmOrderPageList(Map<String, Object> ktbmOrdersch) {
		Map<String, Object> map = new HashMap<String, Object>();
		String no = this.getStringParam(ktbmOrdersch,"no");//订单编号
		String cusId = this.getStringParam(ktbmOrdersch,"cusId");//客户id
		String cusName = this.getStringParam(ktbmOrdersch,"cusName");//客户名称
		String linkName = this.getStringParam(ktbmOrdersch,"linkName");//联系人
		String linkPhone = this.getStringParam(ktbmOrdersch,"linkPhone");//联系人电话
		String groupId = this.getStringParam(ktbmOrdersch,"groupId");//维修分公司id（部门）
		String brand = this.getStringParam(ktbmOrdersch,"brand");//品牌
		String machineGroup = this.getStringParam(ktbmOrdersch,"machineGroup");//型号
		String machineType = this.getStringParam(ktbmOrdersch,"machineType");//机型
		String machineInnerType = this.getStringParam(ktbmOrdersch,"machineInnerType");//机内编号
		String machineOuterType = this.getStringParam(ktbmOrdersch,"machineOuterType");//外机编号
		String status = this.getStringParam(ktbmOrdersch,"status");//状态 100 作废 101 未分配 102 未接单 103 进行中 104 完成
		Integer page = getIntegerParam(ktbmOrdersch,"page");
		Integer size = getIntegerParam(ktbmOrdersch,"size");
		Integer offset = page*size;
		String sql = "";
		sql = "select a.*,ifnull(b.name,'') cusName,getdicyvalue(a.status,'ktbm_order_status') statusName,"
			 + "          getgroupname(groupId) groupName,c.province,c.city,c.district,c.detail "
			 + "  from ktbm_order a,ktbm_customer b,ktbm_address c "
			 + "where a.cusId=b.id and a.addressId=c.id and a.status <>100";
		if(!"".equals(no)){
			sql += " and a.no like '%"+no+"%' ";
		}
		if(!"".equals(no)){
			sql += " and a.cusId = '"+cusId+"' ";
		}
		if(!"".equals(cusName)){
			sql += " and b.name like '%"+cusName+"%' ";
		}
		if(!"".equals(linkName)){
			sql += " and a.linkName like '%"+linkName+"%' ";
		}
		if(!"".equals(linkPhone)){
			sql += " and a.linkPhone like '%"+linkPhone+"%' ";
		}
		if(!"".equals(groupId)){
			sql += " and a.groupId='"+groupId+"' ";
		}
		if(!"".equals(brand)){
			sql += " and a.brand like '%"+brand+"%' ";
		}
		if(!"".equals(machineGroup)){
			sql += " and a.machineGroup like '%"+machineGroup+"%' ";
		}
		if(!"".equals(machineType)){
			sql += " and a.machineType like '%"+machineType+"%' ";
		}
		if(!"".equals(machineInnerType)){
			sql += " and a.machineInnerType like '%"+machineInnerType+"%' ";
		}
		if(!"".equals(machineOuterType)){
			sql += " and a.machineOuterType like '%"+machineOuterType+"%' ";
		}
		if(!"".equals(no)){
			sql += " and a.status= '"+status+"' ";
		}
		map.put("count", dao.getList(sql, null).size());//总记录数
		sql += " order by a.createdTime desc limit "+size+" offset "+offset;
		map.put("datalist",  dao.getList(sql, null));//订单列表数据
		return map;
	}

	/* 保存订单信息
	 * @see com.danze.serviceImpl.KtbmOrderImpl#saveKtbmOrder(java.util.Map)
	 */
	@Override
	public Map<String, Object> saveKtbmOrder(Map<String, Object> dataMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			String id = this.getStringParam(dataMap, "id");//id
			String no = this.getStringParam(dataMap, "no");//订单编号
			String cusId = this.getStringParam(dataMap, "cusId");//用户id
			String linkName = this.getStringParam(dataMap, "linkName");//联系人
			String linkPhone = this.getStringParam(dataMap, "linkPhone");//联系电话
			String addressId = this.getStringParam(dataMap, "addressId");//服务地址
			String serveTime = this.getStringParam(dataMap, "serveTime");//预约服务时间
			String groupId = this.getStringParam(dataMap, "groupId");//维修分公司id（部门）
			String brand = this.getStringParam(dataMap, "brand");//品牌
			String machineGroup = this.getStringParam(dataMap, "machineGroup");//型号
			String machineType = this.getStringParam(dataMap, "machineType");//机型
			String machineInnerType = this.getStringParam(dataMap, "machineInnerType");//机内编号
			String machineOuterType = this.getStringParam(dataMap, "machineOuterType");//外机编号
			String operTime  = this.getToday("yyyy-MM-dd HH:mm:ss");//操作时间
			String sql = "";
			if(!"".equals(id)){//修改
				sql = "update ktbm_order a "
					 + "       set no=?,cusId=?,linkName=?,linkPhone=?,addressId=?,serveTime=?,"
			         + "            groupId=?,brand=?,machineGroup=?,machineType=?,machineInnerType=?,"
			         + "            machineOuterType=?,lastModifiedTime=? "
			         + " where a.id=? ";
				dao.CUD(sql, new Object[]{no,cusId,linkName,linkPhone,addressId,serveTime,groupId,
						brand,machineGroup,machineType,machineInnerType,machineOuterType,operTime,id});
			}else{//新增
				//保存数据
				sql = "insert into ktbm_order(no,cusId,linkName,linkPhone,addressId,serveTime,groupId,"
			         + "brand,machineGroup,machineType,machineInnerType,machineOuterType,createdTime,lastModifiedTime,status) "
			         + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,101)";
				dao.CUD(sql, new Object[]{no,cusId,linkName,linkPhone,addressId,serveTime,groupId,
					brand,machineGroup,machineType,machineInnerType,machineOuterType,operTime,operTime});
			}
			map.put("success", "1");
			map.put("msg", "保存成功！");
		}catch(Exception e){
			map.put("success", "0");
			map.put("msg", "保存失败！");
		}
		return map;
	}

	/* 处理订单信息
	 * @see com.danze.serviceImpl.KtbmOrderImpl#dealKtbmOrder(java.util.Map)
	 */
	@Override
	public Map<String, Object> dealKtbmOrder(Map<String, Object> dataMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			String sql = "";
			String[] sqls = null;
			String orderId = this.getStringParam(dataMap, "orderId");//orderId
			String dealId = this.getStringParam(dataMap, "dealId");//处理人id
			if("".equals(dealId)){
				dealId = "";
			}
			String dealTime =  this.getToday("yyyy-MM-dd HH:mm:ss");//操作时间
			String nextDealId = this.getStringParam(dataMap, "nextDealId");//指派人id
			String allcover = this.getStringParam(dataMap, "allcover");//质保情况（1：维保合同内 2：包内 3：保外）
			String malfunction = this.getStringParam(dataMap, "malfunction");//故障情况
			String status = this.getStringParam(dataMap, "status");//状态 100 作废 101 未分配 102 未接单 103 进行中 104 完成
			String dealType = "";//处理类型（1：派单 2：签到 3：完成）
			if("102".equals(status)){
				dealType = "1";
			}else if("103".equals(status)){
				dealType = "2";
			}else if("104".equals(status)){
				dealType = "3";
			}
			
			sql = "update ktbm_order a set a.status='"+status+"' where a.id='"+orderId+"' ";
			sqls[0]=sql;
			sql = "insert into ktbm_order_flow(orderId,dealType,dealId,dealTime,nextDealId,allcover,malfunction) "
				 + "values('"+orderId+"','"+dealType+"','"+dealId+"','"+dealTime+"','"+nextDealId+"','"+allcover+"','"+malfunction+"') ";
			sqls[1]=sql;
			dao.batchUpdate(sqls);
			map.put("resultflag", "1");
			map.put("resultMgr", "保存成功！");
		} catch(Exception e){
			map.put("resultflag", "0");
			map.put("resultMgr", "保存失败！");
		}
		return map;
	}

	/* 根据订单id获得订单信息
	 * @see com.danze.serviceImpl.KtbmOrderImpl#getKtbmOrderMap(java.lang.String)
	 */
	@Override
	public Map<String, Object> getKtbmOrderMap(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "";
		//获取订单信息
		sql = "select a.* from ktbm_order a where a.id=? ";
		map = dao.getMap(sql, new Object[]{id});
		//获取订单处理信息
		sql = "select a.* from ktbm_order_flow a where a.orderId=? order by a.dealTime";
		List<Map<String,Object>> orderdeallist = dao.getList(sql, new Object[]{id});
		map.put("orderdeallist", orderdeallist);
		//获取评价公司信息
		sql = "select a.* from ktbm_evaluation_group a where a.orderId=?";
		Map<String,Object> orderevaluationgroup = dao.getMap(sql, new Object[]{id});
		map.put("orderevaluationgroup", orderevaluationgroup);
		//获取评价修理人信息
		sql = "select a.* from ktbm_evaluation_user a where a.orderId=? ";
		List<Map<String,Object>> orderevaluationusers = dao.getList(sql, new Object[]{id});
		map.put("orderevaluationusers", orderevaluationusers);
		return map;
	}

	/* 
	 * 根据订单id作废订单信息
	 * @see com.danze.serviceImpl.KtbmOrderImpl#delKtbmOrder(java.lang.String)
	 */
	@Override
	public Map<String, Object> delKtbmOrder(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			String sql = "";
			//获取订单信息
			sql = "update ktbm_order a set a.status=100 where a.id=? ";
			dao.CUD(sql, new Object[]{id});
			map.put("resultflag", "1");
			map.put("resultMgr", "删除成功！");
		}catch(Exception e){
			map.put("resultflag", "0");
			map.put("resultMgr", "删除失败！");
		}
		return map;
	}
	
}
