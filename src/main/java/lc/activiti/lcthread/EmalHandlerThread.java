package lc.activiti.lcthread;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import lc.activiti.model.EmailModel;
import lc.activiti.model.SubApprovalModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmalHandlerThread implements Runnable {

	private EmailModel emailModel;

	public EmalHandlerThread() {
		
	}

	@Override
	public void run() {
		List<String> ccs = new ArrayList<>();
		try {
//			log.info("emailUtils:{}", emailModel.getEmailUtils());
//			log.info("当前审批人:{}", new Gson().toJson(emailModel.getCurrentUsers()));
//			log.info("下级审批人:{}", new Gson().toJson(emailModel.getNextApprovalList()));
//			log.info("EmalHandlerThread--->{}", "run");

			
			if (emailModel.getIsSendSubmit()) {
				emailModel.getEmailUtils().sendHtmlMail(emailModel.getCurrentUsers().getUserEmail(),
						emailModel.getSubmitSubject(), emailModel.getSubContent());
				log.info("【{}】 邮件发送完成。通知人:{}",emailModel.getActivitiProcessType().getProcessDesc(),new Gson().toJson(emailModel.getCurrentUsers()));
				
			}
			
			if ( emailModel.getNextApprovalList()!=null && emailModel.getNextApprovalList().size()!=0) {
				for (SubApprovalModel subApprovalModel : emailModel.getNextApprovalList()) {
					ccs.add(subApprovalModel.getUserEmail());
				}
				emailModel.getEmailUtils().sendHtmlMail(emailModel.getSubject(), emailModel.getContent(), ccs);
				log.info("【{}】 邮件发送完成。通知人:{}",emailModel.getActivitiProcessType().getProcessDesc(),new Gson().toJson(emailModel.getNextApprovalList()));
				
			}
			
			
		} catch (Exception e) {
			log.error("【邮件通知异常】收件用户列表={} 异常={}",ccs, e);
			// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}

	}

	public EmailModel getEmailModel() {
		return emailModel;
	}

	public void setEmailModel(EmailModel emailModel) {
		this.emailModel = emailModel;
	}

}
