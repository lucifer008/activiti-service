package lc.activiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lc.activiti.lcenum.HttpRequestStatus;
import lc.activiti.lcenum.LCExceptionUtils;
import lc.activiti.lcenum.Result;
import lc.activiti.service.ContractApprovalService;
import lc.activiti.service.ContractAuditService;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/contractBrowseApprovalService")
@RestController
public class ConBrowseApprovalController {
	@Autowired
	private ContractApprovalService contractApprovalSerivce;
	@Autowired
	private ContractAuditService contractAuditService;
	
	@PostMapping("/contractBrowseAppSubmit")
	public Result<Object> contractBrowseAppSubmit(@RequestBody SubApprovalModel appModel){
		
		Result<Object> result=new Result<>();
		contractApprovalSerivce.browseAppSubmit(appModel);
		result.setMessage(HttpRequestStatus.Sucess.getDesc());
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
	@PostMapping("/contBrowseApprovalSuccess")
	public Result<Object> contractBrowseApprovalSuccess(@RequestBody SubApprovalModel appModel){
		Result<Object> result=new Result<>();
		contractApprovalSerivce.browseApprovalSuccess(appModel);
		result.setMessage(HttpRequestStatus.Sucess.getDesc());
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
	@PostMapping("/contractBrowseApprovalReject")
	public Result<Object> contractBrowseApprovalReject(@RequestBody SubApprovalModel appModel){
		Result<Object> result=new Result<>();
		contractApprovalSerivce.browseApprovalReject(appModel);
		result.setMessage(HttpRequestStatus.Sucess.getDesc());
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
	@PostMapping("/contractBrowseAuditSuccess")
	public Result<Object> contractBrowseAuditSuccess(@RequestBody SubApprovalModel appModel){
		Result<Object> result=new Result<>();
		contractAuditService.contractBrowseAuditSuccess(appModel);
		result.setMessage(HttpRequestStatus.Sucess.getDesc());
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
	@PostMapping("/contractBrowseAuditReject")
	public Result<Object> contractBrowseRejectSuccess(@RequestBody SubApprovalModel appModel){
		Result<Object> result=new Result<>();
		contractAuditService.contractBrowseAuditReject(appModel);
		result.setMessage(HttpRequestStatus.Sucess.getDesc());
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
}
