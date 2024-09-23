package com.lh.oa.module.bpm.controller.admin.costReimburse.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 费用报销申请分页 RequestVo")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CostReimbursePageReqVo extends PageParam {

}