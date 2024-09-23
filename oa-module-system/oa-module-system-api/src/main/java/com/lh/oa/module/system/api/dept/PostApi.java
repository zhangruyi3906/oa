package com.lh.oa.module.system.api.dept;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.api.dept.dto.PostTO;
import com.lh.oa.module.system.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@FeignClient(name = ApiConstants.NAME) //
@Tag(name = "RPC 服务 - 岗位")
public interface PostApi {

    String PREFIX = ApiConstants.PREFIX + "/post";

    @GetMapping(PREFIX + "/valid")
    //@Operation(summary = "校验岗位是否合法")
    @Parameter(name = "ids", description = "岗位编号数组", example = "1,2", required = true)
    CommonResult<Boolean> validPostList(@RequestParam("ids") Collection<Long> ids);

    @GetMapping(PREFIX + "/getPostList")
    CommonResult<List<PostTO>> getPostList(@RequestParam("ids") Collection<Long> ids);

}
