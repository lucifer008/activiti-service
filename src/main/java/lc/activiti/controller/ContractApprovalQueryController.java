package lc.activiti.controller;

import org.activiti.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lc.activiti.service.WorkflowService;


@RestController
@RequestMapping("contractApprovalQueryService")
public class ContractApprovalQueryController {
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ProcessEngine processEngine;
	
	public String queryContractApprovalStatus(String businessId) {
		
		//processEngine.get().createNativeHistoricVariableInstanceQuery().list();
		return "";
	}
}
