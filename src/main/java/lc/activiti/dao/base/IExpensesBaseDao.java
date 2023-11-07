package lc.activiti.dao.base;

import lc.activiti.entity.ExpensesBase;

public interface IExpensesBaseDao {
    /**
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String expensesId);

    /**
     *
     * @mbg.generated
     */
    int insert(ExpensesBase record);

    /**
     *
     * @mbg.generated
     */
    int insertSelective(ExpensesBase record);

    /**
     *
     * @mbg.generated
     */
    ExpensesBase selectByPrimaryKey(String expensesId);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ExpensesBase record);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ExpensesBase record);
}