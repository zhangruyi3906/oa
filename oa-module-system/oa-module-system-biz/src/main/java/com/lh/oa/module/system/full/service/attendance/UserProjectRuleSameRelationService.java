package com.lh.oa.module.system.full.service.attendance;

import com.lh.oa.module.system.controller.admin.userProject.param.UserProjectRuleSameRelationParam;
import com.lh.oa.module.system.full.entity.attandance.UserProjectRuleSameRelation;

import java.util.List;
import java.util.Set;

/**
 * @author tanghanlin
 * @since 2023/12/08
 */
public interface UserProjectRuleSameRelationService {

    /**
     * 重置用户打卡考勤相似规则数据
     *
     * @param param  参数
     */
    void resetRuleSameRelation(UserProjectRuleSameRelationParam param);

    List<UserProjectRuleSameRelation> selectHireUserPage(Set<Long> userIds, String hireDate, Integer page, Integer pageSize);

    Integer selectHireUserCount(Set<Long> userIds, String hireDate);
}
