package com.lh.oa.module.bpm.dal.dataobject.approvalprocess;

import lombok.*;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;

/**
 * 项目立项 DO
 *
 * @author 狗蛋
 */
@TableName("project_approval_process")
//("project_approval_process_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalProcessDO extends BaseDO {

    /**
     * 项目立项ID
     */
    @TableId
    private Long id;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目开始日期
     */
    private LocalDateTime startDate;
    /**
     * 项目结束日期
     */
    private LocalDateTime endDate;
    /**
     * 项目经理ID
     */
    private Long projectManagerId;
    /**
     * 审批状态
     */
    private Integer approvalStatus;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;

    private Long userId;

}
