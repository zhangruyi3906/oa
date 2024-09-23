package com.lh.oa.module.bpm.service.definition;

import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormFieldExportDO;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmFormFieldExportMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class BpmFormFieldExportServiceImpl implements BpmFormFieldExportService {
    @Resource
    private BpmFormFieldExportMapper bpmFormFieldExportMapper;

    @Override
    public void saveOrUpdate(Long formId, List<BpmFormFieldExportDO> bpmFormFieldExportList) {
        log.info("保存导出表单字段显示信息, bpmFormFieldExportExportList:{}", bpmFormFieldExportList);
        List<BpmFormFieldExportDO> bpmFormFieldExportExportS = bpmFormFieldExportMapper.selectListByFormId(formId);
        if (CollectionUtils.isNotEmpty(bpmFormFieldExportExportS)) {
            bpmFormFieldExportMapper.deleteByFormId(formId);
        }
        if (CollectionUtils.isNotEmpty(bpmFormFieldExportList)) {
            bpmFormFieldExportList.forEach(bpmFormFieldShowDO -> bpmFormFieldShowDO.setFormId(formId));
            bpmFormFieldExportMapper.createdByBpmFormFieldExportExportList(bpmFormFieldExportList);
        }
    }

    @Override
    public List<BpmFormFieldExportDO> getExportList(Long formId) {
        return bpmFormFieldExportMapper.selectListByFormId(formId);
    }
}
