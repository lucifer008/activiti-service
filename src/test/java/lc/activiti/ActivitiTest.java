package lc.activiti;

import static org.junit.Assert.assertTrue;

import org.activiti.engine.ProcessEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lc.activiti.service.ContractProcessService;
import lc.activiti.service.EmailTaskService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiTest {
	
	private static final String processDefinitionKey = "emailTaskProcess";
	@Autowired
	private ProcessEngine procesEngine;
	
	@Test
	public void test1() {
		System.out.println("test1");
		System.out.println(procesEngine);
	}
	
	@Value("${lc.mail.fromMail.addr}")
	private String from;
	@Test
	public void testEmailTask() {
		//assertFalse(false);
		//assertEquals(1, 0);
//		Map<String,Object> vaiables=new HashMap<>();
//		Object name="合同邮件通知";
//		vaiables.put("subject", name);
//		Object to="zhangxiaolin@lecarlink.com";
//		vaiables.put("to", to);
//		//Object from="";
//		vaiables.put("from", from);
////		Object cc="zhangxiaolin@lecarlink.com";
////		vaiables.put("cc", cc);
////		Object bcc="zhangxiaolin@lecarlink.com";
////		vaiables.put("bcc", bcc);
//		Object htmlContext="<html><body>hello</body></html>";
//		vaiables.put("htmlContext", htmlContext);
//		String businessKey=GuidUtils.newGuid();
//		ProcessInstance processInstance=procesEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey, businessKey, vaiables);
//		assertNotNull(processInstance);
	}
	@Autowired
	private EmailTaskService emailTaskService;
	
	@Test
	public void testSendEmail() {
//		ActivitiProcessType activitiProcessType=ActivitiProcessType.ContactApprovalProcess;
//		Object bussinesOperatorType=ContractProcessStatus.Submit;
//		SubApprovalModel currentUsers=new SubApprovalModel();
//		List<SubApprovalModel> nextApprovalList=new ArrayList<>();
//		emailTaskService.sendEmail(activitiProcessType, bussinesOperatorType, currentUsers, nextApprovalList);
		//Timestamp timestamp=new Timestamp(1550215733602)
		
	}
	
	@Autowired
	private ContractProcessService contractProcessService;
	@Test
	public void testDeleteEmailProcess() {
		contractProcessService.deleteEmailTaskProcess();
		assertTrue(true);
	}
	
}
