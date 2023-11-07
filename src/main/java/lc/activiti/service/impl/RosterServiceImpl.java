package lc.activiti.service.impl;

import lc.activiti.service.RosterService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RosterServiceImpl implements RosterService {

    String name="花名册审批流程1.0";
    @Autowired
    private ProcessEngine processEngine;
    @Override
    public void deployProcess() {

        String clssResourse="rosterApprovalProcess.bpmn";
        processEngine.getRepositoryService().createDeployment().addClasspathResource(clssResourse).name(name).deploy();
        log.info("test process Deployed!");
    }

    @Override
    public void deleteProcess() {
//        List<Deployment> deploymentList=processEngine.getRepositoryService().createDeploymentQuery().list();
//        for (Deployment de:deploymentList
//                ) {
//            processEngine.getRepositoryService().deleteDeployment(de.getId(),true);
//        }

    }
}
