package com.lh.oa.module.system.dal.dataobject.worklog;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 员工工作日志 DO
 *
 * @author 管理员
 */
@TableName("system_work_log")
//("system_work_log_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkLogDO extends BaseDO {

    /**
     * 日志ID
     */
    @TableId
    private Long id;
    /**
     * 员工ID
     */
    private Long userId;
    /**
     * 日志日期
     */
    private Date logDate;
    /**
     * 日志内容
     */
    private String logContent;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 字段描述
     */
    private String description;
    /**
     * 是否可修改
     */
    @TableField(exist = false)
    private Boolean isEditable;
    /**
     * 部门id
     */
    private Long deptId;

    private String userName;
    /**
     * 提交时间
     */
    private String submitTime;

}
