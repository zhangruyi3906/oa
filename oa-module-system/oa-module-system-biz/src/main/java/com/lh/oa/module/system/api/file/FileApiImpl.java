package com.lh.oa.module.system.api.file;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.api.file.dto.FileUpdateReqDTO;
import com.lh.oa.module.system.dal.dataobject.file.FileDO;
import com.lh.oa.module.system.dal.mysql.file.FileMapper;
import com.lh.oa.module.system.service.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.pojo.CommonResult.success;
import static com.lh.oa.module.system.enums.ApiConstants.VERSION;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
@Slf4j
public class FileApiImpl implements FileApi {

    @Resource
    private FileService fileService;
    @Resource
    private FileMapper fileMapper;


    @Override
    public CommonResult<String> updateFile(FileUpdateReqDTO updateReqDTO) {
        List<FileDO> beforeFileDOList = fileService.getFileByProcessInstanceIdAndFormId(updateReqDTO.getProcessInstanceId(), updateReqDTO.getFormId());
        Set<String> urls = updateReqDTO.getUrls();
        if (CollectionUtils.isNotEmpty(beforeFileDOList)) {
            Set<String> beforeUrlSet = beforeFileDOList.stream().map(FileDO::getUrl).collect(Collectors.toSet());
            beforeUrlSet.removeAll(urls);
            Set<Long> deleteFileIdSet = beforeFileDOList.stream().filter(FileDO -> beforeUrlSet.contains(FileDO.getUrl())).map(FileDO::getId).collect(Collectors.toSet());
            log.info("删除文件idSet deleteFileIdSet: {}", deleteFileIdSet);
            fileMapper.deleteBatchIds(deleteFileIdSet);
        }
        List<FileDO> fileDOList = fileService.getFileByUrls(urls);
        if (CollectionUtils.isNotEmpty(fileDOList)) {
            Set<Long> fileIdSet = fileDOList.stream().map(FileDO::getId).collect(Collectors.toSet());
            FileDO fileDO = new FileDO()
                    .setInstanceName(updateReqDTO.getInstanceName())
                    .setProcessInstanceId(updateReqDTO.getProcessInstanceId())
                    .setFormId(updateReqDTO.getFormId());
            fileMapper.update(fileDO, new LambdaQueryWrapperX<FileDO>().inIfPresent(FileDO::getId, fileIdSet));
        }
        return success();
    }


}
