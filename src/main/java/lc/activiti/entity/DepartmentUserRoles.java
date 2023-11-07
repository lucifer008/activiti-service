package lc.activiti.entity;

import java.util.Date;

public class DepartmentUserRoles {
    private String cDepartmentUsersId;

    private String cDepartmentId;

    private String cUsersId;

    private Date dInput;

    private String dInputUserId;

    public String getcDepartmentUsersId() {
        return cDepartmentUsersId;
    }

    public void setcDepartmentUsersId(String cDepartmentUsersId) {
        this.cDepartmentUsersId = cDepartmentUsersId;
    }

    public String getcDepartmentId() {
        return cDepartmentId;
    }

    public void setcDepartmentId(String cDepartmentId) {
        this.cDepartmentId = cDepartmentId;
    }

    public String getcUsersId() {
        return cUsersId;
    }

    public void setcUsersId(String cUsersId) {
        this.cUsersId = cUsersId;
    }

    public Date getdInput() {
        return dInput;
    }

    public void setdInput(Date dInput) {
        this.dInput = dInput;
    }

    public String getdInputUserId() {
        return dInputUserId;
    }

    public void setdInputUserId(String dInputUserId) {
        this.dInputUserId = dInputUserId;
    }
}