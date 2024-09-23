package com.lh.oa.module.system.full.entity.attandance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
* 用户项目规则相似关联关系表
*
* @author tanghanlin
* @since 2023-12-08
*/
@Getter
@Setter
@Accessors(chain = true)
@TableName("user_project_rule_same_relation")
public class UserProjectRuleSameRelation extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 相同规则的项目ids，逗号分隔
     */
    private String sameRuleProjectIds;

}
