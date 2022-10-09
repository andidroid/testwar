package me.andidroid.testwar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.microshed.testing.jaxrs.RESTClient;
import org.microshed.testing.jupiter.MicroShedTest;
import org.microshed.testing.SharedContainerConfig;
import org.microshed.testing.testcontainers.ApplicationContainer;
import org.testcontainers.junit.jupiter.Container;

import static io.restassured.RestAssured.given;
import io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.containsString;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;

import static io.restassured.http.ContentType.TEXT;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Disabled
@Tag("IntegrationTest")
public class MailServiceIT
{
    
    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTPS).withConfiguration(GreenMailConfiguration.aConfig().withUser("test@andidroid.io", "test", "test")).withPerMethodLifecycle(false);
    
    private MailService mailService = new MailService(greenMail.getSmtps().createSession(), "test@andidroid.io");
    
    @Test
    public void testSendMail()
    {
        try
        {
            mailService.sendMail("Test", "Message");
        }
        catch(MessagingException e)
        {
            Assertions.fail("error sending mail", e);
        }
        
        // awaitility
        await().atMost(2, SECONDS).untilAsserted(() -> {
            MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            assertEquals(1, receivedMessages.length);
            
            MimeMessage receivedMessage = receivedMessages[0];
            assertEquals("Message", GreenMailUtil.getBody(receivedMessage));
            assertEquals(1, receivedMessage.getAllRecipients().length);
            assertEquals("test@andidroid.io", receivedMessage.getAllRecipients()[0].toString());
        });
    }
}
