package lc.activiti.service;

import lc.activiti.model.SubApprovalModel;

public interface ZTOrderService {

	boolean approvalReject(SubApprovalModel appModel);

	boolean approvalSuccess(SubApprovalModel appModel);

	boolean audtiReject(SubApprovalModel appModel);

	boolean submitApply(SubApprovalModel appModel);

	boolean audtiSuccess(SubApprovalModel appModel);

}
