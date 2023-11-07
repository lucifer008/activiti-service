package lc.activiti.entity;

import java.util.Date;

public class BrowsePermission {
    private String browsePermissionId;

    private String userId;

    private Date start;

    private Date end;

    private Short status;

    private String contractId;

    private Object reason;

    private Date input;

    public String getBrowsePermissionId() {
        return browsePermissionId;
    }

    public void setBrowsePermissionId(String browsePermissionId) {
        this.browsePermissionId = browsePermissionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Object getReason() {
        return reason;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }

    public Date getInput() {
        return input;
    }

    public void setInput(Date input) {
        this.input = input;
    }
}