package com.lh.oa.module.system.dal.mysql.dept;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.dept.vo.post.PostExportReqVO;
import com.lh.oa.module.system.controller.admin.dept.vo.post.PostPageReqVO;
import com.lh.oa.module.system.dal.dataobject.dept.PostDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface PostMapper extends BaseMapperX<PostDO> {

    default List<PostDO> selectList(Collection<Long> ids, Collection<Integer> statuses) {
        return selectList(new LambdaQueryWrapperX<PostDO>()
                .inIfPresent(PostDO::getId, ids)
                .inIfPresent(PostDO::getStatus, statuses));
    }

    default PageResult<PostDO> selectPage(PostPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PostDO>()
//                .likeIfPresent(PostDO::getCode, reqVO.getCode())
                .likeIfPresent(PostDO::getName, reqVO.getName())
                .eqIfPresent(PostDO::getStatus, reqVO.getStatus())
                .eqIfPresent(PostDO::getOrgId, reqVO.getOrgId())
                .orderByAsc(PostDO::getSort));
    }

    default List<PostDO> selectList(PostExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<PostDO>()
//                .likeIfPresent(PostDO::getCode, reqVO.getCode())
                .likeIfPresent(PostDO::getName, reqVO.getName())
                .eqIfPresent(PostDO::getStatus, reqVO.getStatus()));
    }

    default PostDO selectByName(String name) {
        return selectOne(PostDO::getName, name);
    }
    default PostDO selectByParam(String name,Integer orgId) {
        return selectOne(PostDO::getName, name, PostDO::getOrgId,orgId);
    }

//    default PostDO selectByCode(String code) {
//        return selectOne(PostDO::getCode, code);
//    }

    default List<PostDO> selectList(Long orgId,Integer status) {
        return selectList(new LambdaQueryWrapperX<PostDO>()
                .eqIfPresent(PostDO::getOrgId, orgId)
                .eqIfPresent(PostDO::getDeleted,0)
                .eqIfPresent(PostDO::getStatus, status));
    }

}
