package com.danze.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.danze.dao.CircleDAO;
import com.danze.service.FilesService;
import com.danze.utils.BaseService;

@Service(value="filesImpl")
public class FilesImpl extends BaseService implements FilesService {
	@Resource
	CircleDAO dao;

	@Override
	public List<Map<String, Object>> getList(Object tableName, Object id, String pk) {
		if(pk==null||"".equals(pk)) pk = "id";
		return dao.getList("select * from "+tableName+" where "+pk+" in ("+id+")",null);
	}

	@Override
	public Object update(Map<String, Object> raw, String pk, Object tableName) {
		Integer result = 1;
		try{
			String sql = "update "+tableName+" set ";
			if(pk==null||"".equals(pk)) pk="id";
			boolean flag = false;
			for(String key:raw.keySet()){
				if(!key.equals(pk)){
					if(flag){
						sql+=",";
					}else{
						flag=true;
					}
					sql+=key+"=:"+key;
				}
			}
			sql+=" where "+pk+"=:"+pk;
			dao.NamedCUD(sql, raw);
		}catch(Exception e){
			System.out.println(e.toString());
			result = 0;
		}
		return result;
	}

	@Override
	public ResponseEntity<?> download(Object id, HttpServletRequest request, ResourceLoader resourceLoader) {
		String ROOT = request.getSession().getServletContext().getRealPath("/upload");
		if(dao.getCount("select count(1) from _file where id=?", new Object[]{id})>0){
			Map<String,Object> fileMap = dao.getMap("select name from _file where id=?", new Object[]{id});
		    try {
	  			return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, fileMap.get("name")+"").toString()));
	  		} catch (Exception e) {
	  			return ResponseEntity.notFound().build();
	  		}
		} else {
  			return ResponseEntity.notFound().build();
		}
	}

	@Override
	public Map<String, Object> handleFileUpload(MultipartFile file,HttpServletRequest request) throws IllegalStateException, IOException {
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
			fileMap.put("creatorId", "");
			fileMap.put("id",dao.NamedCUDHoldId("_file",fileMap));
			return fileMap;
		} else {
			ret.put("errCode", -1);
			ret.put("errMsg", "no file");
			return ret;
		}
	}
	
}
