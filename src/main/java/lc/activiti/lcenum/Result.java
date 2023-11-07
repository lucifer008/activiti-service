package lc.activiti.lcenum;

import java.io.Serializable;

public class Result<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6167771803432445105L;
	
	/**
	 * 0：成功;1.错误
	 */
	private Integer status;
	private T t;
	/***
	 * 描述信息
	 */
	private String message;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public T getT() {
		return t;
	}
	public void setT(T t) {
		this.t = t;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
