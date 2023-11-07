package lc.activiti.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class TestModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6615577867528511297L;
	private Integer id;
	private String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
