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
    // ��������� 
    private String host = "smtp.163.com"; 
    // �������������û��� 
    private String username = "kaoyanqun@163.com"; 
    // ����������� 
    private String password = "1q2w3e4r"; 

    private String mail_head_name = "�ʼ�ͷ..."; 

    private String mail_head_value = "�ʼ�ͷ..."; 


    private String mail_from = "kaoyanqun@163.com"; 

    private String mail_subject = "�һ�����"; 


    private String personalName = "�ҵ��ʼ�"; 

    public MainTest() 
    { 
    } 

    /** 
     * �˶δ�������������ͨ�����ʼ� 
     */ 
    public boolean send(String pass,String email_to) { 
        try 
        { 
            Properties props = new Properties(); // ��ȡϵͳ���� 
            Authenticator auth = new Email_Autherticator(username,password); // �����ʼ��������û���֤ 
            props.put("mail.smtp.host", host); 
            props.put("mail.smtp.auth", "true"); 
      
            props.put("mail.smtp.port", 25);
            Session session = Session.getDefaultInstance(props, auth); 
            // ����session,���ʼ�����������ͨѶ�� 
            MimeMessage message = new MimeMessage(session); 
            // message.setContent("foobar, "application/x-foobar"); // �����ʼ���ʽ 
            message.setSubject(mail_subject); // �����ʼ����� 
            message.setText("���������ǣ�"+pass+"\n�뾡���޸�"); // �����ʼ����� 
            message.setHeader(mail_head_name, mail_head_value); // �����ʼ����� 
            message.setSentDate(new Date()); // �����ʼ��������� 
            Address address = new InternetAddress(mail_from, personalName); 
            message.setFrom(address); // �����ʼ������ߵĵ�ַ 
            Address toAddress = new InternetAddress(email_to); // �����ʼ����շ��ĵ�ַ 
            message.addRecipient(Message.RecipientType.TO, toAddress); 
            Transport.send(message); // �����ʼ� 
           return true;
        } catch (Exception ex) { 
           ex.printStackTrace();
        } 
        return false;
    }
}
