package me.andidroid.testwar;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.RemoteCacheContainer;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

@RequestScoped
public class CacheService {
    /**
     * Logging via slf4j api
     */
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CacheService.class);

    // @Resource(lookup = "java:jboss/infinispan/remote-container/remote")
    // private RemoteCacheManager client;

    @Inject
    private Tracer tracer;

    public CacheService() {

    }

    public static final String REMOTE_CACHE_NAME = "testcache";

    @Resource(lookup = "java:jboss/infinispan/remote-container/testremotecachecontainer")
    private RemoteCacheContainer remoteCacheContainer;

    public void putRemoteCache(String key, String value) {
        Span span = tracer.spanBuilder("putRemoteCache").setSpanKind(SpanKind.CLIENT).startSpan();
        try (Scope scope = span.makeCurrent()) {
            String object = String.format("%s (%s)", value, new Date());
            remoteCacheContainer.getCache(REMOTE_CACHE_NAME).put(key, object);
            LOGGER.info("put Object in cache key=" + key + " ,value=" + object);
        } catch (Exception e) {
            LOGGER.error("error writing data into cahce", e);
            span.setStatus(StatusCode.ERROR, e.getLocalizedMessage());
            span.recordException(e);
            throw e;
        } finally {
            span.end(); // Cannot set a span after this call
        }
    }

    public Object getRemoteCache(String key) {
        Span span = tracer.spanBuilder("getRemoteCache").setSpanKind(SpanKind.CLIENT).startSpan();
        try (Scope scope = span.makeCurrent()) {
            Object object = remoteCacheContainer.getCache(REMOTE_CACHE_NAME).get(key);
            LOGGER.info("get Object from cache key=" + key + " ,value=" + object);
            return object;
        } catch (Exception e) {
            LOGGER.error("error reading data from cahce", e);
            span.setStatus(StatusCode.ERROR, e.getLocalizedMessage());
            span.recordException(e);
            throw e;
        } finally {
            span.end(); // Cannot set a span after this call
        }
    }

    // public CacheService(RemoteCacheManager client)
    // {
    // this.client = client;
    // }

    // @PostConstruct
    // public void initialize()
    // {
    // this.client.start();

    // }

    // @PreDestroy
    // @Override
    // protected void finalize() throws Throwable
    // {
    // this.client.close();
    // }
}
