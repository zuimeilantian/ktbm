/**
 * JsonResult.java
 * *@author 董春滔
 * 2015年12月25日 上午4:32:09
 */
package com.danze.utils;

public class JsonResult {
	private Boolean success;
	private String msg;
	private Object data;
	
	public JsonResult(){
		
	}
	
	public JsonResult(boolean isSuccess){
		this.success = isSuccess;
	}

	/**
	 * @return the success
	 */
	public Boolean getSuccess() {
		return success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(Boolean success) {
		this.success = success;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	public void setSuccess(String msg) {
		this.success = true;
		this.msg = msg;
	}

	public void setFail(String msg) {
		this.success = false;
		this.msg = msg;
	}
}
