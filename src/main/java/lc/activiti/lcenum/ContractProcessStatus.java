package lc.activiti.lcenum;

public enum ContractProcessStatus {
	/**
	 * 流程提交
	 */
	Submit((short)1,"流程提交"),
	/**
	 * 流程通过审批
	 */
	ApprovalSuccess((short)2,"流程通过审批"),
	/**
	 * 流程审批驳回
	 */
	ApprovalReject((short)3,"流程审批驳回"),
	/**
	 * 流程审核通过
	 */
	AuditSuccess((short)4,"流程审核通过"),
	/**
	 * 流程审核驳回
	 */
	AuditReject((short)5,"流程审核驳回"),
	/**
	 * 驳回再提交
	 */
	RejectSubmit((short)6,"驳回再提交"),
	/**
	 * 财务审批通过
	 */
	FinanceSuccess((short)9,"财务审批通过"),
	/**
	 * 法务审批通过
	 */
	LegalSuccess((short)10,"法务审批通过");
	
	private Short status;
	private String desc;
	private ContractProcessStatus(Short _status,String _desc) {
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
	public static String getContractProcessStatusDesc(Short status) {
		for(ContractProcessStatus contractStatus:ContractProcessStatus.values()) {
			if (contractStatus.status.equals(status)) {
				return contractStatus.desc;
			}
		}
		return "状态错误";
	}
}
