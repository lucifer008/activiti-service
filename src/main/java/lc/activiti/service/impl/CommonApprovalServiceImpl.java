package lc.activiti.service.impl;

import java.util.Date;

import lc.activiti.entity.ApprovalProgressBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lc.activiti.lcenum.BusinessTypeEnu;
import lc.activiti.lcenum.GuidUtils;
import lc.activiti.dao.IApprovalProgressDao;
import lc.activiti.dao.base.IApprovalProgressBaseDao;
import lc.activiti.service.CommonApprovalService;
import lc.activiti.service.ZTOrderService;
import lc.activiti.dao.base.IDepartmentBaseDao;
import lc.activiti.dao.base.IUserBaseDao;
import lc.activiti.entity.Departments;
import lc.activiti.entity.Users;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

/**
 * 类描述：提交，审核，审批相关服务
 * 创建人：lucifer
 * 创建时间：2019年2月26日 上午10:48:12
 */
@Slf4j
@Service
public class CommonApprovalServiceImpl implements CommonApprovalService {

    @Autowired
    private ZTOrderService ztOrderService;

    /**
     * 提交订单
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    @Override
    public boolean submitApply(SubApprovalModel appModel) {
        validata(appModel);
        if (appModel.getBusinessType().equals(BusinessTypeEnu.ZT_Order_Service.getDesc())) {
            return ztOrderService.submitApply(appModel);
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    @Override
    public boolean auditSuccess(SubApprovalModel appModel) {
        validata(appModel);
        if (appModel.getBusinessType().equals(BusinessTypeEnu.ZT_Order_Service.getDesc())) {
            return ztOrderService.audtiSuccess(appModel);
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    @Override
    public boolean auditReject(SubApprovalModel appModel) {
        validata(appModel);
        if (appModel.getBusinessType().equals(BusinessTypeEnu.ZT_Order_Service.getDesc())) {
            return ztOrderService.audtiReject(appModel);
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    @Override
    public boolean approvalSuccess(SubApprovalModel appModel) {
        validata(appModel);
        if (appModel.getBusinessType().equals(BusinessTypeEnu.ZT_Order_Service.getDesc())) {
            return ztOrderService.approvalSuccess(appModel);
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    @Override
    public boolean approvalReject(SubApprovalModel appModel) {
        validata(appModel);
        if (appModel.getBusinessType().equals(BusinessTypeEnu.ZT_Order_Service.getDesc())) {
            return ztOrderService.approvalReject(appModel);
        }
        return false;
    }

    private void validata(SubApprovalModel appModel) {
        if (StringUtils.isBlank(appModel.getBusinessType())) {
            throw new RuntimeException("业务类型为空！");
        }
    }

    @Autowired
    private IDepartmentBaseDao departmentDao;
    @Autowired
    private IUserBaseDao userDao;
    @Autowired
    private IApprovalProgressBaseDao approvalProgressBaseDao;

    @Override
    public void generatorApprovalProcessNodes(String businessId, String applyUserId, String departmentId,
                                              boolean isDepartmentHeader) {
        if (StringUtils.isBlank(departmentId)) {
            throw new RuntimeException("部门不能为空!");
        }
        if (StringUtils.isBlank(applyUserId)) {
            throw new RuntimeException("申请用户不能为空!");
        }
        if (StringUtils.isBlank(businessId)) {
            throw new RuntimeException("业务Id不能为空!");
        }
        Short index = 1;
        Departments departments = departmentDao.selectByPrimaryKey(departmentId);
        //非部门负责人
        if (!isDepartmentHeader) {

            Users depHeader = userDao.selectByPrimaryKey(departments.getDepPersonId());

            ApprovalProgressBase firstApprovalNode = new ApprovalProgressBase();
            firstApprovalNode.setOrderId(businessId);
            firstApprovalNode.setApprovalId(GuidUtils.newGuid());
            firstApprovalNode.setInputDate(new Date());
            firstApprovalNode.setApprovalStatus((short) 0);
            firstApprovalNode.setApprovalUserId(depHeader.getUserId());
            firstApprovalNode.setApprovalUserName(depHeader.getUserName());
            firstApprovalNode.setSortNo(index++);
            approvalProgressBaseDao.insert(firstApprovalNode);
        }
        String parentDepartmentId = departments.getOaId();
        Departments parentDepartment = departmentDao.selectByPrimaryKey(parentDepartmentId);
        Users parentDeparmentHeader = userDao.selectByPrimaryKey(parentDepartment.getDepPersonId());
        if (parentDeparmentHeader == null) {
            throw new RuntimeException("部门Id【"+parentDepartmentId+"】的负责人为空!");
        }
        ApprovalProgressBase endApprovalNode = new ApprovalProgressBase();
        endApprovalNode.setOrderId(businessId);
        endApprovalNode.setApprovalId(GuidUtils.newGuid());
        endApprovalNode.setInputDate(new Date());
        endApprovalNode.setApprovalStatus((short) 0);
        endApprovalNode.setApprovalUserId(parentDeparmentHeader.getUserId());
        endApprovalNode.setApprovalUserName(parentDeparmentHeader.getUserName());
        endApprovalNode.setSortNo(index);
        approvalProgressBaseDao.insert(endApprovalNode);
        log.info("审批节点生成成功");
    }

    @Autowired
    private IApprovalProgressDao approvalProgressDao;

    @Override
    public void updateApprovalProcessNodesByReject(String businessId) {
        approvalProgressDao.updateApprovalProcessNodesByReject(businessId);
    }

    @Override
    public void updateApprovalProcessNodesByPass(String businessId, String applyUserId) {
        approvalProgressDao.updateApprovalProcessNodesByPass(businessId, applyUserId);
    }

}
