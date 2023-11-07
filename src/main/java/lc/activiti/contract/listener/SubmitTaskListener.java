package lc.activiti.contract.listener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;

import lc.activiti.lcenum.ContractProcessStatus;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

/**
 * 工作流 合同提交监听器【上一节点通过时触法】
 * 
 * @author lucifer
 * @Date:2018年8月8日 14:32:38
 */

@Slf4j
public class SubmitTaskListener implements TaskListener, JavaDelegate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8092937940738579381L;
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	//private static Logger log=Logger.getLogger(SubmitTaskListener.class);
	@Override
	public void execute(DelegateExecution arg0) throws Exception {

	}

	@Override
	public void notify(DelegateTask delegateTask) {
		//Boolean isNew = (Boolean) delegateTask.getVariable("isNew");//新建或者驳回，驳回为false不做处理
		Short processTag=(Short)delegateTask.getVariable("processTag");
		if (processTag.equals(ContractProcessStatus.Submit.getStatus())) {
			
			SubApprovalModel subApprovalModel = (SubApprovalModel) delegateTask.getVariable("subApprovalModel");
			TaskService taskService=processEngine.getTaskService();
			
			taskService.setAssignee(delegateTask.getId(), subApprovalModel.getUserId());
			taskService.complete(delegateTask.getId(), delegateTask.getVariables());
			
			Object message="合同审批已提交完成.提交人:"+subApprovalModel.getUserId()+" 业务Id:"+subApprovalModel.getBusinessId();
			//log.info(message);
			log.info("【合同提交】",message);
			return;
		}
//		SubApprovalModel subApprovalModel = (SubApprovalModel) delegateTask.getVariable("subApprovalModel");
//		processEngine.getTaskService().setAssignee(delegateTask.getId(), subApprovalModel.getUserId());
//		Object message="合同审批驳回提交人:"+subApprovalModel.getUserId()+" 业务Id:"+subApprovalModel.getBusinessId();
//		//log.info(message);
//		log.info("SubmitTaskListener",message);
	}

}
