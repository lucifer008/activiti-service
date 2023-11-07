package lc.activiti.service.impl;

import lc.activiti.dao.IApprovalProgressDao;
import lc.activiti.dao.IDepartmentDao;
import lc.activiti.dao.base.IDepartmentBaseDao;
import lc.activiti.dao.base.IOrderInfoBaseDao;
import lc.activiti.entity.Departments;
import lc.activiti.entity.OrderInfoBase;
import lc.activiti.lcenum.ActivitiProcessType;
import lc.activiti.lcenum.BusinessTypeEnu;
import lc.activiti.lcenum.ZTOrdersStatus;
import lc.activiti.model.EmailModel;
import lc.activiti.model.SubApprovalModel;
import lc.activiti.service.CommonApprovalService;
import lc.activiti.service.CommonService;
import lc.activiti.service.EmailTaskService;
import lc.activiti.service.ZTOrderService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 描述： 砖头订单服务 创建人：lucifer 创建时间：2019年2月26日 下午2:24:15
 * 
 * @version
 */
@Slf4j
@Service
public class ZTOrderServiceImpl implements ZTOrderService {

	private static final String processDefinitKey = "commonApprovalProcess";
	@Autowired
	private IOrderInfoBaseDao orderInfoBaseDao;
	@Autowired
	private CommonService commonService;

	@Autowired
	private ProcessEngine processEngine;

	@Autowired
	private IDepartmentBaseDao departmentDao;

	@Autowired
	private CommonApprovalService commonApprovalService;

	@Override
	public boolean approvalReject(SubApprovalModel model) {
		OrderInfoBase orderInfo = validata(model);

		if (orderInfo.getGiftStatus().equals(ZTOrdersStatus.AuditedReject.getStatus())) {
			throw new RuntimeException("订单已驳回！不能重复驳回！");
		}
		// 非部门负责人跳过审核直接审批
		boolean isDepartmentHeaderSub = checkIsDepartmentHeaderSubmit(model.getBusinessId());
		if (!isDepartmentHeaderSub) {
			if (!orderInfo.getGiftStatus().equals(ZTOrdersStatus.Audited.getStatus())) {
				throw new RuntimeException("订单为非审核状态不能驳回！");
			}
		}
		model = commonService.additionalUsers(model);
		// Task task =
		// processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(model.getBusinessId())
		// .singleResult();
		// Map<String, Object> vaiables = new HashMap<>();
		// vaiables.put("isApproval", false);
		// vaiables.put("processTag", ZTOrdersStatus.ApprovaledReject.getStatus());
		// vaiables.put("currentUser", model);
		//
		// processEngine.getTaskService().setAssignee(task.getId(), model.getUserId());
		// processEngine.getTaskService().complete(task.getId(), vaiables);
		completeTask(model, 4);
		// 修正业务状态
		orderInfo.setGiftStatus(ZTOrdersStatus.ApprovaledReject.getStatus());
		orderInfoBaseDao.updateByPrimaryKey(orderInfo);

		// subModel=commonService.additionalUsers(subModel);
		noticeApprovalUser(model, orderInfo.getOrderNo(), 5);
		return false;
	}

	@Override
	public boolean approvalSuccess(SubApprovalModel model) {
		OrderInfoBase orderInfo = validata(model);

		if (orderInfo.getGiftStatus().equals(ZTOrdersStatus.Approvaled.getStatus())) {
			throw new RuntimeException("订单已审批！不能重复审批！");
		}
		// 非部门负责人跳过审核直接审批
		boolean isDepartmentHeaderSub = checkIsDepartmentHeaderSubmit(model.getBusinessId());
		if (!isDepartmentHeaderSub) {
			if (!orderInfo.getGiftStatus().equals(ZTOrdersStatus.Audited.getStatus())) {
				throw new RuntimeException("订单为非审核状态不能驳回！");
			}
		}

		model = commonService.additionalUsers(model);
		// Task task =
		// processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(model.getBusinessId())
		// .singleResult();
		// Map<String, Object> vaiables = new HashMap<>();
		// vaiables.put("isApproval", true);
		// vaiables.put("processTag", ZTOrdersStatus.Approvaled.getStatus());
		// vaiables.put("currentUser", model);
		//
		// processEngine.getTaskService().setAssignee(task.getId(), model.getUserId());
		// processEngine.getTaskService().complete(task.getId(), vaiables);
		completeTask(model, 3);
		// 修正业务状态
		orderInfo.setGiftStatus(ZTOrdersStatus.Approvaled.getStatus());
		orderInfoBaseDao.updateByPrimaryKey(orderInfo);
		noticeApprovalUser(model, orderInfo.getOrderNo(), 4);
		return true;
	}

	@Autowired
	private EmailTaskService emailTaskService;
	@Value("${lc.business-sys-url}")
	private String businessSysUrl;

	/**
	 * 通知审批人或者审核人邮件
	 * 
	 * @param model
	 * @param orderNo
	 * @param action
	 */
	private void noticeApprovalUser(SubApprovalModel model, String orderNo, int action) {
		String emailContent = null;
		String subEmailContent = null;
		String subject = null;
		String subjectSub = null;
		switch (action) {
		case 1:// 申请通知
			subject = "【金砖服务-待审核】";
			emailContent = String.format("您有一个赠品申请【待审核】，订单号【%s】，请登陆乐卡车联业务管理系统查看！<a href='%s'>点此登陆</a>。", orderNo,
					businessSysUrl);
			break;
		case 2:// 审核通过
			subject = "【金砖服务-待审批】";
			subjectSub = "【金砖服务-审核通过】";
			emailContent = String.format("您有一个赠品申请【待审批】，订单号【%s】，请登陆乐卡车联业务管理系统查看！<a href='%s'>点此登陆</a>。", orderNo,
					businessSysUrl);
			subEmailContent = String.format("您有一个赠品申请【审核通过】，订单号【%s】，请登陆乐卡车联业务管理系统查看！<a href='%s'>点此登陆</a>。", orderNo,
					businessSysUrl);
			break;
		case 3:// 审核驳回
			subject = "【金砖服务-审核驳回】";
			// emailContent = String.format("您有一个赠品申请【待审批】，订单号【%s】，请登陆乐卡车联业务管理系统查看！点此登陆。",
			// orderNo);
			emailContent = String.format("您有一个赠品申请【审核驳回】，订单号【%s】，请登陆乐卡车联业务管理系统查看！<a href='%s'>点此登陆</a>。", orderNo,
					businessSysUrl);
			break;
		case 4:// 审批通过
			subject = "【金砖服务-审批已通过】";
			// emailContent = String.format("您有一个赠品申请【待】，订单号【%s】，请登陆乐卡车联业务管理系统查看！点此登陆。",
			// orderNo);
			emailContent = String.format("您有一个赠品申请【审批已通过】，订单号【%s】，请登陆乐卡车联业务管理系统查看！<a href='%s'>点此登陆</a>。", orderNo,
					businessSysUrl);
			break;
		case 5:// 审批驳回
			subject = "【金砖服务-审批驳回】";
			// emailContent = String.format("您有一个赠品申请【待审批】，订单号【%s】，请登陆乐卡车联业务管理系统查看！点此登陆。",
			// orderNo);
			emailContent = String.format("您有一个赠品申请【审批驳回】，订单号【%s】，请登陆乐卡车联业务管理系统查看！<a href='%s'>点此登陆</a>。", orderNo,
					businessSysUrl);
			break;
		default:
			break;
		}
		if (2 == action) {

			EmailModel eModel = new EmailModel();
			eModel.setSubject(subject);
			eModel.setActivitiProcessType(ActivitiProcessType.CommonApprovalProcess);
			// eModel.setBussinesOperatorType(ContractProcessStatus.ApprovalSuccess);
			eModel.setCurrentUsers(model);
			eModel.setIsSendSubmit(true);
			eModel.setContent(emailContent);
			eModel.setSubmitSubject(subjectSub);
			eModel.setSubContent(subEmailContent);
			if (null != model.getNextApprovalUsers() && model.getNextApprovalUsers().size() > 0) {
				eModel.setNextApprovalList(model.getNextApprovalUsers());
			} else {
				List<SubApprovalModel> nextApprovalList = new ArrayList<SubApprovalModel>();
				nextApprovalList.add(model);
				eModel.setNextApprovalList(nextApprovalList);
			}
			emailTaskService.sendEmail(eModel);
		} else {
			EmailModel eModel = new EmailModel();
			eModel.setSubject(subject);
			eModel.setActivitiProcessType(ActivitiProcessType.CommonApprovalProcess);
			// eModel.setBussinesOperatorType(Z.Submit);
			eModel.setCurrentUsers(model);
			eModel.setIsSendSubmit(false);
			eModel.setContent(emailContent);
			if (null != model.getNextApprovalUsers() && model.getNextApprovalUsers().size() > 0) {
				eModel.setNextApprovalList(model.getNextApprovalUsers());
			} else {
				List<SubApprovalModel> nextApprovalList = new ArrayList<SubApprovalModel>();
				nextApprovalList.add(model);
				eModel.setNextApprovalList(nextApprovalList);
			}
			emailTaskService.sendEmail(eModel);
		}

	}

	@Override
	public boolean audtiReject(SubApprovalModel subApprovalModel) {
		OrderInfoBase orderInfo = validata(subApprovalModel);

		if (orderInfo.getGiftStatus().equals(ZTOrdersStatus.AuditedReject.getStatus())) {
			throw new RuntimeException("订单已驳回！不能重复驳回！");
		}

		if (!orderInfo.getGiftStatus().equals(ZTOrdersStatus.Submited.getStatus())) {
			throw new RuntimeException("订单为提交状态不能驳回！");
		}
//		boolean isSkipApproval=checkIsSkipApproval(subApprovalModel.getBusinessId());
//		if (isSkipApproval) {
//			throw new RuntimeException("非法操作，该订单无需审核驳回！");
//		}
		subApprovalModel = commonService.additionalUsers(subApprovalModel);
		// Task task = processEngine.getTaskService().createTaskQuery()
		// .processInstanceBusinessKey(subApprovalModel.getBusinessId()).singleResult();

		// SubApprovalModel subModel = (SubApprovalModel)
		// processEngine.getTaskService().getVariable(task.getId(),
		// "subApprovalModel");

		// Map<String, Object> vaiables = new HashMap<>();
		// vaiables.put("isAudit", false);
		// vaiables.put("processTag", ZTOrdersStatus.AuditedReject.getStatus());
		// vaiables.put("currentUser", subApprovalModel);
		//
		// // processEngine.getTaskService().setAssignee(task.ge, userId);
		// task.setName("审核驳回合同");
		// processEngine.getTaskService().addComment(task.getId(),
		// task.getProcessInstanceId(),
		// subApprovalModel.getReason());
		// processEngine.getTaskService().setAssignee(task.getId(),
		// subApprovalModel.getUserId());
		//
		// processEngine.getTaskService().complete(task.getId(), vaiables);
		completeTask(subApprovalModel, 2);
		// 修正业务状态
		orderInfo.setGiftStatus(ZTOrdersStatus.AuditedReject.getStatus());
		orderInfoBaseDao.updateByPrimaryKey(orderInfo);
		// subModel=commonService.additionalUsers(subModel);
		noticeApprovalUser(subApprovalModel, orderInfo.getOrderNo(), 3);
		return true;
	}

	private boolean checkIsDepartmentHeaderSubmit(String businessId) {
		Task task = processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(businessId)
				.singleResult();

		boolean isDepartmentHeader = (boolean) processEngine.getTaskService().getVariable(task.getId(),
				"isDepartmentHeader");
		return isDepartmentHeader;
	}
	private boolean checkIsSkipApproval(String businessId) {
		Task task = processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(businessId)
				.singleResult();

	return	null== processEngine.getTaskService().getVariable(task.getId(),
				"isAudit")?true:false;
	}
	@Autowired
	private IApprovalProgressDao approvalProgressDao;

	@Override
	public boolean submitApply(SubApprovalModel subModel) {
		OrderInfoBase orderInfo = validata(subModel);
		if (null == orderInfo) {
			throw new RuntimeException("找不到业务Id对应的订单！");
		}
		if (orderInfo.getGiftStatus().equals(ZTOrdersStatus.Submited.getStatus())) {
			throw new RuntimeException("订单已提交！不能重复提交！");
		}
		if (!orderInfo.getGiftStatus().equals(ZTOrdersStatus.WaitSubmit.getStatus())
				&& !orderInfo.getGiftStatus().equals(ZTOrdersStatus.ApprovaledReject.getStatus())
				&& !orderInfo.getGiftStatus().equals(ZTOrdersStatus.AuditedReject.getStatus())) {
			throw new RuntimeException("订单为非待提交状态不能提交！");
		}
		if (!subModel.getUserId().equals(orderInfo.getLkuserId())) {
			throw new RuntimeException("销售用户Id与提交申请人不一致！");
		}
		boolean isDepartmentHeader = checkDepartmentHeader(orderInfo.getDepartmentId(), orderInfo.getLkuserId());
		subModel.setDepartmentId(orderInfo.getDepartmentId());
		SubApprovalModel nextApprovalUser = null;
		if (!isDepartmentHeader) {
			nextApprovalUser = commonService.getNextApprovalUser(orderInfo.getId(), orderInfo.getDepartmentId());
		} else {
			Departments departments = departmentDao.selectByPrimaryKey(orderInfo.getDepartmentId());
			String pDepartmentId = departments.getOaId();
			nextApprovalUser = commonService.getNextApprovalUser(orderInfo.getId(), pDepartmentId);
		}

		long taskCount = processEngine.getTaskService().createTaskQuery()
				.processInstanceBusinessKey(subModel.getBusinessId()).count();

		if (taskCount == 1 || (orderInfo.getGiftStatus().equals(ZTOrdersStatus.ApprovaledReject.getStatus())
				|| orderInfo.getGiftStatus().equals(ZTOrdersStatus.AuditedReject.getStatus()))) {
			// 驳回再提交
			log.info("驳回再提交,业务Id={}", subModel.getBusinessId());
			Map<String, Object> vaiarbles = new HashMap<>();
			vaiarbles.put("subApprovalModel", subModel);
			vaiarbles.put("processTag", ZTOrdersStatus.RejectSubmit.getStatus());
			vaiarbles.put("nextApprovalUser", nextApprovalUser);
			vaiarbles.put("isDepartmentHeader", isDepartmentHeader);
			// 驳回再提交
			Task task = processEngine.getTaskService().createTaskQuery()
					.processInstanceBusinessKey(subModel.getBusinessId()).singleResult();
			processEngine.getTaskService().setAssignee(task.getId(), subModel.getUserId());
			processEngine.getTaskService().complete(task.getId(), vaiarbles);

			approvalProgressDao.deleteApprovalProcessNodes(subModel.getBusinessId());
			commonApprovalService.generatorApprovalProcessNodes(subModel.getBusinessId(), subModel.getUserId(),
					subModel.getDepartmentId(), isDepartmentHeader);

		} else {// 启动申请流程
			// 生成审批节点
			log.info("启动申请流程,业务Id={}", subModel.getBusinessId());

			Map<String, Object> vaiarbles = new HashMap<>();
			vaiarbles.put("subApprovalModel", subModel);
			vaiarbles.put("processTag", ZTOrdersStatus.Submited.getStatus());
			vaiarbles.put("nextApprovalUser", nextApprovalUser);
			vaiarbles.put("isDepartmentHeader", isDepartmentHeader);
			processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitKey, subModel.getBusinessId(),
					vaiarbles);

			commonApprovalService.generatorApprovalProcessNodes(subModel.getBusinessId(), subModel.getUserId(),
					subModel.getDepartmentId(), isDepartmentHeader);

		}
		if(isDepartmentHeader) {
			// 修正业务状态
			orderInfo.setGiftStatus(ZTOrdersStatus.Audited.getStatus());
		}
		else {
			// 修正业务状态
			orderInfo.setGiftStatus(ZTOrdersStatus.Submited.getStatus());	
		}
		
		orderInfoBaseDao.updateByPrimaryKey(orderInfo);

		subModel = commonService.additionalUsers(subModel);
		noticeApprovalUser(subModel, orderInfo.getOrderNo(), 1);
		return true;
	}

	@Autowired
	private IDepartmentDao departmentDao2;

	private boolean checkDepartmentHeader(String departmentId, String userId) {
		Departments departments = departmentDao.selectByPrimaryKey(departmentId);
		if (null == departments.getOaId() && userId.equals(departments.getDepPersonId())) {
			throw new RuntimeException("顶级部门负责人不能提交订单！");
		}
		if (!userId.equals(departments.getDepPersonId())) {
			return false;
		}
		return true;//departmentDao2.isDepartmentLeader(userId);
	}

	@Override
	public boolean audtiSuccess(SubApprovalModel appModel) {
		OrderInfoBase orderInfo = validata(appModel);
		if (null == orderInfo) {
			throw new RuntimeException("找不到业务Id对应的订单！");
		}
		if (orderInfo.getGiftStatus().equals(ZTOrdersStatus.Audited.getStatus())) {
			throw new RuntimeException("订单已审核！不能重复审核！");
		}
		if (!orderInfo.getGiftStatus().equals(ZTOrdersStatus.Submited.getStatus())) {
			throw new RuntimeException("订单为非提交状态不能提交审核！");
		}
//		boolean isSkipApproval= checkIsSkipApproval(appModel.getBusinessId());
//		if (isSkipApproval) {
//			throw new RuntimeException("非法操作，该订单无需审核！");
//		}
		completeTask(appModel, 1);

		// 修正业务状态
		orderInfo.setGiftStatus(ZTOrdersStatus.Audited.getStatus());
		orderInfoBaseDao.updateByPrimaryKey(orderInfo);

		// subModel=commonService.additionalUsers(subModel);
		noticeApprovalUser(appModel, orderInfo.getOrderNo(), 2);
		return true;
	}

	/**
	 * 完成任务
	 * 
	 * @param appModel
	 * @param tag
	 *            1:审核通过;2:审核驳回;3.审批通过；4：审批驳回
	 */
	private void completeTask(SubApprovalModel appModel, int tag) {
		appModel = commonService.additionalUsers(appModel);
		Task task = processEngine.getTaskService().createTaskQuery()
				.processInstanceBusinessKey(appModel.getBusinessId()).singleResult();

		SubApprovalModel subModel = (SubApprovalModel) processEngine.getTaskService().getVariable(task.getId(),
				"subApprovalModel");
		SubApprovalModel currentUserInfo = (SubApprovalModel) processEngine.getTaskService().getVariable(task.getId(),
				"nextApprovalUser");
		SubApprovalModel nextApprovalUser = null;
		Map<String, Object> vaiables = new HashMap<>();
		switch (tag) {
		case 1:// 审核通过
				// 获取当前部门的上级部门
			Departments currentDepartment = departmentDao.selectByPrimaryKey(currentUserInfo.getDepartmentId());

			String partmentDepId = currentDepartment.getOaId();
			if (StringUtils.isBlank(partmentDepId)) {
				throw new RuntimeException("当前部门无上级部门不能提交审核！");
			}
			nextApprovalUser = commonService.getNextApprovalUser(appModel.getBusinessId(), partmentDepId);

			vaiables.put("isAudit", true);
			vaiables.put("processTag", ZTOrdersStatus.Audited.getStatus());
			vaiables.put("currentUser", currentUserInfo);
			vaiables.put("nextApprovalUser", nextApprovalUser);
			processEngine.getTaskService().setVariableLocal(task.getId(), "AuditUserInfo", currentUserInfo);
			commonApprovalService.updateApprovalProcessNodesByPass(appModel.getBusinessId(),
					currentUserInfo.getUserId());
			break;
		case 2:// 审核驳回
			vaiables.put("isAudit", false);
			vaiables.put("processTag", ZTOrdersStatus.AuditedReject.getStatus());
			vaiables.put("currentUser", currentUserInfo);
			processEngine.getTaskService().setAssignee(task.getId(), currentUserInfo.getUserId());
			commonApprovalService.updateApprovalProcessNodesByReject(appModel.getBusinessId());
			break;
		case 3:// 审批通过
			vaiables.put("isApproval", true);
			vaiables.put("processTag", ZTOrdersStatus.Approvaled.getStatus());
			vaiables.put("currentUser", appModel);
			processEngine.getTaskService().setAssignee(task.getId(), currentUserInfo.getUserId());

			commonApprovalService.updateApprovalProcessNodesByPass(appModel.getBusinessId(),
					currentUserInfo.getUserId());
			break;
		case 4:// 审批驳回
			vaiables.put("isApproval", false);
			vaiables.put("processTag", ZTOrdersStatus.ApprovaledReject.getStatus());
			vaiables.put("currentUser", appModel);
			processEngine.getTaskService().setAssignee(task.getId(), currentUserInfo.getUserId());
			commonApprovalService.updateApprovalProcessNodesByReject(appModel.getBusinessId());
			break;
		default:
			break;
		}
		// processEngine.getTaskService().setAssignee(task.getId(),
		// currentUserInfo.getUserId());
		processEngine.getTaskService().addComment(task.getId(), task.getProcessInstanceId(), appModel.getReason());
		processEngine.getTaskService().complete(task.getId(), vaiables);
	}

	OrderInfoBase validata(SubApprovalModel subApprovalModel) {
		if (null == subApprovalModel) {
			throw new RuntimeException("提交信息为空！");
		}
		if (StringUtils.isBlank(subApprovalModel.getUserId())) {
			throw new RuntimeException("用户Id为空！");
		}
		if (StringUtils.isBlank(subApprovalModel.getBusinessType())) {
			throw new RuntimeException("业务类型为空！");
		}
		if (StringUtils.isBlank(subApprovalModel.getBusinessId())) {
			throw new RuntimeException("业务Id为空！");
		}
		if (null == BusinessTypeEnu.getBusinessType(subApprovalModel.getBusinessType())) {
			throw new RuntimeException("业务类型不存在！");
		}
		if (StringUtils.isBlank(subApprovalModel.getReason())) {
			throw new RuntimeException("备注信息不能为空！");
		}
		OrderInfoBase orderInfo = orderInfoBaseDao.selectByPrimaryKey(subApprovalModel.getBusinessId());
		if (null == orderInfo) {
			throw new RuntimeException("找不到业务Id对应的订单！");
		}
		boolean userIsExistDepartment = departmentDao2.userIsExistDepartment(orderInfo.getLkuserId());
		if (!userIsExistDepartment) {
			throw new RuntimeException("订单用户无部门不能提交！");
		}
		boolean userIsExistDepartmentBySub = departmentDao2.userIsExistDepartment(subApprovalModel.getUserId());
		if (!userIsExistDepartmentBySub) {
			throw new RuntimeException("审批流提交用户无部门不能提交！");
		}
		if (null == orderInfo.getGiftStatus()) {
			throw new RuntimeException("业务订单状态错误！");
		}
		if (null == orderInfo.getDepartmentId()) {
			throw new RuntimeException("订单无部门不能提交!");
		}
		return orderInfo;
	}
}
