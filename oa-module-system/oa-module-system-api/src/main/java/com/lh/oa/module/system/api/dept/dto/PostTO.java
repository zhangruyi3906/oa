package com.lh.oa.module.system.api.dept.dto;

import com.lh.oa.framework.common.enums.CommonStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 岗位
 */
@Getter
@Setter
@ToString
public class PostTO implements Serializable {

    /**
     * 岗位序号
     */
    private Long id;
    /**
     * 岗位名称
     */
    private String name;
    /**
     * 岗位编码
     */
    private String code;
    /**
     * 岗位排序
     */
    private Integer sort;
    /**
     * 状态
     * <p>
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
