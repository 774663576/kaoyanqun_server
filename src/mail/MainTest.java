package mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainTest 
{ 
    // 邮箱服务器 
    private String host = "smtp.163.com"; 
    // 这个是你的邮箱用户名 
    private String username = "kaoyanqun@163.com"; 
    // 你的邮箱密码 
    private String password = "1q2w3e4r"; 

    private String mail_head_name = "邮件头..."; 

    private String mail_head_value = "邮件头..."; 


    private String mail_from = "kaoyanqun@163.com"; 

    private String mail_subject = "找回密码"; 


    private String personalName = "我的邮件"; 

    public MainTest() 
    { 
    } 

    /** 
     * 此段代码用来发送普通电子邮件 
     */ 
    public boolean send(String pass,String email_to) { 
        try 
        { 
            Properties props = new Properties(); // 获取系统环境 
            Authenticator auth = new Email_Autherticator(username,password); // 进行邮件服务器用户认证 
            props.put("mail.smtp.host", host); 
            props.put("mail.smtp.auth", "true"); 
      
            props.put("mail.smtp.port", 25);
            Session session = Session.getDefaultInstance(props, auth); 
            // 设置session,和邮件服务器进行通讯。 
            MimeMessage message = new MimeMessage(session); 
            // message.setContent("foobar, "application/x-foobar"); // 设置邮件格式 
            message.setSubject(mail_subject); // 设置邮件主题 
            message.setText("您的密码是："+pass+"\n请尽快修改"); // 设置邮件正文 
            message.setHeader(mail_head_name, mail_head_value); // 设置邮件标题 
            message.setSentDate(new Date()); // 设置邮件发送日期 
            Address address = new InternetAddress(mail_from, personalName); 
            message.setFrom(address); // 设置邮件发送者的地址 
            Address toAddress = new InternetAddress(email_to); // 设置邮件接收方的地址 
            message.addRecipient(Message.RecipientType.TO, toAddress); 
            Transport.send(message); // 发送邮件 
           return true;
        } catch (Exception ex) { 
           ex.printStackTrace();
        } 
        return false;
    }
}
