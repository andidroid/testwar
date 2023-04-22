package me.andidroid.testwar;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.json.Json;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.Table;
import java.util.UUID;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.instrumentation.annotations.WithSpan;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public class MessagingEntityListener {
    public static final int EXPIRATION_TIME = 5000;

    /**
     * Logging via slf4j api
     */
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MessagingEntityListener.class);

    @Inject
    @ConfigProperty(name = "messagingservice.enabled", defaultValue = "true")
    private String enabled;

    @Inject
    // @JMSConnectionFactory("java:/JmsXA") // define own jms connection factory,
    // default is java:/ConnectionFactory
    private JMSContext context;
    // @Resource(lookup = "java:global/remoteContext/TestTopic")
    // private Topic topic;

    @Resource(lookup = "java:global/remoteContext/TestQueue")
    private Queue topic;

    @Inject
    private Tracer tracer;

    /**
     *
     */
    @PostConstruct
    public void initialize() {
        LOGGER.info("MessagingEntityListener.initialize()");
    }

    @Override
    @PreDestroy
    public void finalize() {

    }

    @PostPersist
    public void onCreated(Object obj) {
        this.broadCastEvent("create", obj);
    }

    @PostRemove
    public void onDeleted(Object obj) {
        this.broadCastEvent("delete", obj);
    }

    @PostUpdate
    public void onUpdated(Object obj) {
        this.broadCastEvent("update", obj);
    }

    @PostLoad
    public void onRead(Object obj) {
        this.broadCastEvent("read", obj);
    }

    public void broadCastEvent(String name, Object obj) {
        LOGGER.info("broadCastEvent " + name);
        Span span = tracer.spanBuilder("mesaging").setSpanKind(SpanKind.PRODUCER).startSpan();
        span.setAttribute("ClientID", this.context.getClientID());

        try (Scope scope = span.makeCurrent()) {
            if (Boolean.valueOf(this.enabled)) {

                Table tableAnnotation = obj.getClass().getAnnotation(Table.class);
                String moduleKey = tableAnnotation.schema();
                String resource = tableAnnotation.name();
                String json = Json.createObjectBuilder().add("event", name).add("moduleKey", moduleKey)
                        .add("entity", obj.getClass().getSimpleName()).add("resource", resource)
                        .add("id", obj.toString()).build().toString();

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
        } catch (Exception e) {
            LOGGER.error("error sending async message to sse bradcaster", e);
            span.setStatus(StatusCode.ERROR, e.getLocalizedMessage());
            span.recordException(e);
        } finally {
            span.end(); // Cannot set a span after this call
        }

    }

}
