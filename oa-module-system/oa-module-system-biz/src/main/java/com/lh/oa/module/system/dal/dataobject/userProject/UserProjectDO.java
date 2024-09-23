package com.lh.oa.module.system.dal.dataobject.userProject;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 人员项目 DO
 *
 * @author
 */
@TableName("user_project")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProjectDO extends BaseDO {

    /**
     * 人员项目表id
     */
    @TableId
    private Long id;
    /**
     * 人员id
     */
    private Long userId;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 人员是否已经离开（是0，否1）
     */
    private Byte status;
    /**
     * 离开时间
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date leaveTime;

    private String projectName;

    private String userName;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer type;

    private Integer isRecord;

    /**
     * 入场时间
     */
    private Date inTime;
    /**
     * 转场时间
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date outTime;

    @TableField(exist = false)
    private AdminUserDO adminUserDO;

    @TableField(exist = false)
    private InformationDO informationDO;

}
