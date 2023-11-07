package lc.activiti.model;

import java.io.Serializable;
import java.util.List;

import lc.activiti.lcenum.ActivitiProcessType;
import lc.activiti.utils.EmailUtils;
import lombok.Data;

@Data
public class EmailModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6302952666463756686L;
	private ActivitiProcessType activitiProcessType;

	private Object bussinesOperatorType;
	private String from;
	private SubApprovalModel currentUsers;
	private List<SubApprovalModel> nextApprovalList;

	private EmailUtils emailUtils;
	private String subject;
	private String content;
	private Boolean isSendSubmit = false;
	private String submitSubject;
	private String subContent;
}
