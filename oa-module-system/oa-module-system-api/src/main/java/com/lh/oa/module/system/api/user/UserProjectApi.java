package com.lh.oa.module.system.api.user;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.api.user.dto.UserProjectTo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(value = "system-server", path = "/admin-api/system/user-project", contextId = "UserProjectApi")
@Tag(name = "RPC服务 - 用户项目关联关系")
public interface UserProjectApi {

    @GetMapping("/getByUserIds")
    CommonResult<List<UserProjectTo>> getByUserIds(@RequestParam("userIds") Set<Long> userIds);

}
