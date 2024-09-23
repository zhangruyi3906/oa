package com.lh.oa.module.system.api.file;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.api.file.dto.FileUpdateReqDTO;
import com.lh.oa.module.system.enums.ApiConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = ApiConstants.NAME) //
@Tag(name =  "RPC 服务 - 文件")
public interface FileApi {

    String PREFIX = ApiConstants.PREFIX + "/file";



    @PutMapping(PREFIX + "/update")
    CommonResult<String> updateFile(@Valid @RequestBody FileUpdateReqDTO updateReqDTO);

}