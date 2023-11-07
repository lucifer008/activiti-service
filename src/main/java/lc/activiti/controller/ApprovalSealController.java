package lc.activiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lc.activiti.service.ApprovalSealService;
import lc.activiti.lcenum.HttpRequestStatus;
import lc.activiti.lcenum.LCExceptionUtils;
import lc.activiti.lcenum.Result;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RequestMapping("/approvalSealService")
@Slf4j
@RestController
public class ApprovalSealController {
	
	@Autowired
	private ApprovalSealService approvalSealService;


	@RequestMapping("/deployApprovalSealProcess")
	public Result<Object> deployApprovalSealProcess(){
		Result<Object> result=new Result<>();
		approvalSealService.deployApprovalSealProcess();
		result.setMessage("【审批印章流程1.0】部署成功!");
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
	@RequestMapping("/submitApprovalSealApply")
	public Result<Object> submitApprovalSealApply(@RequestBody SubApprovalModel approvalModel){
		Result<Object> result=new Result<>();
		approvalSealService.submitApprovalSealApply(approvalModel);
		result.setMessage("提交成功!");
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
	@RequestMapping("/approvalSealSuccess")
	public Result<Object> approvalSealSuccess(@RequestBody SubApprovalModel approvalModel){
		Result<Object> result=new Result<>();
		approvalSealService.approvalSealSuccess(approvalModel);
		result.setMessage("提交成功!");
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
	@RequestMapping("/rejectApprovalSeal")
	public Result<Object> rejectApprovalSeal(@RequestBody SubApprovalModel approvalModel){
		Result<Object> result=new Result<>();
		approvalSealService.rejectApprovalSeal(approvalModel);
		result.setMessage("提交成功!");
		result.setStatus(HttpRequestStatus.Sucess.getStatus());
		return result;
	}
}
