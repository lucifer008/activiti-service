package lc.activiti.dao.base;

import lc.activiti.entity.Contract;

public interface IContractBaseDao {
    int deleteByPrimaryKey(String contractId);

    int insert(Contract record);

    int insertSelective(Contract record);

    Contract selectByPrimaryKey(String contractId);

    int updateByPrimaryKeySelective(Contract record);

    int updateByPrimaryKey(Contract record);
}