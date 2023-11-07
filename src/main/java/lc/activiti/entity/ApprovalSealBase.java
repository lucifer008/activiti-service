package lc.activiti.entity;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * T_APPROVAL_SEAL
 */
@Data
public class ApprovalSealBase {
    /**
     * 审批合同Id
     */
    private String approvalSealId;

    /**
     * 印章状态(1:新建;2.已申请;3.已审批;-1:已驳回)
     */
    private Short sealStatus;

    /**
     * 审批类型(1:干线拉直上游合同审批;2: 干线拉直下游合同审批;3: 精准拼车上游合同审批;4: 精准拼车下游合同审批;5: KA上游合同审批;6: KA下游合同审批)
     */
    private Short approvalType;

    /**
     * 申请人
     */
    private String approvalUserId;

    /**
     * 申请时间
     */
    private Date approvalDate;

    /**
     * 合同签立内容
     */
    private String signContent;

    /**
     * 合同签立单位
     */
    private String singUnit;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同金额
     */
    private BigDecimal amount;

    /**
     * 公章类型(1:合同章;2:业务章;3:财务章;4:人名章;5:公章)
     */
    private Short stampType;

    /**
     * 公章主体(1:乐卡车联;2:常州拼满车;3:浙江拼满车;4:运支宝)
     */
    private Short stampSubject;

    /**
     * 更新时间
     */
    private Date update;

    /**
     * 更新用户Id
     */
    private String updateUserId;

    /**
     * 盖章申请单号
     */
    private String sealApplyNo;

    /**
     * 申请人部门
     */
    private String approvalDepId;

    /**
     * 驳回用户Id
     */
    private String rejectUserId;

    /**
     * 驳回日期
     */
    private Date rejectDate;

    /**
     * 驳回备注
     */
    private String rejectMemo;

    /**
     * 申请备注
     */
    private String applyMemo;
}