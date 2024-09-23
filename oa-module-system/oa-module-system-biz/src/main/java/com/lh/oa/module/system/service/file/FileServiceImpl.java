package com.lh.oa.module.system.service.file;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.io.FileUtils;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.file.core.client.FileClient;
import com.lh.oa.framework.file.core.utils.FileTypeUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.api.task.BpmProcessInstanceExtService;
import com.lh.oa.module.system.controller.admin.file.preview.impl.PreviewFactory;
import com.lh.oa.module.system.controller.admin.file.preview.service.PreviewService;
import com.lh.oa.module.system.controller.admin.file.vo.file.FilePageReqVO;
import com.lh.oa.module.system.controller.admin.file.vo.file.FileRespVO;
import com.lh.oa.module.system.controller.admin.file.vo.file.FileUploadReqVO;
import com.lh.oa.module.system.convert.file.FileConvert;
import com.lh.oa.module.system.dal.dataobject.file.FileDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.mysql.file.FileMapper;
import com.lh.oa.module.system.enums.file.FileType;
import com.lh.oa.module.system.service.user.AdminUserService;
import com.lh.oa.module.system.vo.FileVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.lh.oa.module.infra.enums.ErrorCodeConstants.FILE_NOT_EXISTS;

/**
 * 文件 Service 实现类
 *
 * @author
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Resource
    private FileConfigService fileConfigService;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private AdminUserService adminUserService;
    @Resource
    private PreviewFactory previewFactory;
    @Resource
    private BpmProcessInstanceExtService bpmProcessInstanceExtService;

    @Override
    public PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO) {
        return fileMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<FileRespVO> getPublicFilePage(FilePageReqVO pageReqVO) {
        AdminUserDO user = adminUserService.getUser(getLoginUserId());
        pageReqVO.setDeptId(user.getDeptId());
        PageResult<FileDO> fileDOPageResult = fileMapper.selectPublicPage(pageReqVO);
        PageResult<FileRespVO> fileRespVOPageResult = FileConvert.INSTANCE.convertPage(fileDOPageResult);
        List<FileRespVO> fileRespVOList = fileRespVOPageResult.getList();
        fileRespVOList = fileRespVOList.stream().map(fileRespVO -> {
            fileRespVO.setSizeMb(new BigDecimal(fileRespVO.getSize() / 1024.0 / 1024.0).setScale(2, BigDecimal.ROUND_HALF_UP));
            return fileRespVO;
        }).collect(Collectors.toList());
        return fileRespVOPageResult.setList(fileRespVOList);
    }

    @Override
    public PageResult<FileRespVO> getProcedureFilePage(FilePageReqVO pageReqVO) {
        Long loginUserId = getLoginUserId();
        CommonResult<Set<String>> allProcessInstanceIds = bpmProcessInstanceExtService.getAllProcessInstanceIdsByUserId(loginUserId);
        Set<String> processInstanceIdSet = allProcessInstanceIds.getData();
        if (CollectionUtils.isEmpty(processInstanceIdSet)) {
            return PageResult.empty();
        }
        pageReqVO.setProcessInstanceIdSet(processInstanceIdSet);

        PageResult<FileDO> fileDOPageResult = fileMapper.selectProcedurePage(pageReqVO, loginUserId);
        PageResult<FileRespVO> fileRespVOPageResult = FileConvert.INSTANCE.convertPage(fileDOPageResult);
        List<FileRespVO> fileRespVOList = fileRespVOPageResult.getList();
        fileRespVOList = fileRespVOList.stream().map(fileRespVO -> {
            fileRespVO.setSizeMb(new BigDecimal(fileRespVO.getSize() / 1024.0 / 1024.0).setScale(2, BigDecimal.ROUND_HALF_UP));
            return fileRespVO;
        }).collect(Collectors.toList());
        return fileRespVOPageResult.setList(fileRespVOList);
    }

    @Override
    public PageResult<FileRespVO> getCustomerFilePage(FilePageReqVO pageReqVO) {
        PageResult<FileDO> fileDOPageResult = fileMapper.selectCustomerPage(pageReqVO);
        PageResult<FileRespVO> fileRespVOPageResult = FileConvert.INSTANCE.convertPage(fileDOPageResult);
        List<FileRespVO> fileRespVOList = fileRespVOPageResult.getList();
        fileRespVOList = fileRespVOList.stream().map(fileRespVO -> {
            fileRespVO.setSizeMb(new BigDecimal(fileRespVO.getSize() / 1024.0 / 1024.0).setScale(2, BigDecimal.ROUND_HALF_UP));
            return fileRespVO;
        }).collect(Collectors.toList());
        return fileRespVOPageResult.setList(fileRespVOList);
    }

    @Override
    @SneakyThrows
    public FileVo createFile(String name, String path, byte[] content, FileUploadReqVO uploadReqVO) {
        ExceptionThrowUtils.throwIfTrue(name.matches(".*[\\#\\@\\$\\￥\\%\\&\\*\\+].*"), "文件名中不能包含特殊符号");

        String type = FileTypeUtils.getMineType(content, name);
        if (Objects.equals("application/pdf", type) || Objects.equals("text/plain", type)) {
            type = "application/octet-stream";
        }

        if (CharSequenceUtil.isEmpty(path)) {

            if (uploadReqVO.getSource() == null || "".equals(uploadReqVO.getSource())) {
                path = FileUtils.generatePath(content, name);
            } else {
                path = FileUtils.generatePathBySourse(content, name, uploadReqVO.getSource());
            }
        }
        if (CharSequenceUtil.isEmpty(name)) {
            name = path;
        }
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);

        AdminUserDO user = adminUserService.getUser(getLoginUserId());
        FileDO file = Optional.ofNullable(FileDO
                .builder()
                .configId(client.getId())
                .name(name)
                .path(path)
                .url(url)
                .type(type)
                .deptId(user.getDeptId())
                .typeName(uploadReqVO.getTypeName())
                .userPublic(uploadReqVO.getUserPublic())
                .deptPublic(uploadReqVO.getDeptPublic())
                .projectPublic(uploadReqVO.getProjectPublic())
                .projectId(uploadReqVO.getProjectId())
                .typeEn(uploadReqVO.getTypeEn())
                .source(uploadReqVO.getSource())
                .wind(user.getNickname())
                .windId(user.getId())
                .size(content.length)
                .build())
                .orElse(new FileDO());
        fileMapper.insert(file);

        return JsonUtils.covertObject(file, FileVo.class);
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

    @Override
    public HashSet<String> getCustomer() {
        List<FileDO> customer = fileMapper.getCustomer();
        HashSet<String> customerNames = customer.stream()
                .map(FileDO::getCustomerName)
                .collect(Collectors.toCollection(HashSet::new));
        return customerNames;
    }

    @Override
    public List<FileDO> getContractByCustomerId(String customerName) {

        return fileMapper.getContractByCustomerId(customerName);
    }

    @Override
    public CommonResult<String> preview(String url) {
        if (StringUtils.isBlank(url)) {
            log.error("url: {}", url);
            throw new BusinessException("预览文件地址不能为空");
        }
        int suffixIndex = url.lastIndexOf(".");
        if (suffixIndex == -1) {
            log.error("url: {}", url);
            throw new BusinessException("预览文件地址错误");
        }
        String fileSuffix = url.substring(suffixIndex + 1);
        // 区分类型
        PreviewService previewService = previewFactory.get(FileType.get(fileSuffix));
        Long loginUserId = getLoginUserId();
        String loginUserName = "";
        if (loginUserId!= null) {
            AdminUserDO user = adminUserService.getUser(loginUserId);
            if (user!= null) {
                loginUserName = user.getNickname();
            }
        }
        return previewService.previewHandle(url, fileSuffix, loginUserName);
    }

    @Override
    public List<FileDO> getFileByProcessInstanceIdAndFormId(String processInstanceId, Long formId) {
        List<FileDO> fileDOList = fileMapper.selectList(new LambdaQueryWrapperX<FileDO>().eq(FileDO::getProcessInstanceId, processInstanceId).eq(FileDO::getFormId, formId));
        return fileDOList;
    }

    @Override
    public List<FileDO> getFileByUrls(Set<String> urls) {
        List<FileDO> fileDOList = fileMapper.selectList(new LambdaQueryWrapperX<FileDO>().in(FileDO::getUrl, urls));
        Map<String, FileDO> fileDOMap = fileDOList.stream()
                .collect(Collectors.toMap(FileDO::getUrl, fileDO -> fileDO));
        fileDOList = new ArrayList<>(fileDOMap.values());

        return fileDOList;
    }

}
