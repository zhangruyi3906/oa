package com.lh.oa.module.system.controller.admin.file.preview.service;

import com.lh.oa.framework.common.pojo.CommonResult;

/**
 * 文件预览
 *
 * @author Rz Liu
 * @date 2023-01-30
 */
public interface PreviewService {

    /**
     * 预览
     */
    CommonResult<String> previewHandle(String url, String fileSuffix, String loginUserName);


}
