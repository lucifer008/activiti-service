package lc.activiti.dao;

import java.util.List;

import lc.activiti.entity.*;
import org.apache.ibatis.annotations.Param;

public interface ICommonQueryDao {
    List<Users> selectUsersByRoleType(@Param("roleTypeId") Integer roleTypeId);

    List<BrowsePermission> selectContracBrowsePerssiontByContractId(@Param("contractId") String contractId);

    Boolean isForensic(@Param("roleTypeId") Integer roleTypeId, @Param("userId") String userId);

    Contract getContractByBrowsePermissionId(@Param("browsePermissionId") String businessId);

    List<WechartNoticeUsersModel> getCounterSignUserList();

    List<WechartNoticeUsers> getNextApprovalUserList(@Param("userIds") List<String> userIds);

    List<Contract> getWaitingCountSignContractList(@Param("contractId") String contractId);

    List<Contract> getWaitingNoticeContractList();

    WechartNoticeUsers getApplyPersonWebchartInfo(@Param("userId") String userId);

    WXAppLoginInfo getWXAppInfo();

    List<Contract> getSumbitedOrApprovaledContractList();

    List<Contract> getAuditedContractList();

    int ResetContractCounterSign(@Param("contractId") String contractId);

    /**
     * @return 部门岗位计划数
     * @author lucifer
     * @date 2019/9/18 14:08
     */
    Long departPostitionPlanNums(RecruitBase recruitBase);

    /**
     * @return 部门岗位在职数
     * @author lucifer
     * @date 2019/9/18 14:33
     */
    Long departPositionOnTheJobNums(RecruitBase recruitBase);
}
