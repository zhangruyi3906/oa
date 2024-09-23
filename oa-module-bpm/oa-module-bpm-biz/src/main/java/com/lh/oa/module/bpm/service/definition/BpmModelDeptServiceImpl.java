package com.lh.oa.module.bpm.service.definition;

import cn.hutool.core.collection.CollUtil;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmModelDeptDO;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmModelDeptMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BpmModelDeptServiceImpl implements BpmModelDeptService {
    @Resource
    private BpmModelDeptMapper bpmModelDeptMapper;

    @Override
    public void saveOrUpdate(String modelId, List<Long> deptIds) {
        List<BpmModelDeptDO> bpmModelDeptDOS = bpmModelDeptMapper.selectListByModelId(modelId);
        if (CollectionUtils.isNotEmpty(bpmModelDeptDOS)) {
            List<Long> deptIdList = bpmModelDeptDOS.stream().map(BpmModelDeptDO::getDeptId).collect(Collectors.toList());
            List<Long> createDeptIds = CollUtil.subtractToList(deptIds, deptIdList);
            List<Long> deletedDeptIds = CollUtil.subtractToList(deptIdList, deptIds);
            if (CollectionUtils.isNotEmpty(createDeptIds)) {
                bpmModelDeptMapper.createdByModelIdAndDeptIds(modelId, createDeptIds);
            }
            if (CollectionUtils.isNotEmpty(deletedDeptIds)) {
                deletedDeptIds.forEach(deptId -> bpmModelDeptMapper.deleteByModelIdAndDeptId(modelId, deptId));
            }
        } else {
            bpmModelDeptMapper.createdByModelIdAndDeptIds(modelId, deptIds);
        }
    }
}
