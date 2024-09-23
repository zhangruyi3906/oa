package com.lh.oa.module.bpm.dal.dataobject.businessForm.performanceExamine;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
* 绩效考评申请单
*
* @author tanghanlin
* @since 2023-10-24
*/
@Getter
@Setter
@Accessors(chain = true)
@TableName("bpm_performance_examine_form")
public class BpmPerformanceExamineForm extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 被考评人所在部门
     */
    private Long deptId;

    /**
     * 责任部门是否已核实
     */
    private Boolean deptVerification;

    /**
     * 考评事项
     */
    private String examineDetail;

    /**
     * 附件url
     */
    private String annexUrl;

    /**
     * 流程定义id
     */
    private String processInstanceId;

    /**
     * 流程执行结果
     */
    private Integer result;

}
