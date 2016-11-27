package com.danze.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;
import com.danze.filter.OpenIdFilter;
import com.danze.service.KtbmOrderService;
import com.danze.service.PointService;
import com.danze.utils.BaseService;

@Service(value="ktbmOrderImpl")
public class KtbmOrderImpl extends BaseService implements KtbmOrderService {

	@Resource
	CircleDAO dao;
	
	@Resource
	private PointService pointService;

	/**
	 * 获得订单信息List
	 * @param ktbmOrdersch
	 * @return
	 */
	public Map<String, Object> getKtbmOrderPageList(Map<String, Object> ktbmOrdersch) {
		Map<String, Object> map = new HashMap<String, Object>();
		String flag = this.getStringParam(ktbmOrdersch,"flag");//1：个体 2:其他
		String no = this.getStringParam(ktbmOrdersch,"no");//订单编号
		String cusId = "";//客户id
		if("1".equals(flag)){
			cusId = OpenIdFilter.getOpenId();
		}else{
			cusId = this.getStringParam(ktbmOrdersch,"cusId");
		}
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
		sql = "select a.id, a.no, a.cusId, a.linkName, a.linkPhone, a.addressId, "
			+ "           a.serveBeginTime, a.serveEndTime, a.groupId, a.brand, "
			+ "           a.machineGroup, a.machineType, a.machineInnerType, a.machineOuterType,"
			+ "           a.fileIds, a.createdBy, a.createdTime, a.lastModifiedBy, a.lastModifiedTime,a.malfunction,"
			+ "           a.status,ifnull(b.name,'') cusName,getdicyvalue(a.status,'ktbm_order_status') statusName,"
			 + "          ifnull(a.province,'') province,ifnull(a.city,'') city,ifnull(a.district,'') district,ifnull(a.detail,'') detail,"
			 + "          getaddressname(a.province) provinceName, getaddressname(a.city) cityName, getaddressname(a.district) districtName,"
			 + "          getgroupname(groupId) groupName,concat(getaddressname(a.province),getaddressname(a.city), getaddressname(a.district),ifnull(a.detail,'')) address,"
			 + "          (case when a.status=100 then '未接单' when a.status=101 then '未分配' when a.status=102 then '未签到' "
			 + "                   when a.status=103 then '进行中' when a.status=104 then '已完成' else '作废' end) statusName,b.imname "
			 + "  from ktbm_order a,ktbm_customer b "
			 + "where a.cusId=b.openId and a.status <>0 ";
		if(!"".equals(no)){
			sql += " and a.no like '%"+no+"%' ";
		}
		if(!"".equals(cusId)){
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
		if(!"".equals(status)){
			sql += " and a.status in ("+status+") ";
		}
		map.put("count", dao.getList(sql, null).size());//总记录数
		sql += " order by a.lastModifiedTime desc limit "+size+" offset "+offset;
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
			String no = "Dd"+this.getToday("yyyyMMddHHmmss")+this.getRandoms(4);//订单编号
			String cusId = OpenIdFilter.getOpenId();//用户id
			String linkName = this.getStringParam(dataMap, "linkName");//联系人
			String linkPhone = this.getStringParam(dataMap, "linkPhone");//联系电话
			Integer addressId = this.getIntegerParam(dataMap, "addressId");//服务地址
			String province = this.getStringParam(dataMap, "province");//省
			String city = this.getStringParam(dataMap, "city");//市
			String district = this.getStringParam(dataMap, "district");//区
			String detail = this.getStringParam(dataMap, "detail");//详细地址
			String serveBeginTime = this.getStringParam(dataMap, "serveBeginTime");//预约服务时间
			String serveEndTime = this.getStringParam(dataMap, "serveEndTime");//预约服务时间
			Integer groupId = this.getIntegerParam(dataMap, "groupId");//维修分公司id（部门）
			String brand = this.getStringParam(dataMap, "brand");//品牌
			String machineGroup = this.getStringParam(dataMap, "machineGroup");//型号
			String machineType = this.getStringParam(dataMap, "machineType");//机型
			String machineInnerType = this.getStringParam(dataMap, "machineInnerType");//机内编号
			String machineOuterType = this.getStringParam(dataMap, "machineOuterType");//外机编号
			String fileIds = this.getStringParam(dataMap, "fileIds");//附件ids
			String malfunction = this.getStringParam(dataMap, "malfunction");//故障描述
			String operId  = OpenIdFilter.getOpenId();//操作人
			String operTime  = this.getToday("yyyy-MM-dd HH:mm:ss");//操作时间
			String sql = "";
			int cnt = 0;
			sql = "select count(1) from ktbm_order a where a.no=? ";
			if(!"".equals(id)){
				sql += " and a.id<>'"+id+"' ";
			}
			cnt= dao.getCount(sql, new Object[]{no});
			if(cnt>0){
				map.put("success", false);
				map.put("msg", "此订单编号已存在，无法操作！");
			}
			if(!"".equals(id)){//修改
				sql = "update ktbm_order a "
					 + "      set no=?,cusId=?,linkName=?,linkPhone=?,addressId=?,province=?,city=?,"
					 + "            district=?,detail=?,serveBeginTime=?,serveEndTime=?,"
			         + "            groupId=?,brand=?,machineGroup=?,machineType=?,machineInnerType=?,"
			         + "            machineOuterType=?,lastModifiedBy=?,lastModifiedTime=?,fileIds=?,malfunction=? "
			         + "where a.id=? ";
				dao.CUD(sql, new Object[]{no,cusId,linkName,linkPhone,addressId,province,city,district,detail,serveBeginTime,serveEndTime,groupId,
						brand,machineGroup,machineType,machineInnerType,machineOuterType,operId,operTime,fileIds,id});
			}else{//新增
				//保存数据
				sql = "insert into ktbm_order(no,cusId,linkName,linkPhone,addressId,province,city,district,detail,serveBeginTime,"
			        + "serveEndTime,groupId,brand,machineGroup,machineType,machineInnerType,machineOuterType,"
			        + "fileIds,createdBy,createdTime,lastModifiedBy,lastModifiedTime,malfunction,status) "
			        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,100)";
				dao.CUD(sql, new Object[]{no,cusId,linkName,linkPhone,addressId,province,city,district,detail,serveBeginTime,serveEndTime,groupId,
					brand,machineGroup,machineType,machineInnerType,machineOuterType,fileIds,operId,operTime,operId,operTime,malfunction});
			}
			map.put("success", true);
			map.put("msg", "保存成功！");
		}catch(Exception e){
			System.out.println("-----:"+e.toString());
			map.put("success", false);
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
			List<String> sqls = new ArrayList<String>();
			String orderId = this.getStringParam(dataMap, "orderId");//orderId
			Integer dealId = this.getIntegerParam(dataMap, "nextDealId");//处理人id
			String dealTime =  this.getToday("yyyy-MM-dd HH:mm:ss");//操作时间
			Integer nextDealId = this.getIntegerParam(dataMap, "nextDealId");//指派人id
			if(nextDealId==0){
				nextDealId = dealId;
			}
			String allcover = this.getStringParam(dataMap, "allcover");//质保情况（1：维保合同内 2：包内 3：保外）
			String malfunction = this.getStringParam(dataMap, "malfunction");//故障情况
			String status = this.getStringParam(dataMap, "status");//状态 0 作废 100 未接单 101 未分配 102 未签到 103 进行中 104 完成
			String dealType = "";//处理类型（4：接单 1：派单 2：签到 3：完成）;
			Integer mainframeNum = this.getIntegerParam(dataMap, "mainframeNum");//主机数量
			Integer terminalNum = this.getIntegerParam(dataMap, "terminalNum");//末端数量
			String startuseDate = this.getStringParam(dataMap, "startuseDate");//启用日期
			String groupId = "";
			if("4".equals(dealType)){
				status = "101";
			}else if("1".equals(dealType)){
				status = "102";
				sql = "select a.group_id from group_user a where a.user_id=? and a.is_primary=1";
				groupId = this.getStringParam(dao.getMap(sql, new Object[]{dealId}), "group_id");//维修公司id
				if("".equals(groupId)){
					map.put("success", false);
					map.put("msg", "系统不能够获得到您当前的维修公司，无法提交!");
					return map;
				}
			}else if("2".equals(dealType)){
				status = "103";
			}else if("3".equals(dealType)){
				status = "104";
			}
			
			sql = "update ktbm_order a set a.status='"+status+"' where a.id='"+orderId+"' ";
			sqls.add(sql);
			sql = "insert into ktbm_order_flow(orderId,dealType,dealId,dealTime,nextDealId,allcover,malfunction,"
				+ "mainframeNum,terminalNum,startuseDate) "
				+ "values('"+orderId+"','"+dealType+"','"+dealId+"','"+dealTime+"','"+nextDealId+"','"+allcover+"',"
						+ "'"+malfunction+"','"+mainframeNum+"','"+terminalNum+"','"+startuseDate+"') ";
			sqls.add(sql);
			if("1".equals(dealType)){
				sql = "select count(1) from ktbm_evaluation_group a where a.orderId=?";
				Integer isexistsgroup = dao.getCount(sql, new Object[]{orderId});
				sql = "select count(1) from ktbm_evaluation_user a where a.orderId=? and a.userId=? ";
				Integer isexistsuser = dao.getCount(sql, new Object[]{orderId,nextDealId});
				if(isexistsgroup==0){
					sql = "update ktbm_evaluation_group a set a.status=0 where a.orderId='"+orderId+"' and a.status=1";
					sqls.add(sql);
					sql = "insert into ktbm_evaluation_group(groupId,orderId,status) values('"+groupId+"','"+orderId+"',1)";
					sqls.add(sql);
					if(isexistsuser==0){
						sql = "insert into ktbm_evaluation_user(orderId,userId,status) values('"+orderId+"','"+nextDealId+"',1)";
						sqls.add(sql);
					}
				}else{
					if(isexistsuser==0){
						sql = "insert into ktbm_evaluation_user(orderId,userId,status) values('"+orderId+"','"+nextDealId+"',1)";
						sqls.add(sql);
					}
				}
			}
			dao.batchUpdate(ListZhArray(sqls));
			map.put("success", true);
			map.put("msg", "操作成功！");
		} catch(Exception e){
			System.out.println("-----:"+e.toString());
			map.put("success", false);
			map.put("msg", "操作失败！");
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
		sql = "select a.*,ifnull(a.province,'') province,ifnull(a.city,'') city,ifnull(a.district,'') district,ifnull(a.detail,'') detail,a.malfunction,"
			 + "          getaddressname(a.province) provinceName, getaddressname(a.city) cityName, getaddressname(a.district) districtName,"
			 + "          concat(getaddressname(a.province),getaddressname(a.city),getaddressname(a.district),"
			 + "          ifnull(a.detail,'')) address,ifnull(b.name,'') cusName,b.imname,getgroupname(a.groupId) groupName "
			 + " from ktbm_order a,ktbm_customer b where a.cusId=b.openId and a.id=? ";
		map = dao.getMap(sql, new Object[]{id});
		//获取图片信息
		sql = "select ifnull(a.display,'') fileName,CONCAT('/upload/files/',a.name) fileUrl "
			+ "  from _file a "
			+ " where exists(select 1 from ktbm_order b where instr(concat(',',b.fileIds,','),concat(',',a.id,','))>0 and b.id=?)";
		List<Map<String,Object>> filelist = dao.getList(sql, new Object[]{id});
		map.put("filelist", filelist);
		//获取订单处理信息
		sql = "select a.*,ifnull(b.name,'') dealName,ifnull(b.mobile,'') dealMobile,ifnull(b.email,'') dealEmail,"
			 + "          getusername(a.nextDealId) nextDealName,ifnull(b.icon,'') userIcon,"
			 + "          getdicyvalue(a.dealType,'dealType') dealTypeName,getdicyvalue(a.allcover,'allcover') allcoverName "
			 + "  from ktbm_order_flow a,user b where a.dealId=b.id and a.orderId=? order by a.dealTime";
		List<Map<String,Object>> orderdeallist = dao.getList(sql, new Object[]{id});
		map.put("orderdeallist", orderdeallist);
		//获取评价公司信息
		sql = "select a.*,getcusnameopenid(a.operId) operName,getgroupname(a.groupId) groupName "
			 + " from ktbm_evaluation_group a where a.orderId=? and a.status=1";
		Map<String,Object> orderevaluationgroup = dao.getMap(sql, new Object[]{id});
		map.put("orderevaluationgroup", orderevaluationgroup);
		//获取评价修理人信息
		sql = "select a.*,ifnull(b.name,'') userName,concat('"+this.fileurl+"','/upload/icons/',ifnull(b.icon,''))  userIcon,"
			 + "          getcusnameopenid(a.operId) operName "
			 + "from ktbm_evaluation_user a,user b where a.userId=b.id and a.orderId=? and a.status=1";
		List<Map<String,Object>> orderevaluationusers = dao.getList(sql, new Object[]{id});
		map.put("orderevaluationusers", orderevaluationusers);
		
		//获得客户的所在公司
		sql = "select a.* from ktbm_customer a where exists(select 1 from ktbm_order b where b.cusId=a.openId and b.id=?)";
		String company = this.getStringParam(dao.getMap(sql, new Object[]{id}), "company");
		//获取客户合同信息
		sql = "select t.*,getdicyvalueid(t.type,'conditioner_order_type') typeName,"
			+ "           (select m.name from groups m where m.parent_id is null limit 1) yf from (  "
			+ "select a.id companyId,ifnull(a.customer_no,'') customerNo,ifnull(a.designer,'') checkinBy,DATE_FORMAT(a.design_date,'%Y-%m-%d %H:%i:%S') checkinDate, "
			+ "       ifnull(a.customer_name,'') customerName,a.address,ifnull(a.link_no,'') linkNo,ifnull(a.link_man,'') linkMan, "
			+ "       a.remark,b.id,b.type,ifnull(b.contract_no,'') contractNo,ifnull(b.project_name,'') projectName, "
			+ "       ifnull(a.facility_name,'') facilityName,ifnull(a.facility_type,'') facilityType,ifnull(a.aircondition_brand,'') airconditionBrand,"
			+ "       getdicyvalueid(a.aircondition_brand,'ktbm_ktpp') airconditionBrandName,ifnull(a.main_oper,'') mainOper, "
			+ "       ifnull(b.project_address,'') projectAddress,b.designer,DATE_FORMAT(b.design_date,'%Y-%m-%d %H:%i:%S') designDate,ifnull(sign_date,'') signDate, "
			+ "       b.field7,DATE_FORMAT(b.begin_date,'%Y-%m-%d %H:%i:%S') beginDate,DATE_FORMAT(b.end_date,'%Y-%m-%d %H:%i:%S') endDate "
			+ "  from _app_customer a LEFT JOIN _app_contract_file b on a.id=b.company_id "
			+ " where a.customer_name=?) t "
			+ " where t.beginDate<=DATE_FORMAT(SYSDATE(),'%Y-%m-%d %H:%i:%S') and t.endDate>=DATE_FORMAT(SYSDATE(),'%Y-%m-%d %H:%i:%S') ";
		Map<String,Object> customerinfo = dao.getMap(sql, new Object[]{company});
		map.put("customerinfo", customerinfo);
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
			sql = "update ktbm_order a set a.status=0 where a.id=? ";
			dao.CUD(sql, new Object[]{id});
			map.put("success", true);
			map.put("msg", "删除成功！");
		}catch(Exception e){
			map.put("success", false);
			map.put("msg", "删除失败！");
		}
		return map;
	}

	/* 保存评论信息
	 * @see com.danze.serviceImpl.KtbmOrderImpl#saveEvaluation(java.util.Map)
	 */
	@Override
	public Map<String, Object> saveEvaluation(Map<String, Object> dataMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			String sql = "";
			List<String> sqls = new ArrayList<String>();
			//插入公司的评论信息
			Map<String,Object> orderevaluationgroup = (Map<String, Object>) dataMap.get("orderevaluationgroup");//评价公司信息
			String orderId = this.getStringParam(orderevaluationgroup, "orderId");//订单id
			String operId =  OpenIdFilter.getOpenId();//操作人
			String operTime =  this.getToday("yyyy-MM-dd HH:mm:ss");//操作时间
			//删除公司的评论信息
			sql = "update ktbm_evaluation_group a set a.status=0 where a.orderId='"+orderId+"' and a.status=1";
			sqls.add(sql);
			//删除员工的评论信息
			sql = "update ktbm_evaluation_user a set a.status=0 where a.orderId='"+orderId+"' and a.status=1";
			sqls.add(sql);
			String groupId = this.getStringParam(orderevaluationgroup, "groupId");//分公司id
			Double groupStar = this.getDoubleParam(orderevaluationgroup, "groupStar");//星级
			String groupdetails = this.getStringParam(orderevaluationgroup, "details");//描述
			sql = "insert into ktbm_evaluation_group(groupId,orderId,groupStar,details,operId,operTime,status) "
				 + "values('"+groupId+"','"+orderId+"','"+groupStar+"','"+groupdetails+"','"+operId+"','"+operTime+"',1)";
			sqls.add(sql);
			//判断是否已评价过
			sql = "select count(1) from ktbm_evaluation_user a where a.orderId=? and a.groupId=? ";
			if(dao.getCount(sql, new Object[]{orderId,groupId})==1){
				Map<String,Object> pointmap = new HashMap<String, Object>();
				pointmap.put("methodType", "2");
				pointmap.put("columnName", "evaluate");
				Integer point = pointService.getPoints(pointmap);
				String source = "1";//积分来源  1：评价 2：一级分销扫码 3：二级分销扫码
				String type = "2";//积分类型  1：员工 2 客户
				//插入客户获得的积分记录信息
				sql = "insert into ktbm_points(type,source,point,cusId,createTime,wechatId,orderId) "
				    + "values('"+type+"','"+source+"','"+point+"','"+operId+"','"+operTime+"','"+operId+"','"+orderId+"')";
				sqls.add(sql);
				//更新客户总积分
				sql = "update ktbm_customer a set a.points=(a.points+"+point+") where a.openId='"+operId+"' ";
				sqls.add(sql);
			}
			List<Map<String,Object>> orderevaluationusers = (List<Map<String, Object>>) dataMap.get("orderevaluationusers");//评价修理人信息
			String userId = "";
			Double userStar = 0.0;
			String userdetails = "";
			for(Map<String,Object> evaluationusermap : orderevaluationusers){
				orderId = this.getStringParam(evaluationusermap, "orderId");//订单id
				userId = this.getStringParam(evaluationusermap, "userId");//员工id
				userStar = this.getDoubleParam(evaluationusermap, "userStar");//星级
				userdetails = this.getStringParam(evaluationusermap, "details");//描述
				sql = "insert into ktbm_evaluation_user(orderId,userId,userStar,details,operId,operTime,status) "
					 + "values('"+orderId+"','"+userId+"','"+userStar+"','"+userdetails+"','"+operId+"','"+operTime+"',1)";
				sqls.add(sql);
			}
			dao.batchUpdate(ListZhArray(sqls));
			map.put("success", true);
			map.put("msg", "操作成功！");
		}catch(Exception e){
			System.out.println("---操作失败:"+e.toString());
			map.put("success", true);
			map.put("msg", "操作失败！");
		}
		return map;
	}
	
	@Override
	public List<Map<String, Object>> getBoundType(String key) {
		String sql = "select id,t_value as name from dictionary where parent = (select id from dictionary where t_key = ?)";
		List<Map<String, Object>> list = dao.getList(sql, new Object[]{key});
		return list;
	}
}
