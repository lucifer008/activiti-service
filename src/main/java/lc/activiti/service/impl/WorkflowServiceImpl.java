package lc.activiti.service.impl;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lc.activiti.service.WorkflowService;

@Service
public class WorkflowServiceImpl implements WorkflowService {

	@Autowired
	private ProcessEngine processEngine;

	@Override
	public List getApproveHistory(String businessId) {
		//processEngine.getRepositoryService().createDeploymentQuery().
		return null;
	}

}
