package me.andidroid.testwar;

import java.util.Collection;
import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

@RequestScoped
public class TestService {
    /**
     * Logging via slf4j api
     */
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TestService.class);

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Tracer tracer;

    public TestService() {

    }

    public TestService(EntityManager em, Tracer tracer) {
        this.em = em;
        this.tracer = tracer;
    }

    public Test getById(long id) {
        Span prepareHelloSpan = tracer.spanBuilder("prepare-hello").startSpan();
        prepareHelloSpan.makeCurrent();

        Span processHelloSpan = tracer.spanBuilder("process-hello").startSpan();
        processHelloSpan.makeCurrent();

        Test test = em.find(Test.class, id);

        processHelloSpan.end();
        prepareHelloSpan.end();

        return test;
    }

    public Collection<Test> getAll() {
        LOGGER.info("TestService.getAll()");

        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaQuery<Test> criteriaQuery = criteriaBuilder.createQuery(Test.class);
        Root<Test> root = criteriaQuery.from(Test.class);

        criteriaQuery.select(root);

        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));

        TypedQuery<Test> query = this.em.createQuery(criteriaQuery);
        // if(this.isCacheable())
        // {
        // // set "org.hibernate.cacheable" to true
        // query.setHint(QueryHints.CACHEABLE, Boolean.TRUE);
        // }
        // if(this.isReadonly())
        // {
        // // set "org.hibernate.readOnly" to true
        // query.setHint(QueryHints.READ_ONLY, Boolean.TRUE);
        // }
        List<Test> resultList = query.getResultList();
        return resultList;
    }
}
