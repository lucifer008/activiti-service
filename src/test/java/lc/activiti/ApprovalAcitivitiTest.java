package lc.activiti;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lc.activiti.dao.base.IBrowsePermissionBaseDao;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApprovalAcitivitiTest {

    @Autowired
    private IBrowsePermissionBaseDao browsePermissionDao;

    @Test
    public void testSpringBoot() {
        System.out.println(browsePermissionDao);
        System.out.println("hello Workld");
    }
    //此类注入必须配置spring boot 邮件参数
//	@Autowired
//	private EmailUtils emailUtils;
//	
//	@Test
//	public void testSendEmail() {
//		String content="你好，测试邮件";
//		String subject="测试邮件";
//		String to="zhangxiaolin@lecarlink.com";
//		emailUtils.sendSimpleMail(to, subject, content);
//		System.out.println("发送成功!");
//	}
}
