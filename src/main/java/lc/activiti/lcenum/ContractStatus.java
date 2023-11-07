package lc.activiti.lcenum;

/**
 * 合同状态
 * @author Raytine
 *
 */
public enum ContractStatus {
	NewCreate((short)1,"未提交"),
	Apploy((short)2,"已提交"),
	Approvaled((short)3,"已审批"),
	Audit((short)4,"已审核"),
	Mailed((short)5,"已邮寄"),
	Filed((short)6,"已归档"),
	Reject((short)-1,"已驳回"),
	Withdraw((short)-3,"撤回申请"),
	/**
	 * 财务审批通过
	 */
	FinanceSuccess((short)9,"财务审批通过"),
	/**
	 * 法务审批通过
	 */
	LineGroupSuccess((short)10,"线路组审核通过");
	private Short status;
	private String desc;
	private ContractStatus(Short _status,String _desc) {
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
		for(ContractStatus contractStatus:ContractStatus.values()) {
			if (contractStatus.status.equals(status)) {
				return contractStatus.desc;
			}
		}
		return "状态错误";
	}
}
