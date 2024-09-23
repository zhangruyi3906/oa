package com.lh.oa.module.system.controller.admin.userProject.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * @author tanghanlin
 * @since 2023/12/12
 */
@Getter
@Setter
@ToString
public class UserProjectRuleSameRelationParam implements Serializable {

    /**
     * 用户ids
     */
    private Set<Integer> userIds;

    /**
     * 项目id，用来转化
     */
    private Integer trans2UserProjectId;

    public UserProjectRuleSameRelationParam() {
    }

    public UserProjectRuleSameRelationParam(Set<Integer> userIds, Integer trans2UserProjectId) {
        this.userIds = userIds;
        this.trans2UserProjectId = trans2UserProjectId;
    }
}
