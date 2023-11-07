package lc.activiti.service.impl;

import org.activiti.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lc.activiti.service.CommonProcessService;
import lombok.extern.slf4j.Slf4j;


/**   
*    
* 项目名称：activitiapproval   
* 类名称：CommonProcessServiceImpl   
* 类描述： 流程部署相关服务
* 创建人：lucifer   
* 创建时间：2019年2月26日 上午10:51:07   
* @version        
*/
@Slf4j
@Service
public class CommonProcessServiceImpl implements CommonProcessService {

	@Autowired
	private ProcessEngine processEngine;
	final static String commonProcessName = "通用审批流程";
	final static String commonProcessBPMN = "commonApprovalProcess.bpmn";

	@Override
	public boolean deployCommonProcess() {
		processEngine.getRepositoryService().createDeployment().name(commonProcessName)
				.addClasspathResource(commonProcessBPMN).deploy();
		log.info("[{0}]部署成功!",commonProcessName);
		return true;
	}

}
