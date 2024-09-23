package com.lh.oa.module.bpm.service.officesuppliesrequest;

import cn.hutool.core.collection.CollUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.api.task.BpmProcessInstanceApi;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo.OfficeSuppliesRequestCreateVo;
import com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo.OfficeSuppliesRequestPageReqVo;
import com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo.OfficeSuppliesRequestRespVo;
import com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo.OfficeVO;
import com.lh.oa.module.bpm.convert.officesuppliesrequest.OfficeConvert;
import com.lh.oa.module.bpm.convert.officesuppliesrequest.OfficeSuppliesRequestConvert;
import com.lh.oa.module.bpm.dal.dataobject.officesuppliesrequest.OfficeDO;
import com.lh.oa.module.bpm.dal.dataobject.officesuppliesrequest.OfficeSuppliesRequestDO;
import com.lh.oa.module.bpm.dal.mysql.officesuppliesrequest.OfficeMapper;
import com.lh.oa.module.bpm.dal.mysql.officesuppliesrequest.OfficeSuppliesRequestMapper;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.SUPPLY_NOT_EXISTS_ERROR;

@Service
@Validated
public class OfficeSuppliesRequestServiceImpl implements OfficeSuppliesRequestService{

    public static final String PROCESS_KEY = "office-supplies-request";

    @Resource
    private OfficeSuppliesRequestMapper mapper;
    @Resource
    private OfficeMapper officeMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOfficeSu(OfficeSuppliesRequestCreateVo createReqVO) {
        OfficeSuppliesRequestDO office = OfficeSuppliesRequestConvert.INSTANCE.convert(createReqVO)
                .setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        mapper.insert(office);
        if (!CollUtil.isEmpty(createReqVO.getList())){
            List<OfficeDO> officeDOS = OfficeConvert.INSTANCE.convertList(createReqVO.getList());
            officeDOS.forEach(s ->s.setOffSuId(office.getId()));
            officeMapper.insertBatch(officeDOS);
        }
//        Map<String, Object> processInstanceVariables = new HashMap<>();
//        processInstanceVariables.put("days", createReqVO.getDays());
        String processInstanceId = processInstanceApi.createProcessInstance(createReqVO.getUserId(),
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY).setBusinessKey(String.valueOf(office.getId())));

        mapper.updateById(new OfficeSuppliesRequestDO().setId(office.getId()).setProcessInstanceId(processInstanceId));
        return office.getId();
    }

    @Override
    public void updateOfficeSuResult(Long id, Integer result) {
        validateOfficeSuExists(id);
        mapper.updateById(new OfficeSuppliesRequestDO().setId(id).setResult(result));
    }

    @Override
    public OfficeSuppliesRequestRespVo getOfficeSu(Long id) {
        OfficeSuppliesRequestDO officeSuppliesRequestDO = mapper.selectById(id);
        OfficeSuppliesRequestRespVo convert = OfficeSuppliesRequestConvert.INSTANCE.convert(officeSuppliesRequestDO);
        List<OfficeDO> officeDOS = officeMapper.selectList(new LambdaQueryWrapperX<OfficeDO>().eq(OfficeDO::getOffSuId, id));
        if(!CollUtil.isEmpty(officeDOS)) {
            List<OfficeVO> officeVOS = OfficeConvert.INSTANCE.convertList1(officeDOS);
            convert.setList(officeVOS);
        }
        return convert;
    }

    @Override
    public PageResult<OfficeSuppliesRequestDO> getOfficeSuPage(Long userId,OfficeSuppliesRequestPageReqVo pageVO) {
        return  mapper.selectPage(userId,pageVO);
    }

    private void validateOfficeSuExists(Long id){
        if(mapper.selectById(id) == null){
            throw exception(SUPPLY_NOT_EXISTS_ERROR);
        }
    }
}
