package lc.activiti;



import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lc.activiti.lcenum.ContractStatus;
import lc.activiti.lcenum.GuidUtils;
import lc.activiti.dao.base.IContractBaseDao;
import lc.activiti.entity.Contract;
import lc.activiti.service.ContractApprovalService;
import lc.activiti.service.ContractProcessService;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ContractProcessServiceTest {
	@Autowired
	private ContractProcessService contractProcessService;
	
	
	@Test
	public void testDeployContractProcess() {
		contractProcessService.deployContractProcess();
		contractProcessService.deployContractBrowseProcess();
		//assertFalse(true);
		System.out.println("success!");
		log.info("部署成功!");
	}
	@Autowired
	private IContractBaseDao contractDao;
	@Test
	public void testAddContract() {
		Contract contract=new Contract();
		contract.setContractName("工作流测试");
		contract.setSubmitDate(new Date());
		contract.setContractId(GuidUtils.newGuid());
		contract.setContractNo("11");
		contract.setStatus(ContractStatus.NewCreate.getStatus());
		contractDao.insert(contract);
		log.info("业务Id={}",contract.getContractId());
	}
	@Autowired
	private ContractApprovalService contractApprovalService;
	String businessId="6771cee3e6d145d1907886927e8209e0";
	String userId="5d4ba6641e8c436bb55e8b3e1ef40313";
	String departmentId="2bc6ec16fe214ae5ac4e47ec10762b39";
	@Test
	public void testContractSubmit() {
		//0810c66a89aa44818c932308
		SubApprovalModel appModel=new SubApprovalModel();
		//String businessId="35d27937e5ed462a8cf8cf987e236442";
		appModel.setBusinessId(businessId);
		//appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
		appModel.setUserId(userId);
		appModel.setDepartmentId(departmentId);
		appModel.setReason("提交合同申请");
		contractApprovalService.submitApproval(appModel);
		log.info("合同申请提交成功!");
	}
	String dataVpUserId="74e02f2985f3413e9ac07b34c9d9d687";
	@Test
	public void testApprovalContract() {
		SubApprovalModel appModel=new SubApprovalModel();
		//String businessId="35d27937e5ed462a8cf8cf987e236442";
		appModel.setBusinessId(businessId);
		//appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
		appModel.setUserId(dataVpUserId);
		appModel.setReason("合同申请审批通过");
		contractApprovalService.approvalSuccess(appModel);
		log.info("合同申请审批通过！");
	}
	@Test
	public void testRejectContract() {
		SubApprovalModel appModel=new SubApprovalModel();
		//String businessId="35d27937e5ed462a8cf8cf987e236442";
		appModel.setBusinessId(businessId);
		//appModel.setBusinessType(BusinessTypeEnu.ZT_Order_Service.getDesc());
		appModel.setUserId(dataVpUserId);
		appModel.setReason("我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很2伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我害得我很伤心我还我11111345");
		contractApprovalService.rejectApproval(appModel);
		log.info("驳回合同申请 sucess！");
	}
}
