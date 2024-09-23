package com.lh.oa.module.system.controller.admin.kingdee.service.impl;

import com.google.gson.Gson;
import com.kingdee.bos.webapi.entity.RepoRet;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.module.system.controller.admin.kingdee.config.K3cloudConfig;
import com.lh.oa.module.system.controller.admin.kingdee.service.K3cloudService;
import kingdee.bos.json.JSONObject;
import kingdee.bos.webapi.client.K3CloudApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lh.oa.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;

@Service
@Slf4j
public class K3cloudServiceImpl implements K3cloudService {
    private final String SAL_SALE_ORDER = "SAL_SaleOrder";

    @Autowired
    private static K3CloudApiClient client;

    static {
        client = new K3CloudApiClient(K3cloudConfig.getUrl());
    }


    @Override
    public Boolean checkLogin() {
        //TODO 登录信息先从缓存中获取
        client = new K3CloudApiClient(K3cloudConfig.getUrl());
        Boolean loginResult = null;
        try {
            loginResult = client.login(K3cloudConfig.getDbid(), K3cloudConfig.getUser(), K3cloudConfig.getPwd(), K3cloudConfig.getLcid());
        } catch (Exception e) {
            throw new BusinessException("获取登录信息失败");
        }
        return loginResult;
    }


    @Override
    public List<List<Object>> executeBillQuery(String formid, String FieldKeys, String FilterString, String OrderString) throws Exception {
        if (checkLogin()) {
            Map map = new HashMap();
            map.put("FormId", formid);
            map.put("FieldKeys", FieldKeys);
            map.put("FilterString", FilterString);
            map.put("OrderString", OrderString);
            map.put("StartRow", 0);
            map.put("Limit", 0);

            String data = JSONObject.valueToString(map);
            List<List<Object>> lists = client.executeBillQuery(data);
            return lists;
        } else {
            throw new BusinessException("登录金蝶系统失败");
        }
    }

    @Override
    public String saveSALSaleOrder(String data) {
        log.info("调用金蝶云创建销售订单接口，创建人：{}，参数列表：{}", getLoginUser(), data);
        if (checkLogin()) {
            try {
                String demo="{\"IgnoreInterationFlag\":\"\",\"ValidateFlag\":\"true\",\"SubSystemId\":\"\",\"IsDeleteEntry\":\"true\",\"Model\":{\"FID\":0,\"FNote\":\"测试售后配件下单流程by：信能通达-钟星雨-售后 钟星雨  测试数据，请忽略或直接删除319\",\"F_LHZZ_SBBH\":\"140A1\",\"FReceiveAddress\":\"测试地址\",\"FDate\":\"2023-11-11\",\"FSaleOrgId\":{\"FNumber\":\"101\"},\"FSettleId\":{\"FNumber\":\"CUST0344\"},\"FISINIT\":false,\"FSaleOrderFinance\":{\"FSettleCurrId\":{\"FNumber\":\"PRE001\"},\"FExchangeTypeId\":{\"FNumber\":\"HLTX01_SYS\"},\"FIsIncludedTax\":false,\"FOverOrgTransDirect\":false,\"FIsPriceExcludeTax\":true},\"FCustId\":{\"FNumber\":\"CUST0344\"},\"FBillTypeID\":{\"FNUMBER\":\"XSDD16_SYS\"},\"FSaleOrderEntry\":[{\"FIsFree\":false,\"FPriceUnitId\":{\"FNumber\":\"Pcs\"},\"FStockUnitID\":{\"FNumber\":\"Pcs\"},\"FUnitID\":{\"FNumber\":\"Pcs\"},\"FMaterialId\":{\"FNumber\":\"33060097\"},\"FEntryTaxRate\":\"0\",\"FOutLmtUnitID\":{\"FNumber\":\"Pcs\"},\"FOrderEntryPlan\":[{\"FPlanDate\":null,\"FDetailLocAddress\":\"测试地址\",\"FPlanQty\":\"1\"}],\"FEntryID\":\"0\",\"FStockBaseQty\":\"1\",\"FPrice\":\"10\",\"FReserveType\":\"1\",\"FSettleOrgIds\":{\"FNumber\":\"101\"},\"FQty\":\"1\",\"FTaxPrice\":\"10\",\"FDeliveryDate\":null,\"FOUTLMTUNIT\":\"SAL\",\"FDiscount\":\"0\",\"FISMRP \":false,\"FStockQty\":\"1\"}],\"FSalerId\":{\"FNumber\":\"DB0005\"},\"FChargeId\":{\"FNumber\":\"CUST0344\"}},\"IsAutoAdjustField\":\"false\",\"IsVerifyBaseDataField\":\"false\",\"IsEntryBatchFill\":\"true\",\"NumberSearch\":\"true\",\"InterationFlags\":\"\"}";
                //调用接口
                String resultJson = client.save(SAL_SALE_ORDER, data);
                //用于记录结果
                Gson gson = new Gson();
                //对返回结果进行解析和校验
                RepoRet repoRet = gson.fromJson(resultJson, RepoRet.class);
                if (repoRet.getResult().getResponseStatus().isIsSuccess()) {
                    log.info("调用金蝶保存销售订单接口返回结果：{}", gson.toJson(repoRet.getResult()));
                } else {
                    log.error("调用金蝶保存销售订单接口返回结果：{}", gson.toJson(repoRet.getResult().getResponseStatus()));
                    throw new BusinessException("创建金蝶销售订单失败");
                }
                return repoRet.getResult().toString();
            } catch (Exception e) {
                log.error("创建金蝶销售订单失败");
                throw new BusinessException("创建金蝶销售订单失败");
            }
        } else {
            throw new BusinessException("登录金蝶系统失败");
        }
    }
}
