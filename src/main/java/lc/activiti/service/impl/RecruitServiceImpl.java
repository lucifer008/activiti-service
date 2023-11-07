package lc.activiti.service.impl;

import lc.activiti.dao.ICommonQueryDao;
import lc.activiti.dao.base.IDepartmentBaseDao;
import lc.activiti.dao.base.IRecruitBaseDao;
import lc.activiti.entity.Departments;
import lc.activiti.entity.RecruitBase;
import lc.activiti.entity.Users;
import lc.activiti.lcenum.ContractProcessStatus;
import lc.activiti.lcenum.RecruitStatus;
import lc.activiti.lcenum.RoleType;
import lc.activiti.model.SubApprovalModel;
import lc.activiti.service.NewRolesService;
import lc.activiti.service.RecruitService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class RecruitServiceImpl implements RecruitService {

    final static String recruitProcessName = "招聘审批流程";
    final static String recruitProcessBPMN = "recruitProcess.bpmn";
    final static String processDefinitKey = "recruitProcess";
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private IRecruitBaseDao recruitBaseDao;

    @Autowired
    private NewRolesService newRolesService;
    @Override
    public void deleteRecruitProcess() {

    }

    @Override
    public void deployRecruitProcess() {
        processEngine.getRepositoryService().createDeployment().name(recruitProcessName)
                .addClasspathResource(recruitProcessBPMN).deploy();
        log.info("[{0}]部署成功!", recruitProcessName);
    }

    /**
     * 提交审批
     *
     * @return
     * @author lucifer
     * @date 2019/9/17 20:22
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    @Override
    public void submitRecruitApproval(SubApprovalModel approvalModel) {
        validataBySubmit(approvalModel);
        List<SubApprovalModel> nextApprovalList = getFirstApprovalList(approvalModel);
        if (nextApprovalList.size() == 0) {
            throw new RuntimeException("招聘信息的顶级部门无负责人!");
        }
        Map<String, Object> vaiarbles = new HashMap<String, Object>();
        vaiarbles.put("status", RecruitStatus.Applyed.getStatus());
        vaiarbles.put("subApprovalModel", approvalModel);//提交人信息
        vaiarbles.put("nextApprovalList", nextApprovalList);//下级审批人
        long taskCount = processEngine.getTaskService().createTaskQuery()
                .processInstanceBusinessKey(approvalModel.getBusinessId()).count();
        if (taskCount == 1) {
            log.info("驳回再提交,业务Id={}", approvalModel.getBusinessId());
            // 驳回再提交
            Task task = processEngine.getTaskService().createTaskQuery()
                    .processInstanceBusinessKey(approvalModel.getBusinessId()).singleResult();
            processEngine.getTaskService().setAssignee(task.getId(), approvalModel.getUserId());
            processEngine.getTaskService().complete(task.getId(), vaiarbles);
            resetRejectRecruit(approvalModel.getBusinessId());
        } else {
            log.info("【招聘审批流程】申请提交,业务Id={}", approvalModel.getBusinessId());
            processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitKey,
                    approvalModel.getBusinessId(), vaiarbles);
        }
        correctRecruit(approvalModel.getBusinessId(), RecruitStatus.Applyed);
    }

    @Autowired
    private IDepartmentBaseDao departmentBaseDao;

    private List<SubApprovalModel> getFirstApprovalList(SubApprovalModel approvalModel) {
        List<SubApprovalModel> nextApprovalUserList = new ArrayList<>();
        RecruitBase recruit = recruitBaseDao.selectByPrimaryKey(approvalModel.getBusinessId());
        if (StringUtils.isBlank(recruit.getDepId1())) {
            throw new RuntimeException(String.format("业务Id-->%s的顶级部门为空!", recruit.getRecruitId()));
        }
        Departments departments = departmentBaseDao.selectByPrimaryKey(recruit.getDepId1());
        if (null == departments) {
            throw new RuntimeException(String.format("部门Id-->%s不存在!", recruit.getDepId1()));
        }
        if (StringUtils.isBlank(departments.getDepPersonId())) {
            throw new RuntimeException(String.format("业务Id-->%s的部门负责人为空!", recruit.getRecruitId()));
        }
        SubApprovalModel nextApprovalUser = new SubApprovalModel();
        nextApprovalUser.setUserId(departments.getDepPersonId());
        nextApprovalUserList.add(nextApprovalUser);
        return nextApprovalUserList;
    }

    private void resetRejectRecruit(String businessId) {
        RecruitBase recruit = recruitBaseDao.selectByPrimaryKey(businessId);
        recruit.setReject(null);
        recruit.setRejectMemo(null);
        recruit.setRejectUserId(null);
        recruitBaseDao.updateByPrimaryKey(recruit);
    }

    private void correctRecruit(String businessId, RecruitStatus recruitStatus) {
        RecruitBase recruit = recruitBaseDao.selectByPrimaryKey(businessId);
        recruit.setApprovalStatus(recruitStatus.getStatus().shortValue());
        recruitBaseDao.updateByPrimaryKey(recruit);
    }

    /**
     * @return 招聘审批通过
     * @author lucifer
     * @date 2019/9/18 15:31
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    @Override
    public void approvalSuccess(SubApprovalModel approvalModel) {
        boolean isExistApprovalTask = isExistApprovalTask(approvalModel);
        if (!isExistApprovalTask) {
            throw new RuntimeException("无审批可操作!");
        }
        Task task = processEngine.getTaskService().createTaskQuery()
                .processInstanceBusinessKey(approvalModel.getBusinessId()).singleResult();
        String taskDefinitionKey = task.getTaskDefinitionKey();
        Map<String, Object> vaiarbles = new HashMap<>();
        boolean isLastTask = currentApprovalTaskIsLastTask(approvalModel);
        if (isLastTask) {
            correctRecruit(approvalModel.getBusinessId(), RecruitStatus.Approvaled);
        }
        if (!isLastTask) {
            boolean surpassLimits = isSurpassLimits(approvalModel);
            //CEO审批，下级人事审批
            if ("taskCEOApproval".equals(taskDefinitionKey)) {
                List<SubApprovalModel> nextApprovalUserList = getNextApprovalUserList(RoleType.Personnel);
                vaiarbles.put("nextApprovalList", nextApprovalUserList);
            }//超编且是部门总裁审批,下级审批人为CEO角色
            else if (surpassLimits && "taskTopDepartLeaderApproval".equals(taskDefinitionKey)) {
                List<SubApprovalModel> nextApprovalUserList = getNextApprovalUserList(RoleType.CEO);
                vaiarbles.put("nextApprovalList", nextApprovalUserList);
            }
            //不超编是部门总裁审批，下级审批为人事角色
            else if (!surpassLimits && "taskTopDepartLeaderApproval".equals(taskDefinitionKey)) {
                List<SubApprovalModel> nextApprovalUserList = getNextApprovalUserList(RoleType.Personnel);
                vaiarbles.put("nextApprovalList", nextApprovalUserList);
            }

            vaiarbles.put("isPass", true);
            vaiarbles.put("surpassLimits", surpassLimits);
            processEngine.getTaskService().setAssignee(task.getId(), approvalModel.getUserId());
            processEngine.getTaskService().complete(task.getId(), vaiarbles);
        }
    }

    /**
     * 是否超编
     *
     * @return
     * @author lucifer
     * @date 2019/9/17 20:52
     */
    boolean isSurpassLimits(SubApprovalModel approvalModel) {
        return false;
    }

    @Autowired
    private ICommonQueryDao commonQueryDao;

    private List<SubApprovalModel> getNextApprovalUserList(RoleType _roleType) {
        Short roleType = _roleType.getRoleType();
        List<Users> users =commonQueryDao.selectUsersByRoleType(new Integer(roleType));
//        users = newRolesService.queryUsersByRoleName(_roleType.getDesc());
        List<SubApprovalModel> nextApprovalUserList = new ArrayList<>();
        for (Users user : users
                ) {
            SubApprovalModel approvalModel = new SubApprovalModel();
            approvalModel.setUserId(user.getUserId());
            nextApprovalUserList.add(approvalModel);
        }
        if (nextApprovalUserList.size()==0){
            throw new RuntimeException("找不到下级审批人!");
        }
        return nextApprovalUserList;
    }

    boolean currentApprovalTaskIsLastTask(SubApprovalModel approvalModel) {
        boolean isLastTask = false;
        long taskCount = processEngine.getTaskService().createTaskQuery()
                .taskCandidateUser(approvalModel.getUserId())
                .processInstanceBusinessKey(approvalModel.getBusinessId())
                .count();
        if (taskCount > 1) {
            throw new RuntimeException("审批异常!");
        }
        Task task = processEngine.getTaskService().createTaskQuery()
                .taskCandidateUser(approvalModel.getUserId())
                .processInstanceBusinessKey(approvalModel.getBusinessId()).singleResult();
        if (task.getId().equals("taskPersonalApproval")) {
            return true;
        }
        return isLastTask;
    }

    boolean isExistApprovalTask(SubApprovalModel approvalModel) {
        long taskCount = processEngine.getTaskService().createTaskQuery()
                .taskCandidateUser(approvalModel.getUserId())
                .processInstanceBusinessKey(approvalModel.getBusinessId())
                .count();
        if (taskCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * @return 招聘驳回
     * @author lucifer
     * @date 2019/9/18 15:31
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    @Override
    public void approvalReject(SubApprovalModel appModel) {
        validata(appModel);
        if (StringUtils.isBlank(appModel.getReason())) {
            throw new RuntimeException("驳回原因不能为空!");
        }
        RecruitBase recruit = recruitBaseDao.selectByPrimaryKey(appModel.getBusinessId());
        Short approvalStatus = recruit.getApprovalStatus();
        if (approvalStatus.equals(RecruitStatus.Reject.getStatus().shortValue())) {
            throw new RuntimeException(String.format("业务Id=%s 申请已驳回!", appModel.getBusinessId()));
        }
        boolean isExistApprovalTask = isExistApprovalTask(appModel);
        if (!isExistApprovalTask) {
            throw new RuntimeException("无审批可操作!");
        }
        Map<String, Object> vaiarbles = new HashMap<>();
        Task task = processEngine.getTaskService().createTaskQuery()
                .processInstanceBusinessKey(appModel.getBusinessId()).singleResult();

        vaiarbles.put("isPass", false);
        vaiarbles.put("status",RecruitStatus.Reject.getStatus());
        processEngine.getTaskService().addComment(task.getId(), task.getProcessInstanceId(), appModel.getReason());
        processEngine.getTaskService().setAssignee(task.getId(), appModel.getUserId());
        processEngine.getTaskService().complete(task.getId(), vaiarbles);
        recordRejectInfo(appModel);

    }

    /**
     * @return 招聘撤回
     * @author lucifer
     * @date 2019/9/18 15:28
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    @Override
    public void withdrawApply(SubApprovalModel appModel) {
        validata(appModel);
        RecruitBase recruit = recruitBaseDao.selectByPrimaryKey(appModel.getBusinessId());
        Short approvalStatus = recruit.getApprovalStatus();
        if (approvalStatus.equals(RecruitStatus.Approvaled.getStatus().shortValue())) {
            throw new RuntimeException(String.format("业务Id=%s 申请已审批不能撤回!", appModel.getBusinessId()));
        }
        boolean nextTaskIsDepartHeaderNonSuccess = processEngine.getTaskService().createTaskQuery()
                .processInstanceBusinessKey(appModel.getBusinessId()).list().stream().filter(task -> !task.getId().equals("taskTopDepartLeaderApproval")).count() == 0;
        if (nextTaskIsDepartHeaderNonSuccess) {
            throw new RuntimeException(String.format("业务Id=%s 申请已审批不能撤回!", appModel.getBusinessId()));
        }
        Task task = processEngine.getTaskService().createTaskQuery()
                .processInstanceBusinessKey(appModel.getBusinessId()).singleResult();
//		SubApprovalModel subModel = (SubApprovalModel) processEngine.getTaskService().getVariable(task.getId(),
//				"subApprovalModel");
        task.setName("撤回合同");
        processEngine.getTaskService().saveTask(task);
        appModel.setReason("撤回合同");
        Map<String, Object> vaiables = new HashMap<>();
        vaiables.put("isPass", false);
        vaiables.put("status", RecruitStatus.Withdraw.getStatus());
        processEngine.getTaskService().addComment(task.getId(), task.getProcessInstanceId(),
                appModel.getReason());
        processEngine.getTaskService().setAssignee(task.getId(), appModel.getUserId());
        processEngine.getTaskService().complete(task.getId(), vaiables);
        correctRecruit(appModel.getBusinessId(), RecruitStatus.Withdraw);
    }

    private void recordRejectInfo(SubApprovalModel appModel) {
        RecruitBase recruit = recruitBaseDao.selectByPrimaryKey(appModel.getBusinessId());
        recruit.setReject(new Date());
        recruit.setRejectMemo(appModel.getReason());
        recruit.setRejectUserId(appModel.getReason());
        recruit.setApprovalStatus(RecruitStatus.Reject.getStatus().shortValue());
        recruitBaseDao.updateByPrimaryKey(recruit);
    }

    private void validata(SubApprovalModel appModel) {
        if (StringUtils.isBlank(appModel.getBusinessId())) {
            throw new RuntimeException("业务Id为空！");
        }
        if (StringUtils.isBlank(appModel.getUserId())) {
            throw new RuntimeException("提交人用户Id为空！");
        }
        RecruitBase recruit = recruitBaseDao.selectByPrimaryKey(appModel.getBusinessId());
        if (null == recruit) {
            throw new RuntimeException(String.format("业务Id=%s 不存在!", appModel.getBusinessId()));
        }
    }

    private void validataBySubmit(SubApprovalModel appModel) {
        validata(appModel);
        RecruitBase recruit = recruitBaseDao.selectByPrimaryKey(appModel.getBusinessId());
        Short approvalStatus = recruit.getApprovalStatus();
        if (!approvalStatus.equals(RecruitStatus.New.getStatus().shortValue()) && !approvalStatus.equals(RecruitStatus.Reject.getStatus().shortValue()) && !approvalStatus.equals(RecruitStatus.Withdraw.getStatus().shortValue())) {
            throw new RuntimeException(String.format("业务Id=%s 申请已提交不能重复提交!", appModel.getBusinessId()));
        }

    }
}
