package me.andidroid.testwar;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import jakarta.validation.constraints.Email;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class MailService
{
    // @Resource(mappedName = "java:jboss/mail/Default")
    private Session mailSession;
    
    @Inject
    @Email
    @ConfigProperty(name = "MAIL_UESERNAME", defaultValue = "abutest693@gmail.com")
    private String mail;
    
    public MailService()
    {
        
    }
    
    public MailService(Session mailSession, String mail)
    {
        this.mailSession = mailSession;
        this.mail = mail;
    }
    
    // public void init(@Observes
    // @Initialized(ApplicationScoped.class)
    // Object init)
    // {
    // System.out.println("### server ApplicationScoped ready");
    // try
    // {
    // String subject = "testwar started";
    // String message = "Application testwar started.\n\nMail sent from WildFly";
    
    // sendMail(subject, message);
    // }
    // catch(MessagingException e)
    // {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    
    public void sendMail(String subject, String message) throws MessagingException
    {
        System.out.println("try send message " + subject + " to " + mail);
        
        MimeMessage m = new MimeMessage(mailSession);
        Address from = new InternetAddress(mail);
        Address[] to = new InternetAddress[]{new InternetAddress(mail) };
        m.setFrom(from);
        m.setRecipients(RecipientType.TO, to);
        m.setSubject(subject);
        m.setSentDate(new java.util.Date());
        m.setContent(message, "text/plain");
        Transport.send(m);
        
        System.out.println("success: send message " + subject + " to " + mail);
    }
    
    public void destroy(@Observes
    @Destroyed(ApplicationScoped.class)
    Object init)
    {
        mailSession = null;
    }
    
}
