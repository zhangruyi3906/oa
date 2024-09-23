package com.lh.oa.module.system.service.user;

import cn.hutool.core.collection.CollUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.collection.CollectionUtils;
import com.lh.oa.module.system.controller.admin.user.vo.profile.UserProfileUpdatePasswordReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.*;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.full.enums.jnt.ProjectSourceEnum;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.*;

/**
 * 后台用户 Service 接口
 *
 * @author
 */
public interface AdminUserService {

    /**
     * 创建用户
     *
     * @param reqVO 用户信息
     * @return 用户编号
     */
    Long createUser(@Valid UserCreateReqVO reqVO);

    /**
     * 修改用户
     *
     * @param reqVO 用户信息
     */
    void updateUser(@Valid UserUpdateReqVO reqVO);

    /**
     * 手动同步用户到建能通
     *
     * @param userId
     */
    void handSyncUser(Long userId);

    /**
     * 更新用户的最后登陆信息
     *
     * @param id 用户编号
     * @param loginIp 登陆 IP
     */
    void updateUserLogin(Long id, String loginIp);

    /**
     * 修改用户个人信息
     *
     * @param id 用户编号
     * @param reqVO 用户个人信息
     */
    void updateUserProfile(Long id, @Valid UserProfileUpdateReqVO reqVO);

    /**
     * 修改用户个人密码
     *
     * @param id 用户编号
     * @param reqVO 更新用户个人密码
     */
    void updateUserPassword(Long id, @Valid UserProfileUpdatePasswordReqVO reqVO);

    /**
     * 更新用户头像
     *
     * @param id         用户 id
     * @param avatarFile 头像文件
     */
    String updateUserAvatar(Long id, MultipartFile file) throws Exception;

    /**
     * 修改密码
     *
     * @param id       用户编号
     * @param password 密码
     */
    void updateUserPassword(Long id, String password);

    /**
     * 修改状态
     *
     * @param id     用户编号
     * @param status 状态
     */
    void updateUserStatus(Long id, Integer status, ProjectSourceEnum sourceEnum);

    /**
     * 删除用户
     *
     * @param id 用户编号
     */
    void deleteUser(Long id,Boolean notSyncJnt);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    AdminUserDO getUserByUsername(String username);

    /**
     * 通过手机号获取用户
     *
     * @param mobile 手机号
     * @return 用户对象信息
     */
    AdminUserDO getUserByMobile(String mobile);

    /**
     * 获得用户分页列表
     *
     * @param reqVO 分页条件
     * @return 分页列表
     */
    PageResult<AdminUserDO> getUserPage(UserPageReqVO reqVO);

    /**
     * 通过用户 ID 查询用户
     *
     * @param id 用户ID
     * @return 用户对象信息
     */
    AdminUserDO getUser(Long id);

    /**
     * 获得指定部门的用户数组
     *
     * @param deptIds 部门数组
     * @return 用户数组
     */
    List<AdminUserDO> getUserListByDeptIds(Collection<Long> deptIds);

    /**
     * 获得指定岗位的用户数组
     *
     * @param postIds 岗位数组
     * @return 用户数组
     */
    List<AdminUserDO> getUserListByPostIds(Collection<Long> postIds);

    /**
     * 获得用户列表
     *
     * @param ids 用户编号数组
     * @return 用户列表
     */
    List<AdminUserDO> getUserList(Collection<Long> ids);

    /**
     * 校验用户们是否有效。如下情况，视为无效：
     * 1. 用户编号不存在
     * 2. 用户被禁用
     *
     * @param ids 用户编号数组
     */
    void validateUserList(Collection<Long> ids);

    /**
     * 获得用户 Map
     *
     * @param ids 用户编号数组
     * @return 用户 Map
     */
    default Map<Long, AdminUserDO> getUserMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return new HashMap<>();
        }
        return CollectionUtils.convertMap(getUserList(ids), AdminUserDO::getId);
    }

    /**
     * 获得用户列表
     *
     * @param reqVO 列表请求
     * @return 用户列表
     */
    List<AdminUserDO> getUserList(UserExportReqVO reqVO);

    /**
     * 获得用户列表，基于昵称模糊匹配
     *
     * @param nickname 昵称
     * @return 用户列表
     */
    List<AdminUserDO> getUserListByNickname(String nickname);

    /**
     * 批量导入用户
     *
     * @param importUsers     导入用户列表
     * @param isUpdateSupport 是否支持更新
     * @return 导入结果
     */
    List<UserImportExcelVO> importUserList(List<UserImportExcelVO> importUsers, boolean isUpdateSupport);

    /**
     * 获得指定状态的用户们
     *
     * @param status 状态
     * @return 用户们
     */
    List<AdminUserDO> getUserListByStatus(Integer status);

    /**
     * 判断密码是否匹配
     *
     * @param rawPassword 未加密的密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    boolean isPasswordMatch(String rawPassword, String encodedPassword);

    Long createUserAndInformation(UserAndInformation userAndInformation);

    void updateUserAndInformation(UpdateUserAndInformation updateUserAndInformation);

    UserAndInformation getUserAndInformation(Long id);

    List<UserExcelVO> exportUserAndInformation(UserExportReqVO reqVO);

    /**
     * 模糊查询用户的简易信息，如果没有传名字，则查询当前登录用户的信息
     */
    List<UserSimpleRespVO> getUserSimpleInfo(Long loginUserId, String nickName);

    List<AdminUserDO> getUserListByUserSimpleRespVO(UserSimpleRespVO userSimpleRespVO);

    /**
     * 获取导入模板的下拉映射关系
     */
    Map<Integer, List<String>> getImportTemplateDropListMap();

    /**
     * 获取导入模板的下拉映射关系
     */
    Map<Integer, Map<String, List<String>>> getImportTemplateParentChildMap();

    /**
     * 获取导入模板的示例信息
     */
    List<UserImportExcelVO> getImportExample();

    List<AdminUserDO> getAllUsers();
    List<UserPressVO> getPressUsers();
    /**
     * 根据参数获取关联的用户ids的交集
     *
     * @param projectId 项目id
     * @param deptId    部门id
     * @param userId    用户id
     * @return 用户ids的交集
     */
    Set<Long> getContainUserIds(Long projectId, Long deptId, Long userId);

   void  batchUpdatePost();

}
