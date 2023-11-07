package lc.activiti.dao.base;

import lc.activiti.entity.Users;

public interface IUserBaseDao {
    int deleteByPrimaryKey(String userId);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);
}