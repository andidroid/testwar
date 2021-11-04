package me.andidroid.testwar;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.validation.constraints.Email;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class MailService
{
    @Resource(mappedName = "java:jboss/mail/Default")
    private Session mailSession;
    
    @Inject
    @Email
    @ConfigProperty(name = "test.mail", defaultValue = "test@andidroid.io")
    private String mail;
    
    public MailService()
    {
        
    }
    
    public MailService(Session mailSession, String mail)
    {
        this.mailSession = mailSession;
        this.mail = mail;
    }
    
    public void init(@Observes
    @Initialized(ApplicationScoped.class)
    Object init)
    {
        System.out.println("### server ApplicationScoped ready");
        try
        {
            String subject = "testwar started";
            String message = "Application testwar started.\n\nMail sent from WildFly";
            
            sendMail(subject, message);
        }
        catch(MessagingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void sendMail(String subject, String message) throws MessagingException
    {
        MimeMessage m = new MimeMessage(mailSession);
        Address from = new InternetAddress(mail);
        Address[] to = new InternetAddress[]{new InternetAddress(mail) };
        m.setFrom(from);
        m.setRecipients(RecipientType.TO, to);
        m.setSubject(subject);
        m.setSentDate(new java.util.Date());
        m.setContent(message, "text/plain");
        Transport.send(m);
        
        System.out.println("send message " + subject + " to " + mail);
    }
    
    public void destroy(@Observes
    @Destroyed(ApplicationScoped.class)
    Object init)
    {
        mailSession = null;
    }
    
}
