package com.lh.oa.module.system.controller.admin.file.preview.impl;

import com.lh.oa.module.system.controller.admin.file.preview.service.PreviewService;
import com.lh.oa.module.system.enums.file.FileType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Rz Liu
 * @date 2023-02-01
 */
@Slf4j
@AllArgsConstructor
@Service
public class PreviewFactory {
    private final ApplicationContext context;

    /**
     * 根据文件类型获取预览实例
     */
    public PreviewService get(FileType fileType) {
        Map<String, PreviewService> filePreviewMap = context.getBeansOfType(PreviewService.class);
        return filePreviewMap.get(fileType.insName());
    }

}
