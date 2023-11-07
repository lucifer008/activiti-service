package lc.activiti.entity;

import lombok.Data;

@Data
public class NewRolesModel {
    private String roleId;
    private String roleCode;
    private String roleName;
    private String deleted;
    private String customStatus;

    private Integer roletype;
    private String remark;
    private String entryType;
    private String roleStatus;
}
