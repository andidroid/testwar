package me.andidroid.testwar;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.microprofile.opentracing.Traced;

@Traced
@RequestScoped
public class TestService
{
    /**
     * Logging via slf4j api
     */
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TestService.class);
    
    @PersistenceContext
    private EntityManager em;
    
    public TestService()
    {
        
    }
    
    public TestService(EntityManager em)
    {
        this.em = em;
    }
    
    public Test getById(long id)
    {
        return em.find(Test.class, id);
    }
    
    public Collection<Test> getAll()
    {
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
