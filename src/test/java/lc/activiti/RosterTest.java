package lc.activiti;


import lc.activiti.model.ApprovalUserModel;
import lc.activiti.lcenum.GroupType;
import lc.activiti.service.RosterService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RosterTest {
    @Autowired
    private RosterService rosterService;
    @Autowired
    private ProcessEngine processEngine;

    @Test
    public void testRosterProcess() {
        rosterService.deployProcess();
        log.info("花名册审批流程成功!");
    }

    final String processDefKey = "rosterApprovalProcess";

    @Test
    public void submitTask() {
        String businessKey = UUID.randomUUID().toString();
        Map<String, Object> params = new HashMap<>();
        params.put("status", 1);
        ApprovalUserModel users=new ApprovalUserModel();
        users.setUserId("8707b45bf93e4b72b75004dc93652bf9");
        params.put("Users",users);
        processEngine.getRuntimeService().startProcessInstanceByKey(processDefKey, businessKey,params);
        log.info("提交任务!业务Id===>{}", businessKey);
    }

    @Test
    public void queryTask() {
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        for (Task task : tasks) {
            log.info("Task available: " + task.getName());
        }
    }

    @Test
    public void deleteGroup() {
        IdentityService identityService = processEngine.getIdentityService();
        identityService.deleteGroup(GroupType.Personnel.getTypeKey());
        identityService.deleteGroup(GroupType.PersonnelCEO.getTypeKey());
        identityService.deleteGroup(GroupType.FinanceGroup.getTypeKey());
        identityService.deleteGroup(GroupType.FinanceGroupCEO.getTypeKey());
        identityService.deleteGroup(GroupType.DataGroup.getTypeKey());
        identityService.deleteGroup(GroupType.DataGroupCEO.getTypeKey());

        identityService.deleteGroup(GroupType.BusinessGroupCEO.getTypeKey());
        identityService.deleteGroup(GroupType.BusinessGroup.getTypeKey());

        List<User> userList=identityService.createUserQuery().list();
        for (User user: userList
             ) {
            identityService.deleteUser(user.getId());
        }
        log.info("删除成功!");
    }

    @Test
    public void addPersonnelMatter() {
        //创建组
        IdentityService identityService = processEngine.getIdentityService();
        Group personnelGroup = identityService.newGroup(GroupType.Personnel.getTypeKey());
        personnelGroup.setName(GroupType.Personnel.getDesc());
        personnelGroup.setType(GroupType.Personnel.getTypeValue()+"");
        identityService.saveGroup(personnelGroup);
        //创建用户
        String userId = "1c238c800f034c96a5ac6fcc85063f26";
        User users = identityService.newUser(userId);
        identityService.saveUser(users);
        //将用户添加到人事组
        identityService.createMembership(userId, personnelGroup.getId());
        log.info("人事组与用户创建成功!");

    }
    @Test
    public void addPersonnelMatterCEO() {
        //创建组
        IdentityService identityService = processEngine.getIdentityService();
        Group personnelGroup = identityService.newGroup(GroupType.PersonnelCEO.getTypeKey());
        personnelGroup.setName(GroupType.PersonnelCEO.getDesc());
        personnelGroup.setType(GroupType.PersonnelCEO.getTypeValue()+"");
        identityService.saveGroup(personnelGroup);
        //创建用户
        String userId = "9d8c1f1b4f9544ce883a8e91b56c95c4";
        User users = identityService.newUser(userId);
        identityService.saveUser(users);
        //将用户添加到人事组
        identityService.createMembership(userId, personnelGroup.getId());
        log.info("人事总裁组与用户创建成功!");

    }
    @Test
    public void addFinanceGroup() {
        //创建组
        IdentityService identityService = processEngine.getIdentityService();
        Group personnelGroup = identityService.newGroup(GroupType.FinanceGroup.getTypeKey());
        personnelGroup.setName(GroupType.FinanceGroup.getDesc());
        personnelGroup.setType(GroupType.FinanceGroup.getTypeValue()+"");
        identityService.saveGroup(personnelGroup);
        //创建用户
        String userId = "8707b45bf93e4b72b75004dc93652bf9";
        User users = identityService.newUser(userId);
        identityService.saveUser(users);
        //将用户添加到财务组
        identityService.createMembership(userId, personnelGroup.getId());
        log.info("财务组与用户创建成功!");

    }
    @Test
    public void addFinanceGroupCEO() {
        //创建组
        IdentityService identityService = processEngine.getIdentityService();
        Group personnelGroup = identityService.newGroup(GroupType.FinanceGroupCEO.getTypeKey());
        personnelGroup.setName(GroupType.FinanceGroupCEO.getDesc());
        personnelGroup.setType(GroupType.FinanceGroupCEO.getTypeValue()+"");
        identityService.saveGroup(personnelGroup);
        //创建用户
        String userId = "fc5d0e77a98f411196da0c5cc837ed92";
        User users = identityService.newUser(userId);
        identityService.saveUser(users);
        //将用户添加到财务总裁组
        identityService.createMembership(userId, personnelGroup.getId());
        log.info("财务总裁组与用户创建成功!");

    }
    @Test
    public void addDataGroup() {
        //创建组
        IdentityService identityService = processEngine.getIdentityService();
        Group personnelGroup = identityService.newGroup(GroupType.DataGroup.getTypeKey());
        personnelGroup.setName(GroupType.DataGroup.getDesc());
        personnelGroup.setType(GroupType.DataGroup.getTypeValue()+"");
        identityService.saveGroup(personnelGroup);
        //创建用户
        String userId = "822f1f86cc364f819391f3b741a3198c";
        User users = identityService.newUser(userId);
        identityService.saveUser(users);
        //将用户添加到财务组
        identityService.createMembership(userId, personnelGroup.getId());
        log.info("数据组与用户创建成功!");

    }
    @Test
    public void addDataGroupCEO() {
        //创建组
        IdentityService identityService = processEngine.getIdentityService();
        Group personnelGroup = identityService.newGroup(GroupType.DataGroupCEO.getTypeKey());
        personnelGroup.setName(GroupType.DataGroupCEO.getDesc());
        personnelGroup.setType(GroupType.DataGroupCEO.getTypeValue()+"");
        identityService.saveGroup(personnelGroup);
        //创建用户
        String userId = "a3598f8f487f4c4db6309210c74fb24d";
        User users = identityService.newUser(userId);
        identityService.saveUser(users);
        //将用户添加到数据总裁组
        identityService.createMembership(userId, personnelGroup.getId());
        log.info("数据总裁组与用户创建成功!");

    }
    @Test
    public void addBusinessGroup() {
        //创建组
        IdentityService identityService = processEngine.getIdentityService();
        Group personnelGroup = identityService.newGroup(GroupType.BusinessGroup.getTypeKey());
        personnelGroup.setName(GroupType.BusinessGroup.getDesc());
        personnelGroup.setType(GroupType.BusinessGroup.getTypeValue()+"");
        identityService.saveGroup(personnelGroup);
        //创建用户
        String userId = "8faca5d0482f4ec79875546122644902";
        User users = identityService.newUser(userId);
        identityService.saveUser(users);
        //将用户添加到财务组
        identityService.createMembership(userId, personnelGroup.getId());
        log.info("业务组与用户创建成功!");

    }
    @Test
    public void addBusinessGroupCEO() {
        //创建组
        IdentityService identityService = processEngine.getIdentityService();
        Group personnelGroup = identityService.newGroup(GroupType.BusinessGroupCEO.getTypeKey());
        personnelGroup.setName(GroupType.BusinessGroupCEO.getDesc());
        personnelGroup.setType(GroupType.BusinessGroupCEO.getTypeValue()+"");
        identityService.saveGroup(personnelGroup);
        //创建用户
        String userId = "d177b1f986d14e03ae2c874a74fade17";
        User users = identityService.newUser(userId);
        identityService.saveUser(users);
        //将用户添加到业务总裁组
        identityService.createMembership(userId, personnelGroup.getId());
        log.info("业务总裁组与用户创建成功!");
    }
    @Test
    public void generatorAllGroup(){
        addBusinessGroup();
        addBusinessGroupCEO();
        addDataGroup();
        addDataGroupCEO();
        addFinanceGroup();
        addFinanceGroupCEO();
        addPersonnelMatter();
        addPersonnelMatterCEO();
    }
}
