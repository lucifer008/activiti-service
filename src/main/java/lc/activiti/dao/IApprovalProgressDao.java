package lc.activiti.dao;

import org.apache.ibatis.annotations.Param;

public interface IApprovalProgressDao {
	void updateApprovalProcessNodesByReject(@Param("businessId")String businessId);
	void updateApprovalProcessNodesByPass(@Param("businessId")String businessId,@Param("applyUserId") String applyUserId);
	void deleteApprovalProcessNodes(@Param("businessId")String businessId);
}