package mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
 public class Email_Autherticator extends Authenticator 
    { 
	 private String username;
	 private String password;
        public Email_Autherticator() 
        { 
            super(); 
        } 

        public Email_Autherticator(String user, String pwd) 
        { 
            super(); 
            username = user; 
            password = pwd; 
        } 

        public PasswordAuthentication getPasswordAuthentication() 
        { 
            return new PasswordAuthentication(username, password); 
        } 
    } 
