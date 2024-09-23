package com.lh.oa.module.system.dal.dataobject.meetingcontent;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.util.Date;

/**
 * 会议记录 DO
 *
 * @author didida
 */
@TableName("system_meeting_content")
//("system_meeting_content_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingContentDO extends BaseDO {

    /**
     * 会议记录id
     */
    @TableId
    private Long id;
    /**
     * 会议ID
     */
    private Integer meetingId;
    /**
     * 会议议题
     */
    private String topic;
    /**
     * 发言人
     */
    private String speaker;
    /**
     * 会议内容
     */
    private String content;
    /**
     * 记录创建时间
     */
    private Date createdAt;
    /**
     * 记录人id
     */
    private String writeName;

}
