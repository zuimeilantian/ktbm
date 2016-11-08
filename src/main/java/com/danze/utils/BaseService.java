package com.danze.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;

/**
 * 公共基础类
 * @author ws
 */
@Service(value="baseService")
public class BaseService {
	
	@Resource
	CircleDAO dao;
	
	public String getStringParam(Map<String,Object> map,String param){
		if(map.get(param)==null){
			return "";
		}else{
			return ""+map.get(param);
		}
	}
	public Integer getIntegerParam(Map<String,Object> map,String param){
		if(map.get(param)==null){
			return 0;
		}else{
			return Integer.valueOf(""+map.get(param));
		}
	}
	
	/**
	 * 获得id
	 * @return
	 */
	public static String getUUID(){ 
        return UUID.randomUUID().toString().replaceAll("-", "");
    } 
	
	/**
	 * 得到今天的日期
	 * @param pattern  格式：如yy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getToday(String pattern)
	{
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}
	
	public static String getRemortIP(HttpServletRequest request) { 
		if (request.getHeader("x-forwarded-for") == null) { 
		return request.getRemoteAddr(); 
		} 
		return request.getHeader("x-forwarded-for"); 
	} 
	
	/**
     * 插入历史表日志记录 
     * 表不存在则新建 表中对应的必须增加 his_oper_id,his_oper_time,oper_ip,oper_content字段 对应的表中对应的必须增加
     * his_oper_id,his_oper_time,oper_ip,oper_content字段 历史表名必须是tableName_his格式，如：t_user_his
     * @return
     */
    public String insertTableLog(HttpServletRequest request,HttpSession session, String table_name, String sqlwhere,
            String oper_content) {
        String oper_id ="";//当前操作人
        String oper_time = this.getToday("yyyy-MM-dd HH:mm:ss");//操作时间
        String ip = getRemortIP(request);//IP
        String table_name_his = table_name + "_his";//历史表名
        String sql = "select count(1) from information_schema.tables a "
				   + " where UPPER(a.table_schema)=UPPER('nt_tffjyw') "
				   + "   and UPPER(a.table_name)=UPPER(?) ";
        int exc = dao.getCount(sql, new Object[]{table_name});
        //获得表的所有列
    	sql = "select GROUP_CONCAT(a.column_name) column_name from information_schema.COLUMNS a where UPPER(a.table_schema)=UPPER('nt_tffjyw') and UPPER(a.table_name)=UPPER(?)";
    	String columns = getStringParam(dao.getMap(sql,new Object[]{table_name}),"column_name");
        if(exc == 0) {// 不存在进行新建表
            sql = "create table " + table_name_his + " "
            	 + " select " + oper_id + " his_oper_id,'"+oper_time+"' his_oper_time,'" + ip + "' ip,'" + oper_content + "' oper_content," + columns
                 + "  from " + table_name + " a where 1=1 " + sqlwhere;
            dao.CUD(sql, null);
            
            sql = "alter table " + table_name_his + " modify column his_oper_id int(11) ";
            dao.CUD(sql, null);
            
            sql = "alter table " + table_name_his + " modify column ip varchar(100)";
            dao.CUD(sql, null);
            
            sql = "alter table " + table_name_his + " modify column oper_content varchar(2000)";
            dao.CUD(sql, null);
            return "";
        } else {// 存在进行插入表数据信息
            sql = "insert into " + table_name_his + "(his_oper_id,his_oper_time,ip,oper_content," + columns + ") "
            	+ "select " + oper_id + ",'"+oper_time+"','" + ip + "','" + oper_content + "'," + columns + " "
                + "  from " + table_name + " a where 1=1 " + sqlwhere;
            return sql;
        }
    }
    
	/**
	 * 插入内容日志记录
	 * @param session 用户信息
	 * @param batch 批处理
	 * @param template_id 模版id
	 * @param com_id 企业/团队id
	 * @param ids 日志记录对应表的记录id
	 * @param obName 操作对象信息
	 */
	public String  insertContentLog(HttpServletRequest request,HttpSession session, String template_id,Integer com_id,String ids,String obName){
		String ip = request.getRemoteAddr();//IP
		String oper_id = "";//当前操作人
		String oper_name = "";//当前操作人
        String oper_time = this.getToday("yyyy-MM-dd HH:mm:ss");//操作时间
		String sql = "insert into t_log_record(id, template_id, log_content, ip, com_id, ids, status, created_by, created_time)  " 
				   + "select fn_getsequence('t_log_record'),t.id,replace(replace(t.log_content, '#操作人#', '"+oper_name+"'), '#操作对象#', '"+obName+"'),"
				   + "'"+ip+"','"+com_id+"','"+ids+"',1,'"+oper_id+"','"+oper_time+"' from t_log_template t where t.id = '"+template_id+"'";
		return sql;
	}
	
}
