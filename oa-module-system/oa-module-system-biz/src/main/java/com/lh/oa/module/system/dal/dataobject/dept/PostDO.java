package com.lh.oa.module.system.dal.dataobject.dept;

import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位表
 *
 * @author didida
 */
@TableName("system_post")
//("system_post_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class PostDO extends BaseDO {

    /**
     * 岗位序号
     */
    @TableId
    private Long id;
    /**
     * 岗位名称
     */
    private String name;
//    /**
//     * 岗位编码
//     */
//    private String code;
    /**
     * 岗位排序
     */
    private Integer sort;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

    /**
     * 组织ID
     */
    private Long orgId;

}
