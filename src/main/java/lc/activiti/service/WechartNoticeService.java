package lc.activiti.service;

public interface WechartNoticeService {
	void pushNoticeCounterSignPersons(String contractId);
	void pushNoticeNextApprovalUser();
	void pushApprovalResultToApplyPerson(String contractId,String approvalPerson,String applyUserId,boolean isApprovalSuccess,String suggestion);
}
