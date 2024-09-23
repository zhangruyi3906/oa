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

@TableName(value = "bpm_model_dept", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmModelDeptDO extends BaseDO {
    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 关联部门id
     */
    private Long deptId;
    /**
     * 流程模型的编号
     * 关联 Model 的 id 属性
     */
    private String modelId;
}
