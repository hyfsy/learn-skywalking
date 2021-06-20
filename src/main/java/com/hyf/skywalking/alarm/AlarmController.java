package com.hyf.skywalking.alarm;

import com.hyf.skywalking.alarm.dingtalk.DingTalkService;
import com.hyf.skywalking.alarm.mail.MailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2021/06/15
 */
@RestController
@RequestMapping("alarm")
public class AlarmController {

    @Resource
    private DingTalkService dingTalkService;
    @Resource
    private MailService     mailService;

    @PostMapping(value = "dingTalk", consumes = "application/json")
    public void dingTalk(@RequestBody List<AlarmMessage> alarmMessageList) {
        dingTalkService.send(dingTalkService.format(alarmMessageList));
    }

    @PostMapping(value = "email", consumes = "application/json")
    public void email(@RequestBody List<AlarmMessage> alarmMessageList) {
        mailService.send(mailService.format(alarmMessageList));
    }
}
