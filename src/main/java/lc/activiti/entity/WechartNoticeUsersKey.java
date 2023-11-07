package lc.activiti.entity;

import lombok.Data;

/**
 * LCYW.HT_WECHART_NOTICE_USERS
 */
@Data
public class WechartNoticeUsersKey {
    /**
     * 小程序openId
     */
    private String openid;

    /**
     * 小程序formId
     */
    private String formid;
}