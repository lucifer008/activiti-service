package lc.activiti.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Departments {
    private String id;

    private String oaId;

    private String oaId2;

    private String depCode;

    private String depName;

    private String depPerson;

    private String depPersonId;

    private String tel;

    private String mobile;

    private String status;

    private String tax;

    private String deptypeid;

    private String insertUserId;

    private Date insertDateTime;

    private String updateUserId;

    private Date updateDateTime;

    private String companyId;

    private BigDecimal deleted;

    private String depTypeId;

    private BigDecimal dLevel;
    /**
     * @Description:组织类型：0 集团公司 1 子公司 2分公司-独立核算 3分公司-非独核算 4VIE公司 5控股公司 6参股公司 7虚拟体系 8组织性部门 9项目型部门 10小兵团
     */
    private Short type;

    /**
     *组织名称_简称
     */
    private String abbName;
    /**
     * 组织BP：政委或指导员
     */
    private String BP;
    /**
     * 组织PO
     */
    private String PO;
    /**
     * 组织架构树上的排序
     */
    private Short order;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOaId() {
        return oaId;
    }

    public void setOaId(String oaId) {
        this.oaId = oaId;
    }

    public String getOaId2() {
        return oaId2;
    }

    public void setOaId2(String oaId2) {
        this.oaId2 = oaId2;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getDepPerson() {
        return depPerson;
    }

    public void setDepPerson(String depPerson) {
        this.depPerson = depPerson;
    }

    public String getDepPersonId() {
        return depPersonId;
    }

    public void setDepPersonId(String depPersonId) {
        this.depPersonId = depPersonId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getDeptypeid() {
        return deptypeid;
    }

    public void setDeptypeid(String deptypeid) {
        this.deptypeid = deptypeid;
    }

    public String getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(String insertUserId) {
        this.insertUserId = insertUserId;
    }

    public Date getInsertDateTime() {
        return insertDateTime;
    }

    public void setInsertDateTime(Date insertDateTime) {
        this.insertDateTime = insertDateTime;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public BigDecimal getDeleted() {
        return deleted;
    }

    public void setDeleted(BigDecimal deleted) {
        this.deleted = deleted;
    }

    public String getDepTypeId() {
        return depTypeId;
    }

    public void setDepTypeId(String depTypeId) {
        this.depTypeId = depTypeId;
    }

    public BigDecimal getdLevel() {
        return dLevel;
    }

    public void setdLevel(BigDecimal dLevel) {
        this.dLevel = dLevel;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getAbbName() {
        return abbName;
    }

    public void setAbbName(String abbName) {
        this.abbName = abbName;
    }

    public String getBP() {
        return BP;
    }

    public void setBP(String BP) {
        this.BP = BP;
    }

    public String getPO() {
        return PO;
    }

    public void setPO(String PO) {
        this.PO = PO;
    }

    public Short getOrder() {
        return order;
    }

    public void setOrder(Short order) {
        this.order = order;
    }
}