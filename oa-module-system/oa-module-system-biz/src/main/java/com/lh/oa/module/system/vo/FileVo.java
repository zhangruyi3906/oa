package com.lh.oa.module.system.vo;

import com.lh.oa.module.system.dal.dataobject.file.FileConfigDO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangfan
 * @since 2024/1/13 16:50
 */
@Data
public class FileVo implements Serializable {

    /**
     * 编号，数据库自增
     */
    private Long id;
    /**
     * 配置编号
     *
     * 关联 {@link FileConfigDO#getId()}
     */
    private Long configId;
    /**
     * 原文件名
     */
    private String name;
    /**
     * 路径，即文件名
     */
    private String path;
    /**
     * 访问地址
     */
    private String url;
    /**
     * 文件的 MIME 类型，例如 "application/octet-stream"
     */
    private String type;
    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 是否为个人可公开文件
     */
    private Boolean userPublic;
    /**
     * 是否为部门可公开文件
     */
    private Boolean deptPublic;
    /**
     * 分类名称
     */
    private String typeName;
    /**
     * 部门ID
     */
    private Long deptId;

    private Integer typeEn;

    private Boolean projectPublic;

    private Long projectId;

    private String source;
    /**
     * 上传人
     */
    private String wind;

    private Long windId;

    private String customerName;

    private String viewUrl;
}
