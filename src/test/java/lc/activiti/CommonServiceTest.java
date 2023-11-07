package lc.activiti;

import java.util.Date;

import lc.activiti.entity.OrderInfoBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lc.activiti.lcenum.BusinessTypeEnu;
import lc.activiti.lcenum.GuidUtils;
import lc.activiti.lcenum.ZTOrdersStatus;
import lc.activiti.dao.base.IOrderInfoBaseDao;
import lc.activiti.service.CommonApprovalService;
import lc.activiti.service.CommonProcessService;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonServiceTest {
	
	private static final String cropId = "ca19a32fb42d4cceb4ec75d84644634c";

	private static final String productRelationId ="82C4F059FC0A39ADE050A8C0020A2277";
	@Test
	public void helloWorld() {
		log.info("helloWorld");
	}
	@Autowired
	private IOrderInfoBaseDao orderInfoBaseDao;
	@Test
	public void addOrderInfo() {
		OrderInfoBase orderInfoBase=new OrderInfoBase();
		orderInfoBase.setCorpId(cropId);
		orderInfoBase.setOrderNo(GuidUtils.newGuid());
		orderInfoBase.setCreateTime(new Date());
		orderInfoBase.setGiftStatus(ZTOrdersStatus.WaitSubmit.getStatus());
		orderInfoBase.setId(GuidUtils.newGuid());
		orderInfoBase.setProductRelationId(productRelationId);
		String departmentId="94dc4d0d1aca4d74a8eb3aa80b760470";
		orderInfoBase.setDepartmentId(departmentId);
		String lkuserId="44b79f9d53594ef2af873d17fc7ed8fd";
		orderInfoBase.setLkuserId(lkuserId);
		orderInfoBase.setUserId("44b79f9d53594ef2af873d17fc7ed8fd");
		orderInfoBaseDao.insert(orderInfoBase);
		log.info("插入success--->{}",orderInfoBase.getId());
	}
	@Autowired
	private CommonProcessService commonProcessService;
	
	@Autowired
	private CommonApprovalService commonApprovalService;
	
	@Test
	public void testDeployCommonProcess() {
		commonProcessService.deployCommonProcess();
		log.info("部署成功!");
	}
	String businessId="dcf5918e468c40f6a0c0bdca052fa110";
	@Test
	public void testSubmitApply() {
		SubApprovalModel appModel=new SubApprovalModel();
		//String businessId="35d27937e5ed462a8cf8cf987e236442";
		appModel.setBusinessId(businessId);
		appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
		appModel.setUserId("9d03b3f0af884e52b43d475b793de2d9");
		appModel.setReason("提交订单");
		commonApprovalService.submitApply(appModel);
		log.info("订单提交成功!");
	}
	
//	@Test
//	public void addOrderInfoByDepartmentHeader() {
//		OrderInfoBase orderInfoBase=new OrderInfoBase();
//		orderInfoBase.setCorpId(cropId);
//		orderInfoBase.setOrderNo(GuidUtils.newGuid());
//		orderInfoBase.setCreateTime(new Date());
//		orderInfoBase.setGiftStatus(ZTOrdersStatus.WaitSubmit.getStatus());
//		orderInfoBase.setId(GuidUtils.newGuid());
//		orderInfoBase.setProductRelationId(productRelationId);
//		String departmentId="94dc4d0d1aca4d74a8eb3aa80b760470";
//		orderInfoBase.setDepartmentId(departmentId);
//		String lkuserId="44b79f9d53594ef2af873d17fc7ed8fd";
//		orderInfoBase.setLkuserId(lkuserId);
//		orderInfoBase.setUserId("44b79f9d53594ef2af873d17fc7ed8fd");
//		orderInfoBaseDao.insert(orderInfoBase);
//		log.info("插入success--->{}",orderInfoBase.getId());
//	}
	//部门负责人提交审批单
	@Test
	public void testDepartmentHeadSubmitApply() {
		SubApprovalModel appModel=new SubApprovalModel();
		//String businessId="35d27937e5ed462a8cf8cf987e236442";
		appModel.setBusinessId(businessId);
		appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
		appModel.setUserId("36b70126559c475198dc456864cd3ae9");
		appModel.setReason("部门负责人提交订单");
		commonApprovalService.submitApply(appModel);
		log.info("部门负责人提交订单!");
	}
	
	
	@Test
	public void testAudtiSuccess() {
		SubApprovalModel appModel=new SubApprovalModel();
		
		appModel.setBusinessId(businessId);
		appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
		appModel.setUserId("36b70126559c475198dc456864cd3ae9");
		appModel.setReason("审核订单通过");
		commonApprovalService.auditSuccess(appModel);
		log.info("订单审核成功!");
	}
	@Test
	public void testAudtiReject() {
		SubApprovalModel appModel=new SubApprovalModel();
		
		appModel.setBusinessId(businessId);
		appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
		appModel.setUserId("36b70126559c475198dc456864cd3ae9");
		appModel.setReason("订单审核驳回");
		commonApprovalService.auditReject(appModel);
		log.info("订单审核驳回成功!");
	}
	@Test
	public void testApprovalSuccess() {
		SubApprovalModel appModel=new SubApprovalModel();
		
		appModel.setBusinessId(businessId);
		appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
		appModel.setUserId("291bffc9bac8461cac08ca1b9c0eb183");
		appModel.setReason("审批订单通过");
		commonApprovalService.approvalSuccess(appModel);
		log.info("订单审批成功!");
	}
	@Test
	public void testApprovalReject() {
		SubApprovalModel appModel=new SubApprovalModel();
		
		appModel.setBusinessId(businessId);
		appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
		appModel.setUserId("291bffc9bac8461cac08ca1b9c0eb183");
		appModel.setReason("审批订单审批驳回");
		commonApprovalService.approvalReject(appModel);
		log.info("订单审批驳回成功!");
	}
	@Test
	public void testProcees(){

		addOrderInfo();
		testSubmitApply();
		testApprovalSuccess();
		testAudtiSuccess();
	}
}
