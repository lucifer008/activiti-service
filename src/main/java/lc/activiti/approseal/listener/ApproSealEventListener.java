package lc.activiti.approseal.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApproSealEventListener implements ActivitiEventListener {

    @Override
    public void onEvent(ActivitiEvent event) {
        String processInstanceId = event.getProcessInstanceId();
        String processDefinitionId = event.getProcessDefinitionId();
        String executionId = event.getExecutionId();
        switch (event.getType()) {
            case JOB_EXECUTION_SUCCESS:
                System.out.println("A job well done!");
                log.info("A job well done! processInstanceId={},processDefinitionId={},executionId={}", processInstanceId, processDefinitionId,
                        executionId);
                break;

            case JOB_EXECUTION_FAILURE:
                System.out.println("A job has failed...");
                log.info("A job has failed. processInstanceId={},processDefinitionId={},executionId={}", processInstanceId, processDefinitionId, executionId);
                break;

            default:
                System.out.println("Event received: " + event.getType());
        }
    }

    @Override
    public boolean isFailOnException() {

        return false;
    }


}
