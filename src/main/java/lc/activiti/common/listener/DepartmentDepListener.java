package lc.activiti.common.listener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
//部门负责人相关

import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

/**   
*    
* 项目名称：activitiapproval   
* 类名称：DepartmentDepListener   
* 类描述： 部门负责人监听器
* 创建人：lucifer   
* 创建时间：2019年2月26日 上午10:55:07   
* @version        
*/
@Slf4j
public class DepartmentDepListener implements TaskListener,JavaDelegate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2986461842057742151L;
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
	}
	private ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	@Override
	public void notify(DelegateTask delegateTask) {
		//个人任务
		SubApprovalModel currentApprovalUser=(SubApprovalModel)delegateTask.getVariable("nextApprovalUser");
		String userId=currentApprovalUser.getUserId();
		String taskId=delegateTask.getId();
		processEngine.getTaskService().setAssignee(taskId, userId);
		Object message="任务Id:"+delegateTask.getId()+ " 业务Id:"+currentApprovalUser.getBusinessId()+" 下级审批人Id:"+currentApprovalUser.getUserId() +" 设置完成!";
		log.info("ApprovalTaskListener=>{}",message);
	}

}
