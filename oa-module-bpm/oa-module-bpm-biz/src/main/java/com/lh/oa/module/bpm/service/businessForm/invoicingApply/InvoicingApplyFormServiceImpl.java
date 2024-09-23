package com.lh.oa.module.bpm.service.businessForm.invoicingApply;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.module.bpm.api.task.BpmProcessInstanceApi;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.param.InvoicingApplyFormCreateParam;
import com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.vo.InvoicingApplyFormVo;
import com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.vo.InvoicingApplyGoodsVo;
import com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.vo.InvoicingApplyPurchaseVo;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.invoicingApply.BpmInvoicingApplyForm;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.invoicingApply.BpmInvoicingApplyGoods;
import com.lh.oa.module.bpm.dal.dataobject.businessForm.invoicingApply.BpmInvoicingApplyPurchase;
import com.lh.oa.module.bpm.dal.mysql.businessForm.invoicingApply.BpmInvoicingApplyFormMapper;
import com.lh.oa.module.bpm.dal.mysql.businessForm.invoicingApply.BpmInvoicingApplyGoodsMapper;
import com.lh.oa.module.bpm.dal.mysql.businessForm.invoicingApply.BpmInvoicingApplyPurchaseMapper;
import com.lh.oa.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.lh.oa.module.bpm.wrapper.SystemDictDataWrapper;
import com.lh.oa.module.system.api.dept.DeptApi;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lh.oa.module.bpm.enums.ErrorCodeConstants.SUPPLY_NOT_EXISTS_ERROR;

/**
 * @author tanghanlin
 * @since 2023/10/21
 */
@Service
@Slf4j
public class InvoicingApplyFormServiceImpl implements InvoicingApplyFormService {

    /**
     * 开票申请单流程标识，流程模型管理里的流程标识字段就是
     */
    public static final String PROCESS_KEY = "invoicing-apply-form";

    public static final String INVOICING_DICT_TYPES = "sub_company,invoicing_apply_type,invoicing_apply_sale_type";

    @Resource
    private BpmInvoicingApplyFormMapper bpmInvoicingApplyFormMapper;

    @Resource
    private BpmInvoicingApplyGoodsMapper bpmInvoicingApplyGoodsMapper;

    @Resource
    private BpmInvoicingApplyPurchaseMapper bpmInvoicingApplyPurchaseMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Resource
    private SystemDictDataWrapper systemDictDataWrapper;

    @Resource
    private AdminUserApi adminUserApi;

    @Override
    @Transactional
    public Long create(Long userId, InvoicingApplyFormCreateParam param) {
        log.info("创建开票申请单，userId:{}, param:{}", userId, param);
        // 插入实体，获取id
        BpmInvoicingApplyForm entity = JsonUtils.covertObject(param, BpmInvoicingApplyForm.class);
        entity.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        bpmInvoicingApplyFormMapper.insert(entity);
        Long id = entity.getId();

        // 维护关联的采购和货物信息
        List<BpmInvoicingApplyPurchase> purchaseList = JsonUtils.covertList(param.getPurchaseList(), BpmInvoicingApplyPurchase.class);
        purchaseList.forEach(purchase -> purchase.setBpmInvoicingApplyFormId(id));
        bpmInvoicingApplyPurchaseMapper.insertBatch(purchaseList);
        List<BpmInvoicingApplyGoods> goodsList = JsonUtils.covertList(param.getGoodsList(), BpmInvoicingApplyGoods.class);
        goodsList.forEach(goods -> goods.setBpmInvoicingApplyFormId(id));
        bpmInvoicingApplyGoodsMapper.insertBatch(goodsList);

        // 更新流程信息
        BpmProcessInstanceCreateReqDTO processInstance = new BpmProcessInstanceCreateReqDTO(
                PROCESS_KEY, JsonUtils.covertObject2Map(param), id.toString());
        String processInstanceId = processInstanceApi.createProcessInstance(userId, processInstance);
        entity.setProcessInstanceId(processInstanceId);
        bpmInvoicingApplyFormMapper.updateById(entity);
        return id;
    }

    @Override
    public void updateResult(Long id, Integer result) {
        log.info("更新开票申请单流程执行结果，id:{}, result:{}", id, result);
        BpmInvoicingApplyForm applyForm = bpmInvoicingApplyFormMapper.selectById(id);
        ExceptionThrowUtils.throwIfNull(applyForm, SUPPLY_NOT_EXISTS_ERROR);
        bpmInvoicingApplyFormMapper.updateById(new BpmInvoicingApplyForm().setId(id).setResult(result));
    }

    @Override
    public PageResult<InvoicingApplyFormVo> queryPageByParam(PageParam pageParam) {
        log.info("分页查询开票申请单, pageParam:{}", pageParam);
        PageResult<BpmInvoicingApplyForm> page = bpmInvoicingApplyFormMapper.selectPage(pageParam);
        if (page.getList().isEmpty()) {
            return PageResult.empty();
        }
        List<InvoicingApplyFormVo> result = buildFormVo(page.getList());
        return new PageResult<>(result, page.getTotal());
    }

    @Override
    public InvoicingApplyFormVo queryDetail(Long id) {
        log.info("查询开票申请单详情，id:{}", id);
        BpmInvoicingApplyForm applyForm = bpmInvoicingApplyFormMapper.selectById(id);
        ExceptionThrowUtils.throwIfNull(applyForm, SUPPLY_NOT_EXISTS_ERROR);
        InvoicingApplyFormVo result = JsonUtils.covertObject(applyForm, InvoicingApplyFormVo.class);

        Set<Long> userIds = new HashSet<>();
        userIds.add(applyForm.getApplyUserId());
        userIds.add(applyForm.getRelatedSaleUserId());
        userIds.add(Long.valueOf(applyForm.getCreator()));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(userIds);
        AdminUserRespDTO applyUser = userMap.get(applyForm.getApplyUserId());
        AdminUserRespDTO saleUser = userMap.get(applyForm.getRelatedSaleUserId());
        AdminUserRespDTO creator = userMap.get(Long.valueOf(applyForm.getCreator()));
        if (Objects.nonNull(applyUser)) {
            result.setApplyUserName(applyUser.getNickname());
        }
        if (Objects.nonNull(saleUser)) {
            result.setRelatedSaleUserName(saleUser.getNickname());
        }
        if (Objects.nonNull(creator)) {
            result.setCreatorName(creator.getNickname());
        }

        Map<String, Map<String, String>> dictTypeMap = systemDictDataWrapper.getDictTypeMap(INVOICING_DICT_TYPES);
        result.setApplyCompany(dictTypeMap
                .getOrDefault("sub_company", Collections.emptyMap())
                .getOrDefault(result.getApplyCompany(), ""));
        result.setFormType(dictTypeMap
                .getOrDefault("invoicing_apply_type", Collections.emptyMap())
                .getOrDefault(result.getFormType(), ""));
        result.setSaleType(dictTypeMap
                .getOrDefault("invoicing_apply_sale_type", Collections.emptyMap())
                .getOrDefault(result.getSaleType(), ""));

        List<BpmInvoicingApplyPurchase> purchaseList = bpmInvoicingApplyPurchaseMapper.findByFormId(id);
        List<InvoicingApplyPurchaseVo> purchaseVos = JsonUtils.covertList(purchaseList, InvoicingApplyPurchaseVo.class);
        result.setPurchaseList(purchaseVos);

        List<BpmInvoicingApplyGoods> goodsList = bpmInvoicingApplyGoodsMapper.findByFormId(id);
        List<InvoicingApplyGoodsVo> goodsVos = JsonUtils.covertList(goodsList, InvoicingApplyGoodsVo.class);
        result.setGoodsList(goodsVos);
        result.setCreateTime(TimeUtils.formatAsDateTime(applyForm.getCreateTime()));
        return result;
    }

    private List<InvoicingApplyFormVo> buildFormVo(List<BpmInvoicingApplyForm> fromList) {
        Set<Long> userIds = new HashSet<>();
        userIds.addAll(fromList.stream().map(BpmInvoicingApplyForm::getApplyUserId).collect(Collectors.toSet()));
        userIds.addAll(fromList.stream().map(BpmInvoicingApplyForm::getRelatedSaleUserId).collect(Collectors.toSet()));
        userIds.addAll(fromList.stream().map(form -> Long.valueOf(form.getCreator())).collect(Collectors.toSet()));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(userIds);

        Map<String, Map<String, String>> dictTypeMap = systemDictDataWrapper.getDictTypeMap(INVOICING_DICT_TYPES);
        Map<String, String> companyMap = dictTypeMap.getOrDefault("sub_company", Collections.emptyMap());
        Map<String, String> formTypeMap = dictTypeMap.getOrDefault("invoicing_apply_type", Collections.emptyMap());
        Map<String, String> saleTypeMap = dictTypeMap.getOrDefault("invoicing_apply_sale_type", Collections.emptyMap());

        List<InvoicingApplyFormVo> result = new LinkedList<>();
        fromList.forEach(form -> {
            AdminUserRespDTO applyUser = userMap.get(form.getApplyUserId());
            if (Objects.isNull(applyUser)) {
                return;
            }
            AdminUserRespDTO saleUser = userMap.get(form.getRelatedSaleUserId());
            if (Objects.isNull(saleUser)) {
                return;
            }
            AdminUserRespDTO creator = userMap.get(Long.valueOf(form.getCreator()));
            if (Objects.isNull(creator)) {
                return;
            }

            InvoicingApplyFormVo formVo = JsonUtils.covertObject(form, InvoicingApplyFormVo.class);
            formVo.setApplyUserName(applyUser.getNickname());
            formVo.setRelatedSaleUserName(saleUser.getNickname());
            formVo.setApplyCompany(companyMap.getOrDefault(form.getApplyCompany().toString(), ""));
            formVo.setFormType(formTypeMap.getOrDefault(form.getFormType().toString(), ""));
            formVo.setSaleType(saleTypeMap.getOrDefault(form.getSaleType().toString(), ""));
            formVo.setCreateTime(TimeUtils.formatAsDateTime(form.getCreateTime()));
            formVo.setCreatorName(creator.getNickname());
            result.add(formVo);
        });
        return result;
    }

}
