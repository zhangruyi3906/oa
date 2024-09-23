package com.lh.oa.module.system.controller.admin.file.preview.impl;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.controller.admin.file.preview.service.PreviewService;
import com.lh.oa.module.system.enums.file.config.KkConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

/**
 * 文档预览
 *
 * @author Rz Liu
 * @date 2023-01-30
 */
@Slf4j
@AllArgsConstructor
@Service
public class DocPreviewImpl implements PreviewService {

    @Override
    public CommonResult<String> previewHandle(String url, String fileSuffix, String loginUserName) {
        url = KkConfig.server() + KkConfig.SEP_URL + url + KkConfig.SEP_WATERMARK + loginUserName;
        return success(url);
    }
}
