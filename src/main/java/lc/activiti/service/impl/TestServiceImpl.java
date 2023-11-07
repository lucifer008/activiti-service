package lc.activiti.service.impl;

import org.activiti.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lc.activiti.dao.ICommonQueryDao;
import lc.activiti.dao.base.IRolesBaseDao;
import lc.activiti.entity.Roles;
import lc.activiti.service.TestService;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private IRolesBaseDao rolesDao;
    
    @Autowired
    private ICommonQueryDao commonQueryDao;
    
    @Override
    public void testMethod() {
        System.out.println(processEngine);
        System.out.println("testMethod");
      Roles roles= rolesDao.selectByPrimaryKey("6f18242194f520cfe0502c0af7151a71");
      System.out.println(roles.getRoleName());
      System.out.println(commonQueryDao.selectUsersByRoleType(12));
       System.out.println(roles);
    }
}
