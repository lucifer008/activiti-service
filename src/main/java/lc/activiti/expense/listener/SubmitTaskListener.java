package lc.activiti.expense.listener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;

import lc.activiti.entity.ExpensesBase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubmitTaskListener implements TaskListener,JavaDelegate {
	
	private static final long serialVersionUID = -6563448700043951046L;
	private ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("execute");
	}

	@Override
	public void notify(DelegateTask task) {
		log.info("SubmitTaskListener-->notify-->{}",SubmitTaskListener.class);
		boolean h=(boolean)task.getVariable("NonDepartmentHead");
		ExpensesBase expenses=(ExpensesBase)task.getVariable("expenses");
//		if (h) {
//			TaskService taskService=processEngine.getTaskService();
//			String taskId=task.getId();
//			String userId=expenses.getApprovalUserId();
//			taskService.setAssignee(taskId, userId);
//			taskService.complete(taskId);
//			log.info("notify---end");
//		}
	}

}
