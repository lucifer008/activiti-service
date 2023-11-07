package lc.activiti.lcenum;

/**
 * 合同状态
 * @author Raytine
 *
 */
public enum ZTOrdersStatus {
	NewCreate((short)1,"待添加"),
	WaitSubmit((short)2,"待提交"),
	Submited((short)3,"待审核/已提交"),
	Audited((short)4,"已审核"),
	AuditedReject((short)5,"审核驳回"),
	Approvaled((short)6,"已审批"),
	ApprovaledReject((short)7,"审批驳回"),
	Gived((short)8,"已赠送"),
	/**
	 * 驳回再提交
	 */
	RejectSubmit((short)11,"驳回再提交");
	private Short status;
	private String desc;
	private ZTOrdersStatus(Short _status,String _desc) {
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
	public static String getExpsenseDesc(Integer status) {
		for(ZTOrdersStatus contractStatus:ZTOrdersStatus.values()) {
			if (contractStatus.status.equals(status)) {
				return contractStatus.desc;
			}
		}
		return "状态错误";
	}
}
