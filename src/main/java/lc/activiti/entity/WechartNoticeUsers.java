package lc.activiti.entity;

import java.util.Date;
import lombok.Data;

/**
 * LCYW.HT_WECHART_NOTICE_USERS
 */
@Data
public class WechartNoticeUsers extends WechartNoticeUsersKey {
    /**
     * 小程序用户Id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 上传时间
     */
    private Date uploadDate;

    /**
     * 状态(0:正常:1:已失效)
     */
    private Short status;

    /**
     * 修改日期
     */
    private Date update;
}