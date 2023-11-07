package lc.activiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lc.activiti.service.DataExchangeService;

@RestController
//@Slf4j
@RequestMapping("/dataExchangeService")
public class DataExchangeController {
	@Autowired
	private DataExchangeService dataExchangeService;
	
	@RequestMapping("/refreshOldContractSumbitedOrApprovaledData")
	public void refreshOldContractData() {
		dataExchangeService.refreshOldContractSumbitedOrApprovaledContractData();
	}
	@RequestMapping("/refreshOldContractAuditedToApprovaled")
	public void refreshOldContractAuditedToApprovaled() {
		dataExchangeService.refreshOldContractAuditedToApprovaled();
	}
}
