package lc.activiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lc.activiti.lcenum.HttpRequestStatus;
import lc.activiti.lcenum.LCExceptionUtils;
import lc.activiti.lcenum.Result;
import lc.activiti.service.CommonApprovalService;
import lc.activiti.service.CommonProcessService;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/commonService")
public class CommonApprovalController {
	@Autowired
	private CommonApprovalService commonApprovalService;
	/**
	 * 提交订单
	 */
	@PostMapping("/submitApproval")
	public Result<Object> submitApply(@RequestBody SubApprovalModel appModel){
		Result<Object> result=new Result<>();
		commonApprovalService.submitApply(appModel);
		result.setMessage(HttpRequestStatus.Sucess.getDesc());
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
	/**
	 * 审核通过
	 */
	@PostMapping("/auditSuccess")
	public Result<Object> audtiSuccess(@RequestBody SubApprovalModel appModel){
		Result<Object> result=new Result<Object>();
		commonApprovalService.auditSuccess(appModel);
		result.setMessage(HttpRequestStatus.Sucess.getDesc());
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
	/**
	 * 审核驳回
	 */
	@PostMapping("/auditReject")
	public Result<Object> auditReject(@RequestBody SubApprovalModel appModel){
		Result<Object> result=new Result<Object>();
		commonApprovalService.auditReject(appModel);
		result.setMessage(HttpRequestStatus.Sucess.getDesc());
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
	/**
	 * 审批通过
	 */
	@PostMapping("/approvalSuccess")
	public Result<Object> approvalSuccess(@RequestBody SubApprovalModel appModel){
		Result<Object> result=new Result<Object>();
		commonApprovalService.approvalSuccess(appModel);
		result.setMessage(HttpRequestStatus.Sucess.getDesc());
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
	/**
	 * 审批通过
	 */
	@PostMapping("/approvalReject")
	public Result<Object> approvalReject(@RequestBody SubApprovalModel appModel){
		Result<Object> result=new Result<Object>();
		commonApprovalService.approvalReject(appModel);
		result.setMessage(HttpRequestStatus.Sucess.getDesc());
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
	@Autowired
	private CommonProcessService commonProcessService;
	/**
	 * 部署流程
	 */
	@RequestMapping("/deployCommonProcess")
	public Result<Object> deployCommonProcess(){
		Result<Object> result=new Result<Object>();
		commonProcessService.deployCommonProcess();
		result.setMessage("工作流部署成功!");
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
}
