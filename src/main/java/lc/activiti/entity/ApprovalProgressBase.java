package lc.activiti.entity;

import lombok.Data;

import java.util.Date;

/**
 * SHOP_APPROVAL_PROGRESS
 */
@Data
public class ApprovalProgressBase {
    /**
     * 审批表id
     */
    private String approvalId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 审批人ID
     */
    private String approvalUserId;

    /**
     * 审批人名称
     */
    private String approvalUserName;

    /**
     * 审批日期
     */
    private Date approvalDate;

    /**
     * 0未审批，1已审批
     */
    private Short approvalStatus;

    /**
     * 备注信息
     */
    private String memo;

    /**
     * 创建时间
     */
    private Date inputDate;

    /**
     * 排序字段，方便批量插入的数据进行排序
     */
    private Short sortNo;
}