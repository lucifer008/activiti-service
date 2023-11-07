package lc.activiti.service;

import lc.activiti.model.SubApprovalModel;

public interface ContractApprovalService {

	void submitApproval(SubApprovalModel subModel);
	void approvalSuccess(SubApprovalModel subApprovalModel);
	void rejectApproval(SubApprovalModel subApprovalModel);
	
	void browseApprovalReject(SubApprovalModel appModel);
	void browseApprovalSuccess(SubApprovalModel appModel);
	void browseAppSubmit(SubApprovalModel appModel);
	void withdrawApply(SubApprovalModel subModel);
	
}
