package com.lh.oa.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;
import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.dept.vo.post.PostCreateReqVO;
import com.lh.oa.module.system.controller.admin.dept.vo.post.PostExportReqVO;
import com.lh.oa.module.system.controller.admin.dept.vo.post.PostPageReqVO;
import com.lh.oa.module.system.controller.admin.dept.vo.post.PostRespVO;
import com.lh.oa.module.system.controller.admin.dept.vo.post.PostUpdateReqVO;
import com.lh.oa.module.system.convert.dept.PostConvert;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.dept.PostDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.mysql.dept.DeptMapper;
import com.lh.oa.module.system.dal.mysql.dept.PostMapper;
import com.lh.oa.module.system.dal.mysql.user.AdminUserMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.full.entity.jnt.JntPost;
import com.lh.oa.module.system.full.enums.jnt.OperateTypeEnum;
import com.lh.oa.module.system.full.enums.jnt.ProjectSourceEnum;
import com.lh.oa.module.system.full.enums.jnt.SourceEnum;
import com.lh.oa.module.system.full.service.jnt.JntBaseDataSyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 岗位 Service 实现类
 *
 * @author
 */
@Service
@Validated
@Slf4j
public class PostServiceImpl implements PostService {

    @Resource
    private PostMapper postMapper;
    @Resource
    private AdminUserMapper userMapper;
    @Resource
    private JntBaseDataSyncService jntBaseDataSyncService;

    @Resource
    private DeptMapper deptMapper;

    @Transactional
    @Override
    public Long createPost(PostCreateReqVO reqVO) {
        // 校验正确性
        validatePostForCreateOrUpdate(null, reqVO.getName(), reqVO.getOrgId());

        // 插入岗位
        PostDO post = PostConvert.INSTANCE.convert(reqVO);
        postMapper.insert(post);
        if (reqVO.getSource() != null && reqVO.getSource() == ProjectSourceEnum.PMS) {//项目管理平台
            return post.getId();
        }
        //同步建能通
        try {
            JntPost jntPost = new JntPost();
            jntPost.setOaOrgId(reqVO.getOrgId()); // TODO [Rz Liu]: 2023-09-12 暂使用默认值，建能通默认是放蓝海下面
            jntPost.setOperateType(OperateTypeEnum.ADD);
            jntPost.setOaPostId(Math.toIntExact(post.getId()));
            jntPost.setName(reqVO.getName());
//            jntPost.setCode(!hasText(reqVO.getCode()) ? null : reqVO.getCode());
            jntPost.setDescription(reqVO.getRemark());
            jntPost.setSource(SourceEnum.OA);
            jntPost.setOrderNumber(reqVO.getSort());
            jntBaseDataSyncService.syncPost(jntPost);
        } catch (Exception e) {
            throw new BusinessException("[JNT]操作失败，" + e.getLocalizedMessage());
        }
        return post.getId();
    }

    @Transactional
    @Override
    public void updatePost(PostUpdateReqVO reqVO) {
        // 校验正确性
        validatePostForCreateOrUpdate(reqVO.getId(), reqVO.getName(), reqVO.getOrgId());

        // 更新岗位
        PostDO updateObj = PostConvert.INSTANCE.convert(reqVO);
        postMapper.updateById(updateObj);
        if (reqVO.getSource() != null && reqVO.getSource() == ProjectSourceEnum.PMS) {//项目管理平台
            return;
        }
        //同步建能通
        try {
            JntPost jntPost = new JntPost();
            jntPost.setOaOrgId(reqVO.getOrgId());
            jntPost.setOperateType(OperateTypeEnum.UPDATE);
            jntPost.setOaPostId(Math.toIntExact(updateObj.getId()));
            jntPost.setName(updateObj.getName());
//            jntPost.setCode(!hasText(updateObj.getCode()) ? null : updateObj.getCode());
            jntPost.setDescription(updateObj.getRemark());
            jntPost.setSource(SourceEnum.OA);
            jntPost.setOrderNumber(updateObj.getSort());
            jntBaseDataSyncService.syncPost(jntPost);
        } catch (Exception e) {
            throw new BusinessException("[JNT]操作失败，" + e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public void deletePost(Long id, Boolean notSyncJnt) {
        // 校验是否存在
        validatePostExists(id);
        //岗位在使用不允许删除
        validatePostUser(id);
        // 删除部门
        postMapper.deleteById(id);
        if (notSyncJnt) {
            return;
        }
        //同步建能通
        try {
            JntPost jntPost = new JntPost();
            jntPost.setIds(id + "");
            jntPost.setOperateType(OperateTypeEnum.DELETE);
            jntBaseDataSyncService.syncPost(jntPost);
        } catch (Exception e) {
            throw new BusinessException("[JNT]操作失败，" + e.getLocalizedMessage());
        }
    }

    private void validatePostUser(Long id) {
        List<PostDO> postDOS = postMapper.selectList(new LambdaQueryWrapperX<PostDO>().eq(PostDO::getId, id));
        if (!CollUtil.isEmpty(postDOS)) {
            List<Long> collect1 = postDOS.stream().map(PostDO::getId).collect(Collectors.toList());
            boolean hasUser = userMapper.selectObjs(new LambdaQueryWrapperX<AdminUserDO>().in(AdminUserDO::getPostIds, collect1).eq(AdminUserDO::getStatus, 0)).stream().anyMatch(o -> true);
            if (hasUser) {
                throw new BusinessException("存在正在使用该岗位的用户，不允许删除");
            }

        }
    }

    private void validatePostForCreateOrUpdate(Long id, String name, Integer orgId) {
        // 校验自己存在
        validatePostExists(id);
        // 校验岗位名的唯一性
        validatePostNameUnique(id, name, orgId);
        // 校验岗位编码的唯一性
//        validatePostCodeUnique(id, code);
    }

    private void validatePostNameUnique(Long id, String name, Integer orgId) {
        PostDO post = postMapper.selectByParam(name, orgId);
        if (post == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.POST_NAME_DUPLICATE);
        }
        if (!post.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.POST_NAME_DUPLICATE);
        }
    }

//    private void validatePostCodeUnique(Long id, String code) {
//        PostDO post = postMapper.selectByCode(code);
//        if (post == null) {
//            return;
//        }
//        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
//        if (id == null) {
//            throw ServiceExceptionUtil.exception(ErrorCodeConstants.POST_CODE_DUPLICATE);
//        }
//        if (!post.getId().equals(id)) {
//            throw ServiceExceptionUtil.exception(ErrorCodeConstants.POST_CODE_DUPLICATE);
//        }
//    }

    private void validatePostExists(Long id) {
        if (id == null) {
            return;
        }
        if (postMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.POST_NOT_FOUND);
        }
    }

    @Override
    public List<PostDO> getPostList(Collection<Long> ids, Collection<Integer> statuses) {
        return postMapper.selectList(ids, statuses);
    }

    @Override
    public List<PostDO> getPostByPostName(String postName) {
        List<PostDO> postDOS = postMapper.selectList(new LambdaQueryWrapperX<PostDO>().eqIfPresent(PostDO::getName, postName));
        return postDOS;
    }

    @Override
    public List<PostDO> getPostByPostNames(Set<String> postNames) {
        if (CollectionUtils.isEmpty(postNames)) {
            return Collections.emptyList();
        }
        return postMapper.selectList(new LambdaQueryWrapperX<PostDO>().inIfPresent(PostDO::getName, postNames));
    }

    @Override
    public PageResult<PostRespVO> getPostPage(PostPageReqVO reqVO) {
        PageResult<PostRespVO> pageResult = new PageResult<>();
        PageResult<PostDO> result = postMapper.selectPage(reqVO);
        List<PostRespVO> postRespVOList = new ArrayList<>();
        if (null != result) {
            for (PostDO postDO : result.getList()) {
                PostRespVO postRespVO = PostConvert.INSTANCE.convert(postDO);
                if (null != postDO.getOrgId()) {
                    DeptDO deptDO = getDept(postDO.getOrgId());
                    if (ObjectUtils.isEmpty(deptDO)) {
                        continue;
                    }
                    postRespVO.setOrgName(deptDO.getName());
                }
                postRespVOList.add(postRespVO);
            }
        }
        pageResult.setTotal(result.getTotal());
        pageResult.setList(postRespVOList);
        return pageResult;
    }

    @Override
    public List<PostDO> getPostList(PostExportReqVO reqVO) {
        return postMapper.selectList(reqVO);
    }

    @Override
    public PostDO getPost(Long id) {
        return postMapper.selectById(id);
    }

    @Override
    public void validatePostList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得岗位信息
        List<PostDO> posts = postMapper.selectBatchIds(ids);
        Map<Long, PostDO> postMap = convertMap(posts, PostDO::getId);
        // 校验
        ids.forEach(id -> {
            PostDO post = postMap.get(id);
            if (post == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.POST_NOT_FOUND);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(post.getStatus())) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.POST_NOT_ENABLE, post.getName());
            }
        });
    }

    @Override
    public List<PostDO> getPostListByOrgId(Long orgId, Integer status) {
        List<Long> ids = allDeptFromRoot(orgId);
        log.info("deptids:{}", ids);
        return postMapper.selectList(new LambdaQueryWrapperX<PostDO>().in(PostDO::getOrgId, ids).eq(PostDO::getStatus,status));
    }
    /**
     * 获取顶级组织及其下的所有组织id
     */
    private List<Long>  allDeptFromRoot(Long orgId) {
        Long deptRootId = getRootDeptId(orgId);
        List<Long> resultList = new ArrayList<>();
        resultList.add(deptRootId);
        List<DeptDO> children = deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().eq(DeptDO::getParentId, deptRootId));
        if (CollUtil.isEmpty(children)) {
            return resultList;
        }
        children.forEach(deptDO -> {
            resultList.addAll(this.getChildrenIds(deptDO.getId()));
        });
        return resultList;
    }

    private List<Long> getChildrenIds(Long id) {
        List<Long> resultList = new ArrayList<>();
        resultList.add(id);
        List<DeptDO> children = deptMapper.selectList(new LambdaQueryWrapperX<DeptDO>().eq(DeptDO::getParentId, id));
        if (CollUtil.isEmpty(children)) {
            return resultList;
        }
        children.forEach(deptDO -> {
            resultList.addAll(this.getChildrenIds(deptDO.getId()));
        });
        return resultList;
    }

    @Override
    public void synchronousPost() {
        List<PostDO> postDOS = postMapper.selectList(new LambdaQueryWrapperX<PostDO>().eqIfPresent(PostDO::getOrgId, 163));
        postDOS.stream().forEach(s -> {
            //同步建能通
            try {
                JntPost jntPost = new JntPost();
                jntPost.setOaOrgId(s.getOrgId().intValue());
                jntPost.setOperateType(OperateTypeEnum.ADD);
                jntPost.setOaPostId(Math.toIntExact(s.getId()));
                jntPost.setCode("synchronous");
                jntPost.setName(s.getName());
                jntPost.setDescription(s.getRemark());
                jntPost.setSource(SourceEnum.OA);
                jntPost.setOrderNumber(s.getSort());
                jntBaseDataSyncService.syncPost(jntPost);
            } catch (Exception e) {

//                throw new BusinessException("[JNT]操作失败，" + e.getLocalizedMessage());
            }
        });
    }

    /**
     * 根据部门获取顶级部门id
     *
     * @param deptId
     * @return
     */
    public Long getRootDeptId(Long deptId) {
        DeptDO dept = getDept(deptId);
        if (dept == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_FOUND, deptId);
        }
        // 根据一级部门的等级进行判断是否达到一级部门
        if (dept.getParentId() == 0) {
            return dept.getId(); // 已达到一级部门，返回部门ID
        } else {
            // 递归调用，传入父级部门对象进行下一级别的查询
            return getRootDeptId(getDept(deptId).getParentId());
        }
    }

    private DeptDO getDept(Long id) {
        return deptMapper.selectById(id);
    }

}
