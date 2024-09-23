package com.lh.oa.module.bpm.framework.flowable.core.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.module.bpm.framework.rpc.config.SpringContextHolder;
import com.lh.oa.module.system.api.kingdee.K3cloudApi;
import com.lh.oa.module.system.api.user.AdminUserApi;
import com.lh.oa.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.lh.oa.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * 售后配件下单流程任务监听
 */
@Slf4j
public class SalSaleOrderEventListener implements JavaDelegate {
    private K3cloudApi k3cloudApi = SpringContextHolder.getBean(K3cloudApi.class);
    private AdminUserApi adminUserApi = SpringContextHolder.getBean(AdminUserApi.class);

    //必须要
    @Autowired
    private RuntimeService runtimeservice;

    @Override
    public void execute(DelegateExecution execution) {
        log.info("售后配件下单流程任务监听，流程实例Id:{}", execution.getProcessInstanceId());
        //获取流程表单数据
        Map<String, VariableInstance> variableInstances = execution.getVariableInstances();
        String date = this.getSalSaleOrderParams(variableInstances);
        //创建金蝶销售订单
        try {
            CommonResult<String> stringCommonResult = k3cloudApi.saveSALSaleOrder(date);
            CommonResult<Boolean> login = k3cloudApi.login();
            System.out.println("金蝶授权结果------------------------------" + login);
            log.info("销售订单创建成功：{}", JsonUtils.toJsonString(stringCommonResult.getData()));
            log.info("创建金蝶销售订单参数：{}", date);
        } catch (Exception e) {
            log.error("创建金蝶销售订单失败：{}", e.getMessage());
            throw new BusinessException("创建销售订单失败");
        }

    }

    private String getSalSaleOrderParams(Map<String, VariableInstance> variableInstances) {
        String documentType = variableInstances.get("documentType") == null ? "" : variableInstances.get("documentType").getTextValue();//单据类型
        String signingOrganization = variableInstances.get("signingOrganization") == null ? "" : variableInstances.get("signingOrganization").getTextValue();//签约组织
        String custom = variableInstances.get("custom") == null ? "" : variableInstances.get("custom").getTextValue();//客户
        String selectAddress = variableInstances.get("select_address") == null ? "" : variableInstances.get("select_address").getTextValue();//收货地址
        String mainOrderSalesperson = variableInstances.get("mainOrderSalesperson") == null ? "" : variableInstances.get("mainOrderSalesperson").getTextValue();//主成单销售员
        String remark = variableInstances.get("remark") == null ? "" : variableInstances.get("remark").getTextValue();//备注
        String equipmentCode = variableInstances.get("equipmentCode") == null ? "" : variableInstances.get("equipmentCode").getTextValue();//备注
        CommonResult<AdminUserRespDTO> user = adminUserApi.getUser(getLoginUserId());
        if (user == null || user.getStatus() != 200) {
            log.error("添加销售备注时，获取登录人信息失败,登录人id:{}", getLoginUserId());
            remark = remark + "-售后";
        } else {
            remark = remark + "-售后 " + user.getData().getNickname() + "，oa用户id：" + user.getData().getId();
        }
//        remark = remark + "-售后  测试数据，请忽略或直接删除,by:信能通达-钟星雨";
        String taxIncludedMark = variableInstances.get("taxIncludedMark") == null ? "" : variableInstances.get("taxIncludedMark").getTextValue();//是否含税
        Object o = variableInstances.get("table").getValue();
        List<JSONObject> jsonArray = JsonUtils.parseArray(JsonUtils.toJsonString(o), JSONObject.class);
        JSONObject dataJson = new JSONObject();
        dataJson.put("IsDeleteEntry", "true");
        dataJson.put("SubSystemId", "");
        dataJson.put("IsVerifyBaseDataField", "false");
        dataJson.put("IsEntryBatchFill", "true");
        dataJson.put("ValidateFlag", "true");
        dataJson.put("NumberSearch", "true");
        dataJson.put("IsAutoAdjustField", "false");
        dataJson.put("InterationFlags", "");
        dataJson.put("IgnoreInterationFlag", "");
        JSONObject Model = new JSONObject();
        Model.put("FID", 0);
        //日期
        Model.put("FDate", TimeUtils.formatAsDate(new Date()));
        //销售组织
        JSONObject saleorg = new JSONObject();
        saleorg.put("FNumber", signingOrganization);
        Model.put("FSaleOrgId", saleorg);
        //客户
        JSONObject Cust = new JSONObject();
        Cust.put("FNumber", custom);
        Model.put("FCustId", Cust);

        JSONObject FBillTypeID = new JSONObject();
        FBillTypeID.put("FNUMBER", documentType);
        Model.put("FBillTypeID", FBillTypeID);
        //销售员
        JSONObject FSalerId = new JSONObject();
        FSalerId.put("FNumber", mainOrderSalesperson);
        Model.put("FSalerId", FSalerId);
        //收货方地址
        Model.put("FReceiveAddress", selectAddress);
        //结算方
        Model.put("FSettleId", Cust);
        //付款方
        Model.put("FChargeId", Cust);
        //是否期初单据
        Model.put("FISINIT", false);
        //备注
        Model.put("FNote", remark);

        JSONObject FSaleOrderFinance = new JSONObject();
        JSONObject FSettleCurrId = new JSONObject();
        //结算币种
        FSettleCurrId.put("FNumber", "PRE001");
        FSaleOrderFinance.put("FSettleCurrId", FSettleCurrId);
        //是否含税
        boolean FIsIncludedTax = "true".equals(taxIncludedMark);
        FSaleOrderFinance.put("FIsIncludedTax", FIsIncludedTax);
        //价外税
        FSaleOrderFinance.put("FIsPriceExcludeTax", true);
        JSONObject FExchangeTypeId = new JSONObject();
        FExchangeTypeId.put("FNumber", "HLTX01_SYS");
        //汇率类型
        FSaleOrderFinance.put("FExchangeTypeId", FExchangeTypeId);
        //寄售生成跨组织调拨
        FSaleOrderFinance.put("FOverOrgTransDirect", false);
        Model.put("FSaleOrderFinance", FSaleOrderFinance);
        //物料详情
        JSONArray FSaleOrderEntry = new JSONArray();
        JSONObject FStockOrgId;
        for (JSONObject material : jsonArray) {
            Object materialCodeObj = material.get("materialName");
            cn.hutool.json.JSONObject jsonObject = JsonUtils.covertObject(materialCodeObj, cn.hutool.json.JSONObject.class);
            String materialCode = String.valueOf(jsonObject.get("materialCode"));
            String unit = String.valueOf(jsonObject.get("unit"));//单位
            String count = String.valueOf(material.get("count"));//数量
            String price = String.valueOf(material.get("price"));//单价
            String inclueTaxPrice = String.valueOf(material.get("inclueTaxPrice"));//含税单价
            String taxRate = String.valueOf(material.get("taxRate"));//税率%
            String discountAmount = String.valueOf(material.get("discountAmount"));//折扣额
            String total = String.valueOf(material.get("total"));//价税合计

            String fdiscount;
            if (total == null || "0".equals(total) || "null".equals(total)) {
                fdiscount = "0";
            } else {
                fdiscount = new BigDecimal(discountAmount).divide(new BigDecimal(total), 2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();//折扣率
            }
            String issuanceDate = (String) material.get("issuanceDate");//要求发货日期

            JSONObject k3result = new JSONObject(true);
            FStockOrgId = new JSONObject();
            FStockOrgId.put("FNumber", materialCode);

            JSONObject obj = new JSONObject(true);
            obj.put("FNumber", unit);

            //计价单位
            k3result.put("FPriceUnitId", obj);
            //库存单位
            k3result.put("FStockUnitID", obj);
            //销售单位
            k3result.put("FUnitID", obj);
            k3result.put("FEntryID", "0");
            k3result.put("FMaterialId", FStockOrgId);

            //销售数量
            k3result.put("FQty", count);
            boolean fisfree = false;
            if (!fisfree) {
                //单价
                k3result.put("FPrice", price);
                //含税单价
                k3result.put("FTaxPrice", inclueTaxPrice);
                k3result.put("FDiscount", fdiscount);
            }
            //是否赠品
            k3result.put("FIsFree", false);
            //税率
            k3result.put("FEntryTaxRate", taxRate);
            //要货日期
            k3result.put("FDeliveryDate", issuanceDate);
            //结算组织
            k3result.put("FSettleOrgIds", saleorg);
            //货主类型
            k3result.put("FOwnerTypeId", "BD_OwnerOrg");
            //预留类型
            k3result.put("FReserveType", "1");
            //计价基本数量
            k3result.put("FPriceBaseQty", count);
            //库存数量
            k3result.put("FStockQty", count);
            //库存基本数量
            k3result.put("FStockBaseQty", count);
            //超发控制单位类型
            k3result.put("FOUTLMTUNIT", "SAL");
            //已预留
            k3result.put("FISMRP ", false);
            //超发控制单位
            k3result.put("FOutLmtUnitID", obj);
            JSONArray FOrderEntryPlan = new JSONArray();
            JSONObject plan = new JSONObject();
            //交货地址
            plan.put("FDetailLocAddress", selectAddress);
            //要货日期
            plan.put("FPlanDate", issuanceDate);
            //数量
            plan.put("FPlanQty", count);
            FOrderEntryPlan.add(plan);
            k3result.put("FOrderEntryPlan", FOrderEntryPlan);
            FSaleOrderEntry.add(k3result);
        }
        Model.put("FSaleOrderEntry", FSaleOrderEntry);
        Model.put("F_LHZZ_SBBH", equipmentCode); //装备编号
        dataJson.put("Model", Model);
        return JsonUtils.toJsonString(dataJson);
    }
}
