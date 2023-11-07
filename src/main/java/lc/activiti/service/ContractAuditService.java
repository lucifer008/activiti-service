package lc.activiti.service;

import lc.activiti.model.SubApprovalModel;

public interface ContractAuditService {
	void auditSuccess(SubApprovalModel sModel);
	void auditReject(SubApprovalModel sModel);
	void contractBrowseAuditSuccess(SubApprovalModel appModel);
	void contractBrowseAuditReject(SubApprovalModel appModel);
	void financeApprovalSuccess(SubApprovalModel subModel);
	void financeApprovalReject(SubApprovalModel subModel);
	void legalReject(SubApprovalModel subModel);
	void legalSuccess(SubApprovalModel subModel);
	void lineGroupAuditSuccess(SubApprovalModel subModel);
	void lineGroupAuditReject(SubApprovalModel subModel);
}
