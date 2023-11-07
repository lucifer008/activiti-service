package lc.activiti.controller;

import lc.activiti.service.ContractProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 合同审批流程部署服务
 */
@RestController
@RequestMapping("/contractProcessService")
public class ContractProcessController {
	
    @Autowired
    private ContractProcessService contractProcessService;
    
    @RequestMapping("/deployContractProcess")
    public String deployContractProcess(){
        contractProcessService.deployContractProcess();
        return "合同审批流程部署成功!";
    }
    @RequestMapping("/deleteContractProcess")
    public String deleteContractProcess() {
    	boolean sucess=contractProcessService.deleteContractProcess();
    	if (sucess) {
    		return "合同审批流程刪除成功!";		
		}
    	return "合同审批流程刪除失敗!";
    }
    @RequestMapping("/deployContractBrowseProcess")
    public String deployContractBrowseProcess(){
        contractProcessService.deployContractBrowseProcess();
        return "【合同查看流程】部署成功!";
    }
    @RequestMapping("/deleteContractBrowseProcess")
    public String deleteContractBrowseProcess() {
    	boolean sucess=contractProcessService.deleteContractBrowseProcess();
    	if (sucess) {
    		return "【合同查看流程】刪除成功!";		
		}
    	return "【合同查看流程】刪除失敗!";
    }
    @RequestMapping("/deployEmailTaskProcess")
    public String deployEmailTaskProcess(){
        contractProcessService.deployEmailTaskProcess();
        return "【邮件任务流程】部署成功!";
    }
    @RequestMapping("/deleteEmailTaskProcess")
    public String deleteEmailTaskProcess() {
    	boolean sucess=contractProcessService.deleteEmailTaskProcess();
    	if (sucess) {
    		return "【邮件任务流程】刪除成功!";		
		}
    	return "【邮件任务流程】刪除失敗!";
    }
}
