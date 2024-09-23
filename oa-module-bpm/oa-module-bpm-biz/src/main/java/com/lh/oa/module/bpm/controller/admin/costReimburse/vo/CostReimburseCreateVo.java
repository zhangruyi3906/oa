package com.lh.oa.module.bpm.controller.admin.costReimburse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * @author yangsheng
 */
@Schema(description = "管理后台 - 费用报销申请 RequestVo")
@Data
@ToString(callSuper = true)
public class CostReimburseCreateVo extends CostReimburseBaseVo{
}
