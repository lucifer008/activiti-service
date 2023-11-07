package lc.activiti.lcenum;

/**
 *
 *
 * 招聘状态
 * @author lucifer
 * @date 2019/9/17 16:07
 */
public enum RecruitStatus {
    New(0,"新建"),
    Applyed(1,"已申请"),
    Approvaled(2,"已审批"),
    Reject(-1,"已驳回"),
    Withdraw(-2,"已撤回");
    private Integer status;
    private String desc;
    private RecruitStatus(Integer _status,String _desc) {
        this.setStatus(_status);
        this.setDesc(_desc);
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public static String getRecruitStatusDesc(Integer status) {
        for(RecruitStatus recruitStatus:RecruitStatus.values()) {
            if (recruitStatus.status.equals(status)) {
                return recruitStatus.desc;
            }
        }
        return "状态错误";
    }
}
