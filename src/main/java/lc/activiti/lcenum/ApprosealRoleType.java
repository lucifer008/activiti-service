package lc.activiti.lcenum;

/*
 * 角色类型
 * */
public enum ApprosealRoleType {
	PrecisionCarpoolingOffice((short)19,"精拼办公室"),
	TrunkLineOffice((short)20,"干线办公室"),
	FinanceVP((short)6,"财务VP"),
	FunctionVP((short)5,"职能VP"),
	DataVP((short)4,"数据VP");
	
	private Short type;
	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private String desc;
	private ApprosealRoleType(Short _type, String _desc) {
		this.setType(_type);
		this.setDesc(_desc);
	}

	public static String getSealStatusDesc(Integer type) {
		for(ApprosealRoleType roleType: ApprosealRoleType.values()) {
			if (roleType.type.equals(type)) {
				return roleType.desc;
			}
		}
		return "角色类型错误";
	}
}
