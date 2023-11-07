package lc.activiti.entity;

import java.util.Date;
import lombok.Data;

/**
 * LCYW.T_WX_APP_LOGIN_INFO
 */
@Data
public class WXAppLoginInfo {
    /**
     * uuid
     */
    private String appUuid;

    /**
     * 微信小程序appid
     */
    private String appId;

    /**
     * 微信小程序appsecret
     */
    private String appSecret;

    /**
     * 微信小程序查询唯一条件（根据此条件查询微信appid和appsecret）
     */
    private Short appType;

    /**
     * 创建时间
     */
    private Date time;

    /**
     * 微信小程序名称
     */
    private String appName;
}