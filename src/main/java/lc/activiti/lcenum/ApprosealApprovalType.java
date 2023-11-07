package lc.activiti.lcenum;

public enum ApprosealApprovalType {
	//审批类型(1:干线拉直上游合同审批;2: 干线拉直下游合同审批;3: 精准拼车上游合同审批;4: 精准拼车下游合同审批;5: KA上游合同审批;6: KA下游合同审批)
	TrunkLineUpstream((short)1,"干线拉直上游合同审批"),
	TrunkLineDowstream((short)2,"干线拉直下游合同审批"),
	PrecisionCarpoolingUpstream((short)3,"精准拼车上游合同审批"),
	PrecisionCarpoolingDownstream((short)4,"精准拼车下游合同审批"),
	KAUpstream((short)5,"KA上游合同审批"),
	KADownstream((short)6,"KA下游合同审批");
	
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
	private ApprosealApprovalType(Short _type, String _desc) {
		this.setType(_type);
		this.setDesc(_desc);
	}
	public static ApprosealApprovalType getApprovalType(Short type) {
		for(ApprosealApprovalType approvalType: ApprosealApprovalType.values()) {
			if (approvalType.type.equals(type)) {
				return approvalType;
			}
		}
		return null;
	}
	public static String getApprovalTypeDesc(Integer type) {
		for(ApprosealApprovalType approvalType: ApprosealApprovalType.values()) {
			if (approvalType.type.equals(type)) {
				return approvalType.desc;
			}
		}
		return "类型错误";
	}
}
