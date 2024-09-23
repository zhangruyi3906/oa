package com.lh.oa.module.bpm.controller.admin.task.vo.copy;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 流程消息通知的分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmCopyPageReqVO extends PageParam {
    /**
     * 抄送主键
     */
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
    private String delFlag;
    /**
     * 已读状态（0未读，1已读）
     */
    private Integer readState;
    //PC和APP通过关键字查询发起人或流程名，WEB分开查
    /**
     * 用于通过发起人或流程名查询
     */
    private String keyword;
}
