package com.lh.oa.module.bpm.dal.dataobject.definition;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@TableName(value = "bpm_model_ext", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmModelExtDO extends BaseDO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 版本
     */
    private String rev;
    /**
     * 模型id
     */
    private String modelId;
    /**
     * 名字
     */
    private String name;
    /**
     * 标识
     */
    private String modelKey;
    /**
     * 类型
     */
    private String type;
    /**
     * 编号
     */
    private String deploymentId;

}
