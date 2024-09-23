package com.lh.oa.module.bpm.dal.dataobject.task;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@TableName(value = "bpm_save_instance", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmSaveInstanceDO extends BaseDO {
    /**
     *主键id
     */
    private Long saveId;
    /**
     *用户id
     */
    private Long userId;
    /**
     *模型id
     */
    private String modelId;
    /**
     *名字
     */
    private String name;
    /**
     *类型
     */
    private String type;
    /**
     *表单的配置
     */
    private String formConf;
    /**
     *表单值
     */
    private String formVariables;
}
