package com.danze.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.danze.dao.CircleDAO;
import com.danze.service.DeptService;
import com.danze.utils.MapUtils;

@Service
public class DeptServiceImpl implements DeptService {

	@Resource
	CircleDAO dao;
	
	@Override
	public List<Map<String, Object>> getDept() {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sql = "select id,name, 1 as mainDept from groups where parent_id is null or parent_id = -1 ";
		Map<String, Object> map = dao.getMap(sql, null);
		if(map.size()!=0){
			String parentId = MapUtils.getString(map, "id");
			sql = "select id,name,0 as mainDept from groups where parent_id = ? and flag = 2 ";
			list = dao.getList(sql, new Object[]{parentId});
		}
		list.add(map);
		return list;
	}

}
