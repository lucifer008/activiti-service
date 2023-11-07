package lc.activiti.listener;

import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;

@Slf4j
public class RecruitPersonallListener implements TaskListener,JavaDelegate {
    private static final long serialVersionUID = 9212593936129572642L;

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Override
    public void execute(DelegateExecution execution) throws Exception {

    }

    @Override
    public void notify(DelegateTask delegateTask) {
        // 组任务
        String taskId = delegateTask.getId();
        List<SubApprovalModel> nextApprovalList = (List<SubApprovalModel>) delegateTask.getVariable("nextApprovalList");
        if (nextApprovalList == null || nextApprovalList.size() == 0) {
            String info="【招聘审批--->RecruitPersonallListener-->notify 】下级审批人为空!任务Id-->" + delegateTask.getId();
            log.info(info);
            throw new RuntimeException(info);
            //return;
        }
        Integer index = 1;
        for (SubApprovalModel users : nextApprovalList) {
            // processEngine.getTaskService().setAssignee(taskId, users.getUserId());
            processEngine.getTaskService().setVariableLocal(taskId, "apprvolPerson" + index, users.getUserId());
            delegateTask.addCandidateUser(users.getUserId());
            log.info("【招聘审批---> RecruitPersonallListener->notify 】下级审批人任务Id-->" + delegateTask.getId() + " 待办人:" + users.getUserId());
            index++;
        }
    }
}
