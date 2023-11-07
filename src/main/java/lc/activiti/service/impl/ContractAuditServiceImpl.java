package lc.activiti.service.impl;

import java.util.*;

import lc.activiti.entity.NewRolesModel;
import lc.activiti.service.NewRolesService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lc.activiti.lcenum.ActivitiProcessType;
import lc.activiti.lcenum.BrowseAppStatus;

import lc.activiti.lcenum.ContractProcessStatus;
import lc.activiti.lcenum.ContractStatus;
import lc.activiti.lcenum.RoleType;
import lc.activiti.dao.ICommonQueryDao;
import lc.activiti.dao.base.IBrowsePermissionBaseDao;
import lc.activiti.dao.base.IContractBaseDao;
import lc.activiti.entity.BrowsePermission;
import lc.activiti.entity.Contract;
import lc.activiti.model.EmailModel;
import lc.activiti.service.CommonService;
import lc.activiti.service.ContractAuditService;
import lc.activiti.service.EmailTaskService;
import lc.activiti.model.SubApprovalModel;

@Service
public class ContractAuditServiceImpl implements ContractAuditService {

	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private IContractBaseDao contractDao;
	
	@Autowired
	private ICommonQueryDao commomQueryDao;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private EmailTaskService emailTaskService;
	@Autowired
	private NewRolesService newRolesService;
	/**
	 * 【合同审批流程】 审核通过
	 */
	@Transactional(rollbackFor = Exception.class,isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
	@Override
	public void auditSuccess(SubApprovalModel subApprovalModel) {
		try {
			Contract contract = auditVerification(subApprovalModel);
			subApprovalModel=commonService.additionalUsers(subApprovalModel);
			Task task = processEngine.getTaskService().createTaskQuery()
					.processInstanceBusinessKey(subApprovalModel.getBusinessId()).singleResult();
			
			SubApprovalModel subModel = (SubApprovalModel) processEngine.getTaskService().getVariable(task.getId(),
					"subApprovalModel");
			
			Map<String, Object> vaiables = new HashMap<>();
			vaiables.put("isAudit", true);
			vaiables.put("processTag", ContractProcessStatus.AuditSuccess.getStatus());
			vaiables.put("currentUser", subApprovalModel);
			
			processEngine.getTaskService().setAssignee(task.getId(), subApprovalModel.getUserId());
			processEngine.getTaskService().complete(task.getId(), vaiables);

			UpdateContractStatus(subApprovalModel, ContractStatus.Audit.getStatus());
			
			noticeApplyPersonEmail(contract, subModel);
			
		} catch (Exception ex) {
			throw ex;
		}
	}

	private Contract auditVerification(SubApprovalModel subApprovalModel) {
		if (null == subApprovalModel || StringUtils.isBlank(subApprovalModel.getBusinessId())) {
			throw new RuntimeException("提交信息为空！或者业务Id为空");
		}
		Contract contract = contractDao.selectByPrimaryKey(subApprovalModel.getBusinessId());
		if (contract.getStatus().equals(ContractStatus.Audit.getStatus())) {
			throw new RuntimeException("合同已审核!不能重复提交!业务Id:" + subApprovalModel.getBusinessId());
		}
		if (!contract.getStatus().equals(ContractStatus.Approvaled.getStatus())) {
			throw new RuntimeException("合同未审批!不能审核!业务Id:" + subApprovalModel.getBusinessId());
		}
//		Boolean isForensic=commomQueryDao.isForensic(new Integer(RoleType.Forensic.getRoleType()), subApprovalModel.getUserId());
		Boolean isForensic = false;
		List<NewRolesModel> newRolesModels = newRolesService.getNewRolesList(subApprovalModel.getUserId());
		for (NewRolesModel roles:newRolesModels
			 ) {
			if (roles.getRoletype() == RoleType.Forensic.getRoleType().intValue()){
				isForensic =true;
			}
		}
		if (!isForensic) {
			throw new RuntimeException("非法务人员不能审核!");
		}

		return contract;
	}

	private void noticeApplyPersonEmail(Contract contract, SubApprovalModel subModel) {
		String subject = "【合同申请】";
		String subEmailContent = String.format("您的合同审核通过! 合同编号:【%s】 合同名称:【%s】 ", contract.getContractCode(),
				contract.getContractName());
		EmailModel eModel = new EmailModel();
		//eModel.setSubject(subject);
		eModel.setActivitiProcessType(ActivitiProcessType.ContactApprovalProcess);
		eModel.setBussinesOperatorType(ContractProcessStatus.AuditSuccess);
		eModel.setCurrentUsers(subModel);
		eModel.setIsSendSubmit(true);
		//eModel.setContent(subEmailContent);
		eModel.setSubmitSubject(subject);
		eModel.setSubContent(subEmailContent);
		emailTaskService.sendEmail(eModel);
	}

	private void UpdateContractStatus(SubApprovalModel subModel, Short status) {
		Contract contract = contractDao.selectByPrimaryKey(subModel.getBusinessId());
		if (null == contract) {
			throw new RuntimeException("不存在此合同" + subModel.getBusinessId());
		}
		contract.setStatus(status);
		contractDao.updateByPrimaryKey(contract);
	}
	/**
	 * 【合同审批流程】 审核驳回
	 */
	@Override
	public void auditReject(SubApprovalModel subApprovalModel) {
		if (null == subApprovalModel || StringUtils.isBlank(subApprovalModel.getBusinessId())) {
			throw new RuntimeException("提交信息为空！或者业务Id为空");
		}
		Contract contract = contractDao.selectByPrimaryKey(subApprovalModel.getBusinessId());
		if (!contract.getStatus().equals(ContractStatus.Approvaled.getStatus())) {
			throw new RuntimeException("合同不是已审批状态，不能驳回！业务Id:" + subApprovalModel.getBusinessId());
		}
//		Boolean isForensic=commomQueryDao.isForensic(new Integer(RoleType.Forensic.getRoleType()), subApprovalModel.getUserId());
		Boolean isForensic = false;
		List<NewRolesModel> newRolesModels = newRolesService.getNewRolesList(subApprovalModel.getUserId());
		for (NewRolesModel roles:newRolesModels
				) {
			if (roles.getRoletype() == RoleType.Forensic.getRoleType().intValue()){
				isForensic =true;
			}
		}
		if (!isForensic) {
			throw new RuntimeException("非法务人员不能驳回!");
		}
		subApprovalModel=commonService.additionalUsers(subApprovalModel);
		Task task = processEngine.getTaskService().createTaskQuery()
				.processInstanceBusinessKey(subApprovalModel.getBusinessId()).singleResult();
		
		SubApprovalModel subModel = (SubApprovalModel) processEngine.getTaskService().getVariable(task.getId(),
				"subApprovalModel");
		
		Map<String, Object> vaiables = new HashMap<>();
		vaiables.put("isAudit", false);
		vaiables.put("processTag", ContractProcessStatus.AuditReject.getStatus());
		vaiables.put("currentUser", subApprovalModel);
		
		//processEngine.getTaskService().setAssignee(task.ge, userId);
		task.setName("审核驳回合同");
		processEngine.getTaskService().addComment(task.getId(), task.getProcessInstanceId(), subApprovalModel.getReason());
		processEngine.getTaskService().setAssignee(task.getId(), subApprovalModel.getUserId());
		
		processEngine.getTaskService().complete(task.getId(), vaiables);

		UpdateContractStatus(subApprovalModel, ContractStatus.Reject.getStatus());
		
		noticeEmailToApplyPerson(contract, subModel);
	}

	private void noticeEmailToApplyPerson(Contract contract, SubApprovalModel subModel) {
		//邮件相关
		String subject = "【合同申请】";
		String subEmailContent = String.format("您的合同被驳回! 合同编号:【%s】 合同名称:【%s】 ", contract.getContractCode(),
				contract.getContractName());
		EmailModel eModel = new EmailModel();
		//eModel.setSubject(subject);
		eModel.setActivitiProcessType(ActivitiProcessType.ContactApprovalProcess);
		eModel.setBussinesOperatorType(ContractProcessStatus.AuditReject);
		eModel.setCurrentUsers(subModel);
		eModel.setIsSendSubmit(true);
		//eModel.setContent(subEmailContent);
		eModel.setSubmitSubject(subject);
		eModel.setSubContent(subEmailContent);
		emailTaskService.sendEmail(eModel);
	}

	@Autowired
	private IBrowsePermissionBaseDao browsePermissionDao;

	/**
	 * 【合同查看申请】 审核通过
	 */
	@Override
	public void contractBrowseAuditSuccess(SubApprovalModel appModel) {
		try {
			BrowsePermission browsePermission = browseAuditVerification(appModel);
			Map<String, Object> vaiarbles = new HashMap<String, Object>();
			vaiarbles.put("isAudit", true);
			vaiarbles.put("processTag", ContractProcessStatus.ApprovalSuccess.getStatus());
			Task task = processEngine.getTaskService().createTaskQuery()
					.processInstanceBusinessKey(appModel.getBusinessId()).singleResult();
			
			SubApprovalModel subModel = (SubApprovalModel) processEngine.getTaskService().getVariable(task.getId(),
					"subApprovalModel");
			
			processEngine.getTaskService().setAssignee(task.getId(), appModel.getUserId());
			processEngine.getTaskService().complete(task.getId(), vaiarbles);
			
			updateContractBrowseStatus(browsePermission, appModel, BrowseAppStatus.Audit.getStatus());
			
			
			Contract contract=commomQueryDao.getContractByBrowsePermissionId(appModel.getBusinessId());
			noticeEmailBrowseApplyPerson(subModel, contract);
		} catch (Exception e) {
			throw e;
		}
	}

	private void noticeEmailBrowseApplyPerson(SubApprovalModel subModel, Contract contract) {
		String subject = "【合同查看申请】";
		String subEmailContent = String.format("您的合同查看申请审核通过! 合同编号:【%s】 合同名称:【%s】 ", contract.getContractCode(),
				contract.getContractName());
		EmailModel eModel = new EmailModel();
		//eModel.setSubject(subject);
		eModel.setActivitiProcessType(ActivitiProcessType.ContactBrowseApprovalProcess);
		eModel.setBussinesOperatorType(ContractProcessStatus.AuditSuccess);
		eModel.setCurrentUsers(subModel);
		eModel.setIsSendSubmit(true);
		//eModel.setContent(subEmailContent);
		eModel.setSubmitSubject(subject);
		eModel.setSubContent(subEmailContent);
		emailTaskService.sendEmail(eModel);
	}

	private BrowsePermission browseAuditVerification(SubApprovalModel appModel) {
		ValidData(appModel);

		BrowsePermission browsePermission = browsePermissionDao.selectByPrimaryKey(appModel.getBusinessId());
		if (null == browsePermission) {
			throw new RuntimeException("预览申请信息为空!");
		}
		if (!browsePermission.getStatus().equals(BrowseAppStatus.Approvaled.getStatus())) {
			throw new RuntimeException("预览申请非提交状态不能提交!");
		}
//		Boolean isForensic=commomQueryDao.isForensic(new Integer(RoleType.Forensic.getRoleType()), appModel.getUserId());
		Boolean isForensic = false;
		List<NewRolesModel> newRolesModels = newRolesService.getNewRolesList(appModel.getUserId());
		for (NewRolesModel roles:newRolesModels
				) {
			if (roles.getRoletype() == RoleType.Forensic.getRoleType().intValue()){
				isForensic =true;
			}
		}
		if (!isForensic) {
			throw new RuntimeException("非法务人员不能审核!");
		}
		return browsePermission;
	}
	/**
	 * 【合同查看申请】 审核驳回
	 */
	@Override
	public void contractBrowseAuditReject(SubApprovalModel appModel) {
		try {
			ValidData(appModel);

			BrowsePermission browsePermission = browsePermissionDao.selectByPrimaryKey(appModel.getBusinessId());
			if (null == browsePermission) {
				throw new RuntimeException("预览申请信息为空!");
			}
			if (!browsePermission.getStatus().equals(BrowseAppStatus.Approvaled.getStatus())) {
				throw new RuntimeException("预览申请非审批状态不能驳回!");
			}
//			Boolean isForensic=commomQueryDao.isForensic(new Integer(RoleType.Forensic.getRoleType()), appModel.getUserId());
			Boolean isForensic = false;
			List<NewRolesModel> newRolesModels = newRolesService.getNewRolesList(appModel.getUserId());
			for (NewRolesModel roles:newRolesModels
					) {
				if (roles.getRoletype() == RoleType.Forensic.getRoleType().intValue()){
					isForensic =true;
				}
			}
			if (!isForensic) {
				throw new RuntimeException("非法务人员不能驳回!");
			}
			Map<String, Object> vaiarbles = new HashMap<String, Object>();
			vaiarbles.put("isAudit", false);
			vaiarbles.put("processTag", ContractProcessStatus.AuditReject.getStatus());
			Task task = processEngine.getTaskService().createTaskQuery()
					.processInstanceBusinessKey(appModel.getBusinessId()).singleResult();
			
			SubApprovalModel subModel = (SubApprovalModel) processEngine.getTaskService().getVariable(task.getId(),
					"subApprovalModel");
			
			processEngine.getTaskService().addComment(task.getId(), task.getProcessInstanceId(), appModel.getReason());
			processEngine.getTaskService().setAssignee(task.getId(), appModel.getUserId());
			processEngine.getTaskService().complete(task.getId(), vaiarbles);
			
			updateContractBrowseStatus(browsePermission, appModel, BrowseAppStatus.Reject.getStatus());
			
			noticeEmailToApplyPerson(appModel, subModel);
			
		} catch (Exception e) {
			throw e;
		}
	}

	private void noticeEmailToApplyPerson(SubApprovalModel appModel, SubApprovalModel subModel) {
		//邮件相关
		Contract contract=commomQueryDao.getContractByBrowsePermissionId(appModel.getBusinessId());
		String subject = "【合同查看申请】";
		String subEmailContent = String.format("您的合同查看申请被驳回! 合同编号:【%s】 合同名称:【%s】 ", contract.getContractCode(),
				contract.getContractName());
		EmailModel eModel = new EmailModel();
		//eModel.setSubject(subject);
		eModel.setActivitiProcessType(ActivitiProcessType.ContactApprovalProcess);
		eModel.setBussinesOperatorType(ContractProcessStatus.AuditReject);
		eModel.setCurrentUsers(subModel);
		eModel.setIsSendSubmit(true);
		//eModel.setContent(subEmailContent);
		eModel.setSubmitSubject(subject);
		eModel.setSubContent(subEmailContent);
		emailTaskService.sendEmail(eModel);
	}

	private void ValidData(SubApprovalModel appModel) {
		if (null == appModel) {
			throw new RuntimeException("提交Model为空!");
		}
		if (StringUtils.isBlank(appModel.getBusinessId())) {
			throw new RuntimeException("业务Id为空!");
		}
		if (StringUtils.isBlank(appModel.getUserId())) {
			throw new RuntimeException("提交UserId为空!");
		}
		if (StringUtils.isBlank(appModel.getDepartmentId())) {
			throw new RuntimeException("提交DepartmentId为空!");
		}

	}

	private void updateContractBrowseStatus(BrowsePermission browsePermission, SubApprovalModel subModel,
			Short status) {
		// 审核通过修正查看日期范围
		if (status.equals(BrowseAppStatus.Audit.getStatus())) {
			browsePermission.setStart(new Date());
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			browsePermission.setEnd(calendar.getTime());
		}
		browsePermission.setStatus(status);
		browsePermissionDao.updateByPrimaryKey(browsePermission);
	}
	//财务通过
	@Override
	public void financeApprovalSuccess(SubApprovalModel subModel) {
		try {
			ValidData(subModel);
			Contract contract = contractDao.selectByPrimaryKey(subModel.getBusinessId());
			if (null==contract) {
				throw new RuntimeException("非法请求!业务Id:" + subModel.getBusinessId());
			}
			if (!contract.getStatus().equals(ContractStatus.Approvaled.getStatus())) {
				throw new RuntimeException("部门负责人未审批!不能审批!业务Id:" + subModel.getBusinessId());
			}
			if (contract.getStatus().equals(ContractStatus.FinanceSuccess.getStatus())) {
				throw new RuntimeException("合同已审批!不能重复提交!业务Id:" + subModel.getBusinessId());
			}
			
		}
		catch (Exception e) {
			throw e;
		}
	}
	//财务驳回
	@Override
	public void financeApprovalReject(SubApprovalModel subModel) {
		
		ValidData(subModel);
		Contract contract = contractDao.selectByPrimaryKey(subModel.getBusinessId());
		if (null==contract) {
			throw new RuntimeException("非法请求!业务Id:" + subModel.getBusinessId());
		}
		if (!contract.getStatus().equals(ContractStatus.Approvaled.getStatus())) {
			throw new RuntimeException("部门负责人未审批!不能驳回审批!业务Id:" + subModel.getBusinessId());
		}
		if (contract.getStatus().equals(ContractStatus.Reject.getStatus())) {
			throw new RuntimeException("合同已驳回!不能重复提交!业务Id:" + subModel.getBusinessId());
		}
	}
	//法务驳回
	@Override
	public void legalReject(SubApprovalModel subModel) {
		
		ValidData(subModel);
		Contract contract = contractDao.selectByPrimaryKey(subModel.getBusinessId());
		if (null==contract) {
			throw new RuntimeException("非法请求!业务Id:" + subModel.getBusinessId());
		}
		if (!contract.getStatus().equals(ContractStatus.FinanceSuccess.getStatus())) {
			throw new RuntimeException("法务未审批!不能提交审批!业务Id:" + subModel.getBusinessId());
		}
		if (contract.getStatus().equals(ContractStatus.FinanceSuccess.getStatus())) {
			throw new RuntimeException("合同已审批!不能重复提交!业务Id:" + subModel.getBusinessId());
		}
		
	}
	//法务通过
	@Override
	public void legalSuccess(SubApprovalModel subModel) {
		ValidData(subModel);
		Contract contract = contractDao.selectByPrimaryKey(subModel.getBusinessId());
		if (null==contract) {
			throw new RuntimeException("非法请求!业务Id:" + subModel.getBusinessId());
		}
		if (!contract.getStatus().equals(ContractStatus.FinanceSuccess.getStatus())) {
			throw new RuntimeException("财务未审批!不能提交审批!业务Id:" + subModel.getBusinessId());
		}
		if (contract.getStatus().equals(ContractStatus.Reject.getStatus())) {
			throw new RuntimeException("合同已驳回!不能重复提交!业务Id:" + subModel.getBusinessId());
		}
		
	}

	@Override
	public void lineGroupAuditSuccess(SubApprovalModel subModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lineGroupAuditReject(SubApprovalModel subModel) {
		// TODO Auto-generated method stub
		
	}
}
