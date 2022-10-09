package me.andidroid.testwar;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

public class MessagingEntityListener
{
    /**
     * Logging via slf4j api
     */
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MessagingEntityListener.class);
    
    @Inject
    @ConfigProperty(name = "messagingservice.enabled", defaultValue = "true")
    private String enabled;
    
    @Inject
    @Channel("entity-messages")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 300)
    private Emitter<String> emitter;
    
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
                emitter.send(name + "-" + obj);
            }
        }
        catch(Exception e)
        {
            LOGGER.error("error sending async message to sse bradcaster", e);
        }
    }
    
}
