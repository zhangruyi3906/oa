package com.lh.oa.module.system.controller.admin.customerservice.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.lh.oa.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 客户服务分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerServicePageReqVO extends PageParam {

    @Schema(description = "客户id")
    private Long customerId;

    @Schema(description = "服务内容")
    private String serviceContent;

    @Schema(description = "反馈")
    private String feedback;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] createTime;

}
