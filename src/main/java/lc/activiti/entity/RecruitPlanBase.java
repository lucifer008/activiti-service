package lc.activiti.entity;

import java.util.Date;
import lombok.Data;

/**
 * LCYW.OA_RECRUIT_PLAN
 */
@Data
public class RecruitPlanBase {
    /**
     * 招聘计划Id
     */
    private String recruitPlanId;

    /**
     * 一级(没有留空处理)
     */
    private String depId1;

    /**
     * 二级(没有留空处理)
     */
    private String depId2;

    /**
     * 三级(没有留空处理)
     */
    private String depId3;

    /**
     * 四级(没有留空处理)
     */
    private String depId4;

    /**
     * 五级(没有留空处理)
     */
    private String depId5;

    /**
     * 六级(没有留空处理)
     */
    private String depId6;

    /**
     * 七级(没有留空处理)
     */
    private String depId7;

    /**
     * 岗位Id
     */
    private String positionId;

    /**
     * 年
     */
    private Short year;

    /**
     * 一月
     */
    private Short one;

    /**
     * 二月
     */
    private Short second;

    /**
     * 三月
     */
    private Short three;

    /**
     * 四月
     */
    private Short fourth;

    /**
     * 五月
     */
    private Short five;

    /**
     * 六月
     */
    private Short six;

    /**
     * 七月
     */
    private Short senven;

    /**
     * 八月
     */
    private Short eight;

    /**
     * 九月
     */
    private Short night;

    /**
     * 十月
     */
    private Short ten;

    /**
     * 十一月
     */
    private Short eleven;

    /**
     * 十二月
     */
    private Short twelve;

    /**
     * 最终人数
     */
    private Short finalPersons;

    /**
     * 创建时间
     */
    private Date input;

    /**
     * 录入用户Id
     */
    private String inputUserId;

    /**
     * 修改用户Id
     */
    private String updateUserId;

    /**
     * 修改时间
     */
    private Date update;
}