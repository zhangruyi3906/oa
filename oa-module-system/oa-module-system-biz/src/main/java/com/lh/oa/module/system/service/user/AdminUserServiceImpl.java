package com.lh.oa.module.system.service.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.collection.CollectionUtils;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.framework.datapermission.core.util.DataPermissionUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.infra.api.file.FileApi;
import com.lh.oa.module.system.controller.admin.dept.DeptController;
import com.lh.oa.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import com.lh.oa.module.system.controller.admin.dept.vo.dept.DeptRespVO;
import com.lh.oa.module.system.controller.admin.dict.vo.data.DictDataBaseVO;
import com.lh.oa.module.system.controller.admin.dict.vo.data.DictDataBatchVo;
import com.lh.oa.module.system.controller.admin.file.vo.file.FileUploadReqVO;
import com.lh.oa.module.system.controller.admin.information.vo.InformationCreateReqVO;
import com.lh.oa.module.system.controller.admin.information.vo.InformationUpdateReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.profile.UserProfileUpdatePasswordReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UpdateUserAndInformation;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserAndInformation;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserCreateReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserExcelVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserExportReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserImportExcelVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserPageReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserPressVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserSimpleRespVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserUpdateReqVO;
import com.lh.oa.module.system.controller.admin.userProject.vo.UserProjectUserVO;
import com.lh.oa.module.system.convert.user.UserConvert;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.dept.PostDO;
import com.lh.oa.module.system.dal.dataobject.dept.UserPostDO;
import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import com.lh.oa.module.system.dal.dataobject.permission.RoleDO;
import com.lh.oa.module.system.dal.dataobject.permission.UserRoleDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.dataobject.worklog.WorkLogDO;
import com.lh.oa.module.system.dal.mysql.dept.DeptMapper;
import com.lh.oa.module.system.dal.mysql.dept.PostMapper;
import com.lh.oa.module.system.dal.mysql.dept.UserPostMapper;
import com.lh.oa.module.system.dal.mysql.information.InformationMapper;
import com.lh.oa.module.system.dal.mysql.permission.UserRoleMapper;
import com.lh.oa.module.system.dal.mysql.user.AdminUserMapper;
import com.lh.oa.module.system.dal.mysql.worklog.WorkLogMapper;
import com.lh.oa.module.system.enums.DeptTypeEnum;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.enums.common.SexEnum;
import com.lh.oa.module.system.enums.file.FileEnum;
import com.lh.oa.module.system.full.entity.jnt.JntUser;
import com.lh.oa.module.system.full.entity.postsale.PostSaleUser;
import com.lh.oa.module.system.full.enums.jnt.EmployTypeEnum;
import com.lh.oa.module.system.full.enums.jnt.GenderEnum;
import com.lh.oa.module.system.full.enums.jnt.OperateTypeEnum;
import com.lh.oa.module.system.full.enums.jnt.ProjectSourceEnum;
import com.lh.oa.module.system.full.enums.jnt.SourceEnum;
import com.lh.oa.module.system.full.service.jnt.JntBaseDataSyncService;
import com.lh.oa.module.system.full.service.postsale.PostSaleBaseDataSyncService;
import com.lh.oa.module.system.service.dept.DeptService;
import com.lh.oa.module.system.service.dept.PostService;
import com.lh.oa.module.system.service.dict.DictDataService;
import com.lh.oa.module.system.service.file.FileService;
import com.lh.oa.module.system.service.information.InformationService;
import com.lh.oa.module.system.service.permission.PermissionService;
import com.lh.oa.module.system.service.permission.RoleService;
import com.lh.oa.module.system.service.tenant.TenantService;
import com.lh.oa.module.system.service.userProject.UserProjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.Collator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertList;
import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertSet;
import static com.lh.oa.module.system.full.enums.FlagStateEnum.DISABLED;
import static com.lh.oa.module.system.full.enums.FlagStateEnum.ENABLED;
import static com.lh.oa.module.system.full.enums.jnt.ProjectSourceEnum.PMS;

/**
 * 后台用户 Service 实现类
 *
 * @author
 */
@Service("adminUserService")
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

    @Value("${sys.user.init-password:123456}")
    private String userInitPassword;

    @Resource
    private AdminUserMapper userMapper;

    @Resource
    private InformationService informationService;

    @Resource
    private DeptService deptService;
    @Resource
    private PostService postService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    @Lazy
    private TenantService tenantService;

    @Resource
    private UserPostMapper userPostMapper;

    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private FileApi fileApi;

    @Resource
    private FileService fileService;

    @Resource
    private JntBaseDataSyncService jntBaseDataSyncService;

    @Resource
    private InformationMapper informationMapper;

    @Resource
    private RoleService roleService;

    @Resource
    private WorkLogMapper workLogMapper;
    @Resource
    private PostSaleBaseDataSyncService postSaleBaseDataSyncService;

    @Resource
    private DictDataService dictDataService;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private PostMapper postMapper;

    @Autowired
    private UserProjectService userProjectService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserCreateReqVO reqVO) {
        validateUserForCreateOrUpdate(null, reqVO.getUsername(), reqVO.getMobile(), reqVO.getEmail(),
                reqVO.getDeptId(), reqVO.getPostIds());
        AdminUserDO user = UserConvert.INSTANCE.convert(reqVO);
        user.setStatus(CommonStatusEnum.ENABLE.getStatus());
        user.setPassword(encodePassword(reqVO.getPassword()));
        userMapper.insert(user);
        if (CollUtil.isNotEmpty(user.getPostIds())) {
            userPostMapper.insertBatch(convertList(user.getPostIds(),
                    postId -> new UserPostDO().setUserId(user.getId()).setPostId(postId)));
        }
        permissionService.assignUserRole(user.getId(), reqVO.getUserRoleIdList());

        //同步建能通
        syncJntUser(OperateTypeEnum.ADD, user);
        //同步售后服务平台
        try {
            syncPostSaleUser(OperateTypeEnum.ADD, user);
        } catch (Exception e) {
            log.error("同步售后用户信息失败");
        }
        return user.getId();
    }


    public void handSyncUser(Long userId) {
        log.info("手动同步用户, userId:{}", userId);
        AdminUserDO user = userMapper.selectById(userId);
        if (user == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_EXISTS);
        }
        //同步售后服务平台
        try {
            syncPostSaleUser(OperateTypeEnum.ADD, user);
        } catch (Exception e) {
            log.error("同步售后用户信息失败");
        }
        syncJntUser(OperateTypeEnum.ADD, user);
    }

    private void syncJntUser(OperateTypeEnum operateTypeEnum, AdminUserDO user) {
        try {
            JntUser jntUser = new JntUser();
            jntUser.setOperateType(operateTypeEnum);
            DeptDO deptDO = deptService.getDept(user.getDeptId());
            jntUser.setOaUserId(Math.toIntExact(user.getId()));
            if (deptDO == null)
                throw new BusinessException("所选部门有误");
            if (deptDO.getType() == DeptTypeEnum.ORGANIZATION) {
                jntUser.setOaOrgId(Math.toIntExact(deptDO.getId()));
                jntUser.setOaDeptId(null);
            } else {
                DeptDO parentOrgOfCompany = deptService.getParentOrgOfCompany(deptDO.getParentId());
                jntUser.setOaOrgId(Math.toIntExact(parentOrgOfCompany.getId()));
                jntUser.setOaDeptId(Math.toIntExact(deptDO.getId()));
            }
            if (user.getPostIds() != null && user.getPostIds().size() > 0) {
                jntUser.setOaPostId(Math.toIntExact(user.getPostIds().iterator().next()));
            }
            jntUser.setAccount(user.getUsername());
            jntUser.setName(user.getNickname());
            jntUser.setMobile(user.getMobile());
            jntUser.setGender(user.getSex() == null ? GenderEnum.UNKNOWN : (user.getSex() == 0 ? GenderEnum.MAN : GenderEnum.WOMAN));


            if (Objects.equals(CommonStatusEnum.ENABLE.getStatus(), user.getStatus())) {
                jntUser.setState(ENABLED);
            } else {
                jntUser.setState(DISABLED);
            }

            InformationDO information = informationService.getInformationByUserId(user.getId());
            if (Objects.nonNull(information) && Objects.equals(2, information.getInfoType())) {
                jntUser.setEmployType(EmployTypeEnum.OUTER_EMPLOY);
            } else {
                jntUser.setEmployType(EmployTypeEnum.INNER_EMPLOY);
            }
            jntUser.setIdentityCard(information.getIdentityCard());

            jntUser.setSource(SourceEnum.OA);
            jntBaseDataSyncService.syncUser(jntUser);
        } catch (Exception e) {
            throw new BusinessException("[JNT]操作失败，" + e.getLocalizedMessage());
        }
    }

    private void syncPostSaleUser(OperateTypeEnum operateTypeEnum, AdminUserDO user) {
        log.info("同步用户信息至售后服务平台，操作类型：{}，账号：{}", operateTypeEnum == null ? null : operateTypeEnum.getVal(), user.getUsername());
        PostSaleUser postSaleUser = new PostSaleUser();
        postSaleUser.setOperateType(operateTypeEnum);
        DeptDO deptDO = deptService.getDept(user.getDeptId());
        postSaleUser.setOaUserId(Math.toIntExact(user.getId()));
        if (deptDO == null)
            throw new BusinessException("所选部门有误");
        postSaleUser.setUsername(user.getUsername());
        postSaleUser.setName(user.getNickname());
        postSaleUser.setPhone(user.getMobile());
        postSaleUser.setEmail(StringUtils.isEmpty(user.getEmail()) ? "123@qq.com" : user.getEmail());
        if (Objects.equals(CommonStatusEnum.ENABLE.getStatus(), user.getStatus())) {
            postSaleUser.setFlag(ENABLED.value());
        } else {
            postSaleUser.setFlag(DISABLED.value());
        }
        try {
            postSaleBaseDataSyncService.syncUser(postSaleUser);
        } catch (Exception e) {
            throw new BusinessException("[POST_SALE]操作失败，" + e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateReqVO reqVO) {
        validateUserForCreateOrUpdate(reqVO.getId(), reqVO.getUsername(), reqVO.getMobile(), reqVO.getEmail(),
                reqVO.getDeptId(), reqVO.getPostIds());
        AdminUserDO user = UserConvert.INSTANCE.convert(reqVO);
        permissionService.assignUserRole(reqVO.getId(), reqVO.getUserRoleIdList());
        userMapper.updateById(user);
        updateUserPost(reqVO, user);

        //同步建能通
        syncJntUser(OperateTypeEnum.UPDATE, user);
    }

    private void updateUserPost(UserUpdateReqVO reqVO, AdminUserDO updateObj) {
        Long userId = reqVO.getId();
        Set<Long> dbPostIds = convertSet(userPostMapper.selectListByUserId(userId), UserPostDO::getPostId);
        Set<Long> postIds = updateObj.getPostIds();
        Collection<Long> createPostIds = CollUtil.subtract(postIds, dbPostIds);
        Collection<Long> deletePostIds = CollUtil.subtract(dbPostIds, postIds);
        if (!CollUtil.isEmpty(createPostIds)) {
            userPostMapper.insertBatch(convertList(createPostIds,
                    postId -> new UserPostDO().setUserId(userId).setPostId(postId)));
        }
        if (!CollUtil.isEmpty(deletePostIds)) {
            userPostMapper.deleteByUserIdAndPostId(userId, deletePostIds);
        }
    }

    @Override
    public void updateUserLogin(Long id, String loginIp) {
        userMapper.updateById(new AdminUserDO().setId(id).setLoginIp(loginIp).setLoginDate(LocalDateTime.now()));
    }

    @Override
    public void updateUserProfile(Long id, UserProfileUpdateReqVO reqVO) {
        validateUserExists(id);
        validateEmailUnique(id, reqVO.getEmail());
        validateMobileUnique(id, reqVO.getMobile());
        userMapper.updateById(UserConvert.INSTANCE.convert(reqVO).setId(id));
    }

    @Override
    public void updateUserPassword(Long id, UserProfileUpdatePasswordReqVO reqVO) {
        validateOldPassword(id, reqVO.getOldPassword());
        AdminUserDO updateObj = new AdminUserDO().setId(id);
        updateObj.setPassword(encodePassword(reqVO.getNewPassword()));
        userMapper.updateById(updateObj);
    }

    @Override
    public String updateUserAvatar(Long id, MultipartFile file) throws Exception {
        validateUserExists(id);
//        String avatar = fileApi.createFile(IoUtil.readBytes(avatarFile));
        fileService.createFile(file.getOriginalFilename(), null, IoUtil.readBytes(file.getInputStream()), new FileUploadReqVO().setFile(file).setSource(FileEnum.COMMON.getSource()));
        AdminUserDO sysUserDO = new AdminUserDO();
        sysUserDO.setId(id);
        sysUserDO.setAvatar(file.getOriginalFilename());
        userMapper.updateById(sysUserDO);
        return file.getOriginalFilename();
    }

    @Override
    public void updateUserPassword(Long id, String password) {
        validateUserExists(id);
        AdminUserDO updateObj = new AdminUserDO();
        updateObj.setId(id);
        updateObj.setPassword(encodePassword(password));
        userMapper.updateById(updateObj);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(Long id, Integer status, ProjectSourceEnum sourceEnum) {
        validateUserExists(id);
        AdminUserDO updateObj = new AdminUserDO();
        updateObj.setId(id);
        updateObj.setStatus(status);
        userMapper.updateById(updateObj);
        if (PMS == sourceEnum) {
            return;
        }
        syncUserState(id, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id, Boolean notSyncJnt) {
        validateUserExists(id);
        userMapper.deleteById(id);
        permissionService.processUserDeleted(id);
        userPostMapper.deleteByUserId(id);
        InformationDO information = informationMapper.getByUserId(id);
        if (Objects.nonNull(information)) {
            informationService.deleteInformation(information.getId());
        }

        if (notSyncJnt) {
            return;
        }
        //同步建能通
        try {
            JntUser jntUser = new JntUser();
            jntUser.setIds(id + "");
            jntUser.setOperateType(OperateTypeEnum.DELETE);
            jntBaseDataSyncService.syncUser(jntUser);
        } catch (Exception e) {
            throw new BusinessException("[JNT]操作失败，" + e.getLocalizedMessage());
        }
    }

    @Override
    public AdminUserDO getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public AdminUserDO getUserByMobile(String mobile) {
        return userMapper.selectByMobile(mobile);
    }

    @Override
    public PageResult<AdminUserDO> getUserPage(UserPageReqVO reqVO) {
        if (reqVO.getInfoType() == null) {
            return userMapper.selectPage(reqVO, getDeptCondition(reqVO.getDeptId()));
        } else {
            List<InformationDO> informationDOS = informationMapper.selectList(new LambdaQueryWrapperX<InformationDO>().eq(InformationDO::getInfoType, reqVO.getInfoType()));
            if (org.springframework.util.CollectionUtils.isEmpty(informationDOS)) {
                return new PageResult<>();
            }
            Set<Long> userIds = informationDOS.stream().map(InformationDO::getUserId).collect(Collectors.toSet());
            return userMapper.selectPageWithUserInfo(reqVO, getDeptCondition(reqVO.getDeptId()), userIds);
        }
    }

    @Override
    public AdminUserDO getUser(Long id) {
        AdminUserDO adminUserDO = userMapper.selectById(id);
        InformationDO informationDO = informationMapper.getByUserId(id);
        if (Objects.nonNull(informationDO)) {
            log.info("获取用户信息, adminUserDO:{}, informationDO：{}", adminUserDO, informationDO);
            adminUserDO.setBankAccount(ObjectUtils.isNotEmpty(informationDO.getBankAccount()) ? informationDO.getBankAccount() : "无");
            adminUserDO.setBankAccountNumber(ObjectUtils.isNotEmpty(informationDO.getBankAccountNumber()) ? informationDO.getBankAccountNumber() : "无");
        }
        return adminUserDO;
    }

    @Override
    public List<AdminUserDO> getUserListByDeptIds(Collection<Long> deptIds) {
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        return userMapper.selectListByDeptIds(deptIds);
    }

    @Override
    public List<AdminUserDO> getUserListByPostIds(Collection<Long> postIds) {
        if (CollUtil.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        Set<Long> userIds = convertSet(userPostMapper.selectListByPostIds(postIds), UserPostDO::getUserId);
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return userMapper.selectBatchIds(userIds);
    }

    @Override
    public List<AdminUserDO> getUserList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return userMapper.selectBatchIds(ids);
    }

    @Override
    public void validateUserList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        List<AdminUserDO> users = userMapper.selectBatchIds(ids);
        Map<Long, AdminUserDO> userMap = CollectionUtils.convertMap(users, AdminUserDO::getId);
        ids.forEach(id -> {
            AdminUserDO user = userMap.get(id);
            if (user == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(user.getStatus())) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_IS_DISABLE, user.getNickname());
            }
        });
    }

    @Override
    public List<AdminUserDO> getUserList(UserExportReqVO reqVO) {
        if (reqVO.getInfoType() == null) {
            return userMapper.selectList(reqVO, getDeptCondition(reqVO.getDeptId()));
        } else {
            List<InformationDO> informationDOS = informationMapper.selectList(new LambdaQueryWrapperX<InformationDO>().eq(InformationDO::getInfoType, reqVO.getInfoType()));
            if (org.springframework.util.CollectionUtils.isEmpty(informationDOS)) {
                return new ArrayList<>();
            }
            Set<Long> userIds = informationDOS.stream().map(InformationDO::getUserId).collect(Collectors.toSet());
            return userMapper.selectListWithUserInfo(reqVO, getDeptCondition(reqVO.getDeptId()), userIds);
        }
    }

    @Override
    public List<AdminUserDO> getUserListByNickname(String nickname) {
        return userMapper.selectListByNickname(nickname);
    }

    /**
     * 获得部门条件：查询指定部门的子部门编号们，包括自身
     *
     * @param deptId 部门编号
     * @return 部门编号集合
     */
    private Set<Long> getDeptCondition(Long deptId) {
        if (deptId == null) {
            return Collections.emptySet();
        }
        Set<Long> deptIds = convertSet(deptService.getDeptListByParentIdFromCache(
                deptId, true), DeptDO::getId);
        deptIds.add(deptId); // 包括自身
        return deptIds;
    }

    private void validateUserForCreateOrUpdate(Long id, String username, String mobile, String email,
                                               Long deptId, Set<Long> postIds) {
        DataPermissionUtils.executeIgnore(() -> {
            validateUserExists(id);
            validateUsernameUnique(id, username);
            validateMobileUnique(id, mobile);
            validateEmailUnique(id, email);
            deptService.validateDeptList(CollectionUtils.singleton(deptId));
            postService.validatePostList(postIds);
        });
    }

    private void validateUserForCreateOrUpdateByDeptName(Long id, String username, String mobile, String email,
                                                         String deptName, Set<Long> postIds) {
        DataPermissionUtils.executeIgnore(() -> {
            validateUserExists(id);
            validateUsernameUnique(id, username);
            validateMobileUnique(id, mobile);
            validateEmailUnique(id, email);
            deptService.validateDeptListByDeptName(deptName);
            postService.validatePostList(postIds);
        });
    }

    @VisibleForTesting
    void validateUserExists(Long id) {
        if (id == null) {
            return;
        }
        AdminUserDO user = userMapper.selectById(id);
        if (user == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_EXISTS);
        }
    }

    @VisibleForTesting
    void validateUsernameUnique(Long id, String username) {
        if (CharSequenceUtil.isBlank(username)) {
            return;
        }
        AdminUserDO user = userMapper.selectByUsername(username);
        if (user == null) {
            return;
        }
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_USERNAME_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_USERNAME_EXISTS);
        }
    }

    @VisibleForTesting
    void validateEmailUnique(Long id, String email) {
        if (CharSequenceUtil.isBlank(email)) {
            return;
        }
        AdminUserDO user = userMapper.selectByEmail(email);
        if (user == null) {
            return;
        }
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_EMAIL_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_EMAIL_EXISTS);
        }
    }

    @VisibleForTesting
    void validateMobileUnique(Long id, String mobile) {
        if (CharSequenceUtil.isBlank(mobile)) {
            return;
        }
        AdminUserDO user = userMapper.selectByMobile(mobile);
        if (user == null) {
            return;
        }
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_MOBILE_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_MOBILE_EXISTS);
        }
    }

    /**
     * 校验旧密码
     *
     * @param id          用户 id
     * @param oldPassword 旧密码
     */
    @VisibleForTesting
    void validateOldPassword(Long id, String oldPassword) {
        AdminUserDO user = userMapper.selectById(id);
        if (user == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_EXISTS);
        }
        if (!isPasswordMatch(oldPassword, user.getPassword())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_PASSWORD_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserImportExcelVO> importUserList(List<UserImportExcelVO> importUsers, boolean isUpdateSupport) {
        log.info("导入用户数据，importUsers:{}，isUpdateSupport:{}", JsonUtils.toJsonString(importUsers), isUpdateSupport);
        if (CollUtil.isEmpty(importUsers)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_IMPORT_LIST_IS_EMPTY);
        }
        importUsers = importUsers.stream().filter(UserImportExcelVO::isNotEmpty).collect(Collectors.toList());
        if (CollUtil.isEmpty(importUsers)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_IMPORT_LIST_IS_EMPTY);
        }

        // 不同主部门下的部门可以重复，所以需要以parentId区别
        List<DeptDO> parentDeptList = deptService.getAllParentDeptList();
        Map<String, Map<String, Long>> deptParentMap = new HashMap<>();
        parentDeptList.forEach(parentDept -> {
            List<DeptDO> childDeptList = deptService.getDeptListByParentIdFromCache(parentDept.getId(), true);
            Map<String, Long> childDeptMap = childDeptList.stream().collect(Collectors.toMap(DeptDO::getName, DeptDO::getId));
            deptParentMap.put(parentDept.getName(), childDeptMap);
        });

        List<PostDO> postList = postService.getPostByPostNames(importUsers
                .stream()
                .filter(vo -> org.apache.commons.lang3.StringUtils.isNotBlank(vo.getPostName()))
                .map(vo -> Arrays.asList(vo.getPostName().split("，")))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()));
        List<RoleDO> roleList = roleService.selectByRoleNames(importUsers
                .stream()
                .filter(vo -> org.apache.commons.lang3.StringUtils.isNotBlank(vo.getRoleName()))
                .map(vo -> Arrays.asList(vo.getRoleName().split("，")))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()));
        List<AdminUserDO> existNameList = userMapper.selectByUsernames(importUsers.stream().map(UserImportExcelVO::getUsername).collect(Collectors.toSet()));
        List<AdminUserDO> existMobileList = userMapper.selectByMobiles(importUsers.stream().map(UserImportExcelVO::getMobile).collect(Collectors.toSet()));
        List<AdminUserDO> existEmailList = userMapper.selectByMobiles(importUsers.stream().map(UserImportExcelVO::getEmail).collect(Collectors.toSet()));
        Map<String, Long> postMap = postList.stream().collect(Collectors.toMap(PostDO::getName, PostDO::getId));
        Map<String, Long> roleMap = roleList.stream().collect(Collectors.toMap(RoleDO::getName, RoleDO::getId));
        Set<String> existUserNames = existNameList.stream().map(AdminUserDO::getUsername).collect(Collectors.toSet());
        Set<String> existsMobiles = existMobileList.stream().map(AdminUserDO::getMobile).collect(Collectors.toSet());
        Set<String> existsEmails = existEmailList.stream().map(AdminUserDO::getEmail).collect(Collectors.toSet());
        List<DictDataBatchVo> dictDataList = dictDataService.batchQueryDictData("nation");
        Map<String, Map<String, String>> dictTypeMap = dictDataList
                .stream()
                .collect(Collectors.toMap(
                        DictDataBatchVo::getDictType,
                        vo -> vo.getDataVoList()
                                .stream()
                                .collect(Collectors.toMap(DictDataBaseVO::getLabel, DictDataBaseVO::getValue))));
        Map<String, String> nationMap = dictTypeMap.getOrDefault("nation", Collections.emptyMap());

        List<UserImportExcelVO> errList = new LinkedList<>();
        for (UserImportExcelVO importUser : importUsers) {
            // 避免数据行为空的情况
            if (StringUtils.isBlank(importUser.getUsername()) || StringUtils.isBlank(importUser.getNickname())
                    || StringUtils.isBlank(importUser.getLevel()) || StringUtils.isBlank(importUser.getDeptName())
                    || StringUtils.isBlank(importUser.getMobile()) || StringUtils.isBlank(importUser.getPostName())
                    || StringUtils.isBlank(importUser.getSexStr()) || StringUtils.isBlank(importUser.getRoleName())
                    || Objects.isNull(importUser.getHireDateStr()) || StringUtils.isBlank(importUser.getInfoTypeStr())
                    || StringUtils.isBlank(importUser.getParentDeptName())) {
                importUser.setMsg("缺少必填信息");
                errList.add(importUser);
                continue;
            }
            StringBuilder errorMessageBuilder = new StringBuilder();
            UserAndInformation userAndInformation = JsonUtils.covertObject(importUser, UserAndInformation.class);
            // 账号名判重
            if (existUserNames.contains(importUser.getUsername())) {
                errorMessageBuilder.append("账号：").append(importUser.getUsername()).append(" 已存在;");
            }
            // 手机号判重
            if (existsMobiles.contains(importUser.getMobile())) {
                errorMessageBuilder.append("手机号：").append(importUser.getMobile()).append(" 已存在;");
            }
            // 邮箱判重
            if (StringUtils.isNotBlank(importUser.getEmail()) && existsEmails.contains(importUser.getEmail())) {
                errorMessageBuilder.append("邮箱：").append(importUser.getMobile()).append(" 已存在;");
            }

            // 翻译部门信息
            Map<String, Long> childDeptMap = deptParentMap.get(importUser.getParentDeptName());
            if (!org.springframework.util.CollectionUtils.isEmpty(childDeptMap)) {
                Long deptId = childDeptMap.get(importUser.getDeptName());
                if (Objects.isNull(deptId)) {
                    errorMessageBuilder.append("部门：").append(importUser.getDeptName()).append(" 不存在;");
                } else {
                    userAndInformation.setDeptId(deptId);
                }
            } else {
                errorMessageBuilder.append("一级部门：").append(importUser.getParentDeptName()).append(" 不存在;");
            }

            // 翻译岗位信息
            Set<Long> postIds = new HashSet<>();
            String[] postNameSplit = importUser.getPostName().split("，");
            List<String> errorPostList = new LinkedList<>();
            for (String postName : postNameSplit) {
                Long postId = postMap.get(postName);
                if (Objects.isNull(postId)) {
                    errorPostList.add(postName);
                    continue;
                }
                postIds.add(postId);
            }
            if (!errorPostList.isEmpty()) {
                String errorPostStr = Joiner.on(",").join(errorPostList);
                errorMessageBuilder.append("岗位：").append(errorPostStr).append(" 不存在;");
            } else {
                userAndInformation.setPostIds(postIds);
            }
            // 翻译角色信息
            Set<Long> roleIds = new HashSet<>();
            String[] roleNameSplit = importUser.getRoleName().split("，");
            List<String> errorRoleList = new LinkedList<>();
            for (String roleName : roleNameSplit) {
                Long roleId = roleMap.get(roleName);
                if (Objects.isNull(roleId)) {
                    errorRoleList.add(roleName);
                    continue;
                }
                roleIds.add(roleId);
            }
            if (!errorRoleList.isEmpty()) {
                String errorRoleStr = Joiner.on(",").join(errorRoleList);
                errorMessageBuilder.append("角色：").append(errorRoleStr).append(" 不存在;");
            } else {
                userAndInformation.setUserRoleIdList(roleIds);
            }
            String nation = nationMap.get(importUser.getNation());
            if (StringUtils.isNotBlank(importUser.getNation()) && StringUtils.isBlank(nation)) {
                errorMessageBuilder.append("民族：").append(importUser.getNation()).append(" 不存在;");
            } else {
                userAndInformation.setNation(nation);
            }
            Integer sex = SexEnum.getCodeByName(importUser.getSexStr());
            if (Objects.isNull(sex)) {
                errorMessageBuilder.append("性别：").append(importUser.getSexStr()).append(" 不存在;");
            } else {
                userAndInformation.setSex(sex);
            }
            if (!"内聘人员".equals(importUser.getInfoTypeStr()) && !"外聘人员".equals(importUser.getInfoTypeStr())) {
                errorMessageBuilder.append("人员类型：").append(importUser.getInfoTypeStr()).append(" 非法;");
            }
            if (StringUtils.isNotBlank(importUser.getHasProbationStr()) && !"是".equals(importUser.getHasProbationStr()) && !"否".equals(importUser.getHasProbationStr())) {
                errorMessageBuilder.append("是否有试用期：").append(importUser.getHasProbationStr()).append(" 非法;");
            }
            if (StringUtils.isNotBlank(importUser.getIsResignedStr()) && !"是".equals(importUser.getIsResignedStr()) && !"否".equals(importUser.getIsResignedStr())) {
                errorMessageBuilder.append("是否离职：").append(importUser.getIsResignedStr()).append(" 非法;");
            }

            LocalDateTime hireDate = this.getImportLocalDateByStr(importUser.getHireDateStr(), "入职时间", errorMessageBuilder);
            Date formalDate = this.getImportDateByStr(importUser.getFormalDateStr(), "转正日期", errorMessageBuilder);
            Date cardPeriod = this.getImportDateByStr(importUser.getCardPeriodStr(), "身份证有效期", errorMessageBuilder);
            Date birthDate = this.getImportDateByStr(importUser.getBirthDateStr(), "出生日期", errorMessageBuilder);
            Date tryDate = this.getImportDateByStr(importUser.getTryDateStr(), "试用期截止日", errorMessageBuilder);
            Date expiryDate = this.getImportDateByStr(importUser.getExpiryDateStr(), "合同到期日", errorMessageBuilder);
            LocalDateTime resignTime = this.getImportLocalDateByStr(importUser.getResignTimeStr(), "离职时间", errorMessageBuilder);

            // 如果有错误信息，全部一起提示给用户，避免用户重复操作
            String errorMessage = errorMessageBuilder.toString();
            if (StringUtils.isNotBlank(errorMessage)) {
                importUser.setMsg(errorMessage);
                errList.add(importUser);
                continue;
            }

            userAndInformation.setHasProbation(Objects.equals("是", importUser.getHasProbationStr()));
            userAndInformation.setInfoType(Objects.equals("外聘人员", importUser.getInfoTypeStr()) ? 2 : 1);
            userAndInformation.setPassword(this.encodePassword(userInitPassword));
            userAndInformation.setIsResigned(Objects.equals("是", importUser.getIsResignedStr()));
            userAndInformation.setHireDate(hireDate);
            userAndInformation.setFormalDate(formalDate);
            userAndInformation.setCardPeriod(cardPeriod);
            userAndInformation.setBirthDate(birthDate);
            userAndInformation.setTryDate(tryDate);
            userAndInformation.setExpiryDate(expiryDate);
            userAndInformation.setResignTime(resignTime);
            this.createUserAndInformation(userAndInformation);
        }
        return errList;
    }

    private String completionDateStr(String dateStr) {
        // 因为excel会自动转义时间字符串，所以需要补齐位数
        String[] dateSplit = dateStr.split("/");
        String month = dateSplit[1];
        String day = dateSplit[2];
        String monthStr = String.format("%02d", Integer.parseInt(month));
        String dayStr = String.format("%02d", Integer.parseInt(day));
        return dateSplit[0] + "/" + monthStr + "/" + dayStr;
    }

    private Date getImportDateByStr(String dateStr, String columnName, StringBuilder errorMessageBuilder) {
        try {
            if (StringUtils.isNotBlank(dateStr)) {
                return TimeUtils.parseAsDate(completionDateStr(dateStr), "yyyy/MM/dd");
            }
        } catch (Exception e) {
            errorMessageBuilder.append(columnName).append(":").append(dateStr).append(" 格式有误，正确示例:2023/11/24;");
        }
        return null;
    }

    private LocalDateTime getImportLocalDateByStr(String dateStr, String columnName, StringBuilder errorMessageBuilder) {
        try {
            if (StringUtils.isNotBlank(dateStr)) {
                return TimeUtils.parseAsLocalDateTime(completionDateStr(dateStr).replace("/", "-") + " 00:00:00");
            }
        } catch (Exception e) {
            errorMessageBuilder.append(columnName).append(":").append(dateStr).append(" 格式有误，正确示例:2023/11/24;");
        }
        return null;
    }

    @Override
    public List<AdminUserDO> getUserListByStatus(Integer status) {
        List<AdminUserDO> adminUserDOS = userMapper.selectListByStatus(status);
        adminUserDOS.sort((o1, o2) -> {
            Comparator<Object> com = Collator.getInstance(Locale.CHINA);
            return com.compare(o1.getNickname(), o2.getNickname());
        });
        return adminUserDOS;
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    /**
     * 对密码进行加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


    @Override
    @Transactional(rollbackFor = Exception.class) // 添加事务，异常则回滚所有导入
    public Long createUserAndInformation(UserAndInformation userAndInformation) {
        UserCreateReqVO reqVO = new UserCreateReqVO();
        InformationCreateReqVO informationCreateReqVO = new InformationCreateReqVO();
        BeanUtils.copyProperties(userAndInformation, reqVO);
        BeanUtils.copyProperties(userAndInformation, informationCreateReqVO);

        String nickname = userAndInformation.getNickname();
        informationCreateReqVO.setName(nickname);
        Integer infoType = userAndInformation.getInfoType();
        validateUserForCreateOrUpdate(null, reqVO.getUsername(), reqVO.getMobile(), reqVO.getEmail(),
                reqVO.getDeptId(), reqVO.getPostIds());
        AdminUserDO user = UserConvert.INSTANCE.convert(reqVO);
        user.setStatus(CommonStatusEnum.ENABLE.getStatus());
        user.setPassword(encodePassword(reqVO.getPassword()));
        userMapper.insert(user);
        //生成当日空工作微博
        WorkLogDO workLogDO = new WorkLogDO();
        workLogDO.setUserId(user.getId());
        workLogDO.setUserName(user.getNickname());
        workLogDO.setDeptId(user.getDeptId());
        workLogDO.setLogDate(new Date());
        workLogDO.setIsEditable(true);
        workLogMapper.insert(workLogDO);
        if (CollUtil.isNotEmpty(user.getPostIds())) {
            userPostMapper.insertBatch(convertList(user.getPostIds(),
                    postId -> new UserPostDO().setUserId(user.getId()).setPostId(postId)));
        }
        permissionService.assignUserRole(user.getId(), reqVO.getUserRoleIdList());
        informationCreateReqVO.setUserId(user.getId());
        informationService.createInformation(informationCreateReqVO);
        if (userAndInformation.getSource() == PMS) {
            return user.getId();
        }
        //异步，同步JNT报错不影响OA的操作
//        CompletableFuture.runAsync(() -> {
        syncJntUser(OperateTypeEnum.ADD, user);
//        });
        //同步售后服务平台
        CompletableFuture.runAsync(() -> {
            syncPostSaleUser(OperateTypeEnum.ADD, user);
        });

        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 添加事务，异常则回滚所有导入
    public void updateUserAndInformation(UpdateUserAndInformation updateUserAndInformation) {
        UserUpdateReqVO reqVO = new UserUpdateReqVO();

        BeanUtils.copyProperties(updateUserAndInformation, reqVO);
        validateUserForCreateOrUpdate(reqVO.getId(), reqVO.getUsername(), reqVO.getMobile(), reqVO.getEmail(),
                reqVO.getDeptId(), reqVO.getPostIds());
        AdminUserDO user = UserConvert.INSTANCE.convert(reqVO);
        permissionService.assignUserRole(reqVO.getId(), reqVO.getUserRoleIdList());
        //同步用户日志的冗余字段
        changelogDeptFromUser(reqVO, user);

        userMapper.updateById(user);
        updateUserPost(reqVO, user);
        InformationDO information = informationService.getInformationByUserId(reqVO.getId());
        if (information != null) {
            InformationUpdateReqVO informationUpdateReqVO = new InformationUpdateReqVO();
            BeanUtils.copyProperties(updateUserAndInformation, informationUpdateReqVO);
            informationUpdateReqVO.setName(reqVO.getNickname());
            informationUpdateReqVO.setId(information.getId());
            informationUpdateReqVO.setUserId(information.getUserId());
            informationService.updateInformation(informationUpdateReqVO);
        } else {
            InformationCreateReqVO informationCreateReqVO = new InformationCreateReqVO();
            BeanUtils.copyProperties(updateUserAndInformation, informationCreateReqVO);
            informationCreateReqVO.setName(reqVO.getNickname());
            informationCreateReqVO.setUserId(reqVO.getId());
            informationService.createInformation(informationCreateReqVO);
        }
        Integer infoType = updateUserAndInformation.getInfoType();
        if (updateUserAndInformation.getSource() == PMS) {
            return;
        }
        //异步，同步JNT报错不影响OA的操作
//        CompletableFuture.runAsync(() -> {
        syncJntUser(OperateTypeEnum.UPDATE, user);
//        });
    }

    private void changelogDeptFromUser(UserUpdateReqVO reqVO, AdminUserDO user) {
        AdminUserDO adminUserDO = userMapper.selectById(user.getId());
        if (ObjUtil.isEmpty(adminUserDO)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_EXISTS);
        }
        if (ObjectUtils.notEqual(adminUserDO.getDeptId(), user.getDeptId())) {
            List<WorkLogDO> workLogDOList = workLogMapper.selectList(new LambdaQueryWrapperX<WorkLogDO>().eqIfPresent(WorkLogDO::getUserId, reqVO.getId()));
            List<WorkLogDO> otherDeptWorkLogDOList = workLogDOList.stream().filter(workLogDO -> ObjectUtils.notEqual(workLogDO.getDeptId(), user.getDeptId())).collect(Collectors.toList());
            if (CollUtil.isEmpty(otherDeptWorkLogDOList)) {
                return;
            }
            otherDeptWorkLogDOList.forEach(workLogDO -> workLogDO.setDeptId(user.getDeptId()));
            workLogMapper.updateBatch(otherDeptWorkLogDOList, otherDeptWorkLogDOList.size());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateSyncJNT(AdminUserDO updateObj, Integer infoType) {
        //同步建能通
        try {
            EmployTypeEnum employTypeEnum;
            if (infoType == null || infoType != 2) {
                employTypeEnum = EmployTypeEnum.INNER_EMPLOY;
            } else {
                employTypeEnum = EmployTypeEnum.OUTER_EMPLOY;
            }
            JntUser jntUser = new JntUser();
            jntUser.setOperateType(OperateTypeEnum.UPDATE);
            DeptDO deptDO = deptService.getDept(updateObj.getDeptId());
            jntUser.setOaUserId(Math.toIntExact(updateObj.getId()));
            if (deptDO == null)
                throw new BusinessException("所选部门有误");
            if (deptDO.getType() == DeptTypeEnum.ORGANIZATION) {
                jntUser.setOaOrgId(Math.toIntExact(deptDO.getId()));
                jntUser.setOaDeptId(null);
            } else {
                DeptDO parentOrgOfCompany = deptService.getParentOrgOfCompany(deptDO.getParentId());
                jntUser.setOaOrgId(Math.toIntExact(parentOrgOfCompany.getId()));
                jntUser.setOaDeptId(Math.toIntExact(deptDO.getId()));
            }
            if (updateObj.getPostIds() != null && updateObj.getPostIds().size() > 0) {
                jntUser.setOaPostId(Math.toIntExact(updateObj.getPostIds().iterator().next()));
            }
            jntUser.setAccount(updateObj.getUsername());
            jntUser.setName(updateObj.getNickname());
            jntUser.setMobile(updateObj.getMobile());
            jntUser.setGender(updateObj.getSex() == null ? GenderEnum.UNKNOWN : (updateObj.getSex() == 0 ? GenderEnum.MAN : GenderEnum.WOMAN));
            jntUser.setEmployType(employTypeEnum);
            jntUser.setSource(SourceEnum.OA);
            jntBaseDataSyncService.syncUser(jntUser);
        } catch (Exception e) {
            throw new BusinessException("[JNT]操作失败，" + e.getLocalizedMessage());
        }
    }

    @Override
    public UserAndInformation getUserAndInformation(Long id) {
        AdminUserDO adminUserDO = userMapper.selectById(id);
        InformationDO informationByUserId = informationService.getInformationByUserId(id);
        UserAndInformation userAndInformation = new UserAndInformation();
        if (informationByUserId != null)
            BeanUtils.copyProperties(informationByUserId, userAndInformation, "id");
        BeanUtils.copyProperties(adminUserDO, userAndInformation, "bankAccount", "bankAccountNumber");
        return userAndInformation;
    }

    @Override
    public List<UserExcelVO> exportUserAndInformation(UserExportReqVO reqVO) {
        List<AdminUserDO> userList = this.getUserList(reqVO);
        if (userList.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> userIds = userList.stream().map(AdminUserDO::getId).collect(Collectors.toSet());
        Map<Long, String> childIdaAndParentDeptNameMap = new HashMap<>();
        List<DeptDO> parentDeptList = deptService.getAllParentDeptList();
        parentDeptList.forEach(parentDept -> {
            List<DeptDO> childDeptList = deptService.getDeptListByParentIdFromCache(parentDept.getId(), true);
            childDeptList.forEach(childDept -> childIdaAndParentDeptNameMap.put(childDept.getId(), parentDept.getName()));
        });

        List<DeptDO> deptList = deptService.getDeptList(userList.stream().map(AdminUserDO::getDeptId).collect(Collectors.toSet()));
        List<UserPostDO> userPostList = userPostMapper.selectListByUserIds(userIds);
        List<UserRoleDO> userRoleList = userRoleMapper.selectListByUserIds(userIds);
        Set<Long> roleIds = userRoleList.stream().map(UserRoleDO::getRoleId).collect(Collectors.toSet());
        Set<Long> postIds = userPostList.stream().map(UserPostDO::getPostId).collect(Collectors.toSet());
        Map<Long, InformationDO> informationMap = informationMapper.getByUserIds(userIds)
                .stream()
                .collect(Collectors.toMap(InformationDO::getUserId, Function.identity()));
        Map<Long, Set<Long>> userIdAndPostIdsMap = userPostList
                .stream()
                .collect(Collectors.groupingBy(UserPostDO::getUserId,
                        Collectors.mapping(UserPostDO::getPostId, Collectors.toSet())));
        Map<Long, Set<Long>> userIdAndRoleIdsMap = userRoleList
                .stream()
                .collect(Collectors.groupingBy(UserRoleDO::getUserId,
                        Collectors.mapping(UserRoleDO::getRoleId, Collectors.toSet())));
        Map<Long, DeptDO> deptMap = deptList.stream().collect(Collectors.toMap(DeptDO::getId, Function.identity()));
        Map<Long, String> roleNameMap = roleService.getRoleListByIds(roleIds).stream().collect(Collectors.toMap(RoleDO::getId, RoleDO::getName));
        Map<Long, String> postNameMap = postService.getPostList(postIds).stream().collect(Collectors.toMap(PostDO::getId, PostDO::getName));
        List<DictDataBatchVo> dictDataList = dictDataService.batchQueryDictData("nation");
        Map<String, Map<String, String>> dictTypeMap = dictDataList
                .stream()
                .collect(Collectors.toMap(
                        DictDataBatchVo::getDictType,
                        vo -> vo.getDataVoList()
                                .stream()
                                .collect(Collectors.toMap(DictDataBaseVO::getValue, DictDataBaseVO::getLabel))));
        Map<String, String> nationMap = dictTypeMap.getOrDefault("nation", Collections.emptyMap());

        List<UserExcelVO> result = new LinkedList<>();
        userList.forEach(user -> {
            InformationDO information = informationMap.get(user.getId());
            UserExcelVO excelVO = new UserExcelVO();
            // 兼容没有用户扩展信息的情况
            if (Objects.nonNull(information)) {
                excelVO = JsonUtils.covertObject(information, UserExcelVO.class);
                excelVO.setInfoType(Objects.equals(2, information.getInfoType()) ? "外聘人员" : "内聘人员");
                excelVO.setHasProbation(Objects.equals(true, information.getHasProbation()) ? "是" : "否");
                excelVO.setIsResigned(Objects.equals(true, information.getIsResigned()) ? "是" : "否");
                excelVO.setHireDate(TimeUtils.formatAsDate(information.getHireDate()).replace("-", "/"));
                excelVO.setFormalDate(TimeUtils.format(information.getFormalDate(), "yyyy/MM/dd"));
                excelVO.setCardPeriod(TimeUtils.format(information.getCardPeriod(), "yyyy/MM/dd"));
                excelVO.setBirthDate(TimeUtils.format(information.getBirthDate(), "yyyy/MM/dd"));
                excelVO.setTryDate(TimeUtils.format(information.getTryDate(), "yyyy/MM/dd"));
                excelVO.setExpiryDate(TimeUtils.format(information.getExpiryDate(), "yyyy/MM/dd"));
                excelVO.setResignTime(TimeUtils.formatAsDate(information.getResignTime()).replace("-", "/"));
                excelVO.setNation(nationMap.getOrDefault(information.getNation(), ""));
            }
            BeanUtils.copyProperties(user, excelVO);

            DeptDO dept = deptMap.get(user.getDeptId());
            if (Objects.nonNull(dept)) {
                excelVO.setDeptName(dept.getName());
                excelVO.setParentDeptName(childIdaAndParentDeptNameMap.getOrDefault(dept.getId(), ""));
            }

            List<String> roleNameList = new LinkedList<>();
            Set<Long> userRoleIds = userIdAndRoleIdsMap.get(user.getId());
            if (!org.springframework.util.CollectionUtils.isEmpty(userRoleIds)) {
                userRoleIds.forEach(roleId -> {
                    String roleName = roleNameMap.get(roleId);
                    if (StringUtils.isNotBlank(roleName)) {
                        roleNameList.add(roleName);
                    }
                });
                excelVO.setRoleName(Joiner.on("，").join(roleNameList));
            }

            List<String> postNameList = new LinkedList<>();
            Set<Long> userPostIds = userIdAndPostIdsMap.get(user.getId());
            if (!org.springframework.util.CollectionUtils.isEmpty(userPostIds)) {
                userPostIds.forEach(postId -> {
                    String postName = postNameMap.get(postId);
                    if (StringUtils.isNotBlank(postName)) {
                        postNameList.add(postName);
                    }
                });
                excelVO.setPostName(Joiner.on("，").join(postNameList));
            }

            excelVO.setSex(SexEnum.getNameByCode(user.getSex()));
            excelVO.setStatus(CommonStatusEnum.getNameByStatus(user.getStatus()));
            result.add(excelVO);
        });
        return result;
    }

    @Override
    public List<UserSimpleRespVO> getUserSimpleInfo(Long loginUserId, String nickName) {
        if (StringUtils.isEmpty(nickName)) {
            UserSimpleRespVO vo = new UserSimpleRespVO();
            AdminUserDO user = userMapper.selectById(loginUserId);
            vo.setId(loginUserId);
            vo.setNickname(user.getNickname());
            DeptDO dept = deptService.getDept(user.getDeptId());
            if (Objects.nonNull(dept)) {
                vo.setDeptId(dept.getId());
                vo.setDeptName(dept.getName());
            }
            return Collections.singletonList(vo);
        } else {
            List<AdminUserDO> userList = userMapper.selectListByNickname(nickName);
            if (userList.isEmpty()) {
                return Collections.emptyList();
            }
            Set<Long> deptIds = userList.stream().map(AdminUserDO::getDeptId).collect(Collectors.toSet());
            Map<Long, DeptDO> deptMap = deptService.getDeptMap(deptIds);
            List<UserSimpleRespVO> result = new LinkedList<>();
            userList.forEach(user -> {
                UserSimpleRespVO vo = new UserSimpleRespVO();
                vo.setId(user.getId());
                vo.setNickname(user.getNickname());
                DeptDO dept = deptMap.get(user.getDeptId());
                if (Objects.nonNull(dept)) {
                    vo.setDeptId(dept.getId());
                    vo.setDeptName(dept.getName());
                }
                result.add(vo);
            });
            return result;
        }
    }

    @Override
    public List<AdminUserDO> getUserListByUserSimpleRespVO(UserSimpleRespVO userSimpleRespVO) {
        return userMapper.selectList(new LambdaQueryWrapperX<AdminUserDO>()
                .eqIfPresent(AdminUserDO::getId, userSimpleRespVO.getId())
                .likeIfPresent(AdminUserDO::getNickname, userSimpleRespVO.getNickname())
                .eqIfPresent(AdminUserDO::getDeptId, userSimpleRespVO.getDeptId())
                .eqIfPresent(AdminUserDO::getStatus, CommonStatusEnum.ENABLE.getStatus()));
    }

    @Override
    public Map<Integer, List<String>> getImportTemplateDropListMap() {
        Map<Integer, List<String>> result = new HashMap<>();

        List<String> sexList = new LinkedList<>();
        sexList.add("男");
        sexList.add("女");
        result.put(7, sexList);

        List<String> infoTypeList = new LinkedList<>();
        infoTypeList.add("内聘人员");
        infoTypeList.add("外聘人员");
        result.put(10, infoTypeList);

        List<String> hasProbationList = new LinkedList<>();
        hasProbationList.add("是");
        hasProbationList.add("否");
        result.put(21, hasProbationList);

        List<DictDataBatchVo> nationDictList = dictDataService.batchQueryDictData("nation");
        List<String> nationList = nationDictList
                .stream()
                .map(DictDataBatchVo::getDataVoList)
                .flatMap(Collection::stream)
                .map(DictDataBaseVO::getLabel)
                .collect(Collectors.toList());
        result.put(26, nationList);

        List<String> isResignedList = new LinkedList<>();
        isResignedList.add("是");
        isResignedList.add("否");
        result.put(52, isResignedList);

        return result;
    }

    @Override
    public Map<Integer, Map<String, List<String>>> getImportTemplateParentChildMap() {
        Map<String, List<String>> result = new HashMap<>();
        DeptListReqVO reqVO = new DeptListReqVO();
        reqVO.setStatus(0);
        List<DeptDO> list = deptService.getDeptList(reqVO);
        list.sort(Comparator.comparing(DeptDO::getSort));
        List<DeptRespVO> deptTreeList = DeptController.buildMenuTree(JsonUtils.covertList(list, DeptRespVO.class));
        deptTreeList.forEach(deptVo -> {
            List<String> deptNameList = new LinkedList<>();
            recursionLoadTreeDeptName(deptNameList, deptVo.getChildren());
            result.put(deptVo.getName(), deptNameList);
        });
        return Collections.singletonMap(3, result);
    }

    @Override
    public List<UserImportExcelVO> getImportExample() {
        UserImportExcelVO excelVO = new UserImportExcelVO();
        excelVO.setPostName("多个时使用中文逗号分隔");
        excelVO.setRoleName("多个时使用中文逗号分隔");
        excelVO.setHireDateStr("日期格式：年/月/日，例如：2023/01/01");
        excelVO.setFormalDateStr("日期格式：年/月/日，例如：2023/01/01");
        excelVO.setCardPeriodStr("日期格式：年/月/日，例如：2023/01/01");
        excelVO.setBirthDateStr("日期格式：年/月/日，例如：2023/01/01");
        excelVO.setTryDateStr("日期格式：年/月/日，例如：2023/01/01");
        excelVO.setExpiryDateStr("日期格式：年/月/日，例如：2023/01/01");
        excelVO.setResignTimeStr("日期格式：年/月/日，例如：2023/01/01");
        excelVO.setMsg("导入失败时失败信息会回写在这里，请勿主动填写");
        return Collections.singletonList(excelVO);
    }

    @Override
    public List<UserPressVO> getPressUsers() {
        // 获用户门列表，只要开启状态的
        List<AdminUserDO> list = this.getUserListByStatus(CommonStatusEnum.ENABLE.getStatus());
        ArrayList<UserPressVO> list1 = new ArrayList<>();
        List<DeptDO> deptDOS = deptMapper.selectList();
        List<PostDO> postDOS = postMapper.selectList();
        List<InformationDO> informationDOS = informationMapper.selectList();
        Map<Long, DeptDO> deptMap = deptDOS.stream().collect(Collectors.toMap(DeptDO::getId, deptDO -> deptDO));
        Map<Long, PostDO> postMap = postDOS.stream().collect(Collectors.toMap(PostDO::getId, postDO -> postDO));
        Map<Long, InformationDO> informationDOMap = informationDOS.stream().collect(Collectors.toMap(InformationDO::getUserId, informationDO -> informationDO));

        list.forEach(s -> {
            UserPressVO userPressVO = new UserPressVO();
            userPressVO.setLabel(s.getNickname());
            userPressVO.setValue(s.getId());
            userPressVO.setMobile(s.getMobile());
            Integer sex = s.getSex();
            if (sex != null) {
                userPressVO.setSex(sex == 0 ? "男" : "女");
            }
            InformationDO informationDO = informationDOMap.get(s.getId());
            if (ObjectUtils.isNotEmpty(informationDO)) {
                userPressVO.setBankAccount(informationDO.getBankAccount());
                userPressVO.setBankAccountNumber(informationDO.getBankAccountNumber());
                userPressVO.setIdentityCard(informationDO.getIdentityCard());
            }

            if (ObjectUtils.isNotEmpty(s.getDeptId()) && s.getDeptId() != 0) {
                userPressVO.setDeptId(s.getDeptId());
                DeptDO deptDO = deptMap.get(s.getDeptId());
                if (ObjectUtils.isNotEmpty(deptDO)) {
                    userPressVO.setDeptName(deptDO.getName());
                }
            }
            if (ObjectUtils.isNotEmpty(s.getPostIds())) {
                StringJoiner stringJoiner = new StringJoiner(",");
                s.getPostIds().forEach(postId -> {
                    if (postId != null && postId != 0) {
                        PostDO post = postMap.get(postId);
                        if (ObjectUtils.isNotEmpty(post)) {
                            stringJoiner.add(post.getName());
                        }
                    }
                });
                userPressVO.setPostName(stringJoiner.toString());
                userPressVO.setPostIds(s.getPostIds());
            }
            list1.add(userPressVO);
        });
        return list1;
    }

    @Override
    public Set<Long> getContainUserIds(Long projectId, Long deptId, Long userId) {
        log.info("根据参数获取用户ids的交集, projectId:{}, deptId:{}, userId:{}", projectId, deptId, userId);
        Set<Long> deptUserIds = new HashSet<>();
        Set<Long> projectUserIds = new HashSet<>();
        Set<Long> userIds = new HashSet<>();
        if (Objects.nonNull(deptId) && !Objects.equals(0L, deptId)) {
            // 获取所有子级部门id
            List<DeptDO> allChildDeptList = deptService.getDeptListByParentIdFromCache(deptId, true);
            Set<Long> deptIds = allChildDeptList.stream().map(DeptDO::getId).collect(Collectors.toSet());
            deptIds.add(deptId);
            List<AdminUserDO> adminUserDOS = userMapper.selectListByDeptIds(deptIds);
            if (adminUserDOS.isEmpty()) {
                return Collections.emptySet();
            }
            deptUserIds.addAll(adminUserDOS.stream().map(AdminUserDO::getId).collect(Collectors.toSet()));
        }
        if (Objects.nonNull(projectId) && !Objects.equals(0L, projectId)) {
            List<UserProjectUserVO> projectUser = userProjectService.getProjectUser(projectId);
            if (projectUser.isEmpty()) {
                return Collections.emptySet();
            }
            projectUserIds.addAll(projectUser.stream().map(UserProjectUserVO::getUserId).collect(Collectors.toSet()));
        }

        // 获取用户ids的交集
        if (deptUserIds.isEmpty() && !projectUserIds.isEmpty()) {
            userIds = projectUserIds;
        }
        if (!deptUserIds.isEmpty() && projectUserIds.isEmpty()) {
            userIds = deptUserIds;
        }
        if (!deptUserIds.isEmpty() && !projectUserIds.isEmpty()) {
            deptUserIds.retainAll(projectUserIds);
            userIds.addAll(deptUserIds);
        }
        if (Objects.nonNull(userId) && !Objects.equals(0L, userId)) {
            if (userIds.isEmpty()) {
                return Collections.singleton(userId);
            }
            if (userIds.contains(userId)) {
                userIds = Collections.singleton(userId);
            } else {
                userIds = Collections.emptySet();
            }
        }
        return userIds;
    }

    @Override
    public List<AdminUserDO> getAllUsers() {
        return userMapper.selectList();
    }

    private void recursionLoadTreeDeptName(List<String> deptNameList, List<DeptRespVO> deptTreeList) {
        if (deptTreeList.isEmpty()) {
            return;
        }
        deptTreeList.forEach(dept -> {
            deptNameList.add(dept.getName());
            recursionLoadTreeDeptName(deptNameList, dept.getChildren());
        });
    }


    public void batchUpdatePost() {
        //查询出安能达组织下面所有用户
        Set<Long> deptIds = getDeptCondition(163L);
        List<AdminUserDO> userList = userMapper.selectList(new LambdaQueryWrapperX<AdminUserDO>().in(AdminUserDO::getDeptId, deptIds).eq(AdminUserDO::getDeleted, 0));
        userList.stream().forEach(user -> {
            if (null != user.getPostIds() && user.getPostIds().size() > 0) {
                List<PostDO> oldPosts = postMapper.selectList(new LambdaQueryWrapperX<PostDO>().in(PostDO::getId, user.getPostIds()).eq(PostDO::getDeleted, 0));
                List<String> names = oldPosts.stream().map(PostDO::getName).collect(Collectors.toList());
                List<PostDO> newPosts = postMapper.selectList(new LambdaQueryWrapperX<PostDO>().in(PostDO::getName, names).eq(PostDO::getDeleted, 0).eq(PostDO::getOrgId, 163));
                List<Long> ids = newPosts.stream().map(PostDO::getId).collect(Collectors.toList());
                user.setPostIds(new HashSet<>(ids));
                userMapper.updateById(user);
            }
        });
    }


    private void syncUserState(Long id, Integer status) {
        try {
            JntUser jntUser = new JntUser();
            jntUser.setOaUserId(id.intValue());
            jntUser.setState(status == 0 ? ENABLED : DISABLED);
            jntUser.setSource(SourceEnum.OA);
            jntBaseDataSyncService.syncUserState(jntUser);
        } catch (Exception e) {
            throw new BusinessException("[JNT]操作失败，" + e.getLocalizedMessage());
        }
    }

}
