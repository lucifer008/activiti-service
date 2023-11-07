package lc.activiti;

import java.util.Date;
import java.util.List;

import lc.activiti.entity.NewRolesModel;
import lc.activiti.lcenum.ApprosealApprovalType;
import lc.activiti.lcenum.ApprosealRoleType;
import lc.activiti.service.NewRolesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lc.activiti.lcenum.SealStatus;
import lc.activiti.dao.IApprovalSealDao;
import lc.activiti.dao.base.IApprovalSealBaseDao;
import lc.activiti.entity.ApprovalSealBase;
import lc.activiti.service.ApprovalSealService;
import lc.activiti.lcenum.GuidUtils;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApprovalSealProcessTest {
	@Test
	public void testDeployCommonProcess() {
		approvalService.deployApprovalSealProcess();
		log.info("部署成功!");
	}
	
	@Autowired
	private IApprovalSealDao approvalSealDao;
	@Autowired
	private IApprovalSealBaseDao approvalSealBaseDao;
	@Autowired
	private NewRolesService newRolesService;
	@Test
	public void addApprovalSeal() {
		ApprovalSealBase approvalSealBase=new ApprovalSealBase();
		approvalSealBase.setApprovalSealId(GuidUtils.newGuid());
		approvalSealBase.setSealStatus(SealStatus.NewCreate.getStatus());
		approvalSealBase.setApprovalDate(new Date());
		approvalSealBase.setApprovalType(ApprosealApprovalType.TrunkLineDowstream.getType());
		approvalSealBase.setApprovalUserId(userId);
		approvalSealBase.setSealApplyNo(GuidUtils.newGuid());
		approvalSealBaseDao.insert(approvalSealBase);
		log.info("add success! businessId={}",approvalSealBase.getApprovalSealId());
	}
	@Autowired
	private ApprovalSealService approvalService;
	

	String businessId="09c540c24a904bf5b5fe526a869e70fd";
	String userId="9d03b3f0af884e52b43d475b793de2d9";
	@Test
	public void submitApply() {
		String userId="a22518e6ab034bbcb8875023d03bcf52";
		//newRolesService.getNewRolesList(userId);
		newRolesService.queryUsersByRoleName(ApprosealRoleType.TrunkLineOffice.getDesc());
//		SubApprovalModel appModel=new SubApprovalModel();
//		//String businessId="35d27937e5ed462a8cf8cf987e236442";
//		appModel.setBusinessId(businessId);
//		//appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
//		appModel.setUserId(userId);
//		appModel.setReason("提交印章申请");
//		approvalService.submitApprovalSealApply(appModel);
//		log.info("提交印章申请!");
	}
	@Test
	public void testApprovalSealSuccess() {
		SubApprovalModel appModel=new SubApprovalModel();
		//String businessId="35d27937e5ed462a8cf8cf987e236442";
		appModel.setBusinessId(businessId);
		//appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
		appModel.setUserId("8c05787c4cca4183b44739e2476d3ecd");
		appModel.setReason("印章申请审批通过");
		approvalService.approvalSealSuccess(appModel);
		log.info("印章申请审批通过!");
	}
	@Test
	public void testRejectApprovalSeal() {
		SubApprovalModel appModel=new SubApprovalModel();
		//String businessId="35d27937e5ed462a8cf8cf987e236442";
		appModel.setBusinessId(businessId);
		//appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
		appModel.setUserId("ce3112eca33b468eaae89d60b6247a20");
		appModel.setReason("印章申请驳回");
		approvalService.rejectApprovalSeal(appModel);
		log.info("印章申请驳回成功!");
	}
}
