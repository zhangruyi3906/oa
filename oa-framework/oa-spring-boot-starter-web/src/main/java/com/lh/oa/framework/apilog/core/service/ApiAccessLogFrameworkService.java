package com.lh.oa.framework.apilog.core.service;

/**
 * API 访问日志 Framework Service 接口
 *
 * @author
 */
public interface ApiAccessLogFrameworkService {

    /**
     * 创建 API 访问日志
     *
     * @param apiAccessLog API 访问日志
     */
    void createApiAccessLog(ApiAccessLog apiAccessLog);

}
