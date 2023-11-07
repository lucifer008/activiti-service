package lc.activiti.dao.base;

import lc.activiti.entity.OrderInfoBase;

public interface IOrderInfoBaseDao {
    /**
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String id);

    /**
     *
     * @mbg.generated
     */
    int insert(OrderInfoBase record);

    /**
     *
     * @mbg.generated
     */
    int insertSelective(OrderInfoBase record);

    /**
     *
     * @mbg.generated
     */
    OrderInfoBase selectByPrimaryKey(String id);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(OrderInfoBase record);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(OrderInfoBase record);
}