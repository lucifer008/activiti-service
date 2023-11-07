package lc.activiti.contract.listener;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

/**
 * 合同审批监听器【上一节点通过时触法】
 * 
 * @author Raytine
 *
 */
@Slf4j
public class ApprovalTaskListener2 implements TaskListener, JavaDelegate {
	// private static Logger log = LogManager.getLogger(ApprovalTaskListener.class);
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 
	 */
	private static final long serialVersionUID = 3694416603302742352L;

	@Override
	public void execute(DelegateExecution arg0) throws Exception {

	}

	@Override
	public void notify(DelegateTask delegateTask) {
		// 个人任务
		// SubApprovalModel
		// currentApprovalUser=(SubApprovalModel)delegateTask.getVariable("nextApprovalUser");
		// String userId=currentApprovalUser.getUserId();
		// String taskId=delegateTask.getId();
		// processEngine.getTaskService().setAssignee(taskId, userId);
		// Object message="任务Id:"+delegateTask.getId()+ "
		// 业务Id:"+currentApprovalUser.getBusinessId()+"
		// 下级审批人Id:"+currentApprovalUser.getUserId() +" 设置完成!";
		// log.info("ApprovalTaskListener",message);

		// 组任务
		String taskId = delegateTask.getId();
		List<SubApprovalModel> nextAuditUserList = (List<SubApprovalModel>) delegateTask.getVariable("nextApprovalUser");
		if (nextAuditUserList == null || nextAuditUserList.size() == 0) {
			log.info("【ApprovalTaskListener-->notify 】下级审核人为空!任务Id" + delegateTask.getId());
			return;
		}
		Integer index = 1;
		for (SubApprovalModel users : nextAuditUserList) {
			// processEngine.getTaskService().setAssignee(taskId, users.getUserId());
			processEngine.getTaskService().setVariableLocal(taskId, "auditPerson" + index, users.getUserId());
			delegateTask.addCandidateUser(users.getUserId());
			log.info(
					"【ApprovalTaskListener-->notify 】下级审批待办人任务Id" + delegateTask.getId() + " 待办人:" + users.getUserId());
			index++;
		}
	}

}
