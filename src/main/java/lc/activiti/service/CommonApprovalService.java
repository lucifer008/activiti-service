package lc.activiti.service;

import lc.activiti.model.SubApprovalModel;

public interface CommonApprovalService {
	/**
	 * 提交订单
	 */
	boolean submitApply(SubApprovalModel appModel);

	/**
	 * 审核通过
	 * @param appModel
	 * @return
	 */
	boolean auditSuccess(SubApprovalModel appModel);

	/**
	 * 审核驳回
	 * @param appModel
	 * @return
	 */
	boolean auditReject(SubApprovalModel appModel);

	/**
	 * 审批通过
	 */
	boolean approvalSuccess(SubApprovalModel appModel);

	/**
	 * 审批驳回
	 * @param appModel
	 * @return
	 */
	boolean approvalReject(SubApprovalModel appModel);
	/**
	 * 生成审批流程节点
	 * @param applyUserId
	 * @param departmentId
	 * @param isDepartmentHeader 
	 */
	void generatorApprovalProcessNodes(String businessId,String applyUserId,String departmentId, boolean isDepartmentHeader);
	void updateApprovalProcessNodesByReject(String businessId);
	void updateApprovalProcessNodesByPass(String businessId,String applyUserId);
	
}
