package com.lh.oa.module.system.controller.admin.file;

import cn.hutool.core.io.IoUtil;
import com.lh.oa.framework.common.exception.ServiceException;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.servlet.ServletUtils;
import com.lh.oa.module.system.controller.admin.file.vo.file.FilePageReqVO;
import com.lh.oa.module.system.controller.admin.file.vo.file.FileRespVO;
import com.lh.oa.module.system.controller.admin.file.vo.file.FileUploadReqVO;
import com.lh.oa.module.system.convert.file.FileConvert;
import com.lh.oa.module.system.dal.dataobject.file.FileDO;
import com.lh.oa.module.system.dal.mysql.file.FileMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.enums.file.FileEnum;
import com.lh.oa.module.system.service.file.FileService;
import com.lh.oa.module.system.vo.FileVo;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@Tag(name =  "管理后台 - 文件存储")
@RestController
@RequestMapping("/system/file")
@Validated
@Slf4j
public class FileController {

    @Resource
    private FileService fileService;

    @Resource
    private FileMapper fileMapper;


    @PostMapping("/upload")
    //@Operation(summary = "上传文件")
//    @OperateLog(logArgs = false) //
    @SneakyThrows
    public CommonResult<FileVo> uploadFile(FileUploadReqVO uploadReqVO) {
        MultipartFile file = uploadReqVO.getFile();
        String path = uploadReqVO.getPath();
        uploadReqVO.setSource(FileEnum.COMMON.getSource());
        return success(fileService.createFile(file.getOriginalFilename(), path, IoUtil.readBytes(file.getInputStream()),uploadReqVO));
    }



    @PostMapping("/public/upload")
    //@Operation(summary = "上传公共文件")
//    @OperateLog(logArgs = false) //
    @SneakyThrows
    public CommonResult<FileVo> uploadPublicFile(FileUploadReqVO uploadReqVO) {
        MultipartFile file = uploadReqVO.getFile();
        String path = uploadReqVO.getPath();
        uploadReqVO.setSource(FileEnum.PUBLIC.getSource());
        return success(fileService.createFile(file.getOriginalFilename(), path, IoUtil.readBytes(file.getInputStream()),uploadReqVO));
    }


    @PostMapping("/procedure/upload")
    //@Operation(summary = "上传流程文件")
//    @OperateLog(logArgs = false) //
    @SneakyThrows
    public CommonResult<FileVo> uploadProcedureFile(FileUploadReqVO uploadReqVO) {
        MultipartFile file = uploadReqVO.getFile();
        String path = uploadReqVO.getPath();
        uploadReqVO.setSource(FileEnum.PROCEDURE.getSource());
        return success(fileService.createFile(file.getOriginalFilename(), path, IoUtil.readBytes(file.getInputStream()),uploadReqVO));
    }

    @PostMapping("/contract/upload")
    //@Operation(summary = "上传合同文件")
//    @OperateLog(logArgs = false) //
    @SneakyThrows
    public CommonResult<FileVo> uploadContractFile(FileUploadReqVO uploadReqVO) {
        MultipartFile file = uploadReqVO.getFile();
        String path = uploadReqVO.getPath();
        uploadReqVO.setSource(FileEnum.CONTRACT.getSource());
        return success(fileService.createFile(file.getOriginalFilename(), path, IoUtil.readBytes(file.getInputStream()),uploadReqVO));
    }


    @PostMapping("/customer/upload")
    //@Operation(summary = "上传客户文件")
//    @OperateLog(logArgs = false) //
    @SneakyThrows
    public CommonResult<FileVo> uploadCustomerFile(FileUploadReqVO uploadReqVO) {
        MultipartFile file = uploadReqVO.getFile();
        String path = uploadReqVO.getPath();
        uploadReqVO.setSource(FileEnum.CUSTOMER.getSource());
        return success(fileService.createFile(file.getOriginalFilename(), path, IoUtil.readBytes(file.getInputStream()),uploadReqVO));
    }


    @DeleteMapping("/delete")
    //@Operation(summary = "删除文件")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('infra:file:delete')")
    public CommonResult<Boolean> deleteFile(@RequestParam("id") Long id) throws Exception {
        fileService.deleteFile(id);
        return success(true);
    }

    @GetMapping("/{configId}/get")
    @PermitAll
    //@Operation(summary = "下载文件")
    @Parameter(name = "configId", description = "配置编号",  required = true)
    public void getFileContent(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable("configId") Long configId, @RequestParam("id") Long id) throws Exception {
        FileDO fileDO = fileMapper.selectById(id);
        if(fileDO == null) {
           throw new ServiceException(ErrorCodeConstants.FILE_NOT_EXISTS);
        }
        String path = fileDO.getPath();

        byte[] content = fileService.getFileContent(configId, path);
        if (content == null) {
            log.warn("[getFileContent][configId({}) path({}) 文件不存在]", configId, path);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        ServletUtils.writeAttachment(response, path, content);
    }

    @GetMapping("/public/page")
    //@Operation(summary = "获得公共文件分页")
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<PageResult<FileRespVO>> getPublicFilePage(@Valid FilePageReqVO pageVO) {
        return success(fileService.getPublicFilePage(pageVO.setSource(FileEnum.PUBLIC.getSource())));
    }

    @GetMapping("/procedure/page")
    //@Operation(summary = "获得流程文件分页")
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<PageResult<FileRespVO>> getProcedureFilePage(@Valid FilePageReqVO pageVO) {

        PageResult<FileRespVO> pageResult = fileService.getProcedureFilePage(pageVO.setSource(FileEnum.PROCEDURE.getSource()));
        return success(pageResult);
    }

    @GetMapping("/contract/page")
    //@Operation(summary = "获得合同文件分页")
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<PageResult<FileRespVO>> getContractFilePage(@Valid FilePageReqVO pageVO) {
        PageResult<FileDO> pageResult = fileService.getFilePage(pageVO.setSource(FileEnum.CONTRACT.getSource()));
        return success(FileConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/customer/page")
    //@Operation(summary = "获得客户文件分页")
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<PageResult<FileRespVO>> getCustomerFilePage(@Valid FilePageReqVO pageVO) {
        PageResult<FileRespVO> pageResult = fileService.getCustomerFilePage(pageVO.setSource(FileEnum.CUSTOMER.getSource()));
        return success(pageResult);
    }


    @GetMapping("/page")
    //@Operation(summary = "获得文件分页")
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<PageResult<FileRespVO>> getFilePage(@Valid FilePageReqVO pageVO) {

        PageResult<FileDO> pageResult = fileService.getFilePage(pageVO.setSource(FileEnum.COMMON.getSource()));
        return success(FileConvert.INSTANCE.convertPage(pageResult));
    }



    @GetMapping("/getCustomer")
    //@Operation(summary = "获得文件分页")
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<HashSet<String>> getCustomer() {
        return success(fileService.getCustomer());
    }

    @GetMapping("/getContractByCustomerId")
    //@Operation(summary = "获得客户的合同列表")
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<List<FileDO>> getContractByCustomerId(@RequestParam("customerName") String customerName) {
        return success(fileService.getContractByCustomerId(customerName));
    }

    @GetMapping("/preview")
    public CommonResult<String> preview(@NotNull(message = "文件资料URL不能为空") String url) {
        return fileService.preview(url);
    }

}