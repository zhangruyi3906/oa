package com.lh.oa.module.system.controller.admin.file.preview.impl;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.controller.admin.file.preview.service.PreviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.lh.oa.framework.common.pojo.CommonResult.error;

/**
 * 压缩文件预览
 *
 * @author Rz Liu
 * @date 2023-02-01
 */
@Slf4j
@AllArgsConstructor
@Service
public class CompressPreviewImpl implements PreviewService {

    @Override
    public CommonResult<String> previewHandle(String url, String fileSuffix, String loginUserName) {
        return error(500, "此文件类型暂不支持预览");
    }
}
