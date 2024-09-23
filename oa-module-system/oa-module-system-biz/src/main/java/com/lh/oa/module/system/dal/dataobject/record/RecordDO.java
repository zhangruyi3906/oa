package com.lh.oa.module.system.dal.dataobject.record;

import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;

/**
 * 打卡记录 DO
 *
 * @author
 */
@TableName("attendance_record")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordDO extends BaseDO {

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
     * 部门ID
     */
    private Long deptId;
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
    private Byte checkInStatus; //0 正常，1 迟到
    /**
     * 签退状态
     */
    private Byte checkOutStatus; //0 正常，1 早退
    /**
     * 打卡年月日
     */
    private Date punchDate;
    /**
     * 签到类型（）
     */
    private Byte attStatus; //0 部门，1 项目

    private String remarkIn;

    private String remarkOut;
    private String userName;
    private String deptName;
    private Integer isBusiness;

//    private Byte clockStatus; //状态：0 正常、1 旷工、2 异地、3 离线

}
