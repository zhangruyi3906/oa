package com.lh.oa.module.report.enums;

import com.lh.oa.framework.common.exception.ErrorCode;

/**
 * Report 错误码枚举类
 *
 * report 系统，使用 1-003-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== AUTH 模块 1003000000 ==========
    ErrorCode GO_VIEW_PROJECT_NOT_EXISTS = new ErrorCode(1003000000, "GoView 项目不存在");

}
