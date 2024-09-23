package com.lh.oa.module.bpm.framework.flowable.core.listener;

import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 物料申请流程数据校验监听器
 */
@Slf4j
public class MaterialApplyDataCheckListener implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("物料申请流程发起监听，校验数据格式，流程实例id：{}", delegateExecution.getProcessInstanceId());
        this.checkData(delegateExecution);
    }

    private void checkData(DelegateExecution delegateExecution) {
        Map<String, VariableInstance> variableInstances = delegateExecution.getVariableInstances();
        Object table1 = variableInstances.get("table");
        Object newMaterialTable = variableInstances.get("xinzengtable");
        if (table1 == null && newMaterialTable == null) {
            throw new BusinessException("物料申请至少填写一条物料信息");
        }
        int totalSize = 0;//至少一条物料
        Map<String, String> materialCodeCheckMap = new HashMap<>();//编码不能重复
        Map<String, String> materialCodeCheckMap1 = new HashMap<>();//编码不能重复
        StringJoiner repeatMaterialCode = new StringJoiner(",");
        StringJoiner repeatMaterialCode1 = new StringJoiner(",");


        if (table1 != null) {
            cn.hutool.json.JSONObject table2 = JsonUtils.covertObject(table1, cn.hutool.json.JSONObject.class);
            Object cachedValue1 = table2.get("cachedValue");
            List<cn.hutool.json.JSONObject> table = JsonUtils.parseArray(JsonUtils.toJsonString(cachedValue1), cn.hutool.json.JSONObject.class);//新增物料
            totalSize += table.size();
            table.forEach(object -> {
                cn.hutool.json.JSONObject jsonObject = JsonUtils.covertObject(object, cn.hutool.json.JSONObject.class);
                Object materialNameObj = jsonObject.get("materialName");
//                com.alibaba.fastjson.JSONObject material = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.toJSON(materialNameObj);
                cn.hutool.json.JSONObject material = JsonUtils.covertObject(materialNameObj, cn.hutool.json.JSONObject.class);
                String materialName = ((String) material.get("materialName")).trim();//物料名称
                String materialCode = "";
                if (ObjectUtils.isNotEmpty(jsonObject.get("materialCode"))) {
                    materialCode = ((String) jsonObject.get("materialCode")).trim();//物料编码
                }
                if (ObjectUtils.isEmpty(jsonObject.get("materialModel"))) {
                    throw new BusinessException("物料型号不能为空");
                }
                String materialModel = ((String) jsonObject.get("materialModel")).trim();//物料型号
                if (materialName.length() > 50 || materialCode.length() > 50 || materialModel.length() > 50) {
                    throw new BusinessException("物料的名称，编码，型号均不能超过50字，请检查");
                }
                if(ObjectUtils.isEmpty(materialCode)){
                    return;
                }
                String key = materialName + "-" + materialModel;
                if (!materialCodeCheckMap.containsKey(materialCode)) {
                    materialCodeCheckMap.put(materialCode, key);
                } else {
                    repeatMaterialCode.add(materialCode);
                    materialCodeCheckMap.put(materialCode, key);
                }
                if (!materialCodeCheckMap1.containsKey(key)) {
                    materialCodeCheckMap1.put(key, materialCode);
                } else {
                    repeatMaterialCode1.add(key);
                    materialCodeCheckMap1.put(key, materialCode);
                }

            });
        }
        if (newMaterialTable != null) {
            cn.hutool.json.JSONObject jsonObject1 = JsonUtils.covertObject(newMaterialTable, cn.hutool.json.JSONObject.class);
            Object cachedValue = jsonObject1.get("cachedValue");
            if (Objects.isNull(cachedValue)) {
                return;
            }
            List<cn.hutool.json.JSONObject> newTable = JsonUtils.parseArray(JsonUtils.toJsonString(cachedValue), cn.hutool.json.JSONObject.class);//新增物料
            if (CollectionUtils.isEmpty(newTable)) {
                return;
            }
            totalSize += newTable.size();
            newTable.forEach(object -> {

                cn.hutool.json.JSONObject jsonObject = JsonUtils.covertObject(object, cn.hutool.json.JSONObject.class);
                if (Objects.isNull(jsonObject.get("materialName1"))) {
                    throw new BusinessException("物料名称不能为空");
                }
                String materialName = "";
                if (ObjectUtils.isNotEmpty(jsonObject.get("materialName1"))) {
                    materialName = ((String) jsonObject.get("materialName1")).trim();//物料名称
                }
                String materialCode = "";
                if (ObjectUtils.isNotEmpty(jsonObject.get("materialCode1"))) {
                    materialCode = ((String) jsonObject.get("materialCode1")).trim();//物料编码
                }
                if (Objects.isNull(jsonObject.get("materialModel1"))) {
                    throw new BusinessException("物料型号不能为空");
                }
                String materialModel = ((String) jsonObject.get("materialModel1")).trim();//物料型号
                if (materialName.length() > 50 || materialCode.length() > 50 || materialModel.length() > 50) {
                    throw new BusinessException("物料的名称，编码，型号均不能超过50字，请检查");
                }
                String key = materialName + "-" + materialModel;

                if(ObjectUtils.isEmpty(materialCode)){
                   return;
                }
                if (!materialCodeCheckMap.containsKey(materialCode)) {
                    materialCodeCheckMap.put(materialCode, key);
                } else {
                    repeatMaterialCode.add(materialCode);
                    materialCodeCheckMap.put(materialCode, key);
                }

                if (!materialCodeCheckMap1.containsKey(key)) {
                    materialCodeCheckMap1.put(key, materialCode);
                } else {
                    repeatMaterialCode1.add(key);
                    materialCodeCheckMap1.put(key, materialCode);
                }
            });
        }
        if (totalSize == 0) {
            throw new BusinessException("物料申请至少填写一条物料信息");
        }
        if (repeatMaterialCode.length() > 0) {
            throw new BusinessException("存在同一个编码的物料名称和型号不一致，请检查，编码为" + repeatMaterialCode);
        }
        if (repeatMaterialCode1.length() > 0) {
            throw new BusinessException("存在同一个物料名称和型号的编码不一致，请检查，物料名称和名字为" + repeatMaterialCode1);
        }
    }
}
