package lc.activiti.controller;

import lc.activiti.lcenum.HttpRequestStatus;
import lc.activiti.lcenum.LCExceptionUtils;
import lc.activiti.lcenum.Result;
import lc.activiti.model.SubApprovalModel;
import lc.activiti.service.RecruitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/recruitService")
public class RecruitController {
    @Autowired
    private RecruitService recruitService;
    /**
     *
     *

     * @return 部署招聘流程
     * @author lucifer
     * @date 2019/9/18 15:27
     */
    public  Result<Object> deployRecruitProcess(){
        Result<Object> result=new Result<>();
        recruitService.deployRecruitProcess();
        result.setMessage("【招聘审批流程1.0】部署成功!");
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return result;
    }
    /**
     *
     *

     * @return 提交招聘申请
     * @author lucifer
     * @date 2019/9/18 15:27
     */
    @RequestMapping("/submitRecruitApproval")
    public Result<Object>  submitRecruitApproval(@RequestBody SubApprovalModel subApprovalModel){
        Result<Object> result=new Result<>();
        recruitService.submitRecruitApproval(subApprovalModel);
        result.setMessage("提交成功!");
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return  result;
    }
    /**
     *
     *

     * @return 招聘审批通过
     * @author lucifer
     * @date 2019/9/18 15:27
     */
    @RequestMapping("/approvalSuccess")
    public Result<Object> approvalSuccess(@RequestBody SubApprovalModel approvalModel){
        Result<Object> result=new Result<>();
        recruitService.approvalSuccess(approvalModel);
        result.setMessage("提交成功!");
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return result;
    }
    /**
     *
     *

     * @return 招聘审批驳回
     * @author lucifer
     * @date 2019/9/18 15:27
     */
    @RequestMapping("/approvalReject")
    public Result<Object> approvalReject( @RequestBody SubApprovalModel approvalModel){
        Result<Object> result=new Result<>();
        recruitService.approvalReject(approvalModel);
        result.setMessage("提交成功!");
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return result;
    }
    /**
     *
     *
     * @return 招聘撤回
     * @author lucifer
     * @date 2019/9/18 15:26
     */
    @RequestMapping("/withdrawApply")
    Result<Object> withdrawApply(@RequestBody SubApprovalModel subModel){
        Result<Object> result=new Result<>();
        recruitService.withdrawApply(subModel);
        result.setMessage("提交成功!");
        result.setStatus(HttpRequestStatus.Sucess.getStatus());
        return result;
    }
}
