package lc.activiti;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lc.activiti.utils.EmailUtils;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EmailTest {
	@Autowired
	private EmailUtils emailUtils;
	
	@Test
	public void testSendMail() {
		String suject="测试";
		String content="你好";
		String to="zhangxiaolin@lecarlink.com";
		emailUtils.sendSimpleMail(to, suject, content);
		log.info("推送成功!");
	}
}
