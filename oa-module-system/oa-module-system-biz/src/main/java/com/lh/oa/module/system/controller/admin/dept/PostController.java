package com.lh.oa.module.system.controller.admin.dept;

import com.lh.oa.framework.common.enums.CommonStatusEnum;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.excel.core.util.ExcelUtils;
import com.lh.oa.module.system.api.dept.dto.DeptRespDTO;
import com.lh.oa.module.system.controller.admin.dept.vo.post.PostCreateReqVO;
import com.lh.oa.module.system.controller.admin.dept.vo.post.PostExcelVO;
import com.lh.oa.module.system.controller.admin.dept.vo.post.PostExportReqVO;
import com.lh.oa.module.system.controller.admin.dept.vo.post.PostPageReqVO;
import com.lh.oa.module.system.controller.admin.dept.vo.post.PostRespVO;
import com.lh.oa.module.system.controller.admin.dept.vo.post.PostSimpleRespVO;
import com.lh.oa.module.system.controller.admin.dept.vo.post.PostUpdateReqVO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserPressVO;
import com.lh.oa.module.system.convert.dept.PostConvert;
import com.lh.oa.module.system.dal.dataobject.dept.PostDO;
import com.lh.oa.module.system.service.dept.DeptService;
import com.lh.oa.module.system.service.dept.PostService;
import com.lh.oa.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 岗位")
@RestController
@RequestMapping("/system/post")
@Validated
public class PostController {

    @Resource
    private PostService postService;

    @Resource
    private AdminUserService adminUserService;
    @Resource
    private DeptService deptService;

    @PostMapping("/create")
    ////@Operation(summary = "创建岗位")
    @PreAuthorize("@ss.hasPermission('system:post:create')")
    public CommonResult<Long> createPost(@Valid @RequestBody PostCreateReqVO reqVO) {
        Long postId = postService.createPost(reqVO);
        return success(postId);
    }

    @PutMapping("/update")
    ////@Operation(summary = "修改岗位")
    @PreAuthorize("@ss.hasPermission('system:post:update')")
    public CommonResult<Boolean> updatePost(@Valid @RequestBody PostUpdateReqVO reqVO) {
        postService.updatePost(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    ////@Operation(summary = "删除岗位")
    @PreAuthorize("@ss.hasPermission('system:post:delete')")
    public CommonResult<Boolean> deletePost(@RequestParam("id") Long id,@RequestParam(value = "notSyncJnt",required = false,defaultValue = "false")Boolean notSyncJnt) {
        postService.deletePost(id,notSyncJnt);
        return success(true);
    }

    @GetMapping(value = "/get")
    ////@Operation(summary = "获得岗位信息")
    @Parameter(name = "id", description = "岗位编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:post:query')")
    public CommonResult<PostRespVO> getPost(@RequestParam("id") Long id) {
        return success(PostConvert.INSTANCE.convert(postService.getPost(id)));
    }

    @GetMapping("/list-all-simple")
    ////@Operation(summary = "获取岗位精简信息列表")
    @PermitAll
    public CommonResult<List<PostSimpleRespVO>> getSimplePosts() {
        // 获得岗位列表，只要开启状态的
        List<PostDO> list = postService.getPostList(null, Collections.singleton(CommonStatusEnum.ENABLE.getStatus()));
        // 排序后，返回给前端
        list.sort(Comparator.comparing(PostDO::getSort));
        List<PostSimpleRespVO> postSimpleRespVOS = PostConvert.INSTANCE.convertList02(list);
        List<DeptRespDTO> allDeptList = deptService.getAllDepts();
        Map<Long, String> deptMap = allDeptList.stream().collect(Collectors.toMap(DeptRespDTO::getId, DeptRespDTO::getName));
        postSimpleRespVOS.forEach(postSimpleRespVO -> {
            String orgName = deptMap.get(postSimpleRespVO.getOrgId());
            postSimpleRespVO.setOrgName(orgName);
            if(orgName.contains("蓝海")){
                orgName = "蓝海";
            }
            if(orgName.contains("安能达")){
                orgName = "安能达";
            }
            postSimpleRespVO.setPostOrgName(postSimpleRespVO.getName() + "-" + orgName);
        });
        return success(postSimpleRespVOS);
    }

    @GetMapping("/page")
    ////@Operation(summary = "获得岗位分页列表")
    @PreAuthorize("@ss.hasPermission('system:post:query')")
    public CommonResult<PageResult<PostRespVO>> getPostPage(@Validated PostPageReqVO reqVO) {
        return success(postService.getPostPage(reqVO));
    }

    @GetMapping("/export")
    ////@Operation(summary = "岗位管理")
    @PreAuthorize("@ss.hasPermission('system:post:export')")
    //@Operation(type = EXPORT)
    public void export(HttpServletResponse response, @Validated PostExportReqVO reqVO) throws IOException {
        List<PostDO> posts = postService.getPostList(reqVO);
        List<PostExcelVO> data = PostConvert.INSTANCE.convertList03(posts);
        // 输出
        ExcelUtils.write(response, "岗位数据.xls", "岗位列表", PostExcelVO.class, data);
    }

    @GetMapping("/list-post-simple")
    @PermitAll
    ////@Operation(summary = "获取岗位精简信息列表")
    public CommonResult<List<UserPressVO>> getPreePosts() {
        // 获得岗位列表，只要开启状态的
        List<PostDO> list = postService.getPostList(null, Collections.singleton(CommonStatusEnum.ENABLE.getStatus()));
        // 排序后，返回给前端
        list.sort(Comparator.comparing(PostDO::getSort));
        List<PostSimpleRespVO> postSimpleRespVOS = PostConvert.INSTANCE.convertList02(list);
        ArrayList<UserPressVO> list1 = new ArrayList<>();
        postSimpleRespVOS.forEach(s -> {
            UserPressVO userPressVO = new UserPressVO();
            userPressVO.setLabel(s.getName());
            userPressVO.setValue(s.getId());
            list1.add(userPressVO);
        });

        return success(list1);
    }
    @GetMapping("/list-all")
    @PermitAll
    public CommonResult<List<PostSimpleRespVO>> getSimplePosts(@RequestParam("orgId") Long orgId) {
        // 获得岗位列表，只要开启状态的
        List<PostDO> list = postService.getPostListByOrgId(orgId, CommonStatusEnum.ENABLE.getStatus());
        // 排序后，返回给前端
        list.sort(Comparator.comparing(PostDO::getSort));
        return success(PostConvert.INSTANCE.convertList02(list));
    }

    @GetMapping("/synchronous")
    @PermitAll
    public CommonResult synchronousPost() {
        postService.synchronousPost();
        adminUserService.batchUpdatePost();
        return success("同步成功");
    }

}