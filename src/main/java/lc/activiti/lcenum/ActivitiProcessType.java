package lc.activiti.lcenum;


/**
 * 流程种类
 * @author Raytine
 *
 */
public enum ActivitiProcessType {
	ContactApprovalProcess("contractApprovalProcess","合同审批流程"),
	ContactBrowseApprovalProcess("browseApprovalSuccessUserInfo","合同浏览审批流程"),
	EamilTask("emailTaskProcess","邮件任务流程"),
	CommonApprovalProcess("commonApprovalProcess","金砖服务审批流程");
	
	private String processKey;
	private String processDesc;
	
	private ActivitiProcessType(String _processKey,String _processDesc) {
		setProcessKey(_processKey);
		setProcessDesc(_processDesc);
	}
	
	public static String getActivitiProcessTypeDesc(String processKey) {
		for(ActivitiProcessType contractStatus:ActivitiProcessType.values()) {
			if (contractStatus.processKey.equals(processKey)) {
				return contractStatus.processDesc;
			}
		}
		return "状态错误";
	}

	public String getProcessKey() {
		return processKey;
	}

	private void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getProcessDesc() {
		return processDesc;
	}

	private void setProcessDesc(String processDesc) {
		this.processDesc = processDesc;
	}
}
