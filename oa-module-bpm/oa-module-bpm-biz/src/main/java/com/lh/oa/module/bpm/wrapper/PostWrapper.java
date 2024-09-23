package com.lh.oa.module.bpm.wrapper;

import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.module.system.api.dept.PostApi;
import com.lh.oa.module.system.api.dept.dto.PostTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author tanghanlin
 * @since 2023/12/15
 */
@Slf4j
@Component
public class PostWrapper {

    @Resource
    private PostApi postApi;

    public List<PostTO> getPostList(Set<Long> postIds) {
        log.info("根据ids获取岗位数据, postIds:{}", postIds);
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        CommonResult<List<PostTO>> deptResult = postApi.getPostList(postIds);
        log.info("根据ids获取岗位数据-返回结果, postIds:{}, result:{}", postIds, JsonUtils.toJsonString(deptResult));
        ExceptionThrowUtils.throwIfTrue(
                !Objects.equals(HttpStatus.OK.value(), deptResult.getStatus()) || Objects.isNull(deptResult.getData()),
                "系统内部错误，请联系管理员");
        return deptResult.getData();
    }

}
