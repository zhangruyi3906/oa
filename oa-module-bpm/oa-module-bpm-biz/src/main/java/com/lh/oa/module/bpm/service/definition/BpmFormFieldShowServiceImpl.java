package com.lh.oa.module.bpm.service.definition;

import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormFieldShowDO;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmFormFieldShowMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class BpmFormFieldShowServiceImpl implements BpmFormFieldShowService {
    @Resource
    private BpmFormFieldShowMapper bpmFormFieldShowMapper;

    @Override
    public void saveOrUpdate(Long formId, List<BpmFormFieldShowDO> bpmFormFieldShowDOList) {
        log.info("保存展示表单字段显示信息, bpmFormFieldShowDOList:{}", bpmFormFieldShowDOList);
        List<BpmFormFieldShowDO> bpmFormFieldShowDOS = bpmFormFieldShowMapper.selectListByFormId(formId);
        if (CollectionUtils.isNotEmpty(bpmFormFieldShowDOS)) {
            bpmFormFieldShowMapper.deleteByFormId(formId);
        }
        if (CollectionUtils.isNotEmpty(bpmFormFieldShowDOList)) {
            bpmFormFieldShowDOList.forEach(bpmFormFieldShowDO -> bpmFormFieldShowDO.setFormId(formId));
            bpmFormFieldShowMapper.createdByBpmFormFieldShowDOList(bpmFormFieldShowDOList);
        }

    }

    @Override
    public List<BpmFormFieldShowDO> getShowList(Long formId) {
        List<BpmFormFieldShowDO> bpmFormFieldShowDOS = bpmFormFieldShowMapper.selectListByFormId(formId);
        return bpmFormFieldShowDOS;
    }
}
