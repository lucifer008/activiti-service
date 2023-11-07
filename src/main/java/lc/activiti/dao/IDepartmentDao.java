package lc.activiti.dao;

import org.apache.ibatis.annotations.Param;


public interface IDepartmentDao {
	
    boolean isDepartmentLeader(@Param("userId")String userId);
    /**
     * 用户是否存在部门
     * @param userId
     * @return
     */
    boolean userIsExistDepartment(@Param("userId")String userId);
}