package com.lh.oa.module.system.service.file;


import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.file.vo.file.FilePageReqVO;
import com.lh.oa.module.system.controller.admin.file.vo.file.FileRespVO;
import com.lh.oa.module.system.controller.admin.file.vo.file.FileUploadReqVO;
import com.lh.oa.module.system.dal.dataobject.file.FileDO;
import com.lh.oa.module.system.vo.FileVo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 文件 Service 接口
 *
 * @author
 */
public interface FileService {

    /**
     * 获得文件分页
     *
     * @param pageReqVO 分页查询
     * @return 文件分页
     */
    PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO);

    /**
     * 获得公共文件分页
     *
     * @param pageReqVO 分页查询
     * @return 文件分页
     */
    PageResult<FileRespVO> getPublicFilePage(FilePageReqVO pageReqVO);

    /**
     * 获得流程文件分页
     *
     * @param pageReqVO 分页查询
     * @return 文件分页
     */
    PageResult<FileRespVO> getProcedureFilePage(FilePageReqVO pageReqVO);



    /**
     * 获得客户文件分页
     *
     * @param pageReqVO 分页查询
     * @return 文件分页
     */
    PageResult<FileRespVO> getCustomerFilePage(FilePageReqVO pageReqVO);

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param name 原文件名称
     * @param path 文件路径
     * @param content 文件内容
     * @return 文件路径
     */
    FileVo createFile(String name, String path, byte[] content, FileUploadReqVO uploadReqVO);

    /**
     * 删除文件
     *
     * @param id 编号
     */
    void deleteFile(Long id) throws Exception;

    /**
     * 获得文件内容
     *
     * @param configId 配置编号
     * @param path 文件路径
     * @return 文件内容
     */
    byte[] getFileContent(Long configId, String path) throws Exception;

    HashSet<String> getCustomer();

    List<FileDO> getContractByCustomerId(String customerName);

    CommonResult<String> preview(String url);

    List<FileDO> getFileByProcessInstanceIdAndFormId(String processInstanceId, Long formId);

    List<FileDO> getFileByUrls(Set<String> paths);
}
