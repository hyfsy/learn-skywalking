package com.hyf.skywalking.alarm.dingtalk;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.hyf.skywalking.alarm.AlarmMessage;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2021/06/20
 */
@Service
public class DingTalkService {

    @Value("${alarm.ding-talk.webhook}")
    private String webhook;
    @Value("${alarm.ding-talk.secret}")
    private String secret;

    public void send(String message) {
        try {

            long timestamp = System.currentTimeMillis();
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            String sign = URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), "UTF-8");

            String serverUrl = webhook + "&timestamp=" + timestamp + "&sign=" + sign;

            DefaultDingTalkClient client = new DefaultDingTalkClient(serverUrl);
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("text");
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            text.setContent(message);
            request.setText(text);

            System.out.println("发送钉钉告警");
            client.execute(request);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public String format(List<AlarmMessage> alarmMessageList) {
        StringBuilder sb = new StringBuilder();
        for (AlarmMessage alarmMessage : alarmMessageList) {
            sb.append(alarmMessage.toString());
        }
        return sb.toString();
    }
}
