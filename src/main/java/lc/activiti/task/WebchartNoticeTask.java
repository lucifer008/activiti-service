package lc.activiti.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lc.activiti.service.WechartNoticeService;
import lombok.extern.slf4j.Slf4j;

//合同审批通知
//lucifer
//2019年7月1日 18:13:35
@Slf4j
@Component
public class WebchartNoticeTask {
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Autowired
	private WechartNoticeService wecartNoticeService;

	//@Scheduled(cron = "0 0/60 * * * ?")
	public void noticeContractApprovalUser() {
		try {
			log.info("定时任务-->【合同审批通知】任务开始,时间:{}", simpleDateFormat.format(new Date()));
			wecartNoticeService.pushNoticeNextApprovalUser();
		} catch (Exception ex) {
			log.error("定时任务-->【合同审批通知】异常=={}", ex);
		} finally {
			log.info("定时任务-->【合同审批通知】任务结束,时间:{}", simpleDateFormat.format(new Date()));
		}
	}
}
