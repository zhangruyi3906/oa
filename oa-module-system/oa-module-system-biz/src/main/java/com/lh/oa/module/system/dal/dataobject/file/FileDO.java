package com.lh.oa.module.system.dal.dataobject.file;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 文件表
 * 每次文件上传，都会记录一条记录到该表中
 *
 * @author
 */
@TableName("infra_file")
//("infra_file_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDO extends BaseDO {

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