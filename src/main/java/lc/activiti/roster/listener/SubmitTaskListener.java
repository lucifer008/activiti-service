package lc.activiti.roster.listener;

import lc.activiti.model.ApprovalUserModel;
import lc.activiti.lcenum.GroupType;
import lc.activiti.lcenum.RosterSatus;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.identity.Group;

import java.util.Map;


@Slf4j
public class SubmitTaskListener implements TaskListener, JavaDelegate {
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Override
    public void execute(DelegateExecution execution) throws Exception {

    }

    @Override
    public void notify(DelegateTask delegateTask) {
        ApprovalUserModel approvalUserModel = delegateTask.getVariable("Users", ApprovalUserModel.class);
        Short status = delegateTask.getVariable("status", Short.class);
        if (status == RosterSatus.Submit.getStatus()) {
            if (approvalUserModel != null) {
                IdentityService identityService = processEngine.getIdentityService();
                Group group = identityService.createGroupQuery().groupMember(approvalUserModel.getUserId()).singleResult();
                String groupId = group.getId();
                if (!groupId.equals(GroupType.Personnel.getTypeKey()) && !groupId.equals(GroupType.DataGroup.getTypeKey()) && !groupId.equals(GroupType.FinanceGroup.getTypeKey()) && !groupId.equals(GroupType.BusinessGroup.getTypeKey())) {
                    throw new RuntimeException("组类型错误! 组Id-->" + groupId);
                }
                Map<String,Object> taskVariables=delegateTask.getVariables();
                taskVariables.put("groupType",groupId);
                processEngine.getTaskService().setAssignee(delegateTask.getId(),approvalUserModel.getUserId());
                processEngine.getTaskService().complete(delegateTask.getId(),taskVariables);
            }
        }
    }
}
