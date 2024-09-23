package com.lh.oa.module.system.dal.dataobject.errorcode;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.lh.oa.module.system.enums.errorcode.ErrorCodeTypeEnum;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 错误码表
 *
 * @author
 */
@TableName(value = "system_error_code")
//("system_error_code_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErrorCodeDO extends BaseDO {

    /**
     * 错误码编号，自增
     */
    @TableId
    private Long id;
    /**
     * 错误码类型
     *
     * 枚举 {@link ErrorCodeTypeEnum}
     */
    private Integer type;
    /**
     * 应用名
     */
    private String applicationName;
    /**
     * 错误码编码
     */
    private Integer code;
    /**
     * 错误码错误提示
     */
    private String message;
    /**
     * 错误码备注
     */
    private String memo;

}
