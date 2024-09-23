package com.lh.oa.module.system.dal.dataobject.oauth2;

import com.lh.oa.framework.common.enums.UserTypeEnum;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * OAuth2 批准 DO
 *
 * 用户在 sso.vue 界面时，记录接受的 scope 列表
 *
 * @author
 */
@TableName(value = "system_oauth2_approve", autoResultMap = true)
//("system_oauth2_approve_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class OAuth2ApproveDO extends BaseDO {

    /**
     * 编号，数据库自增
     */
    @TableId
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 用户类型
     *
     * 枚举 {@link UserTypeEnum}
     */
    private Integer userType;
    /**
     * 客户端编号
     *
     * 关联 {@link OAuth2ClientDO#getId()}
     */
    private String clientId;
    /**
     * 授权范围
     */
    private String scope;
    /**
     * 是否接受
     *
     * true - 接受
     * false - 拒绝
     */
    private Boolean approved;
    /**
     * 过期时间
     */
    private LocalDateTime expiresTime;

}
