package lc.activiti.contract.listener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;

import lc.activiti.lcenum.ContractProcessStatus;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

/**
 * 合同预览提交监听器【上一节点通过时触法】
 * @author Raytine
 *
 */
@Slf4j
public class ConBrowseAppSubTaskListener implements TaskListener,JavaDelegate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7357011974366697984L;
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		//Boolean isNew = (Boolean) delegateTask.getVariable("isNew");//新建或者驳回，驳回为false不做处理
		//if (isNew) {
		Short processTag=(Short)delegateTask.getVariable("processTag");
		if (processTag.equals(ContractProcessStatus.Submit.getStatus())) {
			SubApprovalModel subApprovalModel = (SubApprovalModel) delegateTask.getVariable("subApprovalModel");
			processEngine.getTaskService().setAssignee(delegateTask.getId(), subApprovalModel.getUserId());
			processEngine.getTaskService().addComment(delegateTask.getId(), delegateTask.getProcessInstanceId(),"查看原因:"+subApprovalModel.getReason());
			processEngine.getTaskService().complete(delegateTask.getId(), delegateTask.getVariables());
			
			Object message="合同预览审批已提交完成.提交人:"+subApprovalModel.getUserId()+" 业务Id:"+subApprovalModel.getBusinessId();
			//log.info(message);
			log.info("ConBrowseAppSubTaskListener",message);
		}
		
	}

}
