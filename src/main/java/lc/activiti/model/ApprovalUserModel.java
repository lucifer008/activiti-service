package lc.activiti.model;

import java.io.Serializable;

import lc.activiti.entity.Users;

public class ApprovalUserModel extends Users implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4820897438365445618L;
	private String depId;
	private String depName;
	public String getDepId() {
		return depId;
	}
	public void setDepId(String depId) {
		this.depId = depId;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	
}
