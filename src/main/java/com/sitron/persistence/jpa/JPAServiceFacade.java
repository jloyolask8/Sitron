package com.sitron.persistence.jpa;

import com.sitron.persistence.entities.Usuario;
import com.sitron.persistence.jsf.filterbuilder.FiltroVista;
import com.sitron.persistence.jsf.filterbuilder.OrderBy;
import com.sitron.persistence.jsf.filterbuilder.Vista;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.apache.commons.lang3.text.WordUtils;
//import org.eclipse.persistence.config.HintValues;
//import org.eclipse.persistence.config.QueryHints;
//import org.eclipse.persistence.descriptors.ClassDescriptor;
//import org.eclipse.persistence.internal.helper.DatabaseField;
//import org.eclipse.persistence.internal.sessions.AbstractSession;
//import org.eclipse.persistence.sessions.Session;

public class JPAServiceFacade extends AbstractJPAController {


    public JPAServiceFacade(UserTransaction utx, EntityManagerFactory emf) {
       super(utx, emf);
    }

   

   

//    /**
//     * Returns the id of the entity. A generated id is not guaranteed to be
//     * available until after the database insert has occurred. Returns null if
//     * the entity does not yet have an id
//     *
//     * This method changed to be used as multitenant
//     *
//     * @param entity
//     * @return id of the entity
//     * @throws IllegalStateException if the entity is found not to be an entity.
//     */
//    public Object getIdentifier(Object entity) {
//        EntityManager em = getEntityManager();
//
//        try {
//            AbstractSession session = (AbstractSession) em.unwrap(Session.class);
//            ClassDescriptor descriptor = session.getDescriptor(entity);
//            if (descriptor.getPrimaryKeyFields().size() != 1) {
//                throw new NotSupportedException("sorry dude Composite PK is not supported yet!");
//            }
//            String methodName = null;
//            for (DatabaseField databaseField : descriptor.getPrimaryKeyFields()) {
//                methodName = createGetIdentifierMethodName(databaseField.getName());
//            }
//
//            java.beans.Expression expresion;
//            expresion = new java.beans.Expression(entity, methodName, new Object[0]);
//
//            expresion.execute();
//            return expresion.getValue();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            em.close();
//        }
//
//        return null;
//    }
//
//    /**
//     * multitenant version of get entity Identifier
//     *
//     * @param em
//     * @param entity
//     * @return
//     */
//    public Object getIdentifier(EntityManager em, Object entity) {
//        try {
//            AbstractSession session = (AbstractSession) em.unwrap(Session.class);
//            ClassDescriptor descriptor = session.getDescriptor(entity);
//            if (descriptor.getPrimaryKeyFields().size() != 1) {
//                throw new NotSupportedException("sorry dude Composite PK is not supported yet!");
//            }
//            String methodName = null;
//            for (DatabaseField databaseField : descriptor.getPrimaryKeyFields()) {
//                methodName = createGetIdentifierMethodName(databaseField.getName());
//            }
//
//            java.beans.Expression expresion;
//            expresion = new java.beans.Expression(entity, methodName, new Object[0]);
//
//            expresion.execute();
//            return expresion.getValue();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return null;
//    }

    public static String createGetIdentifierMethodName(String name) { // "MY_COLUMN"
        String name0 = name.replace("_", " "); // to "MY COLUMN"
        name0 = WordUtils.capitalizeFully(name0); // to "My Column"
        name0 = name0.replace(" ", ""); // to "MyColumn"
//        name0 = WordUtils.uncapitalize(name0); // to "myColumn"
        return "get" + name0;
    }



    public Long countEntities(Vista vista) throws ClassNotFoundException {
        return countEntities(vista, false, null, null);
    }

    public Long countEntities(Vista vista, Usuario who, String query) throws ClassNotFoundException {
        return countEntities(vista, true, who, query);
    }

    public Long countEntities(Vista vista, boolean useNonPersistentFilters, Usuario who, String query) throws ClassNotFoundException {
        EntityManager em = getEntityManager();
//        em.setProperty("javax.persistence.cache.storeMode", javax.persistence.CacheRetrieveMode.USE);

        try {
            if (vista == null || vista.getBaseEntityType() == null) {
                return 0L;
            }
            final String baseEntityType = vista.getBaseEntityType();
            final Class<?> entityClass = Class.forName(baseEntityType);

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root root = criteriaQuery.from(entityClass);

            Predicate predicate = createPredicate(em, criteriaBuilder, root, vista, useNonPersistentFilters, who, query);

            if (predicate != null) {
//                System.out.println("predicate != null");
                criteriaQuery.select(criteriaBuilder.count(root)).where(predicate).distinct(true);
            } else {
//                System.out.println("predicate = null");
                criteriaQuery.select(criteriaBuilder.count(root)).distinct(true);
            }
            Query q = em.createQuery(criteriaQuery);
            q.setHint("eclipselink.query-results-cache", false);
            final Long count = (Long) q.getSingleResult();
//            System.out.println("count = " + count);
            return count;
        } catch (ClassNotFoundException e) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, "ClassNotFoundException countEntities by view of class " + vista.getBaseEntityType(), e);
            throw e;
        } catch (IllegalStateException e) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, "countEntities by view " + vista, e);
            return 0L;
        } finally {
            em.close();
        }
    }

    public List<?> findAllEntities(Vista vista, OrderBy orderBy, Usuario who) throws IllegalStateException, ClassNotFoundException {
        return findEntities(vista, true, true, -1, -1, orderBy, null, who);
    }

    public List<?> findEntities(Vista vista, int maxResults, int firstResult, OrderBy orderBy, Usuario who) throws IllegalStateException, ClassNotFoundException {
        return findEntities(vista, true, false, maxResults, firstResult, orderBy, null, who);
    }

    public List<?> findEntitiesFirstChunk(Vista vista, OrderBy orderBy, String query) throws IllegalStateException, ClassNotFoundException {
        return findEntities(vista, true, false, 10, 0, orderBy, query, null);
    }

    public List<?> findEntities(Vista vista, int maxResults, int firstResult, OrderBy orderBy, Usuario who, String query) throws IllegalStateException, ClassNotFoundException {
        return findEntities(vista, true, false, maxResults, firstResult, orderBy, query, who);
    }

    public List<?> findEntities(Vista vista, boolean useNonPersistentFilters, int maxResults, int firstResult, OrderBy orderBy, Usuario who, String query) throws IllegalStateException, ClassNotFoundException {
        return findEntities(vista, useNonPersistentFilters, false, maxResults, firstResult, orderBy, query, who);
    }

    private List<?> findEntities(Vista vista, boolean useNonPersistentFilters, boolean all, int maxResults, int firstResult, OrderBy orderBy, String query, Usuario who) throws IllegalStateException, ClassNotFoundException {
      
        if(vista == null){
            throw new IllegalStateException("La vista no puede ser null.");
        }
        
        EntityManager em = getEntityManager();

        try {

            final String baseEntityType = vista.getBaseEntityType();
            final Class<?> entityClass = Class.forName(baseEntityType);

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root root = criteriaQuery.from(entityClass);

            Predicate predicate = createPredicate(em, criteriaBuilder, root, vista, useNonPersistentFilters, who, query);

            if (predicate != null) {
                criteriaQuery.where(predicate).distinct(true);
            }
            if (orderBy != null && orderBy.getFieldName() != null) {
                if (orderBy.getOrderType().equals(OrderBy.OrderType.ASC)) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(orderBy.getFieldName())));
                } else {
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get(orderBy.getFieldName())));
                }

            }
            Query q = em.createQuery(criteriaQuery);

            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();

        } catch (IllegalStateException ex) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, ex.getMessage());
//            throw ex;
            return Collections.EMPTY_LIST;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, "ClassNotFoundException at findEntities", ex);
//            throw ex;
            return Collections.EMPTY_LIST;
        } finally {
            em.close();
        }
    }

   

//    public Long nextVal(String seq) throws PreexistingEntityException, RollbackFailureException, Exception {
//        return (Long) getEntityManager().createNativeQuery("select nextval(?)").setParameter(1, seq).getSingleResult();
//    }

    
    /**
     * @param filtro
     * @return the vistaJpaController
     */
    @Override
    protected Predicate createSpecialPredicate(FiltroVista filtro) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected boolean isThereSpecialFiltering(FiltroVista filtro) {
        return false;
    }


}
