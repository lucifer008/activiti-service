package lc.activiti.lcenum;

/*
 * 印章状态
 * */
public enum SealStatus {
	NewCreate((short)1,"未提交"),
	Apploy((short)2,"已申请"),
	Approved((short)3,"已审批"),
	Reject((short)-1,"已驳回");
	
	private Short status;
	private String desc;
	private SealStatus(Short _status,String _desc) {
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
	public static String getSealStatusDesc(Integer status) {
		for(SealStatus sealStatus:SealStatus.values()) {
			if (sealStatus.status.equals(status)) {
				return sealStatus.desc;
			}
		}
		return "状态错误";
	}
}
