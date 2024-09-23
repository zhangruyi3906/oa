package com.lh.oa.module.system.api.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Schema(description = "RPC 服务 - 文件修改 Request DTO")
@Data
public class FileUpdateReqDTO {

    /**
     * 编号，数据库自增
     */
    private Long id;
    /**
     * 路径，即文件名
     */
    private Set<String> urls;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 流程名字
     */
    private String instanceName;
    /**
     * 表单id
     */
    private Long formId;

}
