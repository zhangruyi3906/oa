package com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 办公用品申请创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OfficeSuppliesRequestCreateVo extends OfficeSuppliesRequestBaseVo{
}
