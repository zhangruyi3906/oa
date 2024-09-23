package com.lh.oa.module.system.dal.dataobject.meeting;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;

/**
 * 会议组织 DO
 *
 * @author didida
 */
@TableName("system_meeting")
//("system_meeting_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingDO extends BaseDO {

    /**
     * 会议ID
     */
    @TableId
    private Long id;
    /**
     * 会议标题
     */
    private String title;
    /**
     * 会议描述
     */
    private String description;
    /**
     * 会议开始时间
     */
    private Date startTime;
    /**
     * 会议结束时间
     */
    private Date endTime;
    /**
     * 组织者
     */
    private String organizer;
    /**
     * 会议地点
     */
    private String location;
    /**
     * 会议状态
     */
    private Boolean status;
    /**
     * 记录创建时间
     */
    private Date createdAt;
    /**
     * 组织者id
     */
    private Long userId;

}
