package com.danze.service;

import java.util.List;
import java.util.Map;

public interface PointService {
	
	public Map<String, Object> getPointsMethodPageList(Map<String, Object> pointsmethodsch);

	public Map<String, Object> savePointsMethod(Map<String, Object> dataMap);

	public Map<String, Object> getPointsMethodMap(String id);
	
	public Integer getPoints(Map<String, Object> dataMap);

	public Map<String, Object> delPointsMethod(String id);

	public Map<String, Object> getPointsPageList(Map<String, Object> pointssch);

	public Map<String, Object> getTjPointsPageList(Map<String, Object> pointssch);

	public Map<String, Object> savePoints(Map<String, Object> pointsMap);

	public Map<String, Object> getPointRecord(Map<String, Object> map);

}
