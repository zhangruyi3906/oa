package com.lh.oa.module.bpm.dal.dataobject.definition;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@TableName(value = "bpm_form_field_export", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmFormFieldExportDO extends BaseDO {
    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 关联表单id
     */
    private Long formId;
    /**
     * 字段
     */
    @NotNull(message = "字段不能为空")
    private String field;
    /**
     * 字段名
     */
    @NotNull(message = "字段名不能为空")
    private String fieldName;
}
