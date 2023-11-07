package lc.activiti.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component // 此类注入必须配置邮件参数
public class EmailUtils {
	@Autowired
	private JavaMailSender mailSender;

	@Value("${lc.mail.fromMail.addr}")
	private String from;

	public void sendSimpleMail(String to, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);

		try {
			mailSender.send(message);
			log.info("简单邮件已经发送。");
		} catch (Exception e) {
			log.error("发送简单邮件时发生异常！", e);
		}
	}

	public void sendHtmlMail(String to, String subject, String content) {
		MimeMessage message = mailSender.createMimeMessage();

		try {
			// true表示需要创建一个multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);

			mailSender.send(message);
			log.info("html邮件发送成功");
		} catch (MessagingException e) {
			log.error("发送html邮件时发生异常！", e);
		}
	}

	public void sendHtmlMail(String subject, String content, List<String> tos) {
		MimeMessage message = mailSender.createMimeMessage();

		try {
			String to = "";
			if (tos.size() > 0) {
				to = tos.get(0);
			}
			// true表示需要创建一个multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			if (tos.size() > 1) {
				String[] original = new String[tos.size() - 1];
				//System.arraycopy(tos, 1, original, 0, tos.size() - 1);
				List<String> ccsList=new ArrayList<>();
				for(int index=1;index<tos.size();index++) {
					ccsList.add(tos.get(index));
				}
				ccsList.toArray(original);
				helper.setCc(original);
			}

			mailSender.send(message);
			log.info("邮件发送成功");
		} catch (MessagingException e) {
			log.error("邮件邮件发送异常！", e);
		}
	}

	public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);

			FileSystemResource file = new FileSystemResource(new File(filePath));
			String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
			helper.addAttachment(fileName, file);

			mailSender.send(message);
			log.info("带附件的邮件已经发送。");
		} catch (MessagingException e) {
			log.error("发送带附件的邮件时发生异常！", e);
		}
	}

}
