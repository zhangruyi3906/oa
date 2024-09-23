package com.lh.oa.module.system.controller.admin.monthstatistics.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 考勤月统计创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MonthStatisticsCreateReqVO extends MonthStatisticsBaseVO {

}
