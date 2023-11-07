package lc.activiti.lcenum;

/**
 * 押金状态
 * @author Raytine
 *
 */
public enum ExpenseStatus {
	NewCreate((short)0,"待申请"),
	Applyed((short)1,"已申请"),
	Approvaled((short)2,"已审批"),
	Audited((short)3,"已审核"),
	Payed((short)4,"已支付"),
	Withdrawn((short)5,"已撤回"),
	Reject((short)-1,"已驳回");
	private Short status;
	private String desc;
	private ExpenseStatus(Short _status,String _desc) {
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
		for(ExpenseStatus contractStatus:ExpenseStatus.values()) {
			if (contractStatus.status.equals(status)) {
				return contractStatus.desc;
			}
		}
		return "状态错误";
	}
}
