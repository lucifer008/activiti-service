package lc.activiti.entity;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * OA_EXPENSES
 */
@Data
public class ExpensesBase {
    /**
     * 费用报销单ID
     */
    private String expensesId;

    /**
     * 报销单类型(0:差旅费用报销;1:招待费用报销;2:其他)
     */
    private Short expensesType;

    /**
     * 报销用户ID
     */
    private String expensesUserId;

    /**
     * 报销用户名
     */
    private Object expensesUser;

    /**
     * 报销人部门ID
     */
    private String departmentId;

    /**
     * 报销人职位
     */
    private String positionId;

    /**
     * 费用事由
     */
    private Object feeReson;

    /**
     * 填报日期
     */
    private Date input;

    /**
     * 费用所属项目
     */
    private Object project;

    /**
     * 已借款金额
     */
    private BigDecimal borrwedAmount;

    /**
     * 应付款
     */
    private BigDecimal payments;

    /**
     * 驳回理由
     */
    private Object rejectReson;

    /**
     * 驳回日期
     */
    private Date rejectDate;

    /**
     * 驳回人ID
     */
    private String rejectUserId;

    /**
     * 驳回人
     */
    private Object rejectUserName;

    /**
     * 申请人联系方式
     */
    private Object linkTel;

    /**
     * 更新日期
     */
    private Date updateDate;

    /**
     * 更新用户ID
     */
    private String updateUserId;

    /**
     * 审批状态(0:待申请;1:已申请;2.已审批;3.已审核;4.已支付:-1:已驳回,5:已撤回)
     */
    private Short status;

    /**
     * 报销单号
     */
    private Object expensesNo;

    /**
     * 费用合计
     */
    private BigDecimal totalAmount;

    /**
     * 当前审批级别
     */
    private Short currentLevel;

    /**
     * 部门名称
     */
    private Object departmentName;

    /**
     * 岗位名称
     */
    private Object positionName;

    /**
     * 费用所属项目ID
     */
    private String projectId;

    /**
     * 实付金额
     */
    private BigDecimal actuallyPaid;

    /**
     * 报销单状态(0:未报废1:已报废)
     */
    private Short reimbursementstate;
}