package com.github.bluecatlee.gs4d.sequence.task;

import com.github.bluecatlee.gs4d.sequence.service.SequenceTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 检测数据库存储的sequenceTime是否正确
 */
@Component
public class DbSequenceTimeListener {

    @Value("#{settings['emailAddress']}")
    public String emailAddress;

    @Autowired
    private SequenceTimeService sequenceTimeService;

//    @Autowired
//    private MailSendService mailSendService;

    protected static Logger logger = LoggerFactory.getLogger(DbSequenceTimeListener.class);

    public void listenSequenceTime() {
//        try {
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            String currentTime = format.format(new Date());
//            String sequenceTime = this.sequenceTimeService.getTime();
//            if (Integer.valueOf(sequenceTime.replaceAll("-", "")) < Integer.valueOf(currentTime.replaceAll("-", ""))) {
//                EmailSendRequest emailSendRequest = new EmailSendRequest();
//                emailSendRequest.setDataSign(0L);
//                emailSendRequest.setTenantNumId(1L);
//                emailSendRequest.setEmailModel(1L);
//                emailSendRequest.setEmailSubject("platform_sequence_time 序列号时间表时间监控异常!");
//                emailSendRequest.setEmailSenderType(1L);
//                emailSendRequest.setEmailRecipients(this.emailAddress);
//                List<EmailSendDetail> emailSendDetailList = new ArrayList();
//                EmailSendDetail emailSendDetail = new EmailSendDetail();
//                emailSendDetail.setRequestMehtodName("listenSequenceTime");
//                emailSendDetail.setRequestParam("platform_sequence_time 序列号时间表时间监控异常!");
//                InetAddress inetAddress = InetAddress.getLocalHost();
//                String hostName = inetAddress.getHostName();
//                String hostAddress = inetAddress.getHostAddress();
//                emailSendDetail.setResponseFunllBody("platform_sequence_time 序列号时间表时间监控异常!主机名为:" + hostName + ",IP地址为:" + hostAddress);
//                emailSendDetail.setSystemName("sequence");
//                emailSendDetailList.add(emailSendDetail);
//                emailSendRequest.setEmailDetail(emailSendDetailList);
//                emailSendRequest.setTenantNumId(1L);
//                emailSendRequest.setDataSign(0L);
//                this.mailSendService.sendEmail(emailSendRequest);
//                logger.info("邮件发送成功！");
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }

    }
}

