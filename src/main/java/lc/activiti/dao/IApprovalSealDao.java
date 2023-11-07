package lc.activiti.dao;

import java.util.List;

import lc.activiti.entity.Users;
import org.apache.ibatis.annotations.Param;

public interface IApprovalSealDao {
	List<Users> getUserRoles(@Param("roleTypes") String roleTypes);
}
