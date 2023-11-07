package lc.activiti.dao.base;

import lc.activiti.entity.ContractCounterSign;

public interface IContractCounterSignBaseDao {
    /**
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String contractCountersignId);

    /**
     *
     * @mbg.generated
     */
    int insert(ContractCounterSign record);

    /**
     *
     * @mbg.generated
     */
    int insertSelective(ContractCounterSign record);

    /**
     *
     * @mbg.generated
     */
    ContractCounterSign selectByPrimaryKey(String contractCountersignId);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ContractCounterSign record);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ContractCounterSign record);
}