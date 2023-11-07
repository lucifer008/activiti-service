package lc.activiti.model;

import java.util.Date;

import lc.activiti.lcenum.ApprovalType;
import lombok.Data;

@Data
public class NoticeModel {
	private String openId;
	private String formId;
	private String userName;
	private String applyContent;
	private ApprovalType approvalType;
	private String approvalResult;
	private String approvalPerson;
	private String auditSuggestion;//审核意见
	private Date approvalDate;
	private String applyPerson;
	private Date applyDate;
	private Date approvalOverDate;
	private String applyMemo;
	private String page;
	private int signOrApprovalOrApprovalResult;
}
