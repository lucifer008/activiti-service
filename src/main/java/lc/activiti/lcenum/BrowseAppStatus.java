package lc.activiti.lcenum;

public enum BrowseAppStatus {
	NewCreate((short)1,"未提交"),
	Apploy((short)2,"已提交"),
	Approvaled((short)3,"已审批"),
	Audit((short)4,"已审核"),
	
	Reject((short)-1,"已驳回");
	
	private Short status;
	private String desc;
	private BrowseAppStatus(Short _status,String _desc) {
		this.setStatus(_status);
		this.setDesc(_desc);
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public static String getBrowseAppStatusDesc(Integer status) {
		for(BrowseAppStatus contractStatus:BrowseAppStatus.values()) {
			if (contractStatus.status.equals(status)) {
				return contractStatus.desc;
			}
		}
		return "状态错误";
	}
}
