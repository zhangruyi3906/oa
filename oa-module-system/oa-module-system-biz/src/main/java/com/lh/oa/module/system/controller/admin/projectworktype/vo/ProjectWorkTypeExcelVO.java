package com.lh.oa.module.system.controller.admin.projectworktype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 项目工种 Excel VO
 *
 * @author
 */
@Data
public class ProjectWorkTypeExcelVO {

    @ExcelProperty("主键ID")
    private Integer id;

    @ExcelProperty("项目ID")
    private Integer projectId;

    @ExcelProperty("代码")
    private String code;

    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("来源")
    private String dataSource;

    @ExcelProperty("排序")
    private Integer orderNumber;

    @ExcelProperty("创建时间")
    private Integer createdTime;

    @ExcelProperty("创建人")
    private Integer createdBy;

    @ExcelProperty("修改时间")
    private Integer modifiedTime;

    @ExcelProperty("修改人")
    private Integer modifiedBy;

    @ExcelProperty("统一标志")
    private Integer flag;

}
