package com.lh.oa.module.system.dal.dataobject.project;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;

/**
 * 项目 DO
 *
 * @author 狗蛋
 */
@TableName("pro_project")
//("pro_project_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Integer id;
    /**
     * 所属组织ID
     */
    private Integer orgId;
    /**
     * 名称
     */
    private String name;
    /**
     * 简称
     */
    private String simpleName;
    /**
     * 简介
     */
    private String simpleDescription;
    /**
     * 类型
     */
    private String type;
    /**
     * 效果图ID
     */
    private Integer effectGraphFileId;
    /**
     * 效果图URL
     */
    private String effectGraphFileUrl;
    /**
     * 平面图ID
     */
    private Integer planarGraphFileId;
    /**
     * 平面图URL
     */
    private String planarGraphFileUrl;
    /**
     * 计划开始时间
     */
    private Integer planStartTime;
    /**
     * 计划结束时间
     */
    private Integer planEndTime;
    /**
     * 负责人
     */
    private String directorName;
    /**
     * 负责人电话
     */
    private String directorMobile;
    /**
     * 地址
     */
    private String address;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    /**
     * 施工单位
     */
    private String constructUnit;
    /**
     * 建设单位
     */
    private String workUnit;
    /**
     * 监理单位
     */
    private String superviseUnit;
    /**
     * 设计单位
     */
    private String designUnit;
    /**
     * 勘察单位
     */
    private String surveyUnit;
    /**
     * 微官网ID
     */
    private Integer microWebsiteId;
    /**
     * 排序
     */
    private Integer orderNumber;

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

    private String typeVal;


    /**
     * 置顶
     */
    private String isTop;

    /**
     * 是否禁用
     */
    private Boolean isDisabled;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 完成日期
     */
    private Date finishDate;

    @TableField(exist = false)
    private Boolean topped;
    /**
     * 项目分类
     */
    private String category;

}
