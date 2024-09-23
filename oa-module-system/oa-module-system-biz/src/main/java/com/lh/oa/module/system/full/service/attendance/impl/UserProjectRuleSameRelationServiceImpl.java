package com.lh.oa.module.system.full.service.attendance.impl;

import com.google.common.base.Joiner;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.redis.constant.RedisLockKeyConstant;
import com.lh.oa.framework.redis.template.RedisLockTemplate;
import com.lh.oa.module.system.controller.admin.userProject.param.UserProjectRuleSameRelationParam;
import com.lh.oa.module.system.dal.dataobject.userProject.UserProjectDO;
import com.lh.oa.module.system.dal.mysql.userProject.UserProjectMapper;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleEntity;
import com.lh.oa.module.system.full.entity.attandance.UserProjectRuleSameRelation;
import com.lh.oa.module.system.full.mapper.attendance.SysAttendanceRuleMapper;
import com.lh.oa.module.system.full.service.attendance.UserProjectRuleSameRelationService;
import com.lh.oa.module.system.mapper.UserProjectRuleSameRelationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author tanghanlin
 * @since 2023/12/08
 */
@Slf4j
@Service
public class UserProjectRuleSameRelationServiceImpl implements UserProjectRuleSameRelationService {

    @Resource
    private UserProjectRuleSameRelationMapper userProjectRuleSameRelationMapper;

    @Resource
    private SysAttendanceRuleMapper sysAttendanceRuleMapper;

    @Resource
    private UserProjectMapper userProjectMapper;

    @Resource
    private RedisLockTemplate redisLockTemplate;

    @Transactional
    @Override
    public void resetRuleSameRelation(UserProjectRuleSameRelationParam param) {
        log.info("重置用户项目考勤规则相似关联关系, param:{}", JsonUtils.toJsonString(param));
        redisLockTemplate.tryLock(RedisLockKeyConstant.SYS_USER_PROJECT_SAME_RELATION_RESET_KEY, () -> {
            Set<Integer> userIds = param.getUserIds();
            Integer trans2UserProjectId = param.getTrans2UserProjectId();
            // 获取到需要重置的用户ids
            if (Objects.isNull(userIds)) {
                userIds = new HashSet<>();
            }
            if (Objects.nonNull(trans2UserProjectId)) {
                List<UserProjectRuleSameRelation> existUserProjectList = userProjectRuleSameRelationMapper.selectList(new LambdaQueryWrapperX<UserProjectRuleSameRelation>()
                        .likeIfPresent(UserProjectRuleSameRelation::getSameRuleProjectIds, trans2UserProjectId.toString()));
                userIds.addAll(existUserProjectList.stream().map(relation -> relation.getUserId().intValue()).collect(Collectors.toSet()));
            }
            // 清理相关用户的所有已有记录
            List<UserProjectRuleSameRelation> sameRelationList = userProjectRuleSameRelationMapper.selectList(new LambdaQueryWrapperX<UserProjectRuleSameRelation>()
                    .inIfPresent(UserProjectRuleSameRelation::getUserId, userIds));
            if (!sameRelationList.isEmpty()) {
                userProjectRuleSameRelationMapper.batchDeleteByIds(sameRelationList.stream().map(UserProjectRuleSameRelation::getId).collect(Collectors.toSet()));
            }
            // 查询出现有的考勤规则，将其按照业务意义字段去重后整合，得出有重复业务规则的规则集合
            Map<Integer, SysAttendanceRuleEntity> ruleMap = sysAttendanceRuleMapper.selectAll()
                    .stream()
                    .collect(Collectors.toMap(SysAttendanceRuleEntity::getProjectId, Function.identity()));
            // 用户和项目的关联关系按用户分组后组装
            List<UserProjectDO> allUserProjectList = userProjectMapper.selectList(new LambdaQueryWrapperX<UserProjectDO>()
                    .inIfPresent(UserProjectDO::getUserId, userIds));
            Map<Long, Set<Long>> userIdAndProjectIdsMap = allUserProjectList
                    .stream()
                    .collect(Collectors.groupingBy(
                            UserProjectDO::getUserId,
                            Collectors.mapping(UserProjectDO::getProjectId, Collectors.toSet())));

            List<UserProjectRuleSameRelation> result = new LinkedList<>();
            userIdAndProjectIdsMap.forEach((userId, userProjectIds) -> {
                List<SysAttendanceRuleEntity> ruleList = new LinkedList<>();
                userProjectIds.forEach(userProjectId -> {
                    SysAttendanceRuleEntity rule = ruleMap.get(userProjectId.intValue());
                    if (Objects.isNull(rule)) {
                        return;
                    }
                    ruleList.add(rule);
                });
                if (ruleList.isEmpty()) {
                    return;
                }
                // 将考勤规则相同的项目组合成一条数据
                Map<String, List<SysAttendanceRuleEntity>> ruleGroupByBusinessStr = ruleList
                        .stream()
                        .collect(Collectors.groupingBy(SysAttendanceRuleEntity::getRuleBusinessStr));
                ruleGroupByBusinessStr.forEach((businessStr, sameRuleList) -> {
                    UserProjectRuleSameRelation sameRelation = new UserProjectRuleSameRelation();
                    sameRelation.setUserId(userId);
                    Set<Integer> ruleIds = sameRuleList.stream().map(SysAttendanceRuleEntity::getProjectId).collect(Collectors.toSet());
                    sameRelation.setSameRuleProjectIds(Joiner.on(",").join(ruleIds));
                    sameRelation.setCreateTime(LocalDateTime.now(TimeUtils.ZONE_ID_CN));
                    sameRelation.setUpdateTime(LocalDateTime.now(TimeUtils.ZONE_ID_CN));
                    sameRelation.setCreator("1");
                    sameRelation.setUpdater("1");
                    result.add(sameRelation);
                });
            });
            userProjectRuleSameRelationMapper.insertBatch(result);
        });
    }

    @Override
    public List<UserProjectRuleSameRelation> selectHireUserPage(Set<Long> userIds, String hireDate, Integer page, Integer pageSize) {
        return userProjectRuleSameRelationMapper.selectHireUserPage(userIds, hireDate, (page - 1) * pageSize, pageSize);
    }

    @Override
    public Integer selectHireUserCount(Set<Long> userIds, String hireDate) {
        return userProjectRuleSameRelationMapper.selectHireUserCount(userIds, hireDate);
    }

}
