package com.lh.oa.module.system.service.dict;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.dict.vo.data.*;
import com.lh.oa.module.system.dal.dataobject.dict.DictDataDO;

import java.util.Collection;
import java.util.List;

/**
 * 字典数据 Service 接口
 *
 * @author didida
 */
public interface DictDataService {

    /**
     * 创建字典数据
     *
     * @param reqVO 字典数据信息
     * @return 字典数据编号
     */
    Long createDictData(DictDataCreateReqVO reqVO);

    /**
     * 更新字典数据
     *
     * @param reqVO 字典数据信息
     */
    void updateDictData(DictDataUpdateReqVO reqVO);

    /**
     * 删除字典数据
     *
     * @param id 字典数据编号
     */
    void deleteDictData(Long id);

    /**
     * 获得字典数据列表
     *
     * @return 字典数据全列表
     */
    List<DictDataDO> getDictDataList();

    /**
     * 获得字典数据分页列表
     *
     * @param reqVO 分页请求
     * @return 字典数据分页列表
     */
    PageResult<DictDataDO> getDictDataPage(DictDataPageReqVO reqVO);

    /**
     * 获得字典数据列表
     *
     * @param reqVO 列表请求
     * @return 字典数据列表
     */
    List<DictDataDO> getDictDataList(DictDataExportReqVO reqVO);

    /**
     * 获得字典数据详情
     *
     * @param id 字典数据编号
     * @return 字典数据
     */
    DictDataDO getDictData(Long id);

    /**
     * 获得指定字典类型的数据数量
     *
     * @param dictType 字典类型
     * @return 数据数量
     */
    long countByDictType(String dictType);

    /**
     * 校验字典数据们是否有效。如下情况，视为无效：
     * 1. 字典数据不存在
     * 2. 字典数据被禁用
     *
     * @param dictType 字典类型
     * @param values 字典数据值的数组
     */
    void validateDictDataList(String dictType, Collection<String> values);

    /**
     * 获得指定的字典数据
     *
     * @param dictType 字典类型
     * @param value 字典数据值
     * @return 字典数据
     */
    DictDataDO getDictData(String dictType, String value);

    /**
     * 解析获得指定的字典数据，从缓存中
     *
     * @param dictType 字典类型
     * @param label 字典数据标签
     * @return 字典数据
     */
    DictDataDO parseDictData(String dictType, String label);

    /**
     * 批量查询字典值列表
     *
     * @param dictTypeList 字典项列表，以英文逗号分隔
     * @return 字典值列表
     */
    List<DictDataBatchVo> batchQueryDictData(String dictTypeList);

}
