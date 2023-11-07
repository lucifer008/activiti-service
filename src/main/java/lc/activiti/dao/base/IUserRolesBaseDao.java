package lc.activiti.dao.base;

import lc.activiti.entity.UserRoles;

public interface IUserRolesBaseDao {
    int deleteByPrimaryKey(String id);

    int insert(UserRoles record);

    int insertSelective(UserRoles record);

    UserRoles selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserRoles record);

    int updateByPrimaryKey(UserRoles record);
}