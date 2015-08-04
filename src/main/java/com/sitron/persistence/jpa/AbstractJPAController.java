/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.jpa;

import com.sitron.persistence.entities.Usuario;
import com.sitron.persistence.jsf.filterbuilder.ComparableField;
import com.sitron.persistence.jsf.filterbuilder.EnumFieldType;
import com.sitron.persistence.jsf.filterbuilder.EnumTipoComparacion;
import com.sitron.persistence.jsf.filterbuilder.FieldType;
import com.sitron.persistence.jsf.filterbuilder.FiltroVista;
import com.sitron.persistence.jsf.filterbuilder.TipoComparacion;
import com.sitron.persistence.jsf.filterbuilder.Vista;
import com.sitron.persistence.jsf.filterbuilder.annotations.FilterField;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author jonathan
 */
public abstract class AbstractJPAController {

    protected UserTransaction utx = null;
    protected EntityManagerFactory emf = null;

    public static final String PLACE_HOLDER_CURRENT_USER = "{CURRENT_USER}";
    public static final String PLACE_HOLDER_NULL = "{NULL}";
    public static final String PLACE_HOLDER_ANY = "{ANY_BUT_NULL}";

    public AbstractJPAController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        EntityManager em = emf.createEntityManager();
        return em;
    }

    public void merge(Object o) throws Exception {
        EntityManager em = null;
        try {
            System.out.println("Calling Merge with UserTransaction.Status: " + utx.getStatus());

            if (utx.getStatus() == Status.STATUS_ACTIVE) {

                em = getEntityManager();
                em.merge(o);

            } else {
                System.out.println("begin new merge Transaction");
                utx.begin();
                em = getEntityManager();
                em.merge(o);
                utx.commit();
            }

        } catch (Exception ex) {
            ConstraintViolationExceptionHelper.handleError(ex);
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public <T extends Object> T find(Class<T> entityClass, Object id) {
        return find(entityClass, id, false);
    }

    /**
     * Finds an Entity and Refresh the state of the instance from the database,
     * overwriting changes made to the entity, if any.
     *
     * @param <T>
     * @param entityClass
     * @param primaryKey
     * @param refresh set to true if you want to Refresh the state of the
     * instance from the database. False otherwise goes to the cache.
     * @return
     */
    public <T extends Object> T find(Class<T> entityClass, Object primaryKey, boolean refresh) {

        EntityManager em = getEntityManager();
        try {
            if (refresh) {
                Map<String, Object> properties = new HashMap<>();
//                properties.put("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
//                properties.put(QueryHints.REFRESH, HintValues.TRUE);
                return em.find(entityClass, primaryKey, properties);
            } else {
                return em.find(entityClass, primaryKey);
            }
        } finally {
            em.close();
        }

    }

    public <T extends Object> T getReference(Class<T> entityClass, Object id) throws EntityNotFoundException {
        return getEntityManager().getReference(entityClass, id);
    }

    public void refresh(Object o) {
        getEntityManager().refresh(o);
    }

    public void persist(Object o) throws Exception {
        EntityManager em = null;//
        try {

            System.out.println("Calling persist with UserTransaction.Status: " + utx.getStatus());
            if (utx.getStatus() == Status.STATUS_ACTIVE) {

                em = getEntityManager();
                em.persist(o);

            } else {
                System.out.println("begin new persist Transaction");
                utx.begin();
                em = getEntityManager();
                em.persist(o);
                utx.commit();
            }

        } catch (Exception ex) {
            ConstraintViolationExceptionHelper.handleError(ex);
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new Exception("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void remove(Object o) throws Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.remove(em.merge(o));
            utx.commit();
        } catch (Exception ex) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    public void remove(Class clazz, Object pk) throws Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            //em.remove(emf.getPersistenceUnitUtil().getIdentifier(o));
//            em.remove(em.getReference(clazz, getIdentifier(em, o)));
//            em.remove(em.merge(o));
            em.remove(em.getReference(clazz, pk));
            utx.commit();
        } catch (Exception ex) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    public void removeFromPK(Class clazz, Object pk) throws Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            //em.remove(emf.getPersistenceUnitUtil().getIdentifier(o));
            em.remove(em.getReference(clazz, pk));
            utx.commit();
        } catch (Exception ex) {
            Logger.getLogger(JPAServiceFacade.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    public List<?> findAll(Class entityClass) {
        return findEntities(entityClass, true, 0, 0);
    }

    public List<?> findAll(Class entityClass, String... orderBy) {
        return findEntities(entityClass, true, 0, 0, orderBy);
    }

    public List<?> findEntities(Class entityClass, boolean all, int maxResults, int firstResult) {
        return findEntities(entityClass, all, maxResults, firstResult, (String[]) null);
    }

    private List<?> findEntities(Class entityClass, boolean all, int maxResults, int firstResult, String... orderBy) {
        EntityManager em = getEntityManager();
        try {
            final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery cq = criteriaBuilder.createQuery();
            javax.persistence.criteria.Root rt = cq.from(entityClass);
            cq.select(rt);

            if (orderBy != null && orderBy.length > 0) {
                ArrayList<Order> orderList = new ArrayList<>(orderBy.length);
                for (String field : orderBy) {
                    orderList.add(criteriaBuilder.asc(rt.get(field)));
                }
                cq.orderBy(orderList);
            }

            Query q = em.createQuery(cq);

            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    protected Expression<?> createExpression(Root<?> root, String field) {
        Path<?> path = null;
        String[] fields = field.split("\\.");

        for (String string : fields) {
            if (null == path) {
                path = root.get(string);
            } else {
                path = path.get(string);
            }
        }

        return path;
    }

    /**
     *
     * @param em
     * @param criteriaBuilder
     * @param root
     * @param vista
     * @param whoIsApplyingView THIS CAN BE NULL. when filtering being used
     * outside user context.
     * @return
     * @throws IllegalStateException
     * @throws ClassNotFoundException
     */
    protected Predicate createPredicate(EntityManager em, CriteriaBuilder criteriaBuilder, Root<?> root, Vista vista, boolean useNonPersistentFilters, Usuario whoIsApplyingView, String query) throws IllegalStateException, ClassNotFoundException {

        Predicate predicate = null;

        try {

            if (vista == null) {
                throw new IllegalStateException("La vista no puede ser null!");
            }
            Class baseClass = Class.forName(vista.getBaseEntityType());
            Map<String, ComparableField> annotatedFields = getAnnotatedComparableFieldsMap(baseClass);

            if (vista.getFiltrosVistaList() != null) {
                for (FiltroVista filtro : vista.getFiltrosVistaList()) {

                    if (!useNonPersistentFilters && filtro.getIdFiltro() < 0) {
                        continue;
                    }

                    final ComparableField comparableField = annotatedFields.get(filtro.getIdCampo());

                    if (comparableField == null) {
//                        throw new IllegalStateException("Los valores de los filtros de la vista no cumplen con los requisitos minimos!. Debe especificar un campo comparable.");
                        continue;
                    }

                    TipoComparacion operador = filtro.getIdComparador();

                    String valorAttributo = filtro.getValor();

                    if (operador == null || valorAttributo == null) {
//                        throw new IllegalStateException("Los valores de los filtros de la vista no cumplen con los requisitos minimos.");
                        continue;
                    }

                    final FieldType fieldType = comparableField.getFieldTypeId();

                    if (fieldType == null) {
//                        throw new IllegalStateException("Los filtros de la vista no cumplen con los requisitos minimos!. Debe especificar un tipo de campo.");
                        continue;
                    }

                    Predicate localPredicate = null;

                    if (fieldType.equals(EnumFieldType.TEXT.getFieldType()) || fieldType.equals(EnumFieldType.TEXTAREA.getFieldType())) {
                        //El valor es de tipo String, usarlo tal como esta pero case insensitive

                        Expression<String> expresion = root.get(filtro.getIdCampo());

                        if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.equal(criteriaBuilder.lower(expresion), valorAttributo.trim().toLowerCase());
                        } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.notEqual(criteriaBuilder.lower(expresion), valorAttributo.trim().toLowerCase());
                        } else if (operador.equals(EnumTipoComparacion.CO.getTipoComparacion())) {
                            if (!valorAttributo.contains("*")) {
                                localPredicate = criteriaBuilder.like(criteriaBuilder.lower(expresion), "%" + valorAttributo.trim().toLowerCase() + "%");
                            } else {
                                localPredicate = criteriaBuilder.like(criteriaBuilder.lower(expresion), valorAttributo.trim().toLowerCase().replace("*", "%"));
                            }

                        } else {
                            throw new IllegalStateException("Comparador " + operador.getIdComparador() + " is not supported!!");
                        }
                    } else if (fieldType.equals(EnumFieldType.NUMBER.getFieldType())) {

                        Expression<Long> expresion = root.get(filtro.getIdCampo());

                        if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.equal(expresion, NumberUtils.createLong(valorAttributo.trim()));
                        } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.notEqual(expresion, NumberUtils.createLong(valorAttributo.trim()));
                        } else if (operador.equals(EnumTipoComparacion.LE.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.lessThanOrEqualTo(expresion, NumberUtils.createLong(valorAttributo.trim()));
                        } else if (operador.equals(EnumTipoComparacion.LT.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.lessThan(expresion, NumberUtils.createLong(valorAttributo.trim()));
                        } else if (operador.equals(EnumTipoComparacion.GE.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.greaterThanOrEqualTo(expresion, NumberUtils.createLong(valorAttributo.trim()));
                        } else if (operador.equals(EnumTipoComparacion.GT.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.greaterThan(expresion, NumberUtils.createLong(valorAttributo.trim()));
                        } else if (operador.equals(EnumTipoComparacion.SC.getTipoComparacion())) {
                            //One or more values, as list select many.
                            final List<String> valores = filtro.getValoresList();
                            List<Long> numbers = new ArrayList<>(valores.size());
                            for (String valor : valores) {
                                numbers.add(NumberUtils.createLong(valor.trim()));
                            }

                            localPredicate = expresion.in(numbers);
                        } else {
                            throw new IllegalStateException("Comparador " + operador.getIdComparador() + " is not supported!!");
                        }

                    } else if (fieldType.equals(EnumFieldType.CALENDAR.getFieldType())) {
                        //El valor es de tipo Fecha, usar el String parseado a una fecha
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            Date fecha1 = sdf.parse(valorAttributo);
                            if (fecha1 != null) {
                                Expression<Date> expresion = root.get(filtro.getIdCampo());
                                if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                                    localPredicate = criteriaBuilder.equal(expresion, fecha1);
                                } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                                    localPredicate = criteriaBuilder.notEqual(expresion, fecha1);
                                } else if (operador.equals(EnumTipoComparacion.LE.getTipoComparacion())) {
                                    localPredicate = criteriaBuilder.lessThanOrEqualTo(expresion, fecha1);
                                } else if (operador.equals(EnumTipoComparacion.GE.getTipoComparacion())) {
                                    localPredicate = criteriaBuilder.greaterThanOrEqualTo(expresion, fecha1);
                                } else if (operador.equals(EnumTipoComparacion.LT.getTipoComparacion())) {
                                    localPredicate = criteriaBuilder.lessThan(expresion, fecha1);
                                } else if (operador.equals(EnumTipoComparacion.GT.getTipoComparacion())) {
                                    localPredicate = criteriaBuilder.greaterThan(expresion, fecha1);
                                } else if (operador.equals(EnumTipoComparacion.BW.getTipoComparacion())) {
                                    String[] splitDates = filtro.getValor().split("-");

                                    Date fechaDesde = sdf.parse(splitDates[0].trim());
                                    Date fechaHasta = sdf.parse(splitDates[1].trim());

                                    localPredicate = criteriaBuilder.between(expresion, fechaDesde, fechaHasta);
                                } else {
                                    throw new IllegalStateException("Comparador " + operador.getIdComparador() + " is not supported!!");
                                }
                            } else {
                                Logger.getLogger(AbstractJPAController.class.getName()).log(Level.SEVERE, "filtro " + filtro + " descartado [CALENDAR] la fecha {0} es null.", valorAttributo);
                                continue;
                            }
                        } catch (ParseException | IllegalStateException | ArrayIndexOutOfBoundsException ex) {
                            //ignore this filter. and do not add this filter to the query
                            Logger.getLogger(AbstractJPAController.class.getName()).log(Level.SEVERE, "filtro [CALENDAR]: " + filtro + " descartado!!. La fecha {0} No se puede parsear o no es valida para el operador aplicado. ", valorAttributo);
                            continue;
                        }

                    } else if (fieldType.equals(EnumFieldType.SELECTONE_ENTITY.getFieldType())) {
                        //El valor es el id de un entity, que tipo de Entity?= comparableField.tipo
                        //One or more values??

                        if (operador.equals(EnumTipoComparacion.SC.getTipoComparacion())) {

                            //One or more values, as list select many.
                            final List<String> valores = filtro.getValoresList();

                            if (comparableField.getTipoCampoFullPath().equals(List.class)) {

                                localPredicate = root.joinList(filtro.getIdCampo()).get(comparableField.getListParameterFieldId()).in(valores);

                            } else {

                                boolean addCurrentUserFilter = false;
                                boolean addPlaceHolderNullFilter = false;

                                if (valores.contains(PLACE_HOLDER_CURRENT_USER)) {
//                            valores.remove(PLACE_HOLDER_CURRENT_USER);
                                    addCurrentUserFilter = true;
                                }

                                if (valores.contains(PLACE_HOLDER_NULL)) {
//                            valores.remove(PLACE_HOLDER_NULL);
                                    addPlaceHolderNullFilter = true;
                                }

                                if (valores.contains(PLACE_HOLDER_ANY)) {
                                    valores.remove(PLACE_HOLDER_ANY);
                                    //nothig, just dont send this string to the "in" criteria Predicate.
                                }

                                Expression expresion2 = createExpression(root, comparableField.getIdCampoFullPath()).as(comparableField.getTipoCampoFullPath());
                                localPredicate = criteriaBuilder.equal(expresion2.in(valores), Boolean.TRUE);

                                //handle PLACEHOLDERS! in valores
//                        System.out.println("//remove this, PLACEHOLDERS! valores:" + valores);
                                if (addCurrentUserFilter) {

                                    if (comparableField.getTipo().equals(Usuario.class) && (whoIsApplyingView != null)) {
                                        Expression expresion = root.get(filtro.getIdCampo()).as(comparableField.getTipo());
                                        localPredicate = CriteriaQueryHelper.addOrPredicate(localPredicate, criteriaBuilder.equal(expresion, em.find(comparableField.getTipo(), whoIsApplyingView.getIdUsuario())), criteriaBuilder);
                                    }
                                }

                                if (addPlaceHolderNullFilter) {
                                    Expression expresion = root.get(filtro.getIdCampo()).as(comparableField.getTipo());
                                    localPredicate = CriteriaQueryHelper.addOrPredicate(localPredicate, criteriaBuilder.isNull(expresion), criteriaBuilder);
                                }
                            }

                        } else {

                            Expression expresion = root.get(filtro.getIdCampo()).as(comparableField.getTipo());

                            //some entities may have special place holder that others entities do not have, so here we should call the special method
                            if (isThereSpecialFiltering(filtro)) {
                                localPredicate = createSpecialPredicate(filtro);
                            } else {
                                //Owner has a special place holder that others entities do not have
                                if (comparableField.getTipo().equals(Usuario.class)
                                        //                        if (filtro.getIdCampo().equals(EnumCampoCompCaso.OWNER.getCampoCompCaso().getIdCampo())
                                        && PLACE_HOLDER_CURRENT_USER.equalsIgnoreCase(valorAttributo)
                                        && (whoIsApplyingView != null)) {
                                    if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                                        localPredicate = criteriaBuilder.equal(expresion, em.find(comparableField.getTipo(), whoIsApplyingView.getIdUsuario()));
                                    } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                                        localPredicate = criteriaBuilder.notEqual(expresion, em.find(comparableField.getTipo(), whoIsApplyingView.getIdUsuario()));
                                    } else {
                                        throw new IllegalStateException("Tipo comparacion " + operador.getIdComparador() + " is not supported here!!");
                                    }

                                } else if (PLACE_HOLDER_ANY.equalsIgnoreCase(valorAttributo)) {
                                    if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                                        localPredicate = criteriaBuilder.isNotNull(expresion);
                                    } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                                        localPredicate = criteriaBuilder.isNull(expresion);
                                    } else {
                                        throw new IllegalStateException("Tipo comparacion " + operador.getIdComparador() + " is not supported here!!");
                                    }
                                } else if (PLACE_HOLDER_NULL.equalsIgnoreCase(valorAttributo)) {
                                    if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                                        localPredicate = criteriaBuilder.isNull(expresion);
                                    } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                                        localPredicate = criteriaBuilder.isNotNull(expresion);
                                    } else {
                                        throw new IllegalStateException("Tipo comparacion " + operador.getIdComparador() + " is not supported here!!");
                                    }
                                } else {
                                    if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {

                                        try {
                                            Constructor actionConstructor = Class.forName(comparableField.getTipoCampoFullPath().getName()).getConstructor(String.class);
                                            localPredicate = criteriaBuilder.equal(expresion, em.find(comparableField.getTipo(), actionConstructor.newInstance(valorAttributo)));
//                                } catch (java.lang.IllegalArgumentException e) {
                                        } catch (Exception e) {
                                            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "abstractJPAController error", e);
//                                    localPredicate = criteriaBuilder.equal(expresion, em.find(comparableField.getTipo(), Integer.valueOf(valorAttributo)));
                                        }
                                    } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                                        try {
                                            Constructor actionConstructor = Class.forName(comparableField.getTipoCampoFullPath().getName()).getConstructor(String.class);
                                            localPredicate = criteriaBuilder.notEqual(expresion, em.find(comparableField.getTipo(), actionConstructor.newInstance(valorAttributo)));
//                                } catch (java.lang.IllegalArgumentException e) {
                                        } catch (Exception e) {
                                            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "abstractJPAController error", e);
//                                    localPredicate = criteriaBuilder.notEqual(expresion, em.find(comparableField.getTipo(), Integer.valueOf(valorAttributo)));
                                        }

                                    } else {
                                        throw new IllegalStateException("Tipo comparacion " + operador.getIdComparador() + " is not supported here!!");
                                    }
                                }
                            }
                        }

                    } else if (fieldType.equals(EnumFieldType.SELECTONE_PLACE_HOLDER.getFieldType())) {

                        Expression expresion = root.get(filtro.getIdCampo()).as(comparableField.getTipo());

//                      expresion = root.<List<?>>get(filtro.getIdCampo());
                        //some entities may have special place holder that others entities do not have, so here we should call the special method
                        if (isThereSpecialFiltering(filtro)) {
                            localPredicate = createSpecialPredicate(filtro);
                        } else {
                            if (PLACE_HOLDER_ANY.equalsIgnoreCase(valorAttributo)) {
                                if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                                    if (comparableField.getTipoCampoFullPath().equals(List.class)) {
                                        localPredicate = criteriaBuilder.isNotEmpty(expresion);
                                    } else {
                                        localPredicate = criteriaBuilder.isNotNull(expresion);
                                    }
                                } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                                    if (comparableField.getTipoCampoFullPath().equals(List.class)) {
                                        localPredicate = criteriaBuilder.isEmpty(expresion);
                                    } else {
                                        localPredicate = criteriaBuilder.isNull(expresion);
                                    }
                                } else {
                                    throw new IllegalStateException("Tipo comparacion " + operador.getIdComparador() + " is not supported here!!");
                                }
                            } else if (PLACE_HOLDER_NULL.equalsIgnoreCase(valorAttributo)) {
                                if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                                    if (comparableField.getTipoCampoFullPath().equals(List.class)) {
                                        localPredicate = criteriaBuilder.isEmpty(expresion);
                                    } else {
                                        localPredicate = criteriaBuilder.isNull(expresion);
                                    }
                                } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                                    if (comparableField.getTipoCampoFullPath().equals(List.class)) {
                                        localPredicate = criteriaBuilder.isNotEmpty(expresion);
                                    } else {
                                        localPredicate = criteriaBuilder.isNotNull(expresion);
                                    }

                                } else {
                                    throw new IllegalStateException("Tipo comparacion " + operador.getIdComparador() + " is not supported here!!");
                                }
                            } else {
                                throw new IllegalStateException("valor Attributo " + valorAttributo + " is not supported here!!");
                            }
                        }
                    } else if (fieldType.equals(EnumFieldType.SELECT_MANY_ENTITIES.getFieldType())) {

                        //One or more values, as list select many.
                        final List<String> valores = filtro.getValoresList();

                        if (operador.equals(EnumTipoComparacion.IM.getTipoComparacion())) {
                            if (comparableField.getTipoCampoFullPath().equals(List.class)) {

                                localPredicate = root.joinList(filtro.getIdCampo()).get(comparableField.getListParameterFieldId()).in(valores);

                            } else {
                                throw new IllegalStateException("Field type " + comparableField.getTipoCampoFullPath() + " is not supported in " + EnumFieldType.SELECT_MANY_ENTITIES + ". Expected List.class");
                            }
                        } else {
                            throw new IllegalStateException("Comparador " + operador.getIdComparador() + " is not supported here!!" + EnumFieldType.SELECT_MANY_ENTITIES);
                        }

                    } else if (fieldType.equals(EnumFieldType.CHECKBOX.getFieldType())) {
                        //Boolean comparation
                        //El valor es de tipo boolean, usar el String parseado a un boolean
                        Expression<Boolean> expresion = root.get(filtro.getIdCampo());
                        boolean boolValue = Boolean.valueOf(valorAttributo);

                        if (operador.equals(EnumTipoComparacion.EQ.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.equal(expresion, boolValue);
                        } else if (operador.equals(EnumTipoComparacion.NE.getTipoComparacion())) {
                            localPredicate = criteriaBuilder.notEqual(expresion, boolValue);
                        } else {
                            throw new IllegalStateException("Comparador " + operador.getIdComparador() + " is not supported!!");
                        }

                    } else if (fieldType.equals(EnumFieldType.COMMA_SEPARATED_VALUELIST.getFieldType())) {
                        if (operador.equals(EnumTipoComparacion.IM.getTipoComparacion())) {
//                    Expression<List> expresion = root.get(filtro.getIdCampo());
                            final List<String> valoresList = filtro.getValoresList();
//                    ListJoin<Caso, Etiqueta> tags = root.joinList(filtro.getIdCampo());
//                    localPredicate = root.joinList(filtro.getIdCampo()).get("tagId").in(valoresList);
                            localPredicate = root.joinList(filtro.getIdCampo()).get("tagId").in(valoresList);

//                      Expression expresion2 = createExpression(root, comparableField.getIdCampoFullPath()).as(comparableField.getTipoCampoFullPath());
//                    localPredicate = criteriaBuilder.isTrue(expresion2.in(valoresList));
                        } else {
                            throw new IllegalStateException("Comparador " + operador.getIdComparador() + " is not supported!!");
                        }

                    } else {
                        throw new IllegalStateException("fieldType " + fieldType.getFieldTypeId() + " is not supported yet!!");
                    }

                    if (vista.isAllMustMatch()) {
                        predicate = CriteriaQueryHelper.addPredicate(predicate, localPredicate, criteriaBuilder);
                    } else {
                        predicate = CriteriaQueryHelper.addOrPredicate(predicate, localPredicate, criteriaBuilder);
                    }

                }
            }

            if (!StringUtils.isEmpty(query)) {
                //build predicate to select by query, all TEXT & TEXTAREA fields
                final Predicate predicatesForQuery = createCustomPredicatesForQuery(criteriaBuilder, root, query);
                if (predicatesForQuery != null) {
                    if (predicate != null) {
                        if (vista.isAllMustMatch()) {
                            predicate = CriteriaQueryHelper.addPredicate(predicate, predicatesForQuery, criteriaBuilder);
                        } else {
                            predicate = CriteriaQueryHelper.addOrPredicate(predicate, predicatesForQuery, criteriaBuilder);
                        }
                    } else {
                        predicate = predicatesForQuery;
                    }
                }
            }

        } catch (java.lang.IllegalArgumentException iae) {
            System.err.println("*** create Predicate failed on vista:" + vista);
            iae.printStackTrace();
        }

        return predicate;
    }
//
//    /**
//     * @deprecated @param root
//     * @param criteriaBuilder
//     * @param searchPattern
//     * @return
//     */
//    private Predicate createReglaTriggerSearchExpression(Root<ReglaTrigger> root, CriteriaBuilder criteriaBuilder, String searchPattern) {
//        Expression<String> expresionNombre = root.get("idTrigger");
//        Expression<String> expresionDescripcion = root.get("desccripcion");
//        Expression<String> expresionCampo = root.joinList("condicionList", JoinType.LEFT).get("idCampo");
//        Expression<String> expresionCondicionValor = root.joinList("condicionList", JoinType.LEFT).get("valor");
//        Expression<String> expresionAccionParams = root.joinList("accionList", JoinType.LEFT).get("parametros");
//        Expression<String> expresionAccionNombre = root.joinList("accionList", JoinType.LEFT).get("idNombreAccion").get("idNombreAccion");
//
//        Predicate predicate = criteriaBuilder.or(
//                criteriaBuilder.like(criteriaBuilder.upper(expresionNombre), "%" + searchPattern.toUpperCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.upper(expresionDescripcion), "%" + searchPattern.toUpperCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.upper(expresionCampo), "%" + searchPattern.toUpperCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.upper(expresionCondicionValor), "%" + searchPattern.toUpperCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.upper(expresionAccionParams), "%" + searchPattern.toUpperCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.upper(expresionAccionNombre), "%" + searchPattern.toUpperCase() + "%"));
//        return predicate;
//    }
//
//    /**
//     * @deprecated @param root
//     * @param criteriaBuilder
//     * @param searchPattern
//     * @return
//     */
//    //TODO remove direccion municipal field from sub componente, use id as product code and nombre as product label or direccion
//    public static Predicate createClientesSearchExpression(Root<Cliente> root, CriteriaBuilder criteriaBuilder, String searchPattern) {
//        Expression<String> expresionNombre = root.get(Cliente_.nombres);
//        Expression<String> expresionApellido = root.get(Cliente_.apellidos);
//        Expression<String> expresionRut = root.get(Cliente_.rut);
//        Expression<String> expresionEmail = root.joinList(Cliente_.emailClienteList.getName(), JoinType.LEFT).get("emailCliente");//root.get(Cliente_.emailClienteList).(EmailCliente_.emailCliente);
////        Expression<String> expresionDireccionSubComp = root.joinList("productoContratadoList", JoinType.LEFT)
////                .join("subComponente", JoinType.LEFT).get("direccionMunicipal"); //.get("subComponente").get("direccionMunicipal");
//
//        Expression<String> expresionIdSubComponente = root.joinList("productoContratadoList", JoinType.LEFT)
//                .join("subComponente", JoinType.LEFT).get("idSubComponente");
//        Expression<String> expresionNombreSubComp = root.joinList("productoContratadoList", JoinType.LEFT)
//                .join("subComponente", JoinType.LEFT).get("nombre");
//        Expression<String> expresionDescSubComp = root.joinList("productoContratadoList", JoinType.LEFT)
//                .join("subComponente", JoinType.LEFT).get("descripcion");
//
////        Expression<String> expresionIdComponenteNombre = root.joinList("productoContratadoList", JoinType.LEFT)
////                .join("subComponente", JoinType.LEFT).get("idComponente").get("nombre");
////
////        Expression<String> expresionIdProductoNombre = root.joinList("productoContratadoList", JoinType.LEFT)
////                .join("subComponente", JoinType.LEFT).get("idProducto").get("nombre");
//        Predicate predicate = criteriaBuilder.or(
//                criteriaBuilder.like(criteriaBuilder.upper(criteriaBuilder.concat(criteriaBuilder.concat(expresionNombre, " "), expresionApellido)), "%" + searchPattern.toUpperCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.upper(criteriaBuilder.concat(criteriaBuilder.concat(expresionApellido, " "), expresionNombre)), "%" + searchPattern.toUpperCase() + "%"),
//                //                criteriaBuilder.like(criteriaBuilder.upper(), "%" + searchPattern.toUpperCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.upper(expresionRut), "%" + searchPattern.toUpperCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.upper(expresionEmail), "%" + searchPattern.toUpperCase() + "%"),
//                //                criteriaBuilder.like(criteriaBuilder.upper(expresionDireccionSubComp), "%" + searchPattern.toUpperCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.upper(expresionIdSubComponente), "%" + searchPattern.toUpperCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.upper(expresionNombreSubComp), "%" + searchPattern.toUpperCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.upper(expresionDescSubComp), "%" + searchPattern.toUpperCase() + "%")
//        //                criteriaBuilder.like(criteriaBuilder.upper(expresionIdComponenteNombre), "%" + searchPattern.toUpperCase() + "%"),
//        //                criteriaBuilder.like(criteriaBuilder.upper(expresionIdProductoNombre), "%" + searchPattern.toUpperCase() + "%")
//        );
//        return predicate;
//    }
//
//    /**
//     * @deprecated @param root
//     * @param criteriaBuilder
//     * @param query
//     * @return
//     */
//    public static Predicate createCasoSearchExpression(Root<Caso> root, CriteriaBuilder criteriaBuilder, String query) {
////        Expression<Long> expresion1 = root.get(Caso_.idCaso);
//        Expression<String> expresion2 = root.get(Caso_.tema);
//        Expression<String> expresion3 = root.get(Caso_.descripcion);
//        Expression<String> expresionNombre = root.get("idCliente").get("nombres");
//        Expression<String> expresionApellido = root.get("idCliente").get("apellidos");
////        Expression<String> expresionEmail = root.joinList("idCliente.emailClienteList", JoinType.LEFT).get("emailCliente");
//
//        Expression<String> expresionNotaList = root.joinList(Caso_.notaList.getName(), JoinType.LEFT).get("texto");
////        Expression<Long> expresionNotaIDCaso = root.joinList(Caso_.notaList.getName(), JoinType.LEFT).get("idCaso").get("idCaso");
////        Expression<String> expresionDireccionM = root.joinList("productoContratadoList", JoinType.LEFT)
////                .join("subComponente", JoinType.LEFT).get("direccionMunicipal"); 
//        //.get("subComponente").get("direccionMunicipal");
////        Predicate predicate = criteriaBuilder.equal(expresionNotaIDCaso, expresion1);
//
//        Predicate predicate = criteriaBuilder.or(
//                criteriaBuilder.like(criteriaBuilder.lower(expresion2), "%" + query.toLowerCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.lower(expresion3), "%" + query.toLowerCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.lower(expresionNotaList), "%" + query.toLowerCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.concat(criteriaBuilder.concat(expresionNombre, " "), expresionApellido)), "%" + query.toLowerCase() + "%"),
//                criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.concat(criteriaBuilder.concat(expresionApellido, " "), expresionNombre)), "%" + query.toLowerCase() + "%"));
//
////        if (NumberUtils.isNumber(query.trim())) {
////            Predicate localPredicate = criteriaBuilder.equal(expresion1, query);
////            criteriaBuilder.and(predicate, localPredicate);
//////            predicate = CriteriaQueryHelper.addOrPredicate(predicate, localPredicate, criteriaBuilder);
////        }
//        return predicate;
//    }

    /**
     * Default implementation for searching by query, it creates one criteria
     * for each Text field, as a "field LIKE %query%" operation.
     *
     * @param criteriaBuilder
     * @param root
     * @param query
     * @return
     * @throws IllegalStateException
     * @throws ClassNotFoundException
     */
    private Predicate createCustomPredicatesForQuery(CriteriaBuilder criteriaBuilder, Root<?> root, String query) throws IllegalStateException, ClassNotFoundException {

        Predicate predicate = null;
        if (query == null) {
            return null;
        }

        //All other entities only seach on text and number fields
        List<ComparableField> annotatedFields = getAnnotatedComparableFieldsByClass(root.getJavaType());

        for (ComparableField comparableField : annotatedFields) {
            final FieldType fieldType = comparableField.getFieldTypeId();
            if (fieldType == null) {
                throw new IllegalStateException("Los filtros de la vista no cumplen con los requisitos minimos!. Debe especificar un tipo de campo.");
            }

            Predicate localPredicate = null;

            if (fieldType.equals(EnumFieldType.TEXT.getFieldType()) || fieldType.equals(EnumFieldType.TEXTAREA.getFieldType())) {
                //El valor es de tipo String, usarlo tal como esta pero case insensitive

                Expression<String> expresion = root.get(comparableField.getIdCampo());

                query = query.trim().replace(" ", "%").toLowerCase();

                if (!query.contains("*")) {
                    localPredicate = criteriaBuilder.like(criteriaBuilder.lower(expresion), "%" + query + "%");
                } else {
                    localPredicate = criteriaBuilder.like(criteriaBuilder.lower(expresion), query.replace("*", "%"));
                }

                predicate = CriteriaQueryHelper.addOrPredicate(predicate, localPredicate, criteriaBuilder);
            } else if (fieldType.equals(EnumFieldType.NUMBER.getFieldType())) {

                if (NumberUtils.isNumber(query.trim())) {
                    Expression<Number> expresion = root.get(comparableField.getIdCampo());
                    localPredicate = criteriaBuilder.equal((expresion), query);
                    predicate = CriteriaQueryHelper.addOrPredicate(predicate, localPredicate, criteriaBuilder);
                }

            }

        }

        return predicate;
    }

    public Map<String, ComparableField> getAnnotatedComparableFieldsMap(Class<?> baseClazz) {
        Map<String, ComparableField> fields = new HashMap<>();
        for (ComparableField comparableField : getAnnotatedComparableFieldsByClass(baseClazz)) {
            fields.put(comparableField.getIdCampo(), comparableField);
        }
        return fields;
    }

    public Map<String, ComparableField> getAnnotatedComparableFieldsMap(List<ComparableField> comparableFields) {
        Map<String, ComparableField> fields = new HashMap<>();
        for (ComparableField comparableField : comparableFields) {
            fields.put(comparableField.getIdCampo(), comparableField);
        }
        return fields;
    }

    public List<ComparableField> getAnnotatedComparableFieldsByClass(Class<?> baseClazz) {
        List<ComparableField> filters = new ArrayList<>();
        Field[] fields = baseClazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(FilterField.class)) {

                FilterField filterField = field.getAnnotation(FilterField.class);
                ComparableField comparableField = new ComparableField();
                comparableField.setBaseClass(baseClazz);
                comparableField.setIdCampo(field.getName());
                comparableField.setLabel(filterField.label());
                comparableField.setTipo(field.getType());
                comparableField.setFieldTypeId(filterField.fieldTypeId().getFieldType());
                comparableField.setIdCampoFullPath(filterField.fieldIdFull());
                comparableField.setTipoCampoFullPath(filterField.fieldTypeFull());

                if (field.getType().equals(List.class)) {

//                        Field listGenericField = baseClazz.getDeclaredField(filterField.fieldIdFull());
                    ParameterizedType listGenericType = (ParameterizedType) field.getGenericType();
                    Class<?> listGenericClass = (Class<?>) listGenericType.getActualTypeArguments()[0];
                    comparableField.setListGenericType(listGenericClass);
                    comparableField.setListParameterFieldId(filterField.listGenericTypeFieldId());

                }

                filters.add(comparableField);
            }
        }
        return filters;
    }

    /**
     * try Keep this code for godesk
     *
     * @param comparableField
     * @param userWhoIsAsking
     * @return
     */
    public List<?> findPosibleDBOptionsFor(ComparableField comparableField, Usuario userWhoIsAsking) {
        FieldType fieldType = comparableField.getFieldTypeId();

        if (fieldType == null) {
            return null;
        }

        List<?> entities;

        if (comparableField.getTipo().equals(List.class)) {
            entities = findAll(comparableField.getListGenericType());
            if (comparableField.getListGenericType().equals(Usuario.class)) {
//                entities.remove(EnumUsuariosBase.SISTEMA.getUsuario());
            }
        } else {
            //Que tipo de Entity? -> comparableField.tipo

            entities = findAll(comparableField.getTipo());

            //Usuario entity has a special place holder that others entities do not have
            if (comparableField.getTipo().equals(Usuario.class)) {
//                entities.remove(EnumUsuariosBase.SISTEMA.getUsuario());
            }
        }
        return entities;

    }

    protected abstract Predicate createSpecialPredicate(FiltroVista filtro);

    protected abstract boolean isThereSpecialFiltering(FiltroVista filtro);

    public List<?> findEntitiesByQuery(Class entityClass, boolean all, int maxResults, String query) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery(entityClass.getSimpleName() + ".findAllByQuery")
                    .setParameter("q", query);

            if (!all) {
                q.setMaxResults(maxResults);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    //TODO
    public List<?> findEntitiesBySearchQuery(String query, ComparableField comparableField, Usuario userWhoIsAsking) {
        FieldType fieldType = comparableField.getFieldTypeId();

        if (fieldType == null) {
            return null;
        }

        List<?> entities;

        if (comparableField.getTipo().equals(List.class)) {
            entities = findEntitiesByQuery(comparableField.getListGenericType(), false, 10, query);
            if (comparableField.getListGenericType().equals(Usuario.class)) {
//                entities.remove(EnumUsuariosBase.SISTEMA.getUsuario());
            }
        } else {

            entities = findEntitiesByQuery(comparableField.getTipo(), false, 10, query);
            //Usuario entity has a special place holder that others entities do not have
            if (comparableField.getTipo().equals(Usuario.class)) {
//                entities.remove(EnumUsuariosBase.SISTEMA.getUsuario());
            }
        }
        return entities;

    }
}
