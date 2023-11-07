package lc.activiti.service.impl;

import java.util.ArrayList;
import java.util.List;

import lc.activiti.lcenum.ApprosealRoleType;
import lc.activiti.service.NewRolesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lc.activiti.lcenum.RoleType;
import lc.activiti.dao.ICommonQueryDao;
import lc.activiti.dao.base.IDepartmentBaseDao;
import lc.activiti.dao.base.IUserBaseDao;
import lc.activiti.entity.Departments;
import lc.activiti.entity.Users;
import lc.activiti.service.CommonService;
import lc.activiti.model.SubApprovalModel;

@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	private ICommonQueryDao commonQueryDao;
	@Autowired
	private IDepartmentBaseDao departmentDao;
	@Autowired
	private NewRolesService newRolesService;
	@Autowired
	private IUserBaseDao userDao;

	public SubApprovalModel getNextAuditUser(String businessId, String departmentId) {
		if (StringUtils.isBlank(businessId)) {
			throw new RuntimeException("业务Id不存在! id=:" + businessId);
		}
		if (StringUtils.isBlank(departmentId)) {
			throw new RuntimeException("部门不存在! id=:" + departmentId);
		}
		Departments departments = departmentDao.selectByPrimaryKey(departmentId);
		if (null == departments || StringUtils.isBlank(departments.getDepPersonId())) {
			throw new RuntimeException("部门负责人不存在! id=:" + departmentId);
		}
		Short roleType = RoleType.Forensic.getRoleType();
		List<Users> users = commonQueryDao.selectUsersByRoleType(new Integer(roleType));
//		users = newRolesService.queryUsersByRoleName(RoleType.Forensic.getDesc());
		if (users.size() == 0) {
			throw new RuntimeException("无法务角色人员! id=:" + departmentId);
		}
		if (users.size() > 1) {
			throw new RuntimeException("法务角色人员超过两个! id=:" + departmentId);
		}
		SubApprovalModel auditUser = new SubApprovalModel();
		auditUser.setBusinessId(businessId);
		auditUser.setDepartmentId(departmentId);
		auditUser.setUserId(users.get(0).getUserId());
		return auditUser;
	}

	public List<SubApprovalModel> getNextApprovalUserList(String businessId, String departmentId,
			boolean isFilterDepPerson) {
		if (StringUtils.isBlank(businessId)) {
			throw new RuntimeException("业务Id不存在! id=:" + businessId);
		}
		if (StringUtils.isBlank(departmentId)) {
			throw new RuntimeException("部门不存在! id=:" + departmentId);
		}
		if (!isFilterDepPerson) {
			Departments departments = departmentDao.selectByPrimaryKey(departmentId);
			if (null == departments || StringUtils.isBlank(departments.getDepPersonId())) {
				throw new RuntimeException("部门负责人不存在! id=:" + departmentId);
			}
		}
		Short roleType = RoleType.DataVP.getRoleType();
		List<Users> usersList =commonQueryDao.selectUsersByRoleType(new Integer(roleType));
//		usersList = newRolesService.queryUsersByRoleName(RoleType.DataVP.getDesc());
		if (usersList.size() == 0) {
			throw new RuntimeException("无数据VP角色人员! id=:" + departmentId);
		}
		// if (users.size() > 1) {
		// throw new RuntimeException("法务角色人员超过两个! id=:" + departmentId);
		// }
		List<SubApprovalModel> auditUserList = new ArrayList<>();
		for (Users us : usersList) {
			SubApprovalModel auditUser = new SubApprovalModel();
			auditUser.setBusinessId(businessId);
			auditUser.setDepartmentId(departmentId);
			auditUser.setUserId(us.getUserId());
			Users users = userDao.selectByPrimaryKey(us.getUserId());
			auditUser.setUserEmail(users.getUserEmail());
			auditUser.setUserName(users.getUserName());
			auditUser.setUserMoible(users.getUserMobile());
			auditUserList.add(auditUser);
		}

		return auditUserList;
	}

	/**
	 * @author Raytine
	 * @businessId 业务Id
	 * @depId 业务所在部门Id
	 */
	public SubApprovalModel getNextApprovalUser(String businessId, String depId) {
		if (StringUtils.isBlank(businessId)) {
			throw new RuntimeException("业务Id不存在! id=:" + depId);
		}
		if (StringUtils.isBlank(depId)) {
			throw new RuntimeException("部门不存在! id=:" + depId);
		}
		Departments departments = departmentDao.selectByPrimaryKey(depId);
		if (null == departments) {
			throw new RuntimeException("部门不存在! id=:" + depId);
		}
		if (StringUtils.isBlank(departments.getDepPersonId())) {
			throw new RuntimeException("部门没有负责人不能提交申请! 部门id=:" + depId);
		}
		Users users = userDao.selectByPrimaryKey(departments.getDepPersonId());

		SubApprovalModel nextApprovalUser = new SubApprovalModel();
		nextApprovalUser.setBusinessId(businessId);
		nextApprovalUser.setDepartmentId(depId);
		nextApprovalUser.setUserId(departments.getDepPersonId());
		nextApprovalUser.setUserEmail(users.getUserEmail());
		nextApprovalUser.setUserName(users.getUserName());
		nextApprovalUser.setUserMoible(users.getUserMobile());
		return nextApprovalUser;
	}

	public List<SubApprovalModel> getNextAuditUserList(String businessId, String departmentId, RoleType roleType) {
		if (StringUtils.isBlank(businessId)) {
			throw new RuntimeException("业务Id不存在! id=:" + businessId);
		}
		if (StringUtils.isBlank(departmentId)) {
			throw new RuntimeException("部门不存在! id=:" + departmentId);
		}
		Departments departments = departmentDao.selectByPrimaryKey(departmentId);
		if (null == departments || StringUtils.isBlank(departments.getDepPersonId())) {
			throw new RuntimeException("部门不存在! id=:" + departmentId);
		}
		Short roleTypeV = roleType.getRoleType();
		List<Users> usersList = commonQueryDao.selectUsersByRoleType(new Integer(roleTypeV));
//		usersList = newRolesService.queryUsersByRoleName(roleType.getDesc());
		if (usersList.size() == 0) {
			throw new RuntimeException("无数据VP角色人员! id=:" + departmentId);
		}
		// if (users.size() > 1) {
		// throw new RuntimeException("法务角色人员超过两个! id=:" + departmentId);
		// }
		List<SubApprovalModel> auditUserList = new ArrayList<>();
		for (Users us : usersList) {
			SubApprovalModel auditUser = new SubApprovalModel();
			auditUser.setBusinessId(businessId);
			auditUser.setDepartmentId(departmentId);
			auditUser.setUserId(us.getUserId());
			Users users = userDao.selectByPrimaryKey(us.getUserId());
			auditUser.setUserEmail(users.getUserEmail());
			auditUser.setUserName(users.getUserName());
			auditUser.setUserMoible(users.getUserMobile());
			auditUserList.add(auditUser);
		}

		return auditUserList;
	}

	public SubApprovalModel additionalUsers(SubApprovalModel subApprovalModel) {

		Users users = userDao.selectByPrimaryKey(subApprovalModel.getUserId());
		if (null!=users){
			subApprovalModel.setUserEmail(users.getUserEmail());
			subApprovalModel.setUserName(users.getUserName());
			subApprovalModel.setUserMoible(users.getUserMobile());
		}

		return subApprovalModel;
	}

}
