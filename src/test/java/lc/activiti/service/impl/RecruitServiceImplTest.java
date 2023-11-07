package lc.activiti.service.impl;

import lc.activiti.dao.base.IRecruitBaseDao;
import lc.activiti.entity.RecruitBase;
import lc.activiti.lcenum.GuidUtils;
import lc.activiti.lcenum.RecruitStatus;
import lc.activiti.model.SubApprovalModel;
import lc.activiti.service.RecruitService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecruitServiceImplTest {
    @Autowired
    private RecruitService recruitService;
    @Autowired
    private IRecruitBaseDao recruitBaseDao;

    @Test
    public void testDeployRecruitProcess(){
        recruitService.deployRecruitProcess();
        log.info("部署成功!");
    }
    @Test
    public void addRecruit(){
        String businessId=GuidUtils.newGuid();
        RecruitBase recruitBase=new RecruitBase();
        recruitBase.setRecruitId(businessId);
        recruitBase.setApprovalStatus(RecruitStatus.New.getStatus().shortValue());
        recruitBase.setDepId1("4be95e62a5ad499780be55955dc19ae1");
        recruitBase.setDepId2("94dc4d0d1aca4d74a8eb3aa80b760470");
        recruitBase.setPositionId("daa35fb3d1fe4009842acd71859b98cd");
        recruitBase.setInputUserId("44b79f9d53594ef2af873d17fc7ed8fd");
        recruitBase.setInput(new Date());
        recruitBaseDao.insert(recruitBase);
        log.info("业务Id={}",businessId);
    }
    String businessId="74ceb836d0f5463a998e6d23a8eaee05";
    @Test
    public void testSubmitRecruit(){
        SubApprovalModel appModel=new SubApprovalModel();
        //String businessId="35d27937e5ed462a8cf8cf987e236442";
        appModel.setBusinessId(businessId);
        //appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
        appModel.setUserId("44b79f9d53594ef2af873d17fc7ed8fd");
        appModel.setReason("提交招聘申请");
        recruitService.submitRecruitApproval(appModel);
        log.info("提交招聘申请");
    }
    @Test
    public void testDepartmentLeaderApprovalSuccess(){
        SubApprovalModel appModel=new SubApprovalModel();
        //String businessId="35d27937e5ed462a8cf8cf987e236442";
        appModel.setBusinessId(businessId);
        //appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
        appModel.setUserId("291bffc9bac8461cac08ca1b9c0eb183");
        appModel.setReason("提交招聘审批");
        recruitService.approvalSuccess(appModel);
        log.info("部门总裁审批");
    }

    @Test
    public void testPersonalApprovalSuccess(){
        SubApprovalModel appModel=new SubApprovalModel();
        //String businessId="35d27937e5ed462a8cf8cf987e236442";
        appModel.setBusinessId(businessId);
        //appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
        appModel.setUserId("b13df9f656ce4db9948b1bdb03d8ce6b");
        appModel.setReason("人事提交招聘审批");
        recruitService.approvalSuccess(appModel);
        log.info("人事提交招聘审批");
    }
    //b13df9f656ce4db9948b1bdb03d8ce6b
    @Test
    public void testDepartmentLeaderRejectRecruit(){
        SubApprovalModel appModel=new SubApprovalModel();
        //String businessId="35d27937e5ed462a8cf8cf987e236442";
        appModel.setBusinessId(businessId);
        //appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
        appModel.setUserId("291bffc9bac8461cac08ca1b9c0eb183");
        appModel.setReason("部门总裁驳回招聘审批");
        recruitService.approvalReject(appModel);
    }
    @Test
    public void testPersonalReject(){
        SubApprovalModel appModel=new SubApprovalModel();
        //String businessId="35d27937e5ed462a8cf8cf987e236442";
        appModel.setBusinessId(businessId);
        //appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
        appModel.setUserId("b13df9f656ce4db9948b1bdb03d8ce6b");
        appModel.setReason("人事驳回招聘审批");
        recruitService.approvalReject(appModel);
    }
    @Test
    public void testWithDrawRecruit(){
        SubApprovalModel appModel=new SubApprovalModel();
        //String businessId="35d27937e5ed462a8cf8cf987e236442";
        appModel.setBusinessId(businessId);
        //appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
        appModel.setUserId("291bffc9bac8461cac08ca1b9c0eb183");
        appModel.setReason("撤回招聘申请");
        recruitService.withdrawApply(appModel);
    }
}