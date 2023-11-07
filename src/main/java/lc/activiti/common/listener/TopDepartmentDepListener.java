package lc.activiti.common.listener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;

import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;


/**   
*      
* 描述：   顶级部门审批监听器
* 创建人：lucifer   
* 创建时间：2019年2月27日 下午3:36:22   
* @version        
*/
@Slf4j
public class TopDepartmentDepListener implements TaskListener, JavaDelegate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 115261911783983942L;
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		// 个人任务
		SubApprovalModel currentApprovalUser = (SubApprovalModel) delegateTask.getVariable("nextApprovalUser");
		String userId = currentApprovalUser.getUserId();
		String taskId = delegateTask.getId();
		processEngine.getTaskService().setAssignee(taskId, userId);
		Object message = "任务Id:" + delegateTask.getId() + " 业务Id:" + currentApprovalUser.getBusinessId() + " 下级审批人Id:"
				+ currentApprovalUser.getUserId() + " 设置完成!";
		log.info("TopDepartmentDepListener", message);
	}

}
