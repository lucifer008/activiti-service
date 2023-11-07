package lc.activiti.service.impl;

import java.util.HashMap;
import java.util.Map;

import lc.activiti.dao.base.IDepartmentBaseDao;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lc.activiti.dao.IDepartmentDao;
import lc.activiti.service.CommonService;
import lc.activiti.dao.base.IExpensesBaseDao;
import lc.activiti.entity.ExpensesBase;
import lc.activiti.service.ExpenseApprovalService;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExpenseApprovalServiceImpl implements ExpenseApprovalService {

	static String processDefinitionKey = "expenseApprovalProcess";
	@Autowired
	private ProcessEngine processEngine;

	@Autowired
	private IExpensesBaseDao expenseBaseDao;

	@Autowired
	private IDepartmentDao departmentDao;

	@Autowired
	private IDepartmentBaseDao departmentBaseDao;

	@Autowired
	private CommonService commonService;
	
	@Override
	public boolean submitExpesenApply(SubApprovalModel subApprovalModel) {

		verification(subApprovalModel);
		Map<String, Object> variables = new HashMap<>();
		String businessKey = subApprovalModel.getBusinessId();
		subApprovalModel=commonService.additionalUsers(subApprovalModel);
		ExpensesBase expensesBase = expenseBaseDao.selectByPrimaryKey(subApprovalModel.getBusinessId());
		String depId="";
		SubApprovalModel nextApprovalUser=commonService.getNextApprovalUser(subApprovalModel.getBusinessId(), depId);
		boolean isDepartmentHeader=departmentDao.isDepartmentLeader(subApprovalModel.getUserId());
		variables.put("NonDepartmentHead", isDepartmentHeader);//是否是部门负责人
		variables.put("currentUsers", subApprovalModel);//当前用户
		variables.put("nextApprovalUser", nextApprovalUser);//下级审批人信息
		// variables.put("expenses", expenses);
		
		ProcessInstance processInstance = processEngine.getRuntimeService()
				.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
		log.info("报销提交成功，流程Id={}", processInstance.getId());
		return true;
	}

	void verification(SubApprovalModel subApprovalModel) {
		if (StringUtils.isBlank(subApprovalModel.getBusinessId())) {
			String message = "业务Id不能为空!";
			RuntimeException exception = new RuntimeException(message);
			log.error("【验证异常】", exception);
			throw exception;
		}
		if (StringUtils.isBlank(subApprovalModel.getUserId())) {
			String message = "用户Id不能为空!";
			RuntimeException exception = new RuntimeException(message);
			log.error("【验证异常】", exception);
			throw exception;
		}
		ExpensesBase expensesBase = expenseBaseDao.selectByPrimaryKey(subApprovalModel.getBusinessId());
		if (null == expensesBase) {
			String message = "不存在此报销单!";
			RuntimeException exception = new RuntimeException(message);
			log.error("【验证异常】", exception);
			throw exception;
		}
		if (StringUtils.isBlank(expensesBase.getDepartmentId())) {
			String message = "报销单部门不存在不能提交申请!";
			RuntimeException exception = new RuntimeException(message);
			log.error("【验证异常】", exception);
			throw exception;
		}
		if (StringUtils.isBlank(expensesBase.getExpensesUserId())) {
			String message = "报销单报销用户不存在!";
			RuntimeException exception = new RuntimeException(message);
			log.error("【验证异常】", exception);
			throw exception;
		}
		if (!subApprovalModel.getUserId().equals(expensesBase.getExpensesUserId())) {
			String message = "报销单报销用户与提交申请人不一致!";
			RuntimeException exception = new RuntimeException(message);
			log.error("【验证异常】", exception);
			throw exception;
		}
	}

	@Override
	public boolean approvalSuccess(SubApprovalModel subApprovalModel) {
		verification(subApprovalModel);
		return false;
	}
}
