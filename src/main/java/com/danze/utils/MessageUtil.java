package com.danze.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class MessageUtil {

	private static Logger log = LoggerFactory.getLogger(MessageUtil.class);
	
	public static Map<String, String> parseXml(HttpServletRequest request) {
		
		Map<String, String> map = new HashMap<String, String>();
		//BufferedInputStream bis = new BufferedInputStream();
		BufferedReader bf = null;
		String result = "";
		try {
			bf = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = "";
			while ((line = bf.readLine()) != null) {
				result += line + "\r\n";
			}
			//System.out.println(result);
			map = xml2Map(result);
		} catch (IOException e) {
			log.error(e.toString());
			e.printStackTrace();
		}
		finally {
			HttpUtils.close(bf, null);
		}
		return map;
	}
	
	public static Map<String,String> xml2Map(String xmlString){
		Map<String, String> m = new HashMap<String, String>();
		DocumentBuilder db = null;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			StringReader reader = new StringReader(xmlString);
			InputSource source = new InputSource(reader);
			Document document = db.parse(source);
			Element root = document.getDocumentElement() ;
			NodeList list = root.getChildNodes();//获得page元素
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				String name = node.getNodeName();
				String value = node.getTextContent();
				m.put(name, value);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return m;
	}
}
