package lc.activiti.lcenum;

public enum ActivitiCommentType {
	
	/**
	 * 审批驳回
	 */
	ApprovalRejectComment("ApprovalRejectComment","审批驳回说明"),
	/**
	 * 审核驳回说明
	 */
	AuditRejectComment("AuditRejectComment","审核驳回说明");
	
	private String type;
	private String desc;
	private ActivitiCommentType(String _type,String _desc) {
		this.setType(_type);
		this.setDesc(_desc);
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static String getActivitiCommentTypeStatusDesc(String type) {
		for(ActivitiCommentType aCommentType:ActivitiCommentType.values()) {
			if (aCommentType.type.equals(type)) {
				return aCommentType.desc;
			}
		}
		return "类型错误";
	}
}
