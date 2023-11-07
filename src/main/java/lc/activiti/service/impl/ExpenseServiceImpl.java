package lc.activiti.service.impl;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lc.activiti.service.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired
	private ProcessEngine processEngine;
	String expenseProcessName = "报销审批流程1.0";
	String expenseProcessFileName = "expenseApprolvalProcess.bpmn";

	@Override
	public void deployExpenseProcess() {
		processEngine.getRepositoryService().createDeployment().name(expenseProcessName)
				.addClasspathResource(expenseProcessFileName).deploy();

	}

	@Override
	public void deleteExpenseProcess() {
		List<Deployment> deployments = processEngine.getRepositoryService()
				.createDeploymentQuery()
				.deploymentName(expenseProcessName).list();
		for (Deployment deployment : deployments) {
			processEngine.getRepositoryService().deleteDeployment(deployment.getId(), true);
		}
	}

}
