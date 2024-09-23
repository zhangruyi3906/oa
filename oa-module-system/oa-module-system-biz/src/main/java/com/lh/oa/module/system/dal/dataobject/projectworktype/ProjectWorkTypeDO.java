package com.lh.oa.module.system.dal.dataobject.projectworktype;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;

/**
 * 项目工种 DO
 *
 * @author
 */
@TableName("pro_project_work_type")
//("pro_project_work_type_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectWorkTypeDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Integer id;
    /**
     * 项目ID
     */
    private Integer projectId;
    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 来源
     */
    private String dataSource;
    /**
     * 排序
     */
    private Integer orderNumber;
    /**
     * 创建时间
     */
    private Integer createdTime;
    /**
     * 创建人
     */
    private Integer createdBy;
    /**
     * 修改时间
     */
    private Integer modifiedTime;
    /**
     * 修改人
     */
    private Integer modifiedBy;
    /**
     * 统一标志
     */
    private Integer flag;

}
