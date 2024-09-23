package com.lh.oa.module.system.service.dept;

import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.dept.vo.post.*;
import com.lh.oa.module.system.dal.dataobject.dept.PostDO;
import org.springframework.lang.Nullable;

import java.util.*;

import static com.lh.oa.framework.common.util.collection.SetUtils.asSet;

/**
 * 岗位 Service 接口
 *
 * @author
 */
public interface PostService {

    /**
     * 创建岗位
     *
     * @param reqVO 岗位信息
     * @return 岗位编号
     */
    Long createPost(PostCreateReqVO reqVO);

    /**
     * 更新岗位
     *
     * @param reqVO 岗位信息
     */
    void updatePost(PostUpdateReqVO reqVO);

    /**
     * 删除岗位信息
     *
     * @param id 岗位编号
     */
    void deletePost(Long id,Boolean notSyncJnt);

    /**
     * 获得岗位列表
     *
     * @param ids 岗位编号数组。如果为空，不进行筛选
     * @return 部门列表
     */
    default List<PostDO> getPostList(@Nullable Collection<Long> ids) {
        if (org.springframework.util.CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return getPostList(ids, asSet(CommonStatusEnum.ENABLE.getStatus(), CommonStatusEnum.DISABLE.getStatus()));
    }

    /**
     * 获得符合条件的岗位列表
     *
     * @param ids 岗位编号数组。如果为空，不进行筛选
     * @param statuses 状态数组。如果为空，不进行筛选
     * @return 部门列表
     */
    List<PostDO> getPostList(@Nullable Collection<Long> ids, @Nullable Collection<Integer> statuses);

    List<PostDO> getPostByPostName(String postName);

    List<PostDO> getPostByPostNames(Set<String> postNames);

    /**
     * 获得岗位分页列表
     *
     * @param reqVO 分页条件
     * @return 部门分页列表
     */
    PageResult<PostRespVO> getPostPage(PostPageReqVO reqVO);

    /**
     * 获得岗位列表
     *
     * @param reqVO 查询条件
     * @return 部门列表
     */
    List<PostDO> getPostList(PostExportReqVO reqVO);

    /**
     * 获得岗位信息
     *
     * @param id 岗位编号
     * @return 岗位信息
     */
    PostDO getPost(Long id);

    /**
     * 校验岗位们是否有效。如下情况，视为无效：
     * 1. 岗位编号不存在
     * 2. 岗位被禁用
     *
     * @param ids 岗位编号数组
     */
    void validatePostList(Collection<Long> ids);

    /**
     * 根据组织查询岗位
     * @param orgId
     * @param status
     * @return
     */
    List<PostDO> getPostListByOrgId(Long orgId, Integer status);

    void synchronousPost();
}
