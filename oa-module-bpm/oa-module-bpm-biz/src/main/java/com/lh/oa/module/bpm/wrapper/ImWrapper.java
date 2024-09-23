package com.lh.oa.module.bpm.wrapper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.module.bpm.utils.HttpClientUtil;
import com.lh.oa.module.bpm.utils.TLSSigAPIv2;
import com.lh.oa.module.bpm.wrapper.vo.ImCustomMsgContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Set;

/**
 * @author zhangfan
 * @since 2023/11/7 11:09
 */
@Slf4j
@Component
public class ImWrapper {

    @Value("${im.sdk_appid:1}")
    private long SDK_APPID;

    @Value("${im.key:1}")
    private String KEY;

    private static final String PREFIX_BASE_URL = "https://console.tim.qq.com/"; //IM API地址

    private static final String IDENTIFIER = "administrator"; //管理员账号

    private static final String TODO_ACCOUNT = "daiban"; //待办账号

    private static final String ACCOUNT_MESSAGE_SEND = "/v4/openim/sendmsg"; //发送单聊信息

    private static final String ACCOUNT_MESSAGE_BATCH_SEND = "/v4/openim/batchsendmsg"; //批量发送单聊信息

    private static final int ONE_DAY_SECOND = 86400;

    public void sendAccountMessage(String message, String account) {
        JSONObject params = new JSONObject();
        params.put("SyncOtherMachine", 1);
        params.put("From_Account", TODO_ACCOUNT);
        params.put("To_Account", account);
        params.put("MsgRandom", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        JSONArray array = new JSONArray();
        JSONObject bodyParams = new JSONObject();
        bodyParams.put("MsgType", "TIMTextElem");
        JSONObject msgParams = new JSONObject();
        msgParams.put("Text", message);
        bodyParams.put("MsgContent", msgParams);
        array.add(bodyParams);
        params.put("MsgBody", array);

        log.info("data:" + params);
        JSONObject jsonObj = HttpClientUtil.httpPost(this.generateImFullRestUrl(ACCOUNT_MESSAGE_SEND), params);
        log.info("result:" + jsonObj);
    }
    public void sendAccountMessageFromAdmin(String message, String account) {
        JSONObject params = new JSONObject();
        params.put("SyncOtherMachine", 1);
        params.put("From_Account", IDENTIFIER);
        params.put("To_Account", account);
        params.put("MsgRandom", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        JSONArray array = new JSONArray();
        JSONObject bodyParams = new JSONObject();
        bodyParams.put("MsgType", "TIMTextElem");
        JSONObject msgParams = new JSONObject();
        msgParams.put("Text", message);
        bodyParams.put("MsgContent", msgParams);
        array.add(bodyParams);
        params.put("MsgBody", array);

        log.info("data:" + params);
        JSONObject jsonObj = HttpClientUtil.httpPost(this.generateImFullRestUrl(ACCOUNT_MESSAGE_SEND), params);
        log.info("result:" + jsonObj);
    }


    public void sendAccountCustomMessage(String account, ImCustomMsgContent content) {
        JSONObject params = new JSONObject();
        params.put("SyncOtherMachine", 1);
        params.put("From_Account", TODO_ACCOUNT);
        params.put("To_Account", account);
        params.put("MsgRandom", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        JSONArray array = new JSONArray();
        JSONObject bodyParams = new JSONObject();
        bodyParams.put("MsgType", "TIMCustomElem");
        JSONObject msgParams = new JSONObject();
        msgParams.put("Data", content.getBusinessType());
        msgParams.put("Desc", content.getText());
        msgParams.put("Ext", JsonUtils.toJsonString(content));
        bodyParams.put("MsgContent", msgParams);
        array.add(bodyParams);
        params.put("MsgBody", array);

        log.info("data:" + params);
        JSONObject jsonObj = HttpClientUtil.httpPost(this.generateImFullRestUrl(ACCOUNT_MESSAGE_SEND), params);
        log.info("result:" + jsonObj);
    }

    public void batchSendAccountMessage(String message, Set<String> accountSet) {
        JSONObject params = new JSONObject();
        params.put("SyncOtherMachine", 1);
        params.put("From_Account", IDENTIFIER);
        params.put("To_Account", accountSet.toArray());
        params.put("MsgRandom", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        JSONArray array = new JSONArray();
        JSONObject bodyParams = new JSONObject();
        bodyParams.put("MsgType", "TIMTextElem");
        JSONObject msgParams = new JSONObject();
        msgParams.put("Text", message);
        bodyParams.put("MsgContent", msgParams);
        array.add(bodyParams);
        params.put("MsgBody", array);

        log.info("data:" + params);
        JSONObject jsonObj = HttpClientUtil.httpPost(this.generateImFullRestUrl(ACCOUNT_MESSAGE_BATCH_SEND), params);
        log.info("result:" + jsonObj);
    }

    public String generateImFullRestUrl(String restUrl) {
        return PREFIX_BASE_URL +
                restUrl +
                "?sdkappid=" + SDK_APPID +
                "&identifier=" + IDENTIFIER +
                "&usersig=" + new TLSSigAPIv2(SDK_APPID, KEY).genUserSig(IDENTIFIER, ONE_DAY_SECOND) +
                "&random=" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) +
                "&contenttype=json";
    }

}
