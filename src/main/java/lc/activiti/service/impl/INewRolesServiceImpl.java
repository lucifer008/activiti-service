package lc.activiti.service.impl;

import com.alibaba.fastjson.JSONObject;
import lc.activiti.entity.NewOaObject;
import lc.activiti.entity.NewOaUser;
import lc.activiti.entity.NewRolesModel;
import lc.activiti.entity.Users;
import lc.activiti.service.NewRolesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class INewRolesServiceImpl implements NewRolesService{

    @Autowired
    private RestTemplate restTemplate;
    @Value("${lc.newOaService}")
    private String newOaService;

    @Override
    public List<NewRolesModel> getNewRolesList(String userId) {
        List<NewRolesModel> result = new ArrayList<>();
        ResponseEntity<JSONObject> responseEntity=null;
        responseEntity = restTemplate.getForEntity(newOaService + "/api/v1/Role/getUserRole?userid="+userId, JSONObject.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            JSONObject jsonObject =responseEntity.getBody();
            NewOaObject obj = (NewOaObject) JSONObject.toJavaObject(jsonObject,NewOaObject.class);
            if (null !=obj.getData()) result = obj.getData();
            return result;
        }
        return result;
    }


    public List<Users> queryUsersByRoleName(String roleName){
        List<Users> usersList = new ArrayList<>();
        ResponseEntity<JSONObject> responseEntity=null;
        responseEntity = restTemplate.getForEntity(newOaService + "/api/v1/permissionService/queryUsersByRoleName?roleName="+roleName, JSONObject.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            JSONObject jsonObject =responseEntity.getBody();
            NewOaUser obj = (NewOaUser) JSONObject.toJavaObject(jsonObject,NewOaUser.class);
            if (null !=obj.getData()) usersList = obj.getData();
            return usersList;
        }


        return usersList;
    }
}
