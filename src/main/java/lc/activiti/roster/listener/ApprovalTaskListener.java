package lc.activiti.roster.listener;

import lc.activiti.model.ApprovalUserModel;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;

import java.util.List;
import java.util.Map;


@Slf4j
public class ApprovalTaskListener implements TaskListener, JavaDelegate {
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Override
    public void execute(DelegateExecution execution) throws Exception {

    }

    @Override
    public void notify(DelegateTask delegateTask) {
        ApprovalUserModel approvalUserModel = delegateTask.getVariable("Users", ApprovalUserModel.class);
        if (approvalUserModel != null) {
            Map<String, Object> taskVariables = delegateTask.getVariables();
            String groupId=taskVariables.get("groupType")+"";
            IdentityService identityService = processEngine.getIdentityService();
            Group group=processEngine.getIdentityService().createGroupQuery().groupId(groupId).singleResult();
            List<User> nextUserList=identityService.createUserQuery().memberOfGroup(groupId).list();
            if (nextUserList.stream().filter(user -> user.getId().equals(approvalUserModel.getUserId())).count()==0){
                throw new RuntimeException("当前用户不在审批组--->"+group.getId()+",当前用户Id--->"+approvalUserModel.getUserId());
            }
            processEngine.getTaskService().setAssignee(delegateTask.getId(), approvalUserModel.getUserId());
            processEngine.getTaskService().complete(delegateTask.getId(), taskVariables);
        }
    }
}
