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

/**
 * 审批服务
 */
@Slf4j
@RestController
@RequestMapping("/contractApprovalService")
public class ContractApprovalController {
    @Autowired
    private ContractApprovalService contractApprovalService;
    @Autowired
    private ContractAuditService contractAuditService;

    @PostMapping("/submitApproval")
    public Result<Object> submitApproval(@RequestBody SubApprovalModel subModel) {
        Result<Object> result = new Result<>();
        contractApprovalService.submitApproval(subModel);
        result.setMessage(HttpRequestStatus.Sucess.getDesc());
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return result;
    }

    /**
     * 撤回申请
     *
     * @param subModel
     * @return
     */
    @PostMapping("/withdrawApply")
    public Result<Object> withdrawApply(@RequestBody SubApprovalModel subModel) {
        Result<Object> result = new Result<>();
        contractApprovalService.withdrawApply(subModel);
        result.setMessage(HttpRequestStatus.Sucess.getDesc());
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return result;
    }

    @PostMapping("/approvalSuccess")
    public Result<Object> approvalContract(@RequestBody SubApprovalModel subModel) {
        Result<Object> result = new Result<>();
        contractApprovalService.approvalSuccess(subModel);
        result.setMessage(HttpRequestStatus.Sucess.getDesc());
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return result;
    }

    @PostMapping("/rejectApproval")
    public Result<Object> rejectAppContract(@RequestBody SubApprovalModel subModel) {
        Result<Object> result = new Result<>();
        contractApprovalService.rejectApproval(subModel);
        result.setMessage(HttpRequestStatus.Sucess.getDesc());
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return result;
    }

    @PostMapping("/auditSuccess")
    public Result<Object> auditContract(@RequestBody SubApprovalModel subModel) {
        Result<Object> result = new Result<>();
        contractAuditService.auditSuccess(subModel);
        result.setMessage(HttpRequestStatus.Sucess.getDesc());
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return result;
    }

    @PostMapping("/auditReject")
    public Result<Object> auditReject(@RequestBody SubApprovalModel subModel) {
        Result<Object> result = new Result<>();
        contractAuditService.auditReject(subModel);
        result.setMessage(HttpRequestStatus.Sucess.getDesc());
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return result;
    }

    @PostMapping("/financeApprovalSuccess")
    public Result<Object> financeApprovalSuccess(@RequestBody SubApprovalModel subModel) {
        Result<Object> result = new Result<>();
        contractAuditService.financeApprovalSuccess(subModel);
        result.setMessage(HttpRequestStatus.Sucess.getDesc());
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return result;
    }

    @PostMapping("/financeApprovalReject")
    public Result<Object> financeApprovalReject(@RequestBody SubApprovalModel subModel) {
        Result<Object> result = new Result<>();
        contractAuditService.financeApprovalReject(subModel);
        result.setMessage(HttpRequestStatus.Sucess.getDesc());
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return result;
    }

    @PostMapping("/lineGroupAuditSuccess")
    public Result<Object> lineGroupAuditSuccess(@RequestBody SubApprovalModel subModel) {
        Result<Object> result = new Result<>();
        contractAuditService.lineGroupAuditSuccess(subModel);
        result.setMessage(HttpRequestStatus.Sucess.getDesc());
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return result;
    }

    @PostMapping("/lineGroupAuditReject")
    public Result<Object> lineGroupAuditReject(@RequestBody SubApprovalModel subModel) {
        Result<Object> result = new Result<>();
        contractAuditService.lineGroupAuditReject(subModel);
        result.setMessage(HttpRequestStatus.Sucess.getDesc());
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return result;
    }
}
