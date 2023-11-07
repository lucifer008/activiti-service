package lc.activiti.service;

import lc.activiti.model.SubApprovalModel;

public interface RecruitService {
    /**
     *
     *
      
     * @return 删除招聘流程
     * @author lucifer
     * @date 2019/9/18 15:28
     */
    void deleteRecruitProcess();
    /**
     *
     *
      
     * @return 部署招聘流程 
     * @author lucifer
     * @date 2019/9/18 15:27
     */
    void deployRecruitProcess();
    /**
     *
     *
      
     * @return 提交招聘申请 
     * @author lucifer
     * @date 2019/9/18 15:27
     */
    void submitRecruitApproval(SubApprovalModel subApprovalModel);
    /**
     *
     *
      
     * @return 招聘审批通过 
     * @author lucifer
     * @date 2019/9/18 15:27
     */
    void approvalSuccess(SubApprovalModel approvalModel);
    /**
     *
     *
      
     * @return 招聘审批驳回 
     * @author lucifer
     * @date 2019/9/18 15:27
     */
    void approvalReject(SubApprovalModel approvalModel);
    /**
     *
     *
     * @return 招聘撤回
     * @author lucifer
     * @date 2019/9/18 15:26
     */
    void withdrawApply(SubApprovalModel subModel);
}
