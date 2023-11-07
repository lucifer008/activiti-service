package lc.activiti.dao.base;

import lc.activiti.entity.DepartmentUserRoles;

public interface IDepartmentUserBaseDao {
    int deleteByPrimaryKey(String cDepartmentUsersId);

    int insert(DepartmentUserRoles record);

    int insertSelective(DepartmentUserRoles record);

    DepartmentUserRoles selectByPrimaryKey(String cDepartmentUsersId);

    int updateByPrimaryKeySelective(DepartmentUserRoles record);

    int updateByPrimaryKey(DepartmentUserRoles record);
}