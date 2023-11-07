package lc.activiti.entity;

import java.util.Date;
import lombok.Data;

/**
 * LCYW.HT_CONTRACT_INFO
 */
@Data
public class Contract {
    /**
     * 合同ID
     */
    private String contractId;

    /**
     * 物流公司Id
     */
    private String corpId;

    /**
     * 合同模板编号
     */
    private String templetCode;

    /**
     * 合同名称(协议名称)
     */
    private String contractName;

    /**
     * PDF地址
     */
    private String pdfUrl;

    /**
     * HTML地址
     */
    private String htmlUrl;

    /**
     * 备注
     */
    private String remark;

    /**
     * 录入人ID
     */
    private String inputUserId;

    /**
     * 录入人
     */
    private String inputUserName;

    /**
     * 录入时间
     */
    private Date input;

    /**
     * 是否停用
     */
    private Short delflag;

    /**
     * 合同编号(审盖编号/协议编号)
     */
    private String contractCode;

    /**
     * 合同状态(1:未提交;2:已提交/审批中;3:已审批;4:已审核(已废弃);5:已邮寄(已废弃);6.已确认/待归档(已废弃);7:已归档;-1:已驳回;8:邮寄已审核(已废弃);-2:邮寄被驳回【状态记录在履历中】(已废弃) -3:申请被撤回;
     */
    private Short status;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 是否更新(1:未更新;2:更新待提交;3.更新未通过驳回;4.已更新)
     */
    private Short isUpdate;

    /**
     * 快递公司
     */
    private String expressCompany;

    /**
     * 快递单号
     */
    private String expressNo;

    /**
     * 确认日期
     */
    private Date confirgDate;

    /**
     * 更新驳回原因
     */
    private String updateRejectReason;

    /**
     * 更新驳回日期
     */
    private Date updateRejectDate;

    /**
     * 甲方托运方(我司主体)
     */
    private String shipperName;

    /**
     * 乙方承运商(合作方主体)
     */
    private String carriersName;

    /**
     * 甲方授权代表
     */
    private String asignName;

    /**
     * 乙方授权代表
     */
    private String bsignName;

    /**
     * 签订日期
     */
    private Date signing;

    /**
     * 合同有效期开始
     */
    private Date validityStart;

    /**
     * 合同有效期结束
     */
    private Date validityEnd;

    /**
     * 框架合同编号
     */
    private String contractNo;

    /**
     * 合同类型,1:干线拉直, 2:精准拼车 3:仓储外发 4:其它5:自定义;6:押金协议;7:战略协议;8:上游协议
     */
    private String contractType;

    /**
     * 归档号码
     */
    private String fileNumber;

    /**
     * 业务类型(0:新增;1:续签)
     */
    private Short busType;

    /**
     * 提交时间
     */
    private Date submitDate;

    /**
     * 合同试用开始日期
     */
    private Date probationBegin;

    /**
     * 合同试用结束日期
     */
    private Date probationEnd;

    /**
     * 会签是否通知(0:否;1:是)
     */
    private Short isCountsignNotice;

    /**
     * 审批是否通知(0:否;1:是)
     */
    private Short isApprovalNotice;

    /**
     * 驳回原因
     */
    private String rejectReason;

    /**
     * 驳回日期
     */
    private Date rejectDate;

    /**
     * 关联合同ID
     */
    private String relationContractId;

    /**
     * 合同性质(1:框架合同;2:协议)
     */
    private Short type;

    /**
     * 所属合同id(续签合同HT_CONTRACT_INFO--->C_CONTRACT_ID)
     */
    private String adscriptionContractId;

    /**
     * 合同Word地址(上游)
     */
    private String wordUrl;

    /**
     * 合同上/下游(0:下游;1:上游)
     */
    private Short upOrDown;

    /**
     * 会签批次
     */
    private Short currentBatchNo;
}