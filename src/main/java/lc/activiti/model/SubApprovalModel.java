package lc.activiti.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SubApprovalModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2416492563426592319L;

	//业务Id
	private String businessId;
	private String userId;
	private String departmentId;
	private String reason;
	private String userName;
	private String userEmail;
	private String userMoible;
	// 业务类型(1:金砖服务)
	private String businessType;
	//下级审批人列表
	private List<SubApprovalModel> nextApprovalUsers;
	
	//最后一次会签Id
	private String lastCounterSingnId;
	
}
