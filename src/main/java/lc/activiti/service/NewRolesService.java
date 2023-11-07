package lc.activiti.service;

import lc.activiti.entity.NewRolesModel;
import lc.activiti.entity.Users;

import java.util.List;

public interface NewRolesService {
    /**
    * @Description: 新获取用户角色
    * @author: cuiqingchao
    * @date: 2020/9/28 17:13
    * @param:
    * @return:
    */
    List<NewRolesModel> getNewRolesList(String userId);


    /**
     * @Description:
     * @author: cuiqingchao
     * @date: 2020/9/29 11:27
     * @param:
     * @return:
     */
    List<Users> queryUsersByRoleName(String roleName);
}
