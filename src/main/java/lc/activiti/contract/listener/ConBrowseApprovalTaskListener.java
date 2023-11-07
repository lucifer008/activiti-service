package lc.activiti.contract.listener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;

import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

/**
 * 合同预览审批监听器 【上一节点通过时触法】
 * @author Raytine
 *
 */
@Slf4j
public class ConBrowseApprovalTaskListener implements TaskListener, JavaDelegate {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void notify(DelegateTask delegateTask) {
		SubApprovalModel currentApprovalUser=(SubApprovalModel)delegateTask.getVariable("nextApprovalUser");
		String userId=currentApprovalUser.getUserId();
		String taskId=delegateTask.getId();
		processEngine.getTaskService().setAssignee(taskId, userId);
		Object message="任务Id:"+delegateTask.getId()+ " 业务Id:"+currentApprovalUser.getBusinessId()+" 下级审批人Id:"+currentApprovalUser.getUserId() +" 设置完成!";
		log.info("ConBrowseApprovalTaskListener",message);
	}

}
