package lc.activiti.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lc.activiti.model.EmailModel;
import lc.activiti.service.EmailTaskService;
import lc.activiti.lcthread.EmalHandlerThread;
import lc.activiti.utils.EmailUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailTaskServiceImpl implements EmailTaskService {

	@Value("${lc.mail.fromMail.addr}")
	private String from;
	
	
	@Autowired
	private EmailUtils emailUtils;

	 @Async
	//@Transactional(isolation = Isolation.DEFAULT, propagation =
	// Propagation.REQUIRED)
	@Override
	public void sendEmail(EmailModel emailInfo) {

		log.info("通知邮件异步发送开始--->");
		EmalHandlerThread emailHandlerThread = new EmalHandlerThread();
		emailInfo.setFrom(from);
		emailInfo.setEmailUtils(emailUtils);
		emailHandlerThread.setEmailModel(emailInfo);
		Thread thread = new Thread(emailHandlerThread);
		thread.start();
	//	emailHandlerThread.run();
		log.info("通知邮件异步发送结束--->");
	}

}
