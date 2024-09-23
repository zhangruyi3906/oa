package com.lh.oa.framework.sms.core.client;

import com.lh.oa.framework.common.exception.ErrorCode;
import com.lh.oa.framework.sms.core.enums.SmsFrameworkErrorCodeConstants;

import java.util.function.Function;

/**
 * 将 API 的错误码，转换为通用的错误码
 *
 * @see SmsCommonResult
 * @see SmsFrameworkErrorCodeConstants
 *
 * @author
 */
public interface SmsCodeMapping extends Function<String, ErrorCode> {
}
