package lc.activiti.model;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Map;

public class QueryModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7258482028120941590L;
	private Map<String, String> searchKeys;
	private Integer pageNum;
	private Integer pageSize;
	private String searchSort;
	private String searchId;
	private String userId;
	private String corpId;
	public Map<String, String> getSearchKeys() {
		return searchKeys;
	}
	public void setSearchKeys(Map<String, String> searchKeys) {
		this.searchKeys = searchKeys;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getSearchSort() {
		return searchSort;
	}
	public void setSearchSort(String searchSort) {
		this.searchSort = searchSort;
	}
	public String getSearchId() {
		return searchId;
	}
	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	
}
