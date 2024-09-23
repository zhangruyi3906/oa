package com.lh.oa.module.bpm.dal.dataobject.businessForm.performanceExamine;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
* 绩效考评申请单详情
*
* @author tanghanlin
* @since 2023-10-24
*/
@Getter
@Setter
@Accessors(chain = true)
@TableName("bpm_performance_examine_detail")
public class BpmPerformanceExamineDetail extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 绩效考评申请单id
     */
    private Long performanceExamineFormId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 所在部门
     */
    private Long deptId;

    /**
     * 适用文件，字典表里performance_applicable_file_type的值
     */
    private Integer applicableFile;

    /**
     * 文件编号
     */
    private String fileNo;

    /**
     * 条款内容
     */
    private String termDetail;

    /**
     * 是否是品质问题
     */
    private Boolean qualityProblem;

    /**
     * 考核原因
     */
    private String examineReason;

    /**
     * 考核情况，字典表里performance_examine_situation_type的值
     */
    private Integer examineSituation;

    /**
     * 考核分数
     */
    private BigDecimal examineScore;

}
