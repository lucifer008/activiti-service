package lc.activiti;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lc.activiti.lcenum.ExpenseStatus;
import lc.activiti.lcenum.GuidUtils;
import lc.activiti.dao.base.IExpensesBaseDao;
import lc.activiti.entity.ExpensesBase;
import lc.activiti.service.ExpenseApprovalService;
import lc.activiti.service.ExpenseService;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExpenseServiceTest {
	@Autowired
	private ExpenseService expenseService;
	
	@Autowired
	private ExpenseApprovalService expenseApprovalService;
	@Test
	public void testDeployExpenseProcess() {
		expenseService.deployExpenseProcess();
		log.info("报销审批流程已部署!");
		System.out.println("success");
		//assertFalse(true);
	}
	@Test
	public void testDeleteExpenseProcess() {
		expenseService.deleteExpenseProcess();
		log.info("报销审批流程已删除!");
		System.out.println("success");
		//assertFalse(true);
	}
	
	@Autowired
	private IExpensesBaseDao expenseDao;
	
	@Test
	public void addExpenses() {
		String expenseId=GuidUtils.newGuid();
		ExpensesBase expensesBase=new ExpensesBase();
		expensesBase.setExpensesId(expenseId);
		expensesBase.setInput(new Date());
		String departmentId="3856396092b34135a7093a742f9ed55b";//华东事业部
		expensesBase.setDepartmentId(departmentId);
		expensesBase.setStatus(ExpenseStatus.NewCreate.getStatus());
		String expensesUserId="7bf6539a79f24cbda7ff4a4de37bb61d";//和珅
		expensesBase.setExpensesUserId(expensesUserId);
		expensesBase.setFeeReson("工作流测试数据");
		
		expenseDao.insert(expensesBase);
		log.info("新增报销申请单,业务Id={}",expensesBase.getExpensesId());
	}
	String businessId="2ad63550cf0242dc8220fa9bbb64fa98";
	
	@Test
	public void testSubmitExpesenApply() {
		SubApprovalModel subApprovalModel=new SubApprovalModel();
		subApprovalModel.setUserId("44b79f9d53594ef2af873d17fc7ed8fd");
		subApprovalModel.setBusinessId(businessId);
		expenseApprovalService.submitExpesenApply(subApprovalModel);
		log.info("报销申请已提交,业务Id={}",subApprovalModel.getBusinessId());
//		System.out.println("BusinessKey="+expenses.getExpensesId());
	}
	
}
