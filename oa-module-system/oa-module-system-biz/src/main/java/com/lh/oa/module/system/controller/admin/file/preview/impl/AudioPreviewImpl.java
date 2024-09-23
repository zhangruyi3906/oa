package com.lh.oa.module.system.controller.admin.file.preview.impl;

import cn.hutool.core.util.ArrayUtil;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.module.system.controller.admin.file.preview.service.PreviewService;
import com.lh.oa.module.system.enums.OAConstant;
import com.lh.oa.module.system.enums.file.config.KkConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.lh.oa.framework.common.pojo.CommonResult.error;
import static com.lh.oa.framework.common.pojo.CommonResult.success;

/**
 * 视频预览
 *
 * @author Rz Liu
 * @date 2023-02-01
 */
@Slf4j
@AllArgsConstructor
@Service
public class AudioPreviewImpl implements PreviewService {

    @Override
    public CommonResult<String> previewHandle(String url, String fileSuffix, String loginUserName) {
        if (!ArrayUtil.contains(OAConstant.AUDIO_PREVIEW_EXT, fileSuffix)) {
            return error(500, "此文件类型暂不支持预览");
        }
        url = KkConfig.server() + KkConfig.SEP_URL + url + KkConfig.SEP_WATERMARK + loginUserName;
        return success(url);
    }
}
