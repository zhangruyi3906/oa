package com.lh.oa.module.bpm.wrapper;

import com.lh.oa.module.bpm.api.definition.to.BpmProcessDefinitionFromOptionsTo;
import com.lh.oa.module.system.api.dept.dto.DeptRespDTO;
import com.lh.oa.module.system.api.dept.dto.PostTO;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author tanghanlin
 * @since 2023/12/15
 */
@Slf4j
@Component
public class ProcessDefinitionValueTransWrapper {

    @Resource
    private AdminUserWrapper adminUserWrapper;

    @Resource
    private DeptWrapper deptWrapper;

    @Resource
    private PostWrapper postWrapper;

    /**
     * 获取考勤表单的下拉数据
     *
     * @param sourceType 查询类型
     * @param ids        ids
     * @return 下拉数据
     */
    public List<BpmProcessDefinitionFromOptionsTo> getAttendanceOptionList(String sourceType, Set<Long> ids) {
        log.info("获取考勤表单的下拉数据, sourceType:{}, ids:{}", sourceType, ids);
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<BpmProcessDefinitionFromOptionsTo> result = new LinkedList<>();
        switch (sourceType) {
            case "user":
                List<AdminUserRespDTO> userList = adminUserWrapper.getByIds(ids);
                userList.forEach(user -> result.add(new BpmProcessDefinitionFromOptionsTo(user.getId().toString(), user.getNickname())));
                break;
            case "dept":
                List<DeptRespDTO> deptList = deptWrapper.getDeptList(ids);
                deptList.forEach(dept -> result.add(new BpmProcessDefinitionFromOptionsTo(dept.getId().toString(), dept.getName())));
                break;
            case "post":
                List<PostTO> postList = postWrapper.getPostList(ids);
                postList.forEach(post -> result.add(new BpmProcessDefinitionFromOptionsTo(post.getId().toString(), post.getName())));
                break;
            default:
                break;
        }
        return result;
    }

}
