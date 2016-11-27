package com.danze.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 空调保姆订单管理
 * @author ws
 */
public interface FilesService {
	
	List<Map<String, Object>> getList(Object tableName, Object id, String pk);

	Object update(Map<String, Object> raw, String pk, Object tableName);

	ResponseEntity<?> download(Object id, HttpServletRequest request, ResourceLoader resourceLoader);

	Map<String, Object> handleFileUpload(MultipartFile file, HttpServletRequest request) throws IllegalStateException, IOException;
	
}
