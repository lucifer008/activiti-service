package lc.activiti.expense.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecondTaskListener implements TaskListener,JavaDelegate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6442198426255926554L;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("SecondTaskListener-->execute-->",SecondTaskListener.class);
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		log.info("SecondTaskListener-->notify-->",SecondTaskListener.class);
	}

}
