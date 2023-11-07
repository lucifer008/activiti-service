package lc.activiti.service;

public interface ContractProcessService {
	/**
	 * 部署合同审批流程
	 * 
	 * @return
	 */
	boolean deployContractProcess();

	/**
	 * 删除合同审批流程
	 * 
	 * @return
	 */
	boolean deleteContractProcess();

	/**
	 * 部署合同查看审批流程
	 * @return
	 */
	boolean deployContractBrowseProcess();
	/**
	 * 删除合同查看审批流程
	 * @return
	 */

	boolean deleteContractBrowseProcess();

	boolean deployEmailTaskProcess();

	boolean deleteEmailTaskProcess();
}
