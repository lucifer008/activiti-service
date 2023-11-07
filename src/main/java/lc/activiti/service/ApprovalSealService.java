package lc.activiti.service;

import lc.activiti.model.SubApprovalModel;

public interface ApprovalSealService {
	void deployApprovalSealProcess();
	void deleteApprovalSealProcess();
	boolean submitApprovalSealApply(SubApprovalModel approvalModel);
	/*
	 * 印章审批通过
	 * */
	boolean approvalSealSuccess(SubApprovalModel approvalModel);
	/*
	 * 印章审批拒绝
	 * */
	boolean rejectApprovalSeal(SubApprovalModel approvalModel);
}
