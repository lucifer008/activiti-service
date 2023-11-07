package lc.activiti.service;

import lc.activiti.model.SubApprovalModel;

public interface ExpenseApprovalService {
	boolean submitExpesenApply(SubApprovalModel subApprovalModel);
	boolean approvalSuccess(SubApprovalModel subApprovalModel);
}
