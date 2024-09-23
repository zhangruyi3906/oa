package com.lh.oa.module.system.dal.dataobject.dept;

import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.tenant.core.db.TenantBaseDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.module.system.enums.DeptTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门表
 *
 * @author didida
 * @author
 */
@TableName("system_dept")
//("system_dept_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptDO extends TenantBaseDO {

    /**
     * 部门ID
     */
    @TableId
    private Long id;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 父部门ID
     *
     * 关联 {@link #id}
     */
    private Long parentId;

    /**
     * 类型
     */
    private DeptTypeEnum type;

    public String getTypeVal() {
        return type == null ? null : type.getVal();
    }

    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 负责人
     *
     * 关联 {@link AdminUserDO#getId()}
     */
    private Long leaderUserId;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 部门状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;



}
