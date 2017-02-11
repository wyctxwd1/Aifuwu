package com.ab.yuri.aifuwu.util;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Yuri on 2017/2/10.
 */

public class Mail {
    public static void sendMail(String content,String email) throws MessagingException {
        Properties props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.163.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        // 设置环境信息
        Session session = Session.getInstance(props);

        // 创建邮件对象
        Message msg = new MimeMessage(session);
        msg.setSubject("南邮助手反馈");
        // 设置邮件内容
        String feedbackContent="反馈内容："+content;
        String feedbackEmail="联系方式："+email;
        msg.setText(feedbackContent+"\n"+feedbackEmail);
        // 设置发件人
        msg.setFrom(new InternetAddress("wyctxwd3@163.com"));

        Transport transport = session.getTransport();
        // 连接邮件服务器
        transport.connect("wyctxwd3@163.com", "wyctxwd1");
        // 发送邮件
        transport.sendMessage(msg, new Address[] {new InternetAddress("469104809@qq.com")});
        // 关闭连接
        transport.close();
    }
}
