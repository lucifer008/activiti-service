package lc.activiti.contract.listener;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import lc.activiti.lcenum.ActivitiProcessType;
import lc.activiti.lcenum.ContractProcessStatus;
import lc.activiti.lcenum.GuidUtils;
import lc.activiti.service.EmailTaskService;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件通知相关Listener
 * 
 * @author Raytine
 *
 */

@Slf4j

public class EmailExecutionListener implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1000606018485634208L;
	private ProcessEngine progressEngine = ProcessEngines.getDefaultProcessEngine();

	@Autowired
	private EmailTaskService emaliTaskService;
	@Value("${lc.mail.fromMail.addr}")
	private String from;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		log.info("DelegateExecution:{}", execution.getCurrentActivityName());
		log.info("EmailExecutionListener---> 【邮件通知】 notify--");

		// String variableName = "nextApprovalUser";
		// Object nextApprovalUser = execution.getVariable(variableName);
		// SubApprovalModel nApprovalUsers = TypeUtils.as(SubApprovalModel.class,
		// nextApprovalUser);
		// if (nApprovalUsers == null) {
		// log.info("审批人为空!");
		// return;
		// }
		// log.info("【下级审批人】={}", new Gson().toJson(nApprovalUsers));
		Short contractProcessStatus = (Short) execution.getVariable("processTag");
		// ContractBussinessType
		// contractBussinessType=(ContractBussinessType)execution.getVariable("contractBussinessType");
		if (contractProcessStatus != null) {
			log.info("合同操作类型:{}", ContractProcessStatus.getContractProcessStatusDesc(contractProcessStatus));

			emailNotify(execution);
		}

	}

	/**
	 * 邮件服务相关
	 * 
	 * @param execution
	 */
	private void emailNotify(DelegateExecution execution) {
		try {
			Short contractProcessStatus = (Short) execution.getVariable("processTag");
			if (contractProcessStatus == ContractProcessStatus.Submit.getStatus()
					|| contractProcessStatus == ContractProcessStatus.RejectSubmit.getStatus()) {

				log.info("emaliTaskService={}", emaliTaskService);
				SubApprovalModel subApprovalUser = (SubApprovalModel) execution.getVariable("subApprovalModel");
				SubApprovalModel nextApprovalUser = (SubApprovalModel) execution.getVariable("nextApprovalUser");

				from="noreply-test@lecarlink.com";
				Map<String, Object> variables = new HashMap<>();
				String businessKey = GuidUtils.newGuid();
				String processDefinitionKey = ActivitiProcessType.EamilTask.getProcessKey();
				String subject = "【合同审批流程】";
				variables.put("subject", subject);
				String to = "zhangxiaolin@lecarlink.com";
				variables.put("to", to);
				variables.put("from", from);
				String htmlContext = "<html><body>hello</body></html>";
				variables.put("htmlContext", htmlContext);
				progressEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey, businessKey,
						variables);

				return;
			}

			if (contractProcessStatus == ContractProcessStatus.ApprovalSuccess.getStatus()) {

				return;
			}
			if (contractProcessStatus == ContractProcessStatus.ApprovalReject.getStatus()) {

				return;
			}
			if (contractProcessStatus == ContractProcessStatus.AuditSuccess.getStatus()) {

				return;
			}
			if (contractProcessStatus == ContractProcessStatus.AuditReject.getStatus()) {

				return;
			}
		} catch (Exception e) {
			log.info("【邮件通知异常】 异常明细={}", e);
		}
	}

}
