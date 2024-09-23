package com.lh.oa.module.bpm.framework.flowable.core.behavior.script.impl;

import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.util.number.NumberUtils;
import com.lh.oa.module.bpm.framework.flowable.core.behavior.script.BpmTaskAssignScript;
import com.lh.oa.module.bpm.service.task.BpmProcessInstanceService;
import com.lh.oa.module.system.api.dept.DeptApi;
import com.lh.oa.module.system.api.dept.dto.DeptRespDTO;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.util.collection.SetUtils.asSet;
import static java.util.Collections.emptySet;

/**
 * @author
 */
@Slf4j
public abstract class BpmTaskAssignLeaderAbstractScript implements BpmTaskAssignScript {

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;
    @Resource
    @Lazy // 解决循环依赖
    private BpmProcessInstanceService bpmProcessInstanceService;
    @Value(value = "${lanhai.dept.id}")
    public Long lanHaiDeptId;

    protected Set<Long> calculateTaskCandidateUsers(DelegateExecution execution, int level) {
        Assert.isTrue(level > 0, "level 必须大于 0");
        ProcessInstance processInstance = bpmProcessInstanceService.getProcessInstance(execution.getProcessInstanceId());
        Long startUserId = NumberUtils.parseLong(processInstance.getStartUserId());

        DeptRespDTO dept = getLevelDept(startUserId, level);
        return (Objects.nonNull(dept) && dept.getLeaderUserId() != null) ? asSet(dept.getLeaderUserId()) : emptySet();
    }

    public Set<Long> getPartyAssigneeUser(Object party, int level) {
        if (party instanceof Integer) {
            Integer partyUserId = (Integer) party;
            DeptRespDTO dept = getLevelDept(partyUserId.longValue(), level);
            return (Objects.nonNull(dept) && dept.getLeaderUserId() != null) ? asSet(dept.getLeaderUserId()) : emptySet();
        } else if (party instanceof List) {
            List<Integer> partyUserIds = (List<Integer>) party;
            Set<Long> assigneeUserIds = new HashSet<>();
            for (Integer partyUserId : partyUserIds) {
                DeptRespDTO dept = getLevelDept(partyUserId.longValue(), level);
                if (Objects.nonNull(dept) && dept.getLeaderUserId() != null) {
                    assigneeUserIds.add(dept.getLeaderUserId());
                }
            }
            return assigneeUserIds;
        } else if (party instanceof String) {
            Set<Long> assigneeUserIds = new HashSet<>();
            String partyStr = (String) party;
            Set<Integer> partyUserIds = Arrays.stream(partyStr.split(",")).map(Integer::parseInt).collect(Collectors.toSet());
            for (Integer partyUserId : partyUserIds) {
                DeptRespDTO dept = getLevelDept(partyUserId.longValue(), level);
                if (Objects.nonNull(dept) && dept.getLeaderUserId() != null) {
                    assigneeUserIds.add(dept.getLeaderUserId());
                }
            }
            return assigneeUserIds;
        } else {
            throw new BusinessException("当事人数据格式错误，分配审批人失败");
        }
    }

    public Set<Long> getTransferDeptAssigneeUser(Object transferDept, int level) {
        if (transferDept instanceof Integer) {
            Integer transferDeptId = (Integer) transferDept;
            DeptRespDTO dept = getLevelDeptByDeptId(transferDeptId.longValue(), level);
            return (Objects.nonNull(dept) && dept.getLeaderUserId() != null) ? asSet(dept.getLeaderUserId()) : emptySet();
        } else if (transferDept instanceof List) {
            List<Integer> transferDeptIds = (List<Integer>) transferDept;
            Set<Long> assigneeUserIds = new HashSet<>();
            for (Integer transferDeptId : transferDeptIds) {
                DeptRespDTO dept = getLevelDeptByDeptId(transferDeptId.longValue(), level);
                if (Objects.nonNull(dept) && dept.getLeaderUserId() != null) {
                    assigneeUserIds.add(dept.getLeaderUserId());
                }
            }
            return assigneeUserIds;
        } else if (transferDept instanceof String) {
            Set<Long> assigneeUserIds = new HashSet<>();
            String transferDeptStr = (String) transferDept;
            Set<Integer> transferDeptIds = Arrays.stream(transferDeptStr.split(",")).map(Integer::parseInt).collect(Collectors.toSet());
            for (Integer transferDeptId : transferDeptIds) {
                DeptRespDTO dept = getLevelDeptByDeptId(transferDeptId.longValue(), level);
                if (Objects.nonNull(dept) && dept.getLeaderUserId() != null) {
                    assigneeUserIds.add(dept.getLeaderUserId());
                }
            }
            return assigneeUserIds;
        } else {
            throw new BusinessException("调动部门数据格式错误，分配审批人失败");
        }
    }

    public DeptRespDTO getLevelDept(Long userId, int level) {
        DeptRespDTO dept = null;
        for (int i = 0; i < level; i++) {
            if (dept == null) {
                dept = getUserDept(userId);
                if (dept == null) {
                    return null;
                }
            } else {
                DeptRespDTO parentDept = deptApi.getDept(dept.getParentId()).getCheckedData();
                if (parentDept == null) {
                    //顶级组织交给兰总审批
                    parentDept = deptApi.getDept(lanHaiDeptId).getCheckedData();
                }
                dept = parentDept;
            }
        }
        return dept;
    }

    public DeptRespDTO getLevelDeptByDeptId(Long deptId, int level) {
        DeptRespDTO dept = null;
        for (int i = 0; i < level; i++) {
            if (dept == null) {
                dept = deptApi.getDept(deptId).getCheckedData();
                if (dept == null) {
                    return null;
                }
            } else {
                DeptRespDTO parentDept = deptApi.getDept(dept.getParentId()).getCheckedData();
                if (parentDept == null) {
                    break;
                }
                dept = parentDept;
            }
        }
        return dept;
    }

    public DeptRespDTO getUserDept(Long userId) {
        AdminUserRespDTO user = adminUserApi.getUser(userId).getCheckedData();
        if (user.getDeptId() == null) {
            return null;
        }
        DeptRespDTO deptRespDTO = deptApi.getDept(user.getDeptId()).getCheckedData();
        if (Objects.equals(userId, deptRespDTO.getLeaderUserId())) {
            if (deptRespDTO.getParentId() != 0) {
                return deptApi.getDept(deptRespDTO.getParentId()).getCheckedData();
            }
            //顶级部门默认兰总
            return deptApi.getDept(lanHaiDeptId).getCheckedData();
        }

        return deptRespDTO;
    }

}
