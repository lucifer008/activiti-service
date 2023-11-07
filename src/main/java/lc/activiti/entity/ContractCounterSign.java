package lc.activiti.entity;

import lombok.Data;

import java.util.Date;

/**
 * LCYW.HT_CONTRACT_COUNTERSIGN
 */
@Data
public class ContractCounterSign {
    /**
     * 会签Id
     */
    private String contractCountersignId;

    /**
     * 合同Id
     */
    private String contractId;

    /**
     * 会签用户部门Id
     */
    private String departmentId;

    /**
     * 会签用户Id
     */
    private String userId;

    /**
     * 会签类型(0:无异议;1:存在问题)
     */
    private Short signType;

    /**
     * 会签意见
     */
    private String signSuggestion;

    /**
     * 会签时间
     */
    private Date signDate;

    /**
     * 更新时间
     */
    private Date update;

    /**
     * 更新用户Id
     */
    private String updateUserId;

    /**
     * 删除标识(0:正常1;1:已删除)
     */
    private Short deleted;

    /**
     * 删除日期
     */
    private Date deleteDate;

    /**
     * 撤回(0:否;1:是(申请人撤回,会签记录作废))
     */
    private Short isWithdraw;

    /**
     * 会签批次
     */
    private Short batchNo;

    /**
     * 会签角色
     */
    private String roleName;
}