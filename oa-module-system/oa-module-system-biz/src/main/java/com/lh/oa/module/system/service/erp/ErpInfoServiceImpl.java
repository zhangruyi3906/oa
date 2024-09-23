package com.lh.oa.module.system.service.erp;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.util.StringUtils;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.module.system.controller.admin.erp.vo.*;
import com.lh.oa.module.system.full.constants.ISysConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Service
@Slf4j
public class ErpInfoServiceImpl implements ErpInfoService {
    @Value("${post.sale.platform-url}")
    private String platformUrl;
    @Value("${post.sale.username}")
    private String username;
    @Value("${post.sale.password}")
    private String password;

    @Value("${post.sale.sync-user}")
    private String syncUserUrl;
    @Value("${post.sale.erp-inventory}")
    private String erpInventoryApi;
    @Value("${post.sale.erp-warehouse}")
    private String erpWarehouseApi;
    @Value("${information.pre_url}")
    private String preUrl;
    @Value("${information.post_sale_customer}")
    private String customer;
    @Value("${information.post_sale_man}")
    private String manApi;
    @Value("${post.sale.order_list}")
    private String orderListApi;
    @Autowired
    private AuthServiceImpl authService;

    /**
     * 从售后系统获取erp物料库存
     *
     * @param pageSize
     * @param pageNo
     * @param materialName
     * @return
     */
    @Override
    public List<ErpInventoryVO> getErpInventoryList(Integer pageSize, Integer pageNo, String materialName) {
        log.info("从售后系统获取erp物料库存");
        String accessToken = authService.getPostSaleAccessToken();
        String erpInventory = platformUrl + erpInventoryApi;
        HttpRequest httpRequest;

        httpRequest = HttpRequest.get(erpInventory).form("materialName", materialName);
        log.info("物料库存查询:{}", erpInventory);

        HttpResponse httpResponse = httpRequest.bearerAuth(accessToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("物料库存查询失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("物料库存查询失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("物料库存查询失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
            Object data = body.get("data");
            JSONObject body1 = JSONUtil.parseObj(data);
            Object data1 = body1.get("records");
            String s = com.alibaba.fastjson.JSONObject.toJSONString(data1);
            List<ErpInventoryVO> inventoryVOS = com.alibaba.fastjson.JSONObject.parseArray(s, ErpInventoryVO.class);
            if (ObjectUtils.isEmpty(inventoryVOS)) {
                log.info("物料库存查询失败, " + JSONUtil.toJsonStr(body));
            }
            log.info("获取物料库存完成，共{}条数据", inventoryVOS.size());
            return inventoryVOS;
        }
    }

    /**
     * 从售后系统获取仓库列表
     *
     * @param pageSize
     * @param pageNo
     * @param name
     * @return
     */
    @Override
    public List<ErpWarehouseVO> getErpWarehouseList(Integer pageSize, Integer pageNo, String name) {
        log.info("从售后系统获取erp仓库列表，pageSize:{},pageNo:{},name:{}", pageSize, pageNo, name);
        String accessToken = authService.getPostSaleAccessToken();
        String erpInventory = platformUrl + erpWarehouseApi;
        HttpRequest httpRequest;

        httpRequest = HttpRequest.get(erpInventory).form("size", pageSize).form("current", pageNo).form("materialName", name);
        log.info("仓库列表查询:{}", erpInventory);

        HttpResponse httpResponse = httpRequest.bearerAuth(accessToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("仓库列表查询失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("仓库列表查询失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("仓库列表查询失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
            Object data = body.get("data");
            JSONObject body1 = JSONUtil.parseObj(data);
            Object data1 = body1.get("records");
            String s = com.alibaba.fastjson.JSONObject.toJSONString(data1);
            List<ErpWarehouseVO> warehouseVOS = com.alibaba.fastjson.JSONObject.parseArray(s, ErpWarehouseVO.class);
            if (ObjectUtils.isEmpty(warehouseVOS)) {
                log.info("仓库列表查询失败, " + JSONUtil.toJsonStr(body));
            }
            log.info("仓库列表完成，共{}条数据", warehouseVOS.size());
            return warehouseVOS;
        }
    }

    /**
     * 从信息化系统获取erp配件下单客户
     *
     * @param pageSize
     * @param pageNo
     * @param name
     * @return
     */
    @Override
    public List<ErpSaleCustomerVO> getSaleCustomerList(Integer pageSize, Integer pageNo, String name) {
        log.info("从信息化系统获取erp配件下单客户");
        String erpToken = authService.getErpToken();
        String url = preUrl + customer;
        HttpRequest httpRequest;
        httpRequest = HttpRequest.get(url).form("name", name);
        log.info("erp配件下单客户查询:{}", url);

        HttpResponse httpResponse = httpRequest.bearerAuth(erpToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("erp配件下单客户查询失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("erp配件下单客户查询失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("erp配件下单客户查询失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
            Object data = body.get("data");
            List<ErpSaleCustomerVO> erpSaleCustomerVOS = JsonUtils.parseArray(JsonUtils.toJsonString(data), ErpSaleCustomerVO.class);
            if (ObjectUtils.isEmpty(erpSaleCustomerVOS)) {
                log.info("erp配件下单客户查询失败, " + JSONUtil.toJsonStr(body));
            }
            log.info("erp配件下单客户查询完成，共{}条数据", erpSaleCustomerVOS.size());
            if (CollectionUtils.isEmpty(erpSaleCustomerVOS)) {
                return new ArrayList<>();
            } else if (StringUtils.isNotBlank(name)) {
                return erpSaleCustomerVOS.stream().filter(o -> o.getFname().contains(name)).collect(Collectors.toList());
            } else {
                return erpSaleCustomerVOS;
            }

        }
    }

    /**
     * 从信息化系统获取erp配件下单主成单销售员
     *
     * @param pageSize
     * @param pageNo
     * @param name
     * @return
     */
    @Override
    public List<ErpSaleManVO> getSaleManList(Integer pageSize, Integer pageNo, String name) {
        log.info("从信息化系统获取erp配件下单主成单销售员");
        String erpToken = authService.getErpToken();
        String url = preUrl + manApi;
        HttpRequest httpRequest;
        httpRequest = HttpRequest.get(url);
        log.info("erp配件下单主成单销售员:{}", url);

        HttpResponse httpResponse = httpRequest.bearerAuth(erpToken).timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("erp配件下单主成单销售员失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("erp配件下单主成单销售员查询失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("erp配件下单主成单销售员查询失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
            Object data = body.get("data");
            List<ErpSaleManVO> erpSaleManVOS = JsonUtils.parseArray(JsonUtils.toJsonString(data), ErpSaleManVO.class);
            if (ObjectUtils.isEmpty(erpSaleManVOS)) {
                log.info("erp配件下单主成单销售员查询失败, " + JSONUtil.toJsonStr(body));
            }
            log.info("erp配件下单主成单销售员查询完成，共{}条数据", erpSaleManVOS.size());
            if (CollectionUtils.isEmpty(erpSaleManVOS)) {
                return new ArrayList<>();
            } else if (StringUtils.isNotBlank(name)) {
                return erpSaleManVOS.stream().filter(o -> o.getFname().contains(name)).collect(Collectors.toList());
            } else {
                return erpSaleManVOS;
            }
        }
    }

    /**
     * 获取售后工单
     *
     * @param number
     * @return
     */
    @Override
    public List<PostSaleOrderVO> getPostSaleOrderList(String number, Integer pageSize, Integer pageNo) {
        log.info("获取售后系统工单列表，参数：{}", number);
        String url = platformUrl + orderListApi;
        HttpRequest httpRequest;
        httpRequest = HttpRequest.get(url).form("number", number);
        log.info("售后系统工单列表:{}", url);
        HttpResponse httpResponse = httpRequest.timeout(1000 * 60).execute();
        if (!httpResponse.isOk()) {
            log.error("获取售后系统工单列表失败, " + JSONUtil.toJsonStr(httpResponse));
            throw new BusinessException("获取售后系统工单列表失败");
        } else {
            JSONObject body = JSONUtil.parseObj(httpResponse.body());
            if (body.getInt("status") != HttpStatus.HTTP_OK) {
                log.info("获取售后系统工单列表失败, " + JSONUtil.toJsonStr(body));
                throw new BusinessException(body.getStr("message"));
            }
            Object data = body.get("data");
            List<PostSaleOrderVO> postSaleOrderVOS = JsonUtils.parseArray(JsonUtils.toJsonString(data), PostSaleOrderVO.class);
            if (ObjectUtils.isEmpty(postSaleOrderVOS)) {
                log.info("获取售后系统工单列表失败, " + JSONUtil.toJsonStr(body));
            }
            log.info("获取售后系统工单列表完成，共{}条数据", postSaleOrderVOS.size());
            return postSaleOrderVOS;
        }
    }
}
