package lc.activiti.contract.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class LCEventListener implements ActivitiEventListener {

	@Override
	public void onEvent(ActivitiEvent event) {
		log.info("LCEventListener-------------------->onEvent");
	}

	@Override
	public boolean isFailOnException() {
		log.info("LCEventListener-------------------->isFailOnException");
		return false;
	}

}
