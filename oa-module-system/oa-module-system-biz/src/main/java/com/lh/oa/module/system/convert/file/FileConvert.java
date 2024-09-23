package com.lh.oa.module.system.convert.file;


import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.file.vo.file.FileRespVO;
import com.lh.oa.module.system.dal.dataobject.file.FileDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileConvert {

    FileConvert INSTANCE = Mappers.getMapper(FileConvert.class);

    FileRespVO convert(FileDO bean);

    PageResult<FileRespVO> convertPage(PageResult<FileDO> page);

}
