package lc.activiti.lcenum;

public enum HttpRequestStatus {
	Sucess(0,"成功"),Failed(1,"失败");
	private Integer status;
	private String desc;
	private HttpRequestStatus(Integer _status,String _desc) {
		this.setStatus(_status);
		this.setDesc(_desc);
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public static String getHttpRequestStatusDesc(Integer status) {
		for(HttpRequestStatus contractStatus:HttpRequestStatus.values()) {
			if (contractStatus.status.equals(status)) {
				return contractStatus.desc;
			}
		}
		return "状态错误";
	}
	
}
