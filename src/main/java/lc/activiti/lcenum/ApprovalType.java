package lc.activiti.lcenum;

/**
 * 审批类型
 * @author Raytine
 *
 */
public enum ApprovalType {
	ContractApproval((short)1,"合同审批");
	private Short type;
	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private String desc;
	private ApprovalType(Short _type,String _desc) {
		this.setType(_type);
		this.setDesc(_desc);
	}
	
	public static String getApprovalDesc(Integer type) {
		for(ApprovalType approvalType:ApprovalType.values()) {
			if (approvalType.type.equals(type)) {
				return approvalType.desc;
			}
		}
		return "类型错误";
	}
}
