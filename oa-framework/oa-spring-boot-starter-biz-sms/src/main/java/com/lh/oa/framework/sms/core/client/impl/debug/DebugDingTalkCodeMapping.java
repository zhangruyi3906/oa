package com.lh.oa.framework.sms.core.client.impl.debug;

import com.lh.oa.framework.common.exception.ErrorCode;
import com.lh.oa.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.lh.oa.framework.sms.core.client.SmsCodeMapping;
import com.lh.oa.framework.sms.core.enums.SmsFrameworkErrorCodeConstants;

import java.util.Objects;

/**
 * 钉钉的 SmsCodeMapping 实现类
 *
 * @author
 */
public class DebugDingTalkCodeMapping implements SmsCodeMapping {

    @Override
    public ErrorCode apply(String apiCode) {
        return Objects.equals(apiCode, "0") ? GlobalErrorCodeConstants.SUCCESS : SmsFrameworkErrorCodeConstants.SMS_UNKNOWN;
    }

}
