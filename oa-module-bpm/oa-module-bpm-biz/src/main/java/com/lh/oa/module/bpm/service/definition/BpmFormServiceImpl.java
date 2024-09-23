package com.lh.oa.module.bpm.service.definition;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONObject;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.bpm.controller.admin.definition.vo.form.BpmFormCreateReqVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.form.BpmFormFieldVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.form.BpmFormPageReqVO;
import com.lh.oa.module.bpm.controller.admin.definition.vo.form.BpmFormUpdateReqVO;
import com.lh.oa.module.bpm.convert.definition.BpmFormConvert;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmFormDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
import com.lh.oa.module.bpm.dal.dataobject.definition.BpmTaskAssignRuleDO;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmFormMapper;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmProcessDefinitionExtMapper;
import com.lh.oa.module.bpm.dal.mysql.definition.BpmTaskAssignRuleMapper;
import com.lh.oa.module.bpm.enums.ErrorCodeConstants;
import com.lh.oa.module.bpm.enums.definition.BpmModelFormTypeEnum;
import com.lh.oa.module.bpm.service.definition.dto.BpmFormFieldRespDTO;
import com.lh.oa.module.bpm.service.definition.dto.BpmModelMetaInfoRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 *
 */
@Service
@Validated
@Slf4j
public class BpmFormServiceImpl implements BpmFormService {

    @Resource
    private BpmFormMapper formMapper;

    @Resource
    private BpmProcessDefinitionExtMapper bpmProcessDefinitionExtMapper;
    @Resource
    private BpmTaskAssignRuleMapper bpmTaskAssignRuleMapper;
    @Resource
    private BpmFormFieldExportService bpmFormFieldExportService;
    @Resource
    private BpmFormFieldShowService bpmFormFieldShowService;

    @Override
    public Long createForm(BpmFormCreateReqVO createReqVO) {
        this.checkFields(createReqVO.getFields());
        Long aLong = formMapper.selectCount(new LambdaQueryWrapperX<BpmFormDO>().eqIfPresent(BpmFormDO::getName, createReqVO.getName()));
        if (aLong != 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.FORM_IS_EXISTS);
        }
        if (CollectionUtils.isNotEmpty(createReqVO.getBpmFormFieldExportList())) {
            bpmFormFieldExportService.saveOrUpdate(null, createReqVO.getBpmFormFieldExportList());
        }
        if (CollectionUtils.isNotEmpty(createReqVO.getBpmFormFieldShowList())) {
            bpmFormFieldShowService.saveOrUpdate(null, createReqVO.getBpmFormFieldShowList());
        }
        BpmFormDO form = BpmFormConvert.INSTANCE.convert(createReqVO);
        formMapper.insert(form);
        return form.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateForm(BpmFormUpdateReqVO updateReqVO) {
        this.checkFields(updateReqVO.getFields());
        this.validateFormExists(updateReqVO.getId());
//        List<BpmProcessDefinitionExtDO> bpmProcessDefinitionExtDOS = mapper.selectList(new LambdaQueryWrapperX<BpmProcessDefinitionExtDO>().eq(BpmProcessDefinitionExtDO::getFormId, updateReqVO.getId()));
//        if(CollUtil.isNotEmpty(bpmProcessDefinitionExtDOS)){
//            bpmProcessDefinitionExtDOS.forEach(s -> s.setStatus(s.getStatus()+1));
//        }
//        mapper.updateBatch(bpmProcessDefinitionExtDOS, bpmProcessDefinitionExtDOS.size());
        if (CollectionUtils.isNotEmpty(updateReqVO.getBpmFormFieldExportList())) {
            bpmFormFieldExportService.saveOrUpdate(updateReqVO.getId(), updateReqVO.getBpmFormFieldExportList());
        }
        if (CollectionUtils.isNotEmpty(updateReqVO.getBpmFormFieldShowList())) {
            bpmFormFieldShowService.saveOrUpdate(updateReqVO.getId(), updateReqVO.getBpmFormFieldShowList());
        }
        BpmFormDO updateObj = BpmFormConvert.INSTANCE.convert(updateReqVO);
        formMapper.updateById(updateObj);
    }

    @Override
    public void deleteForm(Long id) {
        this.validateFormExists(id);
        List<BpmProcessDefinitionExtDO> bpmProcessDefinitionExtDOS = bpmProcessDefinitionExtMapper.selectList();
        //流程表单id集合
        Set<Long> definitionFormIdSet = bpmProcessDefinitionExtDOS.stream()
                .filter(s -> ObjectUtils.isNotEmpty(s.getFormId()))
                .map(BpmProcessDefinitionExtDO::getFormId)
                .collect(Collectors.toSet());
        List<BpmTaskAssignRuleDO> bpmTaskAssignRuleDOS = bpmTaskAssignRuleMapper.selectList();
        //节点表单id集合
        Set<Long> taskRuleFormIdSet = bpmTaskAssignRuleDOS.stream()
                .filter(s -> StringUtils.isNotEmpty(s.getFormKey()))
                .map(BpmTaskAssignRuleDO::getFormKey)
                .mapToLong(Long::parseLong)
                .boxed()
                .collect(Collectors.toSet());
        definitionFormIdSet.addAll(taskRuleFormIdSet);
        if (definitionFormIdSet.contains(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.FORM_IS_USED);
        }
        formMapper.deleteById(id);
    }

    private void validateFormExists(Long id) {
        if (formMapper.selectById(id) == null) {
            throw exception(ErrorCodeConstants.FORM_NOT_EXISTS);
        }
    }

    @Override
    public BpmFormDO getForm(Long id) {
        return formMapper.selectById(id);
    }

    @Override
    public List<BpmFormDO> getFormList() {
        return formMapper.selectList(new LambdaQueryWrapperX<BpmFormDO>().eq(BpmFormDO::getStatus, 0));
    }

    @Override
    public List<BpmFormDO> getFormList(Collection<Long> ids) {
        return formMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<BpmFormDO> getFormPage(BpmFormPageReqVO pageReqVO) {
        return formMapper.selectPage(pageReqVO);
    }


    @Override
    public BpmFormDO checkFormConfig(String configStr) {
        BpmModelMetaInfoRespDTO metaInfo = JsonUtils.parseObject(configStr, BpmModelMetaInfoRespDTO.class);
        if (metaInfo == null || metaInfo.getFormType() == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MODEL_DEPLOY_FAIL_FORM_NOT_CONFIG);
        }
        // 校验表单存在
        if (Objects.equals(metaInfo.getFormType(), BpmModelFormTypeEnum.NORMAL.getType())) {
            BpmFormDO form = getForm(metaInfo.getFormId());
            if (form == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.FORM_NOT_EXISTS);
            }
            return form;
        }
        return null;
    }

    /**
     * @param fields field 数组
     */
    private void checkFields(List<String> fields) {
        if (true) {
            return;
        }
        Map<String, String> fieldMap = new HashMap<>(); // key 是 vModel，value 是 label
        for (String field : fields) {
            BpmFormFieldRespDTO fieldDTO = JsonUtils.parseObject(field, BpmFormFieldRespDTO.class);
            Assert.notNull(fieldDTO);
            String oldLabel = fieldMap.put(fieldDTO.getVModel(), fieldDTO.getLabel());
            if (oldLabel == null) {
                continue;
            }
            throw exception(ErrorCodeConstants.FORM_FIELD_REPEAT, oldLabel, fieldDTO.getLabel(), fieldDTO.getVModel());
        }
    }

    @Override
    public List<BpmFormFieldVO> getFormFields(Long id) {
        BpmFormDO form = this.getForm(id);
        log.info("form:{}", form);
        if (ObjectUtils.isEmpty(form))
            return null;
        String conf = form.getConf();
        JSONObject jsonObject = new JSONObject(conf);
        Object body = jsonObject.get("body");
        Object body1 = JsonUtils.parseObject(body.toString(), List.class).get(0);
        List<BpmFormFieldVO> formFields = new ArrayList<>();
        if (body1 instanceof Map) {
            Map<String, Object> formMap = JsonUtils.covertObject2Map(body1);
            Object body2 = formMap.get("body");
            String jsonString = JsonUtils.toJsonString(body2);
            List<Map> maps = JsonUtils.parseArray(jsonString, Map.class);
            log.info("maps:{}", maps);
            maps.forEach(map -> {
                //折叠器
                if (ObjectUtils.equals(map.get("type"), "collapse")) {
                    String name = ObjectUtils.isNotEmpty(map.get("name")) ? map.get("name").toString() : "折叠器";
                    //折叠器数据
                    Object collapse = map.get("body");
                    Object collapseBody = JsonUtils.parseObject(JsonUtils.toJsonString(collapse), List.class).get(0);
                    if (collapseBody instanceof Map) {
                        //组合输入数据
                        Object itemsObj = parseCollapseObj(collapseBody, "items");
                        if (itemsObj instanceof Map) {
                            //组合输入二级数据
                            Object itemsBodyObj = parseCollapseObj(itemsObj, "body");
                            if (itemsBodyObj instanceof Map) {
                                //组合输入三级数据
                                Object collapseBodyObj = parseCollapseObj(itemsObj, "body");
                                if (collapseBodyObj instanceof Map) {
                                    //组合输入四级数据
                                    Map<String, Object> collapseBodyMap = JsonUtils.covertObject2Map(collapseBodyObj);
                                    Object resultBody = collapseBodyMap.get("body");
                                    String resultFields = JsonUtils.toJsonString(resultBody);
                                    List<Map> collapseMaps = JsonUtils.parseArray(resultFields, Map.class);
                                    String finalName = name;
                                    collapseMaps.forEach(collapseMap -> {
                                        BpmFormFieldVO bpmFormFieldVO = new BpmFormFieldVO();
                                        bpmFormFieldVO.setFieldName(String.format(finalName + ".%s", collapseMap.get("label")));
                                        bpmFormFieldVO.setField(String.format("collapse" + ".%s", collapseMap.get("name")));
                                        formFields.add(bpmFormFieldVO);
                                    });
                                }
                            }
                        }
                    }
                    return;
                }
                //不是表格数据
                if (ObjectUtils.notEqual(map.get("type"), "table")) {
                    if (ObjectUtils.isEmpty(map.get("label"))) {
                        return;
                    }
                    BpmFormFieldVO bpmFormFieldVO = new BpmFormFieldVO();
                    bpmFormFieldVO.setFieldName(map.get("label").toString());
                    bpmFormFieldVO.setField(map.get("name").toString());
                    formFields.add(bpmFormFieldVO);
                } else {
                    //表格里的数据
                    Object columns = map.get("columns");
                    String columnFields = JsonUtils.toJsonString(columns);
                    List<Map> columnMaps = JsonUtils.parseArray(columnFields, Map.class);
                    Object tableName = map.get("name");
                    if (ObjectUtils.isEmpty(tableName)) {
                        tableName = "table";
                    }
                    Object finalTableName = tableName;
                    String title;
                    if (ObjectUtils.isEmpty(map.get("title"))) {
                        title = "表格";
                    } else {
                        title = map.get("title").toString();
                        title = title.split("\\(")[0];
                    }
                    String finalTitle = title;
                    columnMaps.forEach(columnMap -> {
                        String name = columnMap.get("name").toString();
                        BpmFormFieldVO bpmFormFieldVO = new BpmFormFieldVO();
                        bpmFormFieldVO.setFieldName(finalTitle + "." + columnMap.get("label").toString());
                        bpmFormFieldVO.setField(String.format(finalTableName + ".%s", name));
                        formFields.add(bpmFormFieldVO);
                    });
                }

            });
        }
        return formFields;
    }

    private static Object parseCollapseObj(Object collapseBody, String str) {
        Map<String, Object> collapseMap = JsonUtils.covertObject2Map(collapseBody);
        Object items = collapseMap.get(str);
        Object itemsObj = JsonUtils.parseObject(JsonUtils.toJsonString(items), List.class).get(0);
        return itemsObj;
    }


}
