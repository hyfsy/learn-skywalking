package com.hyf.skywalking.alarm.mail;

import com.hyf.skywalking.alarm.AlarmMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2021/06/16
 */
@Service
public class MailService {

    @Resource
    private JavaMailSender mailSender;

    public void send(String text) {
        try {

            String from = "1577975140@qq.com";
            String to = "1577975140@qq.com";

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setText(text, true);

            System.out.println("发送邮件告警");
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

    public String format(List<AlarmMessage> alarmMessageList) {
        StringBuilder sb = new StringBuilder();
        for (AlarmMessage alarmMessage : alarmMessageList) {
            sb.append(alarmMessage.toString());
        }
        return sb.toString().replace("\n", "<br/>");
    }
}
