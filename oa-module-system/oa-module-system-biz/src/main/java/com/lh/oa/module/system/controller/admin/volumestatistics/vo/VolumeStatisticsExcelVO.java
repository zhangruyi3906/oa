package com.lh.oa.module.system.controller.admin.volumestatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 员工方量统计 Excel VO
 *
 * @author
 */
@Data
public class VolumeStatisticsExcelVO {

    @ExcelProperty("记录编号")
    private Long id;

    @ExcelProperty("员工编号")
    private Long userId;

    @ExcelProperty("项目编号")
    private Long projectId;

    @ExcelProperty("方量")
    private Long volume;

    @ExcelProperty("日期")
    private Date volumeDate;

}
