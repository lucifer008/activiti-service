package lc.activiti.entity;

import java.util.Date;
import lombok.Data;

/**
 * LCYW.OA_RECRUIT
 */
@Data
public class RecruitBase {
    /**
     * 招聘Id
     */
    private String recruitId;

    /**
     * 一级(没有留空处理)
     */
    private String depId1;

    /**
     * 二级(没有留空处理)
     */
    private String depId2;

    /**
     * 三级(没有留空处理)
     */
    private String depId3;

    /**
     * 四级(没有留空处理)
     */
    private String depId4;

    /**
     * 五级(没有留空处理)
     */
    private String depId5;

    /**
     * 六级(没有留空处理)
     */
    private String depId6;

    /**
     * 七级(没有留空处理)
     */
    private String depId7;

    /**
     * 岗位Id
     */
    private String positionId;

    /**
     * 职级
     */
    private String rank;

    /**
     * 招聘审批状态(0:新建;1:已申请;2:已审批;-1:已驳回;-2:已撤回)
     */
    private Short approvalStatus;

    /**
     * 招聘面试状态(预留字段)
     */
    private Short interviewStatus;

    /**
     * 工作年限
     */
    private String workYear;

    /**
     * 招聘人数
     */
    private Short nums;

    /**
     * 有无笔试
     */
    private Short interview;

    /**
     * 初试负责人Id
     */
    private String preliminaryTestUserId;

    /**
     * 复试负责人Id
     */
    private String reexamineUserId;

    /**
     * 岗位职责
     */
    private String positionResponsibility;

    /**
     * 任职要求
     */
    private String jobRequirements;

    /**
     * 期望完成时间
     */
    private Date expectSucess;

    /**
     * 薪资
     */
    private String salary;

    /**
     * 驳回人Id
     */
    private String rejectUserId;

    /**
     * 驳回时间
     */
    private Date reject;

    /**
     * 驳回备注
     */
    private String rejectMemo;

    /**
     * 创建时间
     */
    private Date input;

    /**
     * 创建用户Id
     */
    private String inputUserId;

    /**
     * 修改时间
     */
    private Date update;

    /**
     * 修改用户Id
     */
    private String updateUserId;

    /**
     * 是否删除(0:否;1:是)
     */
    private Short deleted;
}