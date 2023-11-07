package lc.activiti.dao.base;

import lc.activiti.entity.WechartNoticeUsers;
import lc.activiti.entity.WechartNoticeUsersKey;

public interface IWechartNoticeUsersBaseDao {
    /**
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(WechartNoticeUsersKey key);

    /**
     *
     * @mbg.generated
     */
    int insert(WechartNoticeUsers record);

    /**
     *
     * @mbg.generated
     */
    int insertSelective(WechartNoticeUsers record);

    /**
     *
     * @mbg.generated
     */
    WechartNoticeUsers selectByPrimaryKey(WechartNoticeUsersKey key);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(WechartNoticeUsers record);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(WechartNoticeUsers record);
}