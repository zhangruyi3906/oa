package com.lh.oa.module.system.controller.admin.kingdee.service;

import java.util.List;
public interface K3cloudService {
    //校验登录信息
    Boolean checkLogin() throws Exception;

    /**
     *单据查询
     * @param formid 业务对象表单Id（必录）
     * @param FieldKeys 需查询的字段key集合，字符串类型，格式："key1,key2,..."（必录）
     * @param FilterString 过滤条件，类似sql中where条件内容，格式："COLUMNA='VALUEA' and COLUMNB='VALUEB'"
     * @param OrderString 排序字段，类似sql中order by条件内容，格式："COLUMNA desc"
     */
    List<List<Object>> executeBillQuery(String formid, String FieldKeys, String FilterString,String OrderString) throws Exception;

    /**
     * 保存销售订单
     * @param data JSON格式数据
     */
    String saveSALSaleOrder(String data);
}
