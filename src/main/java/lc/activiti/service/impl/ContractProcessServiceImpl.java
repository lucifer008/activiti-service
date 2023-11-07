package lc.activiti.service.impl;

import lc.activiti.service.ContractProcessService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ContractProcessServiceImpl implements ContractProcessService {

	// private static Logger log = Logger.getLogger(ContractProcessService.class);
	@Autowired
	private ProcessEngine processEngine;
	final static String contractApprovalProcessName = "合同审批1.0";
	final static String contractBrowseProcessName = "合同查看审批1.0";
	final static String emailTaskProcessName="邮件任务流程1.0";
	
	@Override
	public boolean deployContractProcess() {
		// String deplyContractPath="testMyProcess.bpmn";
		// processEngine.getRepositoryService().createDeployment().name("测试流程").addClasspathResource(deplyContractPath).deploy();
		// System.out.println("流程已部署");
		try {
			String deployContractPath = "contractApprovalProcess.bpmn";
			processEngine.getRepositoryService().createDeployment().name(contractApprovalProcessName)
					.addClasspathResource(deployContractPath).deploy();
			return true;
		} catch (Exception e) {
			log.error("deployContractProcess", e);
			;
			return false;
		}
	}

	@Override
	public boolean deleteContractProcess() {
		try {

			List<Deployment> deployments = processEngine.getRepositoryService().createDeploymentQuery()
					.deploymentName(contractApprovalProcessName).list();
			for (Deployment deployment : deployments) {
				processEngine.getRepositoryService().deleteDeployment(deployment.getId(), true);
			}
			return true;
		} catch (RuntimeException e) {
			log.error("deleteContractProcess", e);
			return false;
		}
	}

	@Override
	public boolean deployContractBrowseProcess() {
		try {
			String deployContractPath = "contractBrowseProcess.bpmn";
			processEngine.getRepositoryService().createDeployment().name(contractBrowseProcessName)
					.addClasspathResource(deployContractPath).deploy();
			return true;
		} catch (Exception e) {
			log.error("deployContractBrowseProcess", e);
			;
			return false;
		}
	}

	@Override
	public boolean deleteContractBrowseProcess() {
		try {

			List<Deployment> deployments = processEngine.getRepositoryService().createDeploymentQuery()
					.deploymentName(contractBrowseProcessName).list();
			for (Deployment deployment : deployments) {
				processEngine.getRepositoryService().deleteDeployment(deployment.getId(), true);
			}
			return true;
		} catch (RuntimeException e) {
			log.error("deleteContractBrowseProcess", e);
			return false;
		}
	}

	@Override
	public boolean deployEmailTaskProcess() {
		
		String emailTaskProcessResource="emailTaskProcess.bpmn";
		processEngine.getRepositoryService()
		.createDeployment()
		.name(emailTaskProcessName)
		.addClasspathResource(emailTaskProcessResource)
		.deploy();
		return false;
	}

	@Override
	public boolean deleteEmailTaskProcess() {
		try {

			List<Deployment> deployments = processEngine.getRepositoryService().createDeploymentQuery()
					.deploymentName(emailTaskProcessName).list();
			for (Deployment deployment : deployments) {
				processEngine.getRepositoryService().deleteDeployment(deployment.getId(), true);
			}
			return true;
		} catch (RuntimeException e) {
			log.error("deleteEmailTaskProcess", e);
			return false;
		}
		
	}
}
