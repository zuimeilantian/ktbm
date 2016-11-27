package com.danze.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;


import org.springframework.jdbc.datasource.DataSourceTransactionManager;

public class CircleDAO{
	String sqlMapKey;
	private JdbcTemplate jdbcTemplate;
	private DataSourceTransactionManager transactionManager;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.transactionManager = new DataSourceTransactionManager(dataSource);
    }
	public void NamedCUD(String sql,Map<String,Object> payload){
		this.namedJdbcTemplate.update(sql, payload);
	}
	public void CUD(String sql,Object[] param){
		this.jdbcTemplate.update(sql, param);
	}
	public int NamedCUDByTableName(String tableName,Map<String,Object> parameters){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(tableName).usingGeneratedKeyColumns("id");
        return insert.execute(parameters);
	}
	public void NamedUByTableName(String tableName,Map<String,Object> parameters,String pk){
		String sql = " update "+tableName+" set ";
		int i=0;
        for(String key:parameters.keySet()){
        	i++;
        	if(!key.equals(pk)){
        		sql+=key+"=:"+key;
        		if(i<parameters.keySet().size()){
        			sql+=",";
        		}
        	}
        }
        sql += " where "+pk+"="+parameters.get(pk);
		this.namedJdbcTemplate.update(sql, parameters);
	}
	public long NamedCUDHoldId(String tableName,Map<String,Object> parameters){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(tableName).usingGeneratedKeyColumns("id");
        final Number key = insert.executeAndReturnKey(parameters);
        final long pk = key.longValue();
		return pk;
	}
	public int getFlag(String sql,Object[] parameters){
		return this.jdbcTemplate.queryForObject(sql, parameters, Integer.class);
	}
	@SuppressWarnings("unchecked")
	public Object getObject(String sql,Object[] parameters,Class clazz){
		try {
			return this.jdbcTemplate.queryForObject(sql, parameters, clazz);
		}catch(Exception e){
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public Object getObject(String sql,Object[] parameters){
		try {
			return this.jdbcTemplate.queryForObject(sql, parameters, Object.class);
		}catch(Exception e){
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getNamedObject(String sql,Map<String,Object> parameters,Class<T> clazz){
		try {
			return this.namedJdbcTemplate.queryForObject(sql, parameters, clazz);
		}catch(Exception e){
			return null;
		}
	}
	public Integer getCount(String sql,Object[] parameters){
		try {
			return this.jdbcTemplate.queryForObject(sql, parameters, Integer.class);
		} catch (Exception e) {
			return null;
		}
	}
	public Integer getNamedCount(String sql,Map<String,Object> parameters){
		try {
			return this.namedJdbcTemplate.queryForObject(sql, parameters, Integer.class);
		} catch (Exception e) {
			return null;
		}
	}
	public Map<String, Object> getMap(String sql,Object[] parameters){
		try {
			return this.jdbcTemplate.queryForMap(sql,parameters);
		} catch (Exception e) {
			return new HashMap<String, Object>();
		}
	}
	public Map<String, Object> getMapById(String sql,int id){
		return this.jdbcTemplate.queryForMap(sql,id);
	}
	public Map<String, Object> getNamedMap(String sql,Map<String,Object> parameters){
		return this.namedJdbcTemplate.queryForMap(sql,parameters);
	}
	public List<Object> getObjectList(String sql,Object[] parameters,Class elementType){
		try {
			return this.jdbcTemplate.queryForList(sql, parameters, elementType);
		} catch (Exception e) {
			return new ArrayList<Object>();
		}
	}
	public List<Object> getObjectList(String sql,Object[] parameters){
		return this.jdbcTemplate.queryForList(sql, parameters, Object.class);
	}
	public List<Map<String,Object>> getList(String sql,Object[] parameters){
		try {
			return this.jdbcTemplate.queryForList(sql,parameters);
		} catch (Exception e) {
			return new ArrayList<Map<String,Object>>();
		}
	}
	public List<Map<String,Object>> getListById(String sql,int id){
		List<Map<String,Object>> result = this.jdbcTemplate.queryForList(sql,id);
		return result;
	}
	public List<Map<String,Object>> getNamedList(String sql,Map<String,Object> parameters){
		return this.namedJdbcTemplate.queryForList(sql,parameters);
	}
	public DataSource getDataSource() {
		return this.jdbcTemplate.getDataSource();
	}
	public int[] batchUpdate(String[] sql){
		return this.jdbcTemplate.batchUpdate(sql);
		
	}
	public int[] namedBatchUpdate(String sql, Map<String,Object>[] batchValues){
		return this.namedJdbcTemplate.batchUpdate(sql, batchValues);
	}
	
	public List<Object> getNamedObjectList(String sql, Map<String, Object> param){
		return this.namedJdbcTemplate.queryForList(sql, param, Object.class);
	}
	public boolean flagTableExist(String tableKey){
		boolean ret = true;
		try{
			Integer count = this.jdbcTemplate.queryForObject("select count(1) from information_schema.tables WHERE table_name ='"
		+tableKey+"'",null,Integer.class);
			return count>0;
		}catch(Exception e){
			return false;
		}
	}
}