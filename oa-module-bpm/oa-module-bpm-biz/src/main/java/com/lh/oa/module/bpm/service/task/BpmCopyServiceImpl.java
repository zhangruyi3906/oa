package com.lh.oa.module.bpm.service.task;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.module.bpm.controller.admin.task.vo.copy.BpmCopy;
import com.lh.oa.module.bpm.controller.admin.task.vo.copy.BpmCopyPageReqVO;
import com.lh.oa.module.bpm.controller.admin.task.vo.task.BpmTaskVo;
import com.lh.oa.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.lh.oa.module.bpm.dal.mysql.task.BpmCopyMapper;
import com.lh.oa.module.bpm.dal.mysql.task.BpmProcessInstanceExtMapper;
import com.lh.oa.module.bpm.enums.im.ImMessageBusinessType;
import com.lh.oa.module.bpm.wrapper.ImWrapper;
import com.lh.oa.module.bpm.wrapper.vo.ImCustomMsgContent;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.lh.oa.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * 流程抄送Service业务层处理
 *
 * @author KonBAI
 * @date 2022-05-19
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class BpmCopyServiceImpl implements BpmCopyService {

    private final BpmCopyMapper baseMapper;

    private final HistoryService historyService;
    @Resource
    private BpmProcessInstanceExtMapper bpmProcessInstanceExtMapper;

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ImWrapper imWrapper;

    /**
     * 查询流程抄送
     *
     * @param copyId 流程抄送主键
     * @return 流程抄送
     */
    @Override
    public BpmCopy queryById(Long copyId) {
        return baseMapper.selectById(copyId);
    }


    /**
     * 查询流程抄送列表
     *
     * @param bpmCopyPageReqVO 流程抄送
     * @return 流程抄送
     */
    @Override
    public PageResult<BpmCopy> selectPageList(BpmCopyPageReqVO bpmCopyPageReqVO) {
        bpmCopyPageReqVO.setUserId(getLoginUserId());
        LambdaQueryWrapper<BpmCopy> lqw = buildQueryPageWrapper(bpmCopyPageReqVO);
        lqw.orderByAsc(BpmCopy::getReadState);
        lqw.orderByDesc(BpmCopy::getCreateTime);
        return baseMapper.selectPage(bpmCopyPageReqVO, lqw);
    }
    private LambdaQueryWrapper<BpmCopy> buildQueryPageWrapper(BpmCopyPageReqVO bpmCopyPageReqVO) {
        LambdaQueryWrapper<BpmCopy> lqw = Wrappers.lambdaQuery();
        lqw.eq(bpmCopyPageReqVO.getUserId() != null, BpmCopy::getUserId, bpmCopyPageReqVO.getUserId());
        lqw.like(StringUtils.isNotBlank(bpmCopyPageReqVO.getProcessName()), BpmCopy::getProcessName, bpmCopyPageReqVO.getProcessName());
        lqw.like(StringUtils.isNotBlank(bpmCopyPageReqVO.getTitle()), BpmCopy::getTitle, bpmCopyPageReqVO.getTitle());
        lqw.like(StringUtils.isNotBlank(bpmCopyPageReqVO.getOriginatorName()), BpmCopy::getOriginatorName, bpmCopyPageReqVO.getOriginatorName());
        if (StringUtils.isNotBlank(bpmCopyPageReqVO.getKeyword())) {
            lqw.like(BpmCopy::getOriginatorName, bpmCopyPageReqVO.getKeyword()).or().like(BpmCopy::getProcessName, bpmCopyPageReqVO.getKeyword());
        }
        return lqw;
    }
    /**
     * 查询流程抄送列表
     *
     * @param bpmCopy 流程抄送
     * @return 流程抄送
     */
    @Override
    public List<BpmCopy> selectList(BpmCopy bpmCopy) {
        LambdaQueryWrapper<BpmCopy> lqw = buildQueryWrapper(bpmCopy);
        return baseMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<BpmCopy> buildQueryWrapper(BpmCopy bpmCopy) {
        LambdaQueryWrapper<BpmCopy> lqw = Wrappers.lambdaQuery();
        lqw.eq(bpmCopy.getUserId() != null, BpmCopy::getUserId, bpmCopy.getUserId());
        lqw.like(StringUtils.isNotBlank(bpmCopy.getProcessName()), BpmCopy::getProcessName, bpmCopy.getProcessName());
        lqw.like(StringUtils.isNotBlank(bpmCopy.getTitle()), BpmCopy::getTitle, bpmCopy.getTitle());
        lqw.like(StringUtils.isNotBlank(bpmCopy.getOriginatorName()), BpmCopy::getOriginatorName, bpmCopy.getOriginatorName());
        return lqw;
    }

    @Override
    public void makeCopy(@Valid BpmTaskVo bpmTaskVo) {
        Long loginUserId = getLoginUserId();
        log.info("抄送审批人: {}, 被抄送人： {}", loginUserId, bpmTaskVo.getCopyUserIds());

        if (StringUtils.isBlank(bpmTaskVo.getCopyUserIds())) {
            // 若抄送用户为空，则不需要处理，返回成功
            return;
        }

        if (loginUserId == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_LOGIN);
        }

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(bpmTaskVo.getProcInsId()).singleResult();

        String[] ids = bpmTaskVo.getCopyUserIds().split(",");
        List<BpmCopy> copyList = new ArrayList<>(ids.length);

        BpmProcessInstanceExtDO bpmProcessInstanceExtDO = bpmProcessInstanceExtMapper.selectByProcessInstanceId(bpmTaskVo.getProcInsId());
        String title = bpmProcessInstanceExtDO.getInstanceName();
        CommonResult<AdminUserRespDTO> user = adminUserApi.getUser(bpmProcessInstanceExtDO.getStartUserId());
        String nickname = "";
        Long originatorId = 0L;
        String originatorName = "";
        if (ObjectUtils.isNotEmpty(user.getData())) {
            nickname = user.getData().getNickname();
            originatorId = bpmProcessInstanceExtDO.getStartUserId();
            originatorName = nickname;
        }
        for (String id : ids) {
            Long userId = Long.valueOf(id);
            BpmCopy copy = new BpmCopy();
            copy.setTitle(title);
            copy.setProcessId(historicProcessInstance.getProcessDefinitionId());
            copy.setProcessName(historicProcessInstance.getProcessDefinitionName());
            copy.setDeploymentId(historicProcessInstance.getDeploymentId());
            copy.setInstanceId(bpmTaskVo.getProcInsId());
            copy.setTaskId(bpmTaskVo.getTaskId());
            copy.setUserId(userId);
            copy.setOriginatorId(originatorId);
            copy.setOriginatorName(originatorName);
            copy.setCreateTime(new Date());
            copy.setReadState(0);
            copyList.add(copy);
        }

        baseMapper.insertBatch(copyList);

        // 异步发送抄送消息
        ThreadUtil.execAsync(() -> {
            Arrays.stream(ids).forEach(id -> {
                // app暂未显示抄送菜单，暂时注释
                sendCopyTaskImMessage(historicProcessInstance, id);
            });
        });
    }

    @Override
    public void readCopy(Long copyId) {
        BpmCopy bpmCopy = baseMapper.selectOne(new LambdaQueryWrapper<BpmCopy>().eq(BpmCopy::getCopyId, copyId));
        if (ObjectUtils.isEmpty(bpmCopy)) {
            throw new BusinessException("抄送消息不存在");
        }
        bpmCopy.setReadState(1);
        baseMapper.updateById(bpmCopy);
    }

    @Override
    public Integer unreadCount(Long userId) {
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<BpmCopy>()
                .eq(BpmCopy::getUserId, userId)
                .eq(BpmCopy::getReadState, 0));
        return Math.toIntExact(count);
    }

    private void sendCopyTaskImMessage(HistoricProcessInstance historicProcessInstance, String id) {
        String startUserId = historicProcessInstance.getStartUserId();
        CommonResult<String> sysUserAccount = adminUserApi.getSysUserAccount(id);
        AdminUserRespDTO startUser = adminUserApi.getUser(Long.parseLong(startUserId)).getData();
        String content = historicProcessInstance.getName() + "-" + startUser.getNickname() + "-" + TimeUtils.formatAsDate(historicProcessInstance.getStartTime());
        String msg = String.format("您有一条新的抄送消息【%s】，请及时处理！", content);

        ImCustomMsgContent msgContent = new ImCustomMsgContent();
        msgContent.setText(msg);
        msgContent.setBusinessType(ImMessageBusinessType.BPM_TASK_COPY.getBusinessType());

        JSONObject params = new JSONObject();
        params.put("oaUserId", id);
        params.put("processInstanceId", historicProcessInstance.getId());
        params.put("processDefinitionId", historicProcessInstance.getProcessDefinitionId());
        msgContent.setParams(JsonUtils.toJsonString(params));

        imWrapper.sendAccountCustomMessage(sysUserAccount.getData(), msgContent);
    }
}
