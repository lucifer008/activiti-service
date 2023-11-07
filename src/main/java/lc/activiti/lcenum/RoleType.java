package lc.activiti.lcenum;

/**
 * 合同状态
 * @author Raytine
 *
 */
public enum RoleType {
	Office((short)17,"办公室"),
	Forensic((short)16,"法务"),
	DataVP((short)4,"数据VP"),
	Personnel((short)102,"人事"),
	CEO((short)0,"总裁");
	
	private Short roleType;
	private String desc;
	private RoleType(Short _roleType,String _desc) {
		roleType=_roleType;
		desc=_desc;
	}
	public void setRoleType(Short roleType) {
		this.roleType = roleType;
	}
	public Short getRoleType() {
		return roleType;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDesc() {
		return desc;
	}
	public static String getRoleTypeDesc(Integer roleType) {
		for(RoleType contractStatus:RoleType.values()) {
			if (contractStatus.roleType.equals(roleType)) {
				return contractStatus.desc;
			}
		}
		return "角色类型错误";
	}
}
