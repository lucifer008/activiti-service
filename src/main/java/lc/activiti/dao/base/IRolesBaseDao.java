package lc.activiti.dao.base;

import lc.activiti.entity.Roles;

public interface IRolesBaseDao {
    int deleteByPrimaryKey(String id);

    int insert(Roles record);

    int insertSelective(Roles record);

    Roles selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Roles record);

    int updateByPrimaryKey(Roles record);
}