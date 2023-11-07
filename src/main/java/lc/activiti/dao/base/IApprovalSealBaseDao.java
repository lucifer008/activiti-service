package lc.activiti.dao.base;

import lc.activiti.entity.ApprovalSealBase;

public interface IApprovalSealBaseDao {
    /**
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String approvalSealId);

    /**
     *
     * @mbg.generated
     */
    int insert(ApprovalSealBase record);

    /**
     *
     * @mbg.generated
     */
    int insertSelective(ApprovalSealBase record);

    /**
     *
     * @mbg.generated
     */
    ApprovalSealBase selectByPrimaryKey(String approvalSealId);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ApprovalSealBase record);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ApprovalSealBase record);
}