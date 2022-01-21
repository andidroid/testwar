package me.andidroid.testwar;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.json.Json;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.Table;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public class MessagingEntityListener
{
    public static final int EXPIRATION_TIME = 5000;
    
    /**
     * Logging via slf4j api
     */
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MessagingEntityListener.class);
    
    @Inject
    @ConfigProperty(name = "messagingservice.enabled", defaultValue = "true")
    private String enabled;
    
    @Inject
    // @JMSConnectionFactory("java:/JmsXA") // define own jms connection factory, default is java:/ConnectionFactory
    private JMSContext context;
    
    // @Resource(lookup = "java:global/remoteContext/TestTopic")
    // private Topic topic;

    @Resource(lookup = "java:global/remoteContext/TestQueue")
    private Queue topic;
    
    /**
     *
     */
    @PostConstruct
    public void initialize()
    {
        LOGGER.info("MessagingEntityListener.initialize()");
    }

    @Override
    @PreDestroy
    public void finalize()
    {
        
    }
    
    @PostPersist
    public void onCreated(Object obj)
    {
        this.broadCastEvent("create", obj);
    }
    
    @PostRemove
    public void onDeleted(Object obj)
    {
        this.broadCastEvent("delete", obj);
    }
    
    @PostUpdate
    public void onUpdated(Object obj)
    {
        this.broadCastEvent("update", obj);
    }
    
    @PostLoad
    public void onRead(Object obj)
    {
        this.broadCastEvent("read", obj);
    }
    
    public void broadCastEvent(String name, Object obj)
    {
        LOGGER.info("broadCastEvent " + name);
        try
        {
            if(Boolean.valueOf(this.enabled))
            {
                Table tableAnnotation = obj.getClass().getAnnotation(Table.class);
                String moduleKey = tableAnnotation.schema();
                String resource = tableAnnotation.name();
                String json = Json.createObjectBuilder().add("event", name).add("moduleKey", moduleKey).add("entity", obj.getClass().getSimpleName()).add("resource", resource).add("id", obj.toString()).build().toString();
                
                TextMessage message = this.context.createTextMessage();
                message.setJMSCorrelationID(UUID.randomUUID().toString());
                message.setStringProperty("sender", "test");
                message.setJMSExpiration(EXPIRATION_TIME);
                message.setText(json);
                message.setStringProperty("applicationName", moduleKey);
                message.setStringProperty("title", resource);
                message.setStringProperty("topic", "topic");
                
                JMSProducer producer = this.context.createProducer();
                producer.setTimeToLive(EXPIRATION_TIME);
                producer.setAsync(new AsyncMessageCompletionListener());
                producer.send(this.topic, message);
            }
        }
        catch(Exception e)
        {
            LOGGER.error("error sending async message to sse bradcaster", e);
        }
    }
    
}
