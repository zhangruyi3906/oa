package com.lh.oa.module.system.dal.dataobject.holidayInfo;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("holiday_info")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolidayInfoDO extends BaseDO {
    // 1代表是，2代表否，从数据库中文字段可以反推出结果来
    @TableId
    private Long id;
    private Integer year;
    private Integer month;
    private Integer date;
    private Integer yearweek;
    private Integer yearday;
    private Integer lunarYear;
    private Integer lunarMonth;
    private Integer lunarDate;
    private Integer lunarYearday;
    private Integer week;
    private Integer weekend;
    private Integer workday;
    private Integer holiday;
    private Integer holidayOr;
    private Integer holidayOvertime;
    private Integer holidayToday;
    private Integer holidayLegal;
    private Integer holidayRecess;
    private String yearCn;
    private String monthCn;
    private String dateCn;
    private String yearweekCn;
    private String yeardayCn;
    private String lunarYearCn;
    private String lunarMonthCn;
    private String lunarDateCn;
    private String lunarYeardayCn;
    private String weekCn;
    private String weekendCn;
    private String workdayCn;
    private String holidayCn;
    private String holidayOrCn;
    private String holidayOvertimeCn;
    private String holidayTodayCn;
    private String holidayLegalCn;
    private String holidayRecessCn;
}

