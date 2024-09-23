package com.lh.oa.module.bpm.controller.admin.task.vo.copy;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.module.bpm.api.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bpm_copy")
public class BpmCopy extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 抄送主键
     */
    @TableId(value = "copy_id")
    private String copyId;
    /**
     * 抄送标题
     */
    private String title;
    /**
     * 流程主键
     */
    private String processId;
    /**
     * 流程名称
     */
    private String processName;
    /**
     * 流程分类主键
     */
    private String categoryId;
    /**
     * 部署主键
     */
    private String deploymentId;
    /**
     * 流程实例主键
     */
    private String instanceId;
    /**
     * 任务主键
     */
    private String taskId;
    /**
     * 用户主键
     */
    private Long userId;
    /**
     * 发起人Id
     */
    private Long originatorId;
    /**
     * 发起人名称
     */
    private String originatorName;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;
    /**
     * 已读状态（0未读，1已读）
     */
    private Integer readState;


}