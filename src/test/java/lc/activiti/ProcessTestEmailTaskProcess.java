package lc.activiti;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lc.activiti.lcenum.GuidUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessTestEmailTaskProcess {

	private String filename = "/lc/activiti/diagrams/emailTaskProcess.bpmn";
	@Value("${lc.mail.fromMail.addr}")
	private String from;
//	@Autowired
//	@Rule
	//private ActivitiRule activitiRule ;
	@Autowired
	private ProcessEngine processEngine;
	@Test
	public void startProcess() throws Exception {
		String businessKey=GuidUtils.newGuid();
		
//		String path= this.getClass().getClassLoader().getResource("emailTaskProcess.bpmn").getPath();
//		
//		RepositoryService repositoryService = processEngine.getRepositoryService();//activitiRule.getRepositoryService();
//		repositoryService.createDeployment().name("邮件测试").addClasspathResource("emailTaskProcess.bpmn").deploy();
//		RuntimeService runtimeService = processEngine.getRuntimeService();//activitiRule.getRuntimeService();
//		Map<String, Object> variableMap = new HashMap<String, Object>();
//		variableMap.put("name", "Activiti");
		Map<String,Object> vaiables=new HashMap<>();
		Object name="合同邮件通知";
		vaiables.put("subject", name);
		Object to="zhangxiaolin@lecarlink.com";
		vaiables.put("to", to);
		//Object from="";
		vaiables.put("from", from);
//		Object cc="zhangxiaolin@lecarlink.com";
//		vaiables.put("cc", cc);
//		Object bcc="zhangxiaolin@lecarlink.com";
//		vaiables.put("bcc", bcc);
		Object htmlContext="<html><body>hello</body></html>";
		vaiables.put("htmlContext", htmlContext);
		ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("emailTaskProcess",businessKey, vaiables);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
}