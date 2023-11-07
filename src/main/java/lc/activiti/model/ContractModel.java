package lc.activiti.model;

import java.io.Serializable;

import lc.activiti.entity.Contract;
import lombok.Data;

@Data
public class ContractModel extends Contract implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2605109776983644109L;
	private String statusTxt;
}
