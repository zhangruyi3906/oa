package com.lh.oa.framework.common.exception.util;

import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 主要封装一些抛出业务异常的场景，缩短代码长度
 *
 * @author tanghanlin
 * @since 2023/10/23
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionThrowUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 当参数为空时抛出业务异常
     *
     * @param object    对象
     * @param errorCode 错误码
     */
    public static void throwIfNull(Object object, ErrorCode errorCode) {
        if (Objects.isNull(object)) {
            throw exception(errorCode);
        }
    }

    /**
     * 当集合为空时抛出业务异常
     *
     * @param collection 集合
     * @param errorCode  错误码
     */
    public static void throwIfEmpty(Collection<?> collection, ErrorCode errorCode) {
        if (CollectionUtils.isEmpty(collection)) {
            throw exception(errorCode);
        }
    }

    /**
     * 当Map为空时抛出业务异常
     *
     * @param map       map
     * @param errorCode 错误码
     */
    public static void throwIfEmpty(Map<?, ?> map, ErrorCode errorCode) {
        if (MapUtils.isEmpty(map)) {
            throw exception(errorCode);
        }
    }

    /**
     * 当表达式结果为true时抛出业务异常
     *
     * @param expression 判断表达式
     * @param errorCode  错误码
     */
    public static void throwIfTrue(Boolean expression, ErrorCode errorCode) {
        if (expression) {
            throw exception(errorCode);
        }
    }


    /**
     * 当参数为空时抛出业务异常
     *
     * @param object        对象
     * @param errorMessage  错误码
     */
    public static void throwIfNull(Object object, String errorMessage) {
        if (Objects.isNull(object)) {
            throw new BusinessException(errorMessage);
        }
    }

    /**
     * 当集合为空时抛出业务异常
     *
     * @param collection    集合
     * @param errorMessage  错误码
     */
    public static void throwIfEmpty(Collection<?> collection, String errorMessage) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(errorMessage);
        }
    }

    /**
     * 当Map为空时抛出业务异常
     *
     * @param map           map
     * @param errorMessage  错误码
     */
    public static void throwIfEmpty(Map<?, ?> map, String errorMessage) {
        if (MapUtils.isEmpty(map)) {
            throw new BusinessException(errorMessage);
        }
    }

    /**
     * 当表达式结果为true时抛出业务异常
     *
     * @param expression    判断表达式
     * @param errorMessage  错误码
     */
    public static void throwIfTrue(Boolean expression, String errorMessage) {
        if (expression) {
            throw new BusinessException(errorMessage);
        }
    }

}
