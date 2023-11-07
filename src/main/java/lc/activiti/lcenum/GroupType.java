package lc.activiti.lcenum;

/**
 *
 *

 * 组类型
 * @author lucifer
 * @date 2019/9/9 14:15
 */
public enum GroupType {
    Personnel("Personnel",Short.parseShort("1"),"人事组"),
    PersonnelCEO("PersonnelCEO",Short.parseShort("11"),"人事总裁组"),

    FinanceGroup("FinanceGroup",Short.parseShort("2"),"财务组"),
    FinanceGroupCEO("FinanceGroupCEO",Short.parseShort("22"),"财务总裁组"),

    DataGroup("DataGroup",Short.parseShort("3"),"数据组"),
    DataGroupCEO("DataGroupCEO",Short.parseShort("33"),"数据总裁组"),

    BusinessGroup("BusinessGroup",Short.parseShort("4"),"业务组"),
    BusinessGroupCEO("BusinessGroupCEO",Short.parseShort("44"),"业务总裁组")
    ;
     GroupType(String _typeKey,Short _typeValue,String _desc){
        this.typeKey=_typeKey;
        this.typeValue=_typeValue;
        this.desc=_desc;
    }
    private String typeKey;
    private Short typeValue;
    private String desc;

    public Short getTypeValue() {
        return typeValue;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public String getDesc() {
        return desc;
    }

    public static String getGroupTypeDesc(String typeKey) {
        for(GroupType groupType:GroupType.values()) {
            if (groupType.typeKey.equals(typeKey)) {
                return groupType.desc;
            }
        }
        return "类型错误";
    }
}
