package com.danze.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.danze.dao.CircleDAO;
import com.danze.service.FilesService;

/**
 * 空调保姆订单管理
 * @author ws
 */
@Controller
@RequestMapping("/ktbm/filesController")
public class FilesController {
//	@Autowired  
//    private HttpServletRequest request;
	
	@Resource
	CircleDAO dao;
	
	@Resource
	private FilesService filesService;
	
	public static final String filePath = "files/";
	private final ResourceLoader resourceLoader;
	@Autowired
	public FilesController(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	@RequestMapping("/getList/{tableName}/{id}")
	@ResponseBody
	public List<Map<String,Object>> getList(@PathVariable Object tableName,
			@PathVariable Object id,
			@RequestParam(required=false) String pk){
		return filesService.getList(tableName,id,pk);
	}
	
	@RequestMapping("/update/{tableName}")
	@ResponseBody
	public Object update(
			@RequestBody Map<String,Object> raw,
			@RequestParam(required=false) String pk,
			@PathVariable Object tableName){
		return filesService.update(raw,pk,tableName);
	}
	
	@RequestMapping("download/{id}") 
	@ResponseBody
	public ResponseEntity<?> download(@PathVariable Object id,HttpServletRequest request) { 
		return filesService.download(id,request,resourceLoader);
	} 
	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	@ResponseBody
	public Map<String,Object> handleFileUpload(@RequestParam("file") MultipartFile file,HttpServletRequest request)throws IllegalStateException, IOException {
		Map<String,Object> ret = new HashMap<String,Object>();
		if (!file.isEmpty()) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmm");
			String timestamp = sdf.format(date);
			String originalFilename = file.getOriginalFilename();
			if(originalFilename.length()>50){
				originalFilename = originalFilename.substring(0, 50);
			}
			String name = timestamp+"-"+originalFilename;
			file.transferTo(new File(request.getSession().getServletContext().getRealPath("/")+"/upload/files/"+name));
			String suffix = name.substring(name.lastIndexOf(".") + 1);
			String contentType = file.getContentType().split("/")[0];
			Integer type = 0;
			if("image".equals(contentType)){
				type = 1;
			} else if("text".equals(contentType)){
				type = 2;
			} else if("application".equals(contentType)){
				type = 3;
			} else if("video".equals(contentType)){
				type = 4;
			} else if("audio".equals(contentType)){
				type = 5;
			}
			Map<String,Object> fileMap = new HashMap<String,Object>();
			fileMap.put("display", originalFilename);
			fileMap.put("name", name);
			fileMap.put("suffix", suffix);
			fileMap.put("type", type);
			fileMap.put("createdDate", date);
			fileMap.put("size",file.getSize());
			fileMap.put("creatorId", 1);
			fileMap.put("id",dao.NamedCUDHoldId("_file",fileMap));
			return fileMap;
		} else {
			ret.put("errCode", -1);
			ret.put("errMsg", "no file");
			return ret;
		}
	}
}
