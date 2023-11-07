package lc.activiti.common.listener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;

import lc.activiti.lcenum.ZTOrdersStatus;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;


/**   
*    
* 项目名称：activitiapproval   
* 类名称：SubmitListener   
* 类描述：  提交或者驳回监听器
* 创建人：lucifer   
* 创建时间：2019年2月26日 上午10:53:12   
* @version        
*/
@Slf4j
public class SubmitListener implements TaskListener,JavaDelegate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -625336593423982111L;
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		
		Short processTag=(Short)delegateTask.getVariable("processTag");
		if (processTag.equals(ZTOrdersStatus.Submited.getStatus())) {
			
			SubApprovalModel subApprovalModel = (SubApprovalModel) delegateTask.getVariable("subApprovalModel");
			TaskService taskService=processEngine.getTaskService();
			
			taskService.setAssignee(delegateTask.getId(), subApprovalModel.getUserId());
			taskService.complete(delegateTask.getId(), delegateTask.getVariables());
			
			Object message="订单已提交完成.提交人:"+subApprovalModel.getUserId()+" 业务Id:"+subApprovalModel.getBusinessId();
			//log.info(message);
			log.info("【合同提交】",message);
			return;
		}
	}

}
