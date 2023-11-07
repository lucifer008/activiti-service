package lc.activiti;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestEmailTaskProcess2 {

	// private String filename =
	// "E:\\my\\eJavaSource\\activitiapproval\\src\\main\\java\\lc\\activiti\\contract\\diagrams\\emailTaskProcess.bpmn";
	// https://github.com/Activiti/Activiti/issues/1884
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	// @Deployment
	@Deployment(resources = { "emailTaskProcess.bpmn", "emailTaskProcess.png" })
	public void startProcess() throws Exception {
		// RepositoryService repositoryService = activitiRule.getRepositoryService();
		// repositoryService.createDeployment().addInputStream("emailTaskProcess.bpmn20.xml",
		// new FileInputStream(filename)).deploy();

		RuntimeService runtimeService = activitiRule.getRuntimeService();
		if (runtimeService == null) {
			System.out.println("runtimeService为null");
			assertNotNull(runtimeService);
			return;
		}

		Map<String, Object> vaiables = new HashMap<String, Object>();
		vaiables.put("name", "Activiti");
		Object name = "合同邮件通知11";
		vaiables.put("subject", name);
		Object to = "zhangxiaolin@lecarlink.com";
		vaiables.put("to", to);
		Object from = "noreply-test@lecarlink.com";
		vaiables.put("from", from);
		Object htmlContext = "<html><body>hello</body></html>";
		vaiables.put("htmlContext", htmlContext);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("emailTaskProcess", vaiables);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
	}
}