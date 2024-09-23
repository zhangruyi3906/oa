package com.lh.oa.module.infra.service.file;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.io.FileUtils;
import com.lh.oa.framework.file.core.client.FileClient;
import com.lh.oa.framework.file.core.utils.FileTypeUtils;
import com.lh.oa.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import com.lh.oa.module.infra.controller.admin.file.vo.file.FileUploadReqVO;
import com.lh.oa.module.infra.dal.dataobject.file.FileDO;
import com.lh.oa.module.infra.dal.mysql.file.FileMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Optional;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.module.infra.enums.ErrorCodeConstants.FILE_NOT_EXISTS;

/**
 * 文件 Service 实现类
 *
 * @author
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileConfigService fileConfigService;

    @Resource
    private FileMapper fileMapper;

    @Override
    public PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO) {
        return fileMapper.selectPage(pageReqVO);
    }

    @Override
    @SneakyThrows
    public String createFile(String name, String path, byte[] content, FileUploadReqVO uploadReqVO) {
        String type = FileTypeUtils.getMineType(content, name);
        if (CharSequenceUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        if (CharSequenceUtil.isEmpty(name)) {
            name = path;
        }

        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);

//        FileDO file = new FileDO();
//        file.setConfigId(client.getId());
//        file.setName(name);
//        file.setPath(path);
//        file.setUrl(url);
//        file.setType(type);
//        file.setSize(content.length);
        FileDO file = Optional.ofNullable(FileDO
                .builder()
                .configId(client.getId())
                .name(name)
                .path(path)
                .url(url)
                .type(type)
                .deptId(uploadReqVO.getDeptId())
                .typeName(uploadReqVO.getTypeName())
                .userPublic(uploadReqVO.getUserPublic())
                .deptPublic(uploadReqVO.getDeptPublic())
                .projectPublic(uploadReqVO.getProjectPublic())
                .projectId(uploadReqVO.getProjectId())
                .typeEn(uploadReqVO.getTypeEn())
                .size(content.length)
                .build())
                .orElse(new FileDO());
        fileMapper.insert(file);
        return url;
    }

    @Override
    public void deleteFile(Long id) throws Exception {
        // 校验存在
        FileDO file = validateFileExists(id);

        FileClient client = fileConfigService.getFileClient(file.getConfigId());
        Assert.notNull(client, "客户端({}) 不能为空", file.getConfigId());
        client.delete(file.getPath());

        fileMapper.deleteById(id);
    }

    private FileDO validateFileExists(Long id) {
        FileDO fileDO = fileMapper.selectById(id);
        if (fileDO == null) {
            throw exception(FILE_NOT_EXISTS);
        }
        return fileDO;
    }

    @Override
    public byte[] getFileContent(Long configId, String path) throws Exception {
        FileClient client = fileConfigService.getFileClient(configId);
        Assert.notNull(client, "客户端({}) 不能为空", configId);
        return client.getContent(path);
    }

}
