package me.andidroid.testwar;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import org.eclipse.microprofile.opentracing.Traced;

@Traced
@RequestScoped
public class TestService {

    @PersistenceContext
    private EntityManager em;

    public TestService() {

    }

    public TestService(EntityManager em) {
        this.em = em;
    }

    public Test getById(long id) {
        return em.find(Test.class, id);
    }
}
