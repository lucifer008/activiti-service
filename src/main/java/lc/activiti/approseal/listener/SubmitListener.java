package lc.activiti.approseal.listener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;

import lc.activiti.lcenum.SealStatus;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubmitListener implements TaskListener,JavaDelegate {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5084772754396951174L;
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		Short processTag=(Short)delegateTask.getVariable("processTag");
		if (processTag==null) {
			return;
		}
		if (processTag.equals(SealStatus.Apploy.getStatus())) {
			
			SubApprovalModel subApprovalModel = (SubApprovalModel) delegateTask.getVariable("subApprovalModel");
			TaskService taskService=processEngine.getTaskService();
			
			taskService.setAssignee(delegateTask.getId(), subApprovalModel.getUserId());
			taskService.complete(delegateTask.getId(), delegateTask.getVariables());
			
			Object message="印章申请已提交完成.提交人:"+subApprovalModel.getUserId()+" 业务Id:"+subApprovalModel.getBusinessId();
			//log.info(message);
			log.info("【印章申请提交】",message);
			return;
		}
	}

}
