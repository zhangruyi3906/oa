package com.lh.oa.module.system.controller.admin.userProject.vo;

import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 人员项目 Excel VO
 *
 * @author didida
 */
@Data
public class UserProjectExcelVO {

    @ExcelProperty("人员项目表id")
    private Long id;

    @ExcelProperty("人员id")
    private Long userId;

    @ExcelProperty("项目id")
    private Long projectId;

    @ExcelProperty("人员是否已经离开（是0，否1）")
    private Byte status;

    @ExcelProperty("人员名称")
    private String userName;

    @ExcelProperty("项目名称")
    private String projectName;

    @ExcelProperty("入场时间")
    private Date inTime;

    @ExcelProperty("转场时间")
    private Date outTime;

}
