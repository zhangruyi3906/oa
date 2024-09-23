package com.lh.oa.module.system.full.entity.attandance.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AttendanceRecordDTO {
    private int userId;
    private int deptId;
    private int projectId;

    private String clockDate; //
    private String clockTime; //

    private String clockPosition;
    private Double clockLongitude;
    private Double clockLatitude;

    /**
     * 打卡照片
     */
    private String photoUrl;
    /**
     * 是否离线打卡，默认在线打卡
     */
    private boolean online = true;

}