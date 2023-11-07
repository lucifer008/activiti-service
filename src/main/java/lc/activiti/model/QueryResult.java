package lc.activiti.model;

import java.io.Serializable;
import java.util.List;

public class QueryResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4535483762057467867L;
	private Integer currentPage;
	private Integer fixedCount;
	private List<List<String>> listExt;
	private List<List<String>> statisticsExt;
	private Integer totalCount;
	private Integer totalPage;
	private Object statModel;
	
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getFixedCount() {
		return fixedCount;
	}
	public void setFixedCount(Integer fixedCount) {
		this.fixedCount = fixedCount;
	}
	public List<List<String>> getListExt() {
		return listExt;
	}
	public void setListExt(List<List<String>> listExt) {
		this.listExt = listExt;
	}
	public List<List<String>> getStatisticsExt() {
		return statisticsExt;
	}
	public void setStatisticsExt(List<List<String>> statisticsExt) {
		this.statisticsExt = statisticsExt;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public Object getStatModel() {
		return statModel;
	}
	public void setStatModel(Object statModel) {
		this.statModel = statModel;
	}
	
}
