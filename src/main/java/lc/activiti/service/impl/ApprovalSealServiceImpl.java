package lc.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lc.activiti.lcenum.ApprosealRoleType;
import lc.activiti.service.NewRolesService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lc.activiti.lcenum.ApprosealApprovalType;
import lc.activiti.lcenum.SealStatus;
import lc.activiti.dao.IApprovalSealDao;
import lc.activiti.dao.base.IApprovalSealBaseDao;
import lc.activiti.entity.ApprovalSealBase;
import lc.activiti.service.ApprovalSealService;
import lc.activiti.entity.Users;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalSealServiceImpl implements ApprovalSealService {
	@Autowired
	private ProcessEngine processEngine;
	private final String approvalSealProcessName = "审批印章流程1.0";
	private final String approvalSealProcessBPM = "approvalSealProcess.bpmn";
	private static final String processDefinitKey = "approvalSealProcess";
	@Autowired
	private NewRolesService newRolesService;
	// 部署审批盖章流程
	@Override
	public void deployApprovalSealProcess() {
		processEngine.getRepositoryService().createDeployment().name(approvalSealProcessName)
				.addClasspathResource(approvalSealProcessBPM).deploy();
	}

	// 删除盖章审批流程
	@Override
	public void deleteApprovalSealProcess() {

	}

	@Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
	@Override
	public boolean submitApprovalSealApply(SubApprovalModel approvalModel) {
		validata(approvalModel);
		String approvalSealId = approvalModel.getBusinessId();
		ApprovalSealBase approvalSealBase = approvalSealBaseDao.selectByPrimaryKey(approvalSealId);
		if (!SealStatus.NewCreate.getStatus().equals(approvalSealBase.getSealStatus())
				&& !SealStatus.Reject.getStatus().equals(approvalSealBase.getSealStatus())) {
			throw new RuntimeException("审批已申请成功!不能重复申请!");
		}
		Map<String, Object> vaiarbles = getVaiableByBusinessType(approvalModel);
		ApprosealApprovalType approvalType= ApprosealApprovalType.getApprovalType(approvalSealBase.getApprovalType());
		List<SubApprovalModel> nextApprovalList=getFirstApprovalList(approvalType);
		vaiarbles.put("approvalType", approvalType);
		vaiarbles.put("nextApprovalList", nextApprovalList);
		vaiarbles.put("processTag", SealStatus.Apploy.getStatus());
		vaiarbles.put("subApprovalModel", approvalModel);
		long taskCount = processEngine.getTaskService().createTaskQuery()
				.processInstanceBusinessKey(approvalModel.getBusinessId()).count();
		if (taskCount == 1) {
			// 驳回再提交
			log.info("驳回再提交,业务Id={}", approvalModel.getBusinessId());
			// 驳回再提交
			Task task = processEngine.getTaskService().createTaskQuery()
					.processInstanceBusinessKey(approvalModel.getBusinessId()).singleResult();
			processEngine.getTaskService().setAssignee(task.getId(), approvalModel.getUserId());
			processEngine.getTaskService().complete(task.getId(), vaiarbles);
			correctApprovalSealInfo(approvalModel.getBusinessId());
		} else {
			log.info("【审批印章流程】申请提交,业务Id={}", approvalModel.getBusinessId());
			
			processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitKey,
					approvalModel.getBusinessId(), vaiarbles);
		}
		Short status = SealStatus.Apploy.getStatus();
		correctApprovalSeal(approvalModel.getBusinessId(), status,null,null);
		return false;
	}
	//再提交重置驳回信息
	private void correctApprovalSealInfo(String businessId) {
		ApprovalSealBase approvalSeal = approvalSealBaseDao.selectByPrimaryKey(businessId);
		approvalSeal.setRejectDate(null);
		approvalSeal.setRejectMemo(null);
		approvalSeal.setRejectUserId(null);
		approvalSealBaseDao.updateByPrimaryKey(approvalSeal);
	}
	@Autowired
	private IApprovalSealDao approvalSealDao;
	
	private List<SubApprovalModel> getFirstApprovalList(ApprosealApprovalType approvalType) {
		List<SubApprovalModel> nextApprovalList=new ArrayList<>();
		if (approvalType==null) {
			throw new RuntimeException("业务类型不能为空!");
		}
		List<Users> nUsersList=null;
		switch (approvalType.getType()) {
		case 1://干线拉直上游合同审批
		case 2://干线拉直下游合同审批
			nUsersList=approvalSealDao.getUserRoles(String.format("%s",
					ApprosealRoleType.TrunkLineOffice.getType()));

//			nUsersList = newRolesService.queryUsersByRoleName(ApprosealRoleType.TrunkLineOffice.getDesc());

			if (nUsersList.size()==0) {
				throw new RuntimeException("无干线办公室角色人员,请确认存在后提交申请!");
			}
			for (Users users : nUsersList) {
				SubApprovalModel sApprovalModel=new SubApprovalModel();
				sApprovalModel.setUserId(users.getUserId());
				sApprovalModel.setUserName(users.getUserName());
				nextApprovalList.add(sApprovalModel);
			}
			break;
		case 3://精准拼车上游合同审批
		case 4://精准拼车下游合同审批
		case 6://KA下游合同审批
			nUsersList=approvalSealDao.getUserRoles(String.format("%s",
					ApprosealRoleType.PrecisionCarpoolingOffice.getType()));
//			nUsersList = newRolesService.queryUsersByRoleName(ApprosealRoleType.PrecisionCarpoolingOffice.getDesc());
			if (nUsersList.size()==0) {
				throw new RuntimeException("无精准办公室角色人员,请确认存在后提交申请!");
			}
			for (Users users : nUsersList) {
				SubApprovalModel sApprovalModel=new SubApprovalModel();
				sApprovalModel.setUserId(users.getUserId());
				sApprovalModel.setUserName(users.getUserName());
				nextApprovalList.add(sApprovalModel);
			}
			break;
		case 5://KA上游合同审批
			nUsersList=approvalSealDao.getUserRoles(String.format("%s",
					ApprosealRoleType.DataVP.getType()));
//			nUsersList = newRolesService.queryUsersByRoleName(ApprosealRoleType.DataVP.getDesc());
			if (nUsersList.size()==0) {
				throw new RuntimeException("无精数据VP角色人员,请确认存在后提交申请!");
			}
			for (Users users : nUsersList) {
				SubApprovalModel sApprovalModel=new SubApprovalModel();
				sApprovalModel.setUserId(users.getUserId());
				sApprovalModel.setUserName(users.getUserName());
				nextApprovalList.add(sApprovalModel);
			}
			break;
		default:
			throw new RuntimeException("业务类型异常!");
			//break;
		}
		return nextApprovalList;
	}

	@Autowired
	private IApprovalSealBaseDao approvalSealBaseDao;

	private void correctApprovalSeal(String businessId, Short status,String reason,String userId) {
		ApprovalSealBase approvalSeal = approvalSealBaseDao.selectByPrimaryKey(businessId);
		approvalSeal.setSealStatus(status);
		if (!StringUtils.isBlank(reason)) {
			approvalSeal.setRejectMemo(reason);
			approvalSeal.setRejectDate(new Date());
			approvalSeal.setRejectUserId(userId);
		}
		approvalSealBaseDao.updateByPrimaryKey(approvalSeal);
	}

	@Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
	@Override
	public boolean approvalSealSuccess(SubApprovalModel approvalModel) {
		validata(approvalModel);
		boolean isExistApprovalTask=isExistApprovalTask(approvalModel);
		if (!isExistApprovalTask) {
			throw new RuntimeException("无审批可操作!");
		}
		Map<String, Object> vaiarbles=new HashMap<>();
		Task task = processEngine.getTaskService().createTaskQuery()
				.processInstanceBusinessKey(approvalModel.getBusinessId()).singleResult();
		//Map<String, Object> variables=task.getProcessVariables();
		//ApprosealApprovalType approvalType=(ApprosealApprovalType)processEngine.getRuntimeService().getVariable(task.getExecutionId(), "approvalType");
		String taskDefinitionKey=task.getTaskDefinitionKey();
		List<SubApprovalModel> nextApprovalUserList=getNextApprovalUserList(taskDefinitionKey);
		vaiarbles.put("nextApprovalList", nextApprovalUserList);
		vaiarbles.put("isApproval", true);
		processEngine.getTaskService().setAssignee(task.getId(), approvalModel.getUserId());
		processEngine.getTaskService().complete(task.getId(), vaiarbles);
		if ("taskFunctionVP".equals(taskDefinitionKey)) {
			//修正状态
			correctApprovalSeal(approvalModel.getBusinessId(), SealStatus.Approved.getStatus(),null,null);
		}
		return true;
	}

	private List<SubApprovalModel> getNextApprovalUserList(String taskDefinitionKey) {
		List<Users> nUsersList=null;
		List<SubApprovalModel> nextApprovalUserList=new ArrayList<>();
		switch (taskDefinitionKey) {
		case "taskTrunkLineOffice"://干线
			nUsersList=approvalSealDao.getUserRoles(String.format("%s",
					ApprosealRoleType.FinanceVP.getType()));
//			nUsersList = newRolesService.queryUsersByRoleName(ApprosealRoleType.FinanceVP.getDesc());
			if (nUsersList.size()==0) {
				throw new RuntimeException("无【财务VP】角色人员,请确认存在后提交!");
			}
			for (Users users : nUsersList) {
				SubApprovalModel sApprovalModel=new SubApprovalModel();
				sApprovalModel.setUserId(users.getUserId());
				sApprovalModel.setUserName(users.getUserName());
				nextApprovalUserList.add(sApprovalModel);
			}
			break;
		case "taskFinanceVP"://财务VPTask
		case "taskPrecisionCarpoolingOffice"://精准拼车
			nUsersList=approvalSealDao.getUserRoles(String.format("%s",
					ApprosealRoleType.DataVP.getType()));
//			nUsersList = newRolesService.queryUsersByRoleName(ApprosealRoleType.DataVP.getDesc());
			if (nUsersList.size()==0) {
				throw new RuntimeException("无【数据VP】角色人员,请确认存在后提交!");
			}
			for (Users users : nUsersList) {
				SubApprovalModel sApprovalModel=new SubApprovalModel();
				sApprovalModel.setUserId(users.getUserId());
				sApprovalModel.setUserName(users.getUserName());
				nextApprovalUserList.add(sApprovalModel);
			}
			break;
		
		case "taskDataVP"://数据vp
			nUsersList=approvalSealDao.getUserRoles(String.format("%s",
					ApprosealRoleType.FunctionVP.getType()));
//			nUsersList = newRolesService.queryUsersByRoleName(ApprosealRoleType.FunctionVP.getDesc());
			if (nUsersList.size()==0) {
				throw new RuntimeException("无【职能VP】角色人员,请确认存在后提交!");
			}
			for (Users users : nUsersList) {
				SubApprovalModel sApprovalModel=new SubApprovalModel();
				sApprovalModel.setUserId(users.getUserId());
				sApprovalModel.setUserName(users.getUserName());
				nextApprovalUserList.add(sApprovalModel);
			}
			break;
		default:
			break;
		}
		return nextApprovalUserList;
	}

	@Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
	@Override
	public boolean rejectApprovalSeal(SubApprovalModel approvalModel) {
		validata(approvalModel);
		if (StringUtils.isEmpty(approvalModel.getReason())) {
			throw new RuntimeException("驳回原因不能为空!");
		}
		String approvalSealId = approvalModel.getBusinessId();
		ApprovalSealBase approvalSealBase = approvalSealBaseDao.selectByPrimaryKey(approvalSealId);
		if (SealStatus.Reject.getStatus().equals(approvalSealBase.getSealStatus())) {
			throw new RuntimeException("审批已驳回!");
		}
		boolean isExistApprovalTask=isExistApprovalTask(approvalModel);
		if (!isExistApprovalTask) {
			throw new RuntimeException("无审批可操作!");
		}
		
		Map<String, Object> vaiarbles=new HashMap<>();
		Task task = processEngine.getTaskService().createTaskQuery()
				.processInstanceBusinessKey(approvalModel.getBusinessId()).singleResult();
		//Map<String, Object> variables=task.getProcessVariables();
		//ApprosealApprovalType approvalType=(ApprosealApprovalType)processEngine.getRuntimeService().getVariable(task.getExecutionId(), "approvalType");
		//String taskId=task.getId();
		//List<SubApprovalModel> nextApprovalUserList=getNextApprovalUserList(taskId);
		//vaiarbles.put("nextApprovalList", nextApprovalUserList);
		vaiarbles.put("isApproval", false);
		vaiarbles.put("processTag", SealStatus.Reject.getStatus());
		processEngine.getTaskService().addComment(task.getId(), task.getProcessInstanceId(), approvalModel.getReason());
		processEngine.getTaskService().setAssignee(task.getId(), approvalModel.getUserId());
		processEngine.getTaskService().complete(task.getId(), vaiarbles);
		//修正状态
		correctApprovalSeal(approvalModel.getBusinessId(), SealStatus.Reject.getStatus(),approvalModel.getReason(),approvalModel.getUserId());
		return true;
	}
	boolean isExistApprovalTask(SubApprovalModel approvalModel) {
		long taskCount = processEngine.getTaskService().createTaskQuery()
				.taskCandidateUser(approvalModel.getUserId())
				.processInstanceBusinessKey(approvalModel.getBusinessId())
				.count();
		if (taskCount>0) {
			return true;
		}
		return false;
	}
	Map<String, Object> getVaiableByBusinessType(SubApprovalModel approvalModel) {
		Map<String, Object> vaiarable = new HashMap<>();
		ApprovalSealBase approvalSealBase = approvalSealBaseDao.selectByPrimaryKey(approvalModel.getBusinessId());
		if (ApprosealApprovalType.PrecisionCarpoolingDownstream.getType().equals(approvalSealBase.getApprovalType())
				|| ApprosealApprovalType.PrecisionCarpoolingUpstream.getType().equals(approvalSealBase.getApprovalType())
				|| ApprosealApprovalType.KADownstream.getType().equals(approvalSealBase.getApprovalType())) {
			vaiarable.put("IsPrecisionCarpooling", true);
			vaiarable.put("IsTrunkLine", false);
			vaiarable.put("isKAUpstream", false);
			return vaiarable;
		}
		if (ApprosealApprovalType.TrunkLineUpstream.getType().equals(approvalSealBase.getApprovalType())
				|| ApprosealApprovalType.TrunkLineDowstream.getType().equals(approvalSealBase.getApprovalType())) {
			vaiarable.put("IsTrunkLine", true);
			vaiarable.put("isKAUpstream", false);
			vaiarable.put("IsPrecisionCarpooling", false);
			return vaiarable;

		}
		if (ApprosealApprovalType.KAUpstream.getType().equals(approvalSealBase.getApprovalType())) {
			vaiarable.put("isKAUpstream", true);
			vaiarable.put("IsTrunkLine", false);
			vaiarable.put("IsPrecisionCarpooling", false);
			return vaiarable;
		}
		return vaiarable;
	}

	void validata(SubApprovalModel approvalModel) {
		if (null == approvalModel) {
			throw new RuntimeException("提交审批信息为空!");
		}
		if (StringUtils.isBlank(approvalModel.getBusinessId())) {
			throw new RuntimeException("业务Id不能为空!");
		}
		if (StringUtils.isBlank(approvalModel.getUserId())) {
			throw new RuntimeException("userId不能为空!");
		}
			
	}
}
