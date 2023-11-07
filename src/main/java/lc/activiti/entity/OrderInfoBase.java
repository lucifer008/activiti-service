package lc.activiti.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * SHOP_ORDER_INFO
 */
@Data
public class OrderInfoBase {
    /**
     * 主键ID订单ID
     */
    private String id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 订单数量
     */
    private String quantity;

    /**
     * 过期时间
     */
    private Date expTime;

    /**
     * 付款方式（1-运支保；2-微信；3-支付宝）
     */
    private Short payType;

    /**
     * 订单状态(1-待支付；2-已取消；3-已支付；4-已退款)
     */
    private Short status;

    /**
     * 公司ID
     */
    private String corpId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 订单描述
     */
    private String description;

    /**
     * 备注
     */
    private String memo;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 微信、支付宝的流水号
     */
    private String thirdPlateFlowNo;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 赠品状态：默认1待添加，2待提交，3待审核，4审核通过，5审核驳回，6审批通过，7审批驳回  8已赠送,0(345三种状态)
     */
    private Short giftStatus;

    /**
     * 销售ID
     */
    private String lkuserId;

    /**
     * 商品附属ID
     */
    private String productRelationId;

    /**
     * 订单是否删除 （ 0——未删除  1——删除）
     */
    private Short isDelete;
}