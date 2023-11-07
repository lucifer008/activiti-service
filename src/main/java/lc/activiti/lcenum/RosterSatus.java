package lc.activiti.lcenum;
/**
 *
 *

 * 员工花名册
 * @author lucifer
 * @date 2019/9/9 14:12
 */
public enum RosterSatus {
    /**
     * 流程提交
     */
    Submit((short)1,"流程提交"),
    /**
     * 流程通过审批
     */
    ApprovalSuccess((short)2,"流程通过审批"),
    /**
     * 流程审批驳回
     */
    ApprovalReject((short)-1,"流程审批驳回");
    private Short status;
    private String desc;
    private RosterSatus(Short _status,String _desc) {
        this.setStatus(_status);
        this.setDesc(_desc);
    }
    public Short getStatus() {
        return status;
    }
    public void setStatus(Short status) {
        this.status = status;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public static String getRosterStatus(Short status) {
        for(RosterSatus rosterSatus:RosterSatus.values()) {
            if (rosterSatus.status.equals(status)) {
                return rosterSatus.desc;
            }
        }
        return "状态错误";
    }
}
