package lc.activiti.dao.base;

import lc.activiti.entity.WXAppLoginInfo;

public interface IWXAppLoginInfoBaseDao {
    /**
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String appUuid);

    /**
     *
     * @mbg.generated
     */
    int insert(WXAppLoginInfo record);

    /**
     *
     * @mbg.generated
     */
    int insertSelective(WXAppLoginInfo record);

    /**
     *
     * @mbg.generated
     */
    WXAppLoginInfo selectByPrimaryKey(String appUuid);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(WXAppLoginInfo record);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(WXAppLoginInfo record);
}