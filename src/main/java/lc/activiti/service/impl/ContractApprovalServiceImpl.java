package lc.activiti.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lc.activiti.dao.base.*;
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
import lc.activiti.lcenum.GuidUtils;
import lc.activiti.lcenum.RoleType;
import lc.activiti.dao.ICommonQueryDao;
import lc.activiti.dao.base.IContractCounterSignBaseDao;
import lc.activiti.entity.BrowsePermission;
import lc.activiti.entity.Contract;
import lc.activiti.entity.ContractCounterSign;
import lc.activiti.model.EmailModel;
import lc.activiti.service.CommonService;
import lc.activiti.service.ContractApprovalService;
import lc.activiti.service.EmailTaskService;
import lc.activiti.service.WechartNoticeService;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContractApprovalServiceImpl implements ContractApprovalService {

	@Autowired
	private ProcessEngine processEngine;
	private String processDefinitKey = "contractApprovalProcess";
	@Autowired
	private IContractBaseDao contractDao;
	@Autowired
	private IDepartmentBaseDao departmentDao;
	@Autowired
	private ICommonQueryDao commonQueryDao;

	@Autowired
	private IUserBaseDao userDao;

	@Autowired
	private CommonService commonService;

	@Autowired
	private EmailTaskService emailTaskService;

	@Autowired
	private WechartNoticeService wechartNoticeService;

	/**
	 * 【合同审批流程】 提交合同
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void submitApproval(@NotNull SubApprovalModel subModel) {
		// if (null == subModel) {
		// throw new RuntimeException("提交信息不能为空！");
		// }
		// SubApprovalModel nextApprovalUser =
		// commonService.getNextApprovalUser(subModel.getBusinessId(),
		// subModel.getDepartmentId());
		List<SubApprovalModel> nextApprovalUserList = commonService.getNextApprovalUserList(subModel.getBusinessId(),
				subModel.getDepartmentId(), true);
		Contract contract = contractDao.selectByPrimaryKey(subModel.getBusinessId());
		if (!contract.getStatus().equals(ContractStatus.NewCreate.getStatus())
				&& !contract.getStatus().equals(ContractStatus.Reject.getStatus())
				&& !contract.getStatus().equals(ContractStatus.Withdraw.getStatus())) {
			throw new RuntimeException("合同已提交!不能重复提交!业务Id:" + subModel.getBusinessId());
		}

		String businessId = subModel.getBusinessId();

		subModel = commonService.additionalUsers(subModel);

		// 启动申请流程
		if (!contract.getStatus().equals(ContractStatus.Reject.getStatus())
			&& !contract.getStatus().equals(ContractStatus.Withdraw.getStatus())) {
			Map<String, Object> vaiarbles = new HashMap<>();
			vaiarbles.put("subApprovalModel", subModel);
			vaiarbles.put("processTag", ContractProcessStatus.Submit.getStatus());
			vaiarbles.put("nextApprovalUser", nextApprovalUserList);
			processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitKey, businessId, vaiarbles);
		} else {
			// 驳回再提交
			Map<String, Object> vaiarbles = new HashMap<>();
			vaiarbles.put("subApprovalModel", subModel);
			vaiarbles.put("processTag", ContractProcessStatus.RejectSubmit.getStatus());
			vaiarbles.put("nextApprovalUser", nextApprovalUserList);
			// 驳回再提交
			Task task = processEngine.getTaskService().createTaskQuery()
					.processInstanceBusinessKey(subModel.getBusinessId()).singleResult();
			processEngine.getTaskService().setAssignee(task.getId(), subModel.getUserId());
			processEngine.getTaskService().complete(task.getId(), vaiarbles);
		}
		Short batch = contract.getCurrentBatchNo()==null?0:contract.getCurrentBatchNo();
		UpdateContractStatus(subModel, ContractStatus.Apploy.getStatus(),batch);
//		wechartNoticeService.pushNoticeCounterSignPersons(subModel.getBusinessId());
		noticeNextApprovalUser(subModel, nextApprovalUserList, contract);
	}

	private void noticeNextApprovalUser(SubApprovalModel subModel, List<SubApprovalModel> nextApprovalUserList,
			Contract contract) {
		List<SubApprovalModel> nextApprovalList = nextApprovalUserList;

		String emailContent = String.format("您有一个合同【待审批】,合同编号:【%s】 合同名称:【%s】", contract.getContractCode(),
				contract.getContractName());
		EmailModel eModel = new EmailModel();
		eModel.setSubject("【合同申请】");
		eModel.setActivitiProcessType(ActivitiProcessType.ContactApprovalProcess);
		eModel.setBussinesOperatorType(ContractProcessStatus.Submit);
		eModel.setCurrentUsers(subModel);
		eModel.setIsSendSubmit(false);
		eModel.setContent(emailContent);
		eModel.setNextApprovalList(nextApprovalList);

		emailTaskService.sendEmail(eModel);
	}

	private Contract UpdateContractStatus(SubApprovalModel subModel, Short status,Short currentBatchNo) {
		Contract contract = contractDao.selectByPrimaryKey(subModel.getBusinessId());
		if (null == contract) {
			throw new RuntimeException("不存在此合同" + subModel.getBusinessId());
		}
		contract.setStatus(status);
		if (currentBatchNo!=null)contract.setCurrentBatchNo((short) (currentBatchNo+1));
		if (ContractStatus.Apploy.getStatus().equals(status)) {
			contract.setSubmitDate(new Date());
			contract.setRejectDate(null);
			contract.setRejectReason(null);
		} else if (ContractStatus.Reject.getStatus().equals(status)) {
			contract.setRejectDate(new Date());
			contract.setRejectReason(subModel.getReason());
		}
		contractDao.updateByPrimaryKey(contract);
		return contract;
	}

	/**
	 * 【合同审批流程】 合同 审批通过
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void approvalSuccess(@NotNull SubApprovalModel subApprovalModel) {
		if (null == subApprovalModel || StringUtils.isBlank(subApprovalModel.getBusinessId())) {
			throw new RuntimeException("提交信息为空！或者业务Id为空");
		}
		Contract contract = contractDao.selectByPrimaryKey(subApprovalModel.getBusinessId());
		if (!contract.getStatus().equals(ContractStatus.Apploy.getStatus())) {
			throw new RuntimeException("合同不是已提交状态!不能提交审批!业务Id:" + subApprovalModel.getBusinessId());
		}
		if (contract.getStatus().equals(ContractStatus.Approvaled.getStatus())) {
			throw new RuntimeException("合同已审批!不能重复提交!业务Id:" + subApprovalModel.getBusinessId());
		}
		subApprovalModel = commonService.additionalUsers(subApprovalModel);
		Task task = processEngine.getTaskService().createTaskQuery()
				.processInstanceBusinessKey(subApprovalModel.getBusinessId()).singleResult();
		// Map<String, Object> currVariables =
		// processEngine.getTaskService().getVariables(task.getId());
		// SubApprovalModel currentApprovalUser = (SubApprovalModel)
		// currVariables.get("nextApprovalUser");

		// List<SubApprovalModel> nextAuditUserList = commonService
		// .getNextAuditUserList(currentApprovalUser.getBusinessId(),
		// currentApprovalUser.getDepartmentId());
		SubApprovalModel subModel = (SubApprovalModel) processEngine.getTaskService().getVariable(task.getId(),
				"subApprovalModel");

		Map<String, Object> vaiables = new HashMap<>();
		vaiables.put("isApproval", true);
		vaiables.put("processTag", ContractProcessStatus.ApprovalSuccess.getStatus());
		// vaiables.put("auditUsers", nextAuditUserList);
		vaiables.put("currentUser", subApprovalModel);
		processEngine.getTaskService().setVariableLocal(task.getId(), "approvalSuccessUserInfo", vaiables);

		// processEngine.getTaskService().complete(task.getId(), vaiables);
		if (!StringUtils.isBlank(subApprovalModel.getLastCounterSingnId())) {
			ContractCounterSign cCounterSign = contractCounterSignDao
					.selectByPrimaryKey(subApprovalModel.getLastCounterSingnId());
			cCounterSign.setSignDate(new Date());
			cCounterSign.setUpdate(new Date());

			contractCounterSignDao.updateByPrimaryKey(cCounterSign);
		}
		processEngine.getTaskService().setAssignee(task.getId(), subApprovalModel.getUserId());
		processEngine.getTaskService().complete(task.getId(), vaiables);
		UpdateContractStatus(subApprovalModel, ContractStatus.Approvaled.getStatus(),null);

//		String approvalPerson =userDao.selectByPrimaryKey(contract.getInputUserId()).getInputUserName();
//		String applyUserId = contract.getInputUserId();
//		String suggestion = subApprovalModel.getReason();
//		wechartNoticeService.pushApprovalResultToApplyPerson(subApprovalModel.getBusinessId(), applyUserId,
//				approvalPerson, true, suggestion);

		noticeEmailApplyPersonAndNextApproval(contract, subModel);

	}

	@Autowired
	private IContractCounterSignBaseDao contractCounterSignDao;

	private void noticeEmailApplyPersonAndNextApproval(Contract contract, SubApprovalModel subModel) {
		String subject = "【合同申请】";
		String subjectAudit = "【合同申请】";
		// String emailContent = String.format("您有一个合同【待审核】,合同编号:【%s】 合同名称:【%s】",
		// contract.getContractCode(),
		// contract.getContractName());
		String subEmailContent = String.format("您的合同编号:【%s】 合同名称:【%s】 通过审批!", contract.getContractCode(),
				contract.getContractName());
		EmailModel eModel = new EmailModel();
		eModel.setSubject(subjectAudit);
		eModel.setActivitiProcessType(ActivitiProcessType.ContactApprovalProcess);
		eModel.setBussinesOperatorType(ContractProcessStatus.ApprovalSuccess);
		eModel.setCurrentUsers(subModel);
		// eModel.setIsSendSubmit(true);
		// eModel.setContent(emailContent);
		eModel.setSubmitSubject(subject);
		eModel.setSubContent(subEmailContent);
		// eModel.setNextApprovalList(nextAuditUserList);

		emailTaskService.sendEmail(eModel);
	}

	// private void noticeEmailApplyPersonAndNextApproval(Contract contract,
	// List<SubApprovalModel> nextAuditUserList,
	// SubApprovalModel subModel) {
	// String subject = "【合同申请】";
	// String subjectAudit="【合同申请】";
	// String emailContent = String.format("您有一个合同【待审核】,合同编号:【%s】 合同名称:【%s】",
	// contract.getContractCode(),
	// contract.getContractName());
	// String subEmailContent = String.format("您的合同编号:【%s】 合同名称:【%s】 通过审批!",
	// contract.getContractCode(),
	// contract.getContractName());
	// EmailModel eModel = new EmailModel();
	// eModel.setSubject(subjectAudit);
	// eModel.setActivitiProcessType(ActivitiProcessType.ContactApprovalProcess);
	// eModel.setBussinesOperatorType(ContractProcessStatus.ApprovalSuccess);
	// eModel.setCurrentUsers(subModel);
	// eModel.setIsSendSubmit(true);
	// eModel.setContent(emailContent);
	// eModel.setSubmitSubject(subject);
	// eModel.setSubContent(subEmailContent);
	// eModel.setNextApprovalList(nextAuditUserList);
	//
	// emailTaskService.sendEmail(eModel);
	// }

	/**
	 * 【合同审批流程】 审批驳回
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void rejectApproval(SubApprovalModel subApprovalModel) {
		if (null == subApprovalModel || StringUtils.isBlank(subApprovalModel.getBusinessId())) {
			throw new RuntimeException("提交信息为空！或者业务Id为空");
		}
		Contract contract = contractDao.selectByPrimaryKey(subApprovalModel.getBusinessId());
		if (null==contract){
			throw new RuntimeException("合同错误,找不到业务Id!");
		}
		if (contract.getStatus().equals(ContractStatus.Reject.getStatus())) {
			throw new RuntimeException("合同已驳回!不能重复提交!业务Id:" + subApprovalModel.getBusinessId());
		}
		subApprovalModel = commonService.additionalUsers(subApprovalModel);
		Task task = processEngine.getTaskService().createTaskQuery()
				.processInstanceBusinessKey(subApprovalModel.getBusinessId()).singleResult();
		// SubApprovalModel
		// currentApprovalUser=(SubApprovalModel)task.getProcessVariables().get("nextApprovalUser");

		// SubApprovalModel
		// nextApprovalUser=getNextAuditUser(currentApprovalUser.getBusinessId(),
		// currentApprovalUser.getDepartmentId());
		// task.setName("审批驳回合同");

		SubApprovalModel subModel = (SubApprovalModel) processEngine.getTaskService().getVariable(task.getId(),
				"subApprovalModel");

		Map<String, Object> vaiables = new HashMap<>();
		vaiables.put("isApproval", false);
		vaiables.put("processTag", ContractProcessStatus.ApprovalReject.getStatus());
		vaiables.put("currentUser", subApprovalModel);
		processEngine.getTaskService().setVariableLocal(task.getId(), "approvalRejectUserInfo", subApprovalModel);

		processEngine.getTaskService().addComment(task.getId(), task.getProcessInstanceId(),
				subApprovalModel.getReason());
		processEngine.getTaskService().setAssignee(task.getId(), subApprovalModel.getUserId());
		processEngine.getTaskService().complete(task.getId(), vaiables);
		UpdateContractStatus(subApprovalModel, ContractStatus.Reject.getStatus(),null);

//		String approvalPerson = userDao.selectByPrimaryKey(contract.getInputUserId()).getInputUserName();//contract.getInputUserName();
//		String applyUserId = contract.getInputUserId();
//		String suggestion = subApprovalModel.getReason();
//		wechartNoticeService.pushApprovalResultToApplyPerson(subApprovalModel.getBusinessId(), applyUserId,
//				approvalPerson, false, suggestion);
		noticeEmailByRejectContract(contract, subModel);
	}

	private void noticeEmailByRejectContract(Contract contract, SubApprovalModel subModel) {
		String subject = "【合同申请】";
		String subEmailContent = String.format("您的合同被驳回! 合同编号:【%s】 合同名称:【%s】 ", contract.getContractCode(),
				contract.getContractName());
		EmailModel eModel = new EmailModel();
		// eModel.setSubject(subject);
		eModel.setActivitiProcessType(ActivitiProcessType.ContactApprovalProcess);
		eModel.setBussinesOperatorType(ContractProcessStatus.ApprovalSuccess);
		eModel.setCurrentUsers(subModel);
		eModel.setIsSendSubmit(true);
		// eModel.setContent(subEmailContent);
		eModel.setSubmitSubject(subject);
		eModel.setSubContent(subEmailContent);
		emailTaskService.sendEmail(eModel);
	}

	@Autowired
	private IBrowsePermissionBaseDao browsePermissionDao;
	private final static String browseContractProcessInstanceKey = "contractBrowseProcess";

	/**
	 * 【查看申请流程】 审批驳回
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void browseApprovalReject(SubApprovalModel appModel) {
		try {
			ValidData(appModel);

			BrowsePermission browsePermission = browsePermissionDao.selectByPrimaryKey(appModel.getBusinessId());
			if (null == browsePermission) {
				throw new RuntimeException("预览申请信息为空!");
			}
			if (!browsePermission.getStatus().equals(BrowseAppStatus.Apploy.getStatus())) {
				throw new RuntimeException("预览申请非提交状态不能提交!");
			}

			Map<String, Object> vaiarbles = new HashMap<String, Object>();
			vaiarbles.put("isApproval", false);
			vaiarbles.put("processTag", ContractProcessStatus.ApprovalReject.getStatus());
			Task task = processEngine.getTaskService().createTaskQuery()
					.processInstanceBusinessKey(appModel.getBusinessId()).singleResult();

			SubApprovalModel subModel = (SubApprovalModel) processEngine.getTaskService().getVariable(task.getId(),
					"subApprovalModel");

			processEngine.getTaskService().setVariableLocal(task.getId(), "browseApprovalRejectUserInfo", appModel);
			processEngine.getTaskService().addComment(task.getId(), task.getProcessInstanceId(), appModel.getReason());
			processEngine.getTaskService().complete(task.getId(), vaiarbles);

			updateContractBrowseStatus(browsePermission, appModel, BrowseAppStatus.Reject.getStatus());

			noticeEmailToApplyPerson(appModel, subModel);
		} catch (Exception e) {
			throw e;
		}
	}

	private void noticeEmailToApplyPerson(SubApprovalModel appModel, SubApprovalModel subModel) {
		Contract contract = commonQueryDao.getContractByBrowsePermissionId(appModel.getBusinessId());
		String subject = "【合同查看申请】";
		String subEmailContent = String.format("您的合同查看申请被驳回! 合同编号:【%s】 合同名称:【%s】 ", contract.getContractCode(),
				contract.getContractName());
		EmailModel eModel = new EmailModel();
		// eModel.setSubject(subject);
		eModel.setActivitiProcessType(ActivitiProcessType.ContactBrowseApprovalProcess);
		eModel.setBussinesOperatorType(ContractProcessStatus.ApprovalReject);
		eModel.setCurrentUsers(subModel);
		eModel.setIsSendSubmit(true);
		// eModel.setContent(subEmailContent);
		eModel.setSubmitSubject(subject);
		eModel.setSubContent(subEmailContent);
		emailTaskService.sendEmail(eModel);
	}

	/**
	 * 【查看申请流程】 审批通过
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void browseApprovalSuccess(SubApprovalModel appModel) {
		try {
			ValidData(appModel);

			BrowsePermission browsePermission = browsePermissionDao.selectByPrimaryKey(appModel.getBusinessId());
			if (null == browsePermission) {
				throw new RuntimeException("预览申请信息为空!");
			}
			if (!browsePermission.getStatus().equals(BrowseAppStatus.Apploy.getStatus())) {
				throw new RuntimeException("预览申请非提交状态不能提交!");
			}
			// SubApprovalModel nextApprovalUser =
			// getNextAuditUser(appModel.getBusinessId(), appModel.getDepartmentId());

			List<SubApprovalModel> nextAuditUserList = commonService.getNextAuditUserList(appModel.getBusinessId(),
					appModel.getDepartmentId(), RoleType.Forensic);
			Map<String, Object> vaiarbles = new HashMap<String, Object>();
			vaiarbles.put("isApproval", true);
			// vaiarbles.put("nextApprovalUser", nextApprovalUser);
			vaiarbles.put("auditUsers", nextAuditUserList);
			vaiarbles.put("processTag", ContractProcessStatus.ApprovalSuccess.getStatus());
			Task task = processEngine.getTaskService().createTaskQuery()
					.processInstanceBusinessKey(appModel.getBusinessId()).singleResult();
			SubApprovalModel subModel = (SubApprovalModel) processEngine.getTaskService().getVariable(task.getId(),
					"subApprovalModel");
			processEngine.getTaskService().setVariableLocal(task.getId(), "browseApprovalSuccessUserInfo",
					nextAuditUserList);

			processEngine.getTaskService().complete(task.getId(), vaiarbles);

			updateContractBrowseStatus(browsePermission, appModel, BrowseAppStatus.Approvaled.getStatus());

			noticeEmailToApplyPersonAndNextApprovalPerson(appModel, nextAuditUserList, subModel);
		} catch (Exception e) {
			throw e;
		}
	}

	private void noticeEmailToApplyPersonAndNextApprovalPerson(SubApprovalModel appModel,
			List<SubApprovalModel> nextAuditUserList, SubApprovalModel subModel) {
		// 邮件相关
		Contract contract = commonQueryDao.getContractByBrowsePermissionId(appModel.getBusinessId());
		String subject = "【合同查看申请】";
		String emailContent = String.format("您有一个【合同查看申请】【待审批】,合同编号:【%s】 合同名称:【%s】", contract.getContractCode(),
				contract.getContractName());
		String subEmailContent = String.format("您的【合同查看申请】  通过审核! 合同编号:【%s】 合同名称:【%s】", contract.getContractCode(),
				contract.getContractName());
		EmailModel eModel = new EmailModel();
		eModel.setSubject(subject);
		eModel.setActivitiProcessType(ActivitiProcessType.ContactBrowseApprovalProcess);
		eModel.setBussinesOperatorType(ContractProcessStatus.ApprovalSuccess);
		eModel.setCurrentUsers(subModel);
		eModel.setIsSendSubmit(true);
		eModel.setContent(emailContent);
		eModel.setSubmitSubject(subject);
		eModel.setSubContent(subEmailContent);
		eModel.setNextApprovalList(nextAuditUserList);

		emailTaskService.sendEmail(eModel);
	}

	private BrowsePermission getSubitBrowsePersmission(String buinessId) {
		List<BrowsePermission> rBrowsePermissions = commonQueryDao.selectContracBrowsePerssiontByContractId(buinessId);
		if (rBrowsePermissions.size() > 0) {
			return rBrowsePermissions.get(0);
		}
		// java.util.UUID.randomUUID()
		return null;
	}

	/**
	 * 【查看申请流程】 提交申请
	 */
	@Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
	@Override
	public void browseAppSubmit(SubApprovalModel appModel) {
		try {
			ValidData(appModel);

			BrowsePermission browsePermission = getSubitBrowsePersmission(appModel.getBusinessId());
			if (null != browsePermission && browsePermission.getStatus().equals(BrowseAppStatus.Apploy.getStatus())) {
				throw new RuntimeException("预览申请已发出，不能再提交!");
			} else {
				browsePermission = new BrowsePermission();
				browsePermission.setContractId(appModel.getBusinessId());
				browsePermission.setReason(appModel.getReason());
				browsePermission.setUserId(appModel.getUserId());
				browsePermission.setStatus(BrowseAppStatus.Apploy.getStatus());
				browsePermission.setBrowsePermissionId(GuidUtils.newGuid());
				browsePermission.setInput(new Date());
				browsePermissionDao.insert(browsePermission);
			}
			SubApprovalModel nextApprovalUser = commonService.getNextApprovalUser(appModel.getBusinessId(),
					appModel.getDepartmentId());
			appModel = commonService.additionalUsers(appModel);

			Map<String, Object> vaiarbles = new HashMap<String, Object>();
			vaiarbles.put("subApprovalModel", appModel);
			// vaiarbles.put("isNew", true);
			vaiarbles.put("nextApprovalUser", nextApprovalUser);
			vaiarbles.put("processTag", ContractProcessStatus.Submit.getStatus());
			processEngine.getRuntimeService().startProcessInstanceByKey(browseContractProcessInstanceKey,
					browsePermission.getBrowsePermissionId(), vaiarbles);

			noticeEmailToNextApproval(appModel, nextApprovalUser);

		} catch (Exception e) {
			throw e;
		}
	}

	private void noticeEmailToNextApproval(SubApprovalModel appModel, SubApprovalModel nextApprovalUser) {
		List<SubApprovalModel> nextApprovalList = new ArrayList<>();
		nextApprovalList.add(nextApprovalUser);
		Contract contract = contractDao.selectByPrimaryKey(appModel.getBusinessId());
		String emailContent = String.format("您有一个合同查看申请【待审批】,合同编号:【%s】 合同名称:【%s】", contract.getContractCode(),
				contract.getContractName());
		EmailModel eModel = new EmailModel();
		eModel.setSubject("【合同查看申请】");
		eModel.setActivitiProcessType(ActivitiProcessType.ContactBrowseApprovalProcess);
		eModel.setBussinesOperatorType(ContractProcessStatus.Submit);
		eModel.setCurrentUsers(appModel);
		eModel.setIsSendSubmit(false);
		eModel.setContent(emailContent);
		eModel.setNextApprovalList(nextApprovalList);

		emailTaskService.sendEmail(eModel);
	}

	void addBrowseApp(SubApprovalModel appModel) {
		BrowsePermission browsePermission = new BrowsePermission();
		browsePermission.setContractId(appModel.getBusinessId());
		browsePermission.setReason(appModel.getReason());
		browsePermission.setUserId(appModel.getUserId());
		browsePermission.setStatus(BrowseAppStatus.Apploy.getStatus());
		browsePermissionDao.insert(browsePermission);
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

	
	/**
	 * 撤回合同-->走一次驳回节点
	 * 
	 */
	@Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
	@Override
	public void withdrawApply(SubApprovalModel subModel) {
		if (null == subModel) {
			throw new RuntimeException("提交Model为空!");
		}
		if (StringUtils.isBlank(subModel.getBusinessId())) {
			throw new RuntimeException("业务Id为空!");
		}
		if (StringUtils.isBlank(subModel.getUserId())) {
			throw new RuntimeException("提交UserId为空!");
		}
		Contract contract = contractDao.selectByPrimaryKey(subModel.getBusinessId());
		if (contract.getStatus().equals(ContractStatus.Approvaled.getStatus())) {
			throw new RuntimeException("合同已审批!不能撤回!业务Id:" + subModel.getBusinessId());
		}
		SubApprovalModel subApprovalModel = commonService.additionalUsers(subModel);
		Task task = processEngine.getTaskService().createTaskQuery()
				.processInstanceBusinessKey(subModel.getBusinessId()).singleResult();
//		SubApprovalModel subModel = (SubApprovalModel) processEngine.getTaskService().getVariable(task.getId(),
//				"subApprovalModel");
		task.setName("撤回合同");
		processEngine.getTaskService().saveTask(task);
		subApprovalModel.setReason("撤回合同");
		Map<String, Object> vaiables = new HashMap<>();
		vaiables.put("isApproval", false);
		vaiables.put("processTag", ContractProcessStatus.ApprovalReject.getStatus());
		vaiables.put("currentUser", subApprovalModel);
		processEngine.getTaskService().setVariableLocal(task.getId(), "approvalRejectUserInfo", subApprovalModel);

		processEngine.getTaskService().addComment(task.getId(), task.getProcessInstanceId(),
				subApprovalModel.getReason());
		processEngine.getTaskService().setAssignee(task.getId(), subApprovalModel.getUserId());
		processEngine.getTaskService().complete(task.getId(), vaiables);
		UpdateContractStatus(subApprovalModel, ContractStatus.Withdraw.getStatus(),null);
		commonQueryDao.ResetContractCounterSign(subModel.getBusinessId());
		//ResetContractCounterSign(subModel.getBusinessId());
	}
	
//	@Autowired
//	private IContractCounterSignBaseDao contractCounterSignDao;
	
	
}
