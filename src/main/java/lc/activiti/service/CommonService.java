package lc.activiti.service;

import java.util.List;

import lc.activiti.lcenum.RoleType;
import lc.activiti.model.SubApprovalModel;

public interface CommonService {
	/**
	 * 
	 * @param businessId 业务Id
	 * @param depId 业务订单用户所在的部门Id
	 * @return
	 */
	SubApprovalModel getNextApprovalUser(String businessId, String depId);

	SubApprovalModel getNextAuditUser(String businessId, String depId);

	List<SubApprovalModel> getNextAuditUserList(String businessId, String departmentId,RoleType roleType);
	SubApprovalModel additionalUsers(SubApprovalModel subApprovalModel);
	List<SubApprovalModel> getNextApprovalUserList(String businessId, String departmentId,boolean isFilterDepPerson);

}
