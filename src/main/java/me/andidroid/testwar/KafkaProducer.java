package me.andidroid.testwar;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

public class KafkaProducer
{
    
    /**
     * Logging via slf4j api
     */
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(KafkaProducer.class);
    
    // @Merge
    @Incoming("entity-messages")
    @Outgoing("kafka-messages")
    public Message<String> process(Message<String> m)
    {
        String s = m.getPayload();
        LOGGER.info("process {}", s);
        return m;
    }
}
