package com.lh.oa.module.system.service.information;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.information.vo.InformationCreateReqVO;
import com.lh.oa.module.system.controller.admin.information.vo.InformationExportReqVO;
import com.lh.oa.module.system.controller.admin.information.vo.InformationPageReqVO;
import com.lh.oa.module.system.controller.admin.information.vo.InformationUpdateReqVO;
import com.lh.oa.module.system.controller.admin.salarysettlement.vo.MonthAttendance;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserPageItemRespVO;
import com.lh.oa.module.system.convert.information.InformationConvert;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.dataobject.userProject.UserProjectDO;
import com.lh.oa.module.system.dal.mysql.information.InformationMapper;
import com.lh.oa.module.system.dal.mysql.userProject.UserProjectMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.service.dept.DeptService;
import com.lh.oa.module.system.service.user.AdminUserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 员工信息 Service 实现类
 *
 * @author
 */
@Service
@Validated
public class InformationServiceImpl implements InformationService {

    @Resource
    private InformationMapper informationMapper;

    @Resource
    private UserProjectMapper userProjectMapper;

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private DeptService deptService;

    @Override
    public Long createInformation(InformationCreateReqVO createReqVO) {
        // 插入
        InformationDO information = InformationConvert.INSTANCE.convert(createReqVO);
        Long i = informationMapper.selectCount(new LambdaQueryWrapperX<InformationDO>().eqIfPresent(InformationDO::getUserId, createReqVO.getUserId()));
        if (i != 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.INFORMATION_IS_EXISTS);
        }
        informationMapper.insert(information);
        // 返回
        return information.getId();
    }

    @Override
    public void updateInformation(InformationUpdateReqVO updateReqVO) {
        // 校验存在
        validateInformationExists(updateReqVO.getId());
        // 更新
        InformationDO updateObj = InformationConvert.INSTANCE.convert(updateReqVO);
        informationMapper.updateById(updateObj);
    }

    @Override
    public void deleteInformation(Long id) {
        // 校验存在
        validateInformationExists(id);
        // 删除
        informationMapper.deleteById(id);
    }

    private void validateInformationExists(Long id) {
        if (informationMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.INFORMATION_NOT_EXISTS);
        }
    }

    @Override
    public InformationDO getInformation(Long id) {
        InformationDO informationDO = informationMapper.selectById(id);
        AdminUserDO user = adminUserService.getUser(informationDO.getUserId());
        informationDO.setAdminUserDo(user);
        UserPageItemRespVO.Dept dept = new UserPageItemRespVO.Dept();
        if (user.getDeptId() != null && "".equals(user.getDeptId())) {
            DeptDO deptDO = deptService.getDept(user.getDeptId());
            dept.setId(deptDO.getId()).setName(deptDO.getName());
            informationDO.setDeptId(deptDO.getId());
            informationDO.setDeptName(deptDO.getName());
        }
        return informationDO.setDept(dept);
    }

    @Override
    public InformationDO getInformationByUserId(Long userId) {
        QueryWrapper<InformationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        InformationDO informationDO = informationMapper.selectOne(queryWrapper);
        if (informationDO != null) {
            AdminUserDO user = adminUserService.getUser(informationDO.getUserId());
            informationDO.setAdminUserDo(user);
            UserPageItemRespVO.Dept dept = new UserPageItemRespVO.Dept();
            if (user.getDeptId() != null && "".equals(user.getDeptId())) {
                DeptDO deptDO = deptService.getDept(user.getDeptId());
                dept.setId(deptDO.getId()).setName(deptDO.getName());
                informationDO.setDeptId(deptDO.getId());
                informationDO.setDeptName(deptDO.getName());
            }
            informationDO.setDept(dept);
            return informationDO.setDept(dept);
        }
        return null;
    }

    @Override
    public List<InformationDO> getInformationList(Collection<Long> ids) {
        List<InformationDO> informationDOS = informationMapper.selectBatchIds(ids);
        return getInformationDOS(informationDOS);
    }

    @Override
    public PageResult<InformationDO> getInformationPage(InformationPageReqVO pageReqVO) {
        PageResult<InformationDO> informationDOPageResult = informationMapper.selectPage(pageReqVO);
        if (informationDOPageResult == null) {
            return null;
        }
        List<InformationDO> list = informationDOPageResult.getList();
        if (list == null) {
            return null;
        }
        List<InformationDO> informationDOS = getInformationDOS(list);
        return informationDOPageResult.setList(informationDOS);
    }

    @Override
    public List<UserProjectDO> getInforProject(InformationPageReqVO pageReqVO) {
        PageResult<InformationDO> page = informationMapper.selectPage(pageReqVO);
        List<UserProjectDO> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(page.getList())) {
            list = userProjectMapper.selectList(new LambdaQueryWrapperX<UserProjectDO>()
                    .eq(UserProjectDO::getUserId, pageReqVO.getUserId())
                    .eq(UserProjectDO::getStatus, 1));
        }
        return list;
    }

    @Override
    public List<InformationDO> getInformationList(InformationExportReqVO exportReqVO) {
        List<InformationDO> informationDOS = informationMapper.selectList(exportReqVO);
        return getInformationDOS(informationDOS);
    }

    @Override
    public List<InformationDO> selectListInDeptIdsOrUserIds(MonthAttendance createReqVO) {
        List<InformationDO> informationDOS = informationMapper.selectListInDeptIdsOrUserIds(createReqVO);
        return getInformationDOS(informationDOS);
    }

    @Override
    public List<InformationDO> selectListByUserIds(Set<Long> userIds) {
        return informationMapper.selectList(new LambdaQueryWrapperX<InformationDO>()
                .eq(InformationDO::getDeleted, 0)
                .in(InformationDO::getUserId, userIds));
    }

    @NotNull
    private List<InformationDO> getInformationDOS(List<InformationDO> informationDOS) {
        for (InformationDO informationDO : informationDOS) {
            AdminUserDO user = adminUserService.getUser(informationDO.getUserId());
            if (user != null) {
                informationDO.setAdminUserDo(user);
                UserPageItemRespVO.Dept dept = new UserPageItemRespVO.Dept();
                if (user.getDeptId() != null && !"".equals(user.getDeptId())) {
                    DeptDO deptDO = deptService.getDept(user.getDeptId());
                    if(deptDO != null) {
                        dept.setId(deptDO.getId()).setName(deptDO.getName());
                        informationDO.setDeptId(deptDO.getId());
                        informationDO.setDeptName(deptDO.getName());
                    }
                }
                informationDO.setDept(dept);
            }
        }
        return informationDOS;
    }

}
