package lc.activiti.listener;

import lc.activiti.lcenum.RecruitStatus;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;

@Slf4j
public class RecruitSubmitListener implements TaskListener,JavaDelegate {


    private static final long serialVersionUID = -3027728805683786063L;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

    }
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    @Override
    public void notify(DelegateTask delegateTask) {
        Integer processTag=(Integer)delegateTask.getVariable("status");
        if (processTag==null) {
            return;
        }
        if (processTag.equals(RecruitStatus.Applyed.getStatus())) {

            SubApprovalModel subApprovalModel = (SubApprovalModel) delegateTask.getVariable("subApprovalModel");
            TaskService taskService=processEngine.getTaskService();

            taskService.setAssignee(delegateTask.getId(), subApprovalModel.getUserId());
            taskService.complete(delegateTask.getId(), delegateTask.getVariables());

            Object message="招聘审批已提交完成.提交人:"+subApprovalModel.getUserId()+" 业务Id:"+subApprovalModel.getBusinessId();
            //log.info(message);
            log.info("【招聘审批提交】",message);
            return;
        }
    }
}
