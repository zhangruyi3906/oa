package com.lh.oa.module.system.dal.dataobject.schedule;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 日程管理 DO
 *
 * @author didida
 */
@TableName("system_schedule")
//("system_schedule_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDO extends BaseDO {

    /**
     * 日程记录ID
     */
    @TableId
    private Long id;
    /**
     * 日程标题
     */
    private String title;
    /**
     * 日程描述
     */
    private String description;
    /**
     * 日程时间
     */
    private LocalDateTime expireTime;

    /**
     * 是否过期
     */
    private Boolean expired;
    /**
     * 创建者id
     */
    private Long userId;
    /**
     * 日程日期
     */
    private String expireDate;


    private Date expireDateDay;

    private Date scheStartTime;

    private Date scheEndTime;

    private String remark;

    private Integer status;


}
