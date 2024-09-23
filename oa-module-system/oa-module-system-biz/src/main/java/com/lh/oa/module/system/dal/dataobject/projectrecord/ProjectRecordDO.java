package com.lh.oa.module.system.dal.dataobject.projectrecord;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


@TableName("attendance_project_record")
//("attendance_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRecordDO extends BaseDO {
    /**
     * 打卡记录ID
     */
    @TableId
    private Long id;
    /**
     * 员工ID
     */
    private Long userId;
    /**
     * 签到时间
     */
    private LocalDateTime checkInTime;
    /**
     * 签退时间
     */
    private LocalDateTime checkOutTime;
    /**
     * 签到状态
     */
    private Byte checkInStatus;
    /**
     * 签退状态
     */
    private Byte checkOutStatus;
    /**
     * 打卡年月日
     */
    private Date punchDate;
    /**
     * 签到类型（）
     */
    private Byte attStatus;
    /**
     * 项目id
     */
    private Long projectId;

    private String remarkIn;

    private String remarkOut;

    private String userName;

    private String projectName;

    private String photoUrl;
}
