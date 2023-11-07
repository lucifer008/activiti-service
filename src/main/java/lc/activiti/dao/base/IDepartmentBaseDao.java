package lc.activiti.dao.base;

import lc.activiti.entity.Departments;

public interface IDepartmentBaseDao {
    int deleteByPrimaryKey(String id);

    int insert(Departments record);

    int insertSelective(Departments record);

    Departments selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Departments record);

    int updateByPrimaryKey(Departments record);
}