package lc.activiti.lcenum;

/**   
*     
* 类描述：   业务类型枚举
* 创建人：lucifer   
* 创建时间：2019年2月26日 上午11:41:58   
* @version        
*/
public enum BusinessTypeEnu {
	ZT_Order_Service(1,"砖头服务");
	private Integer type;
	private String desc;
	private BusinessTypeEnu(Integer _type,String _desc) {
		this.setType(_type);
		this.setDesc(_desc);
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer _type) {
		this.type = _type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public static String getBusinessDesc(Integer _type) {
		for(BusinessTypeEnu contractStatus:BusinessTypeEnu.values()) {
			if (contractStatus.type.equals(_type)) {
				return contractStatus.desc;
			}
		}
		return null;
	}
	public static Integer getBusinessType(String _desc) {
		for(BusinessTypeEnu contractStatus:BusinessTypeEnu.values()) {
			if (contractStatus.desc.equals(_desc)) {
				return contractStatus.type;
			}
		}
		return null;
	}
}
