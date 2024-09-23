package com.lh.oa.module.system.sal.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.service.user.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.lh.oa.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Service
@Slf4j
public class SalSaleServiceImpl implements SalSaleService{
    @Resource
    private AdminUserService adminUserService;
    @Override
    public String getSalSaleOrderParams(Map<String, Object> variableInstances) {
        String documentType = variableInstances.get("documentType") == null ? "" : variableInstances.get("documentType").toString();//单据类型
        String signingOrganization = variableInstances.get("signingOrganization") == null ? "" : variableInstances.get("signingOrganization").toString();//签约组织
        String custom = variableInstances.get("custom") == null ? "" : variableInstances.get("custom").toString();//客户
        String selectAddress = variableInstances.get("select_address") == null ? "" : variableInstances.get("select_address").toString();//收货地址
        String mainOrderSalesperson = variableInstances.get("mainOrderSalesperson") == null ? "" : variableInstances.get("mainOrderSalesperson").toString();//主成单销售员
        String remark = variableInstances.get("remark") == null ? "" : variableInstances.get("remark").toString();//备注
        String equipmentCode = variableInstances.get("equipmentCode") == null ? "" : variableInstances.get("equipmentCode").toString();//备注
        Long loginUserId = getLoginUserId();
        AdminUserDO user = adminUserService.getUser(loginUserId);

        if (user == null) {
            log.error("添加销售备注时，获取登录人信息失败,登录人id:{}", getLoginUserId());
            remark = remark + "-售后";
        } else {
            remark = remark + "-售后 " + user.getNickname() + "，oa用户id：" + user.getId();
        }
//        remark = remark + "-售后  测试数据，请忽略或直接删除,by:信能通达-钟星雨";
        String taxIncludedMark = variableInstances.get("taxIncludedMark") == null ? "" : variableInstances.get("taxIncludedMark").toString();//是否含税
        Object o = variableInstances.get("table");
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
