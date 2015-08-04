/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.jsf;

import com.auth0.Auth0User;
import com.sitron.persistence.entities.Usuario;
import com.sitron.persistence.jpa.JPAServiceFacade;
import com.sitron.persistence.jsf.filterbuilder.FiltroVista;
import com.sitron.persistence.jsf.filterbuilder.JPAFilterHelper;
import com.sitron.persistence.jsf.filterbuilder.OrderBy;
import com.sitron.persistence.jsf.filterbuilder.Vista;
import com.sitron.persistence.jsf.util.DateUtils;
import com.sitron.persistence.jsf.util.JsfUtil;
import static com.sitron.persistence.jsf.util.JsfUtil.getRequest;
import com.sitron.persistence.jsf.util.PaginationHelper;
import com.sitron.persistence.jsf.util.UAgentInfo;
import com.sitron.persistence.webapp.UserSessionBean;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;
import javax.ws.rs.core.HttpHeaders;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.ocpsoft.prettytime.PrettyTime;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jonathan
 * @param <E>
 */
public abstract class AbstractManagedBean<E> implements Serializable {

    @PostConstruct
    protected void init() {

    }
    
    @Resource
    protected UserTransaction utx = null;
    @PersistenceUnit(unitName = "sitronPU")
    protected EntityManagerFactory emf = null;

    //go back button
//    private String backOutcome;
    protected static  Locale LOCALE_ES_CL = new Locale("es", "CL");
    protected static  SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEE, dd 'de' MMMM 'de' yyyy HH:mm", LOCALE_ES_CL);
    protected static  SimpleDateFormat dayDateFormat = new SimpleDateFormat("HH:mm");
    protected static  SimpleDateFormat monthDateFormat = new SimpleDateFormat("dd MMM", LOCALE_ES_CL);
    protected static  SimpleDateFormat yearDateFormat = new SimpleDateFormat("dd/MM/yy", LOCALE_ES_CL);

    protected static  SimpleDateFormat monthDateFormatWTime = new SimpleDateFormat("EEE, dd MMM HH:mm", LOCALE_ES_CL);
    protected static  SimpleDateFormat yearDateFormatWTime = new SimpleDateFormat("dd/MM/yy HH:mm", LOCALE_ES_CL);

    private Class<E> entityClass;

    protected transient JPAServiceFacade jpaController = null;
    protected transient DataModel items = null;
    protected transient PaginationHelper pagination;
    private int paginationPageSize = 10;
    protected E current;
    private List<E> selectedItems;
    private JPAFilterHelper filterHelper;

    private String query = null;
    private boolean filterViewToggle = false;

    protected Vista vista;

    protected String backOutcome;

    
     public Auth0User getAuth0User() {
        return (Auth0User.get(getRequest()));
    }
     
    public void selectEntityFromDialog(E o) {
        RequestContext.getCurrentInstance().closeDialog(o);
    }

    public String goBack() {
        if (this.backOutcome == null) {
            recreateModel();
            return getListPage();
        } else {
            //TODO Callback to the mbean and refresh vistas
            return this.backOutcome;
        }
    }

    public AbstractManagedBean() {
        this.entityClass = null;
    }

    public AbstractManagedBean(Class<E> entityClass) {
        this.entityClass = entityClass;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "{0} for {1} created", new Object[]{this.getClass().getSimpleName(), entityClass.getSimpleName()});
    }

    /**
     * Queries for an autocomplete List
     *
     * @param query
     * @return
     */
    public List<E> complete(String query) {
        //System.out.println("complete...");
        List<E> results = (List<E>) getJpaController().findEntitiesByQuery(getEntityClass(), false, 10, query);
        if (results == null) {
            return Collections.EMPTY_LIST;
        }
        return results;
    }

    public void showMessageInDialog(FacesMessage.Severity severity, String msg, String detail) {
        FacesMessage message = new FacesMessage(severity, msg, detail);
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    public boolean isAjaxRequest() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    protected abstract Class getDataModelImplementationClass();

    public OrderBy getDefaultOrderBy() {
        return null;
    }

    public Usuario getDefaultUserWho() {
        return null;
    }

//    public abstract PaginationHelper getPagination();
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(getPaginationPageSize()) {
                private Integer countCache = null;

                @Override
                public int getItemsCount() {
                    try {
                        if (countCache == null) {
                            countCache = getJpaController().countEntities(getVista(), getDefaultUserWho(), getQuery()).intValue();// getJpaController().count(Caso.class).intValue();
                        }

                        return countCache;
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "ClassNotFoundException at getItemsCount", ex);
                    }
                    return 0;
                }

                @Override
                public DataModel createPageDataModel() {
                    try {
                        DataModel<E> dataModel = (DataModel) getDataModelImplementationClass().newInstance();

                        final List<?> data = getJpaController().findEntities(getVista(), getPageSize(), getPageFirstItem(), getDefaultOrderBy(), getDefaultUserWho(), getQuery());

                        if (Comparable.class.isAssignableFrom(getEntityClass())) {
                            Collections.sort((List<Comparable>) data);
                        }

                        dataModel.setWrappedData(data);
                        return dataModel;
                    } catch (IllegalStateException ex) {//error en el filtro
                        addErrorMessage("Existe un problema con el filtro. Favor corregir e intentar nuevamente.");
                    } catch (ClassNotFoundException ex) {
                        addErrorMessage("Lo sentimos, ocurrió un error inesperado. Favor contactar a soporte.");
                        Logger.getLogger(AbstractManagedBean.class.getName()).log(Level.SEVERE, "ClassNotFoundException createPageDataModel", ex);
                    } catch (IllegalAccessException ex) {
                        addErrorMessage("Lo sentimos, ocurrió un error inesperado. Favor contactar a soporte.");
                        Logger.getLogger(AbstractManagedBean.class.getName()).log(Level.SEVERE, "IllegalAccessException createPageDataModel", ex);
                    } catch (InstantiationException ex) {
                        addErrorMessage("Lo sentimos, ocurrió un error inesperado. Favor contactar a soporte.");
                        Logger.getLogger(AbstractManagedBean.class.getName()).log(Level.SEVERE, "InstantiationException createPageDataModel", ex);
                    }
                    return null;
                }
            };
        }
        return pagination;
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    protected void recreateModel() {
        items = null;
    }

    protected void recreatePagination() {
        pagination = null;
    }

    public JPAFilterHelper getFilterHelper() {
        if (filterHelper == null) {
            filterHelper = new JPAFilterHelper((getEntityClass()).getName(), emf) {
                @Override
                public JPAServiceFacade getJpaService() {
                    return getJpaController();
                }
            };
        }
        return filterHelper;
    }

    public void filter() {
        System.out.println("filter()");
        recreatePagination();
        recreateModel();
    }

    /**
     * ejemplo de llamada a este metdodo:
     * executeInClient("WidgetVarName.show();");
     *
     * @param command
     */
    protected void executeInClient(String command) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute(command);
    }

    protected void updateComponentInClient(String componentId) {
        RequestContext.getCurrentInstance().update(componentId);
    }

    /**
     * RequestContext is a helper with various utilities. Update component(s)
     * programmatically. Execute javascript from beans. Add ajax callback
     * parameters as JSON. ScrollTo a specific component after ajax update.
     * Invoke conditional javascript on page load.
     *
     * @return RequestContext
     */
    protected RequestContext getPrimefacesRequestContext() {
        return RequestContext.getCurrentInstance();
    }

    protected void refreshPage() {
        FacesContext fc = FacesContext.getCurrentInstance();
        String refreshpage = fc.getViewRoot().getViewId();
        ViewHandler ViewH = fc.getApplication().getViewHandler();
        UIViewRoot UIV = ViewH.createView(fc, refreshpage);
        UIV.setViewId(refreshpage);
        fc.setViewRoot(UIV);
    }

    public void next() {
        getPagination().nextPage();
        recreateModel();
    }

    public void previous() {
        getPagination().previousPage();
        recreateModel();
    }

    public void last() {
        getPagination().lastPage();
        recreateModel();
    }

    public void first() {
        getPagination().firstPage();
        recreateModel();
    }

    public void resetPageSize() {
        recreatePagination();
        recreateModel();
    }

    public JPAServiceFacade getJpaController() {
        if (jpaController == null) {
            jpaController = new JPAServiceFacade(utx, emf);
        }
        return jpaController;
    }

    public void addMessageTo(FacesMessage.Severity sev, String clientId, String sumary) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, sumary, null);
        FacesContext.getCurrentInstance().addMessage(clientId, facesMsg);
    }

    public void addMessage(FacesMessage.Severity sev, String sumary) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, sumary, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public void addErrorMessage(String sumary) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, sumary, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public void addErrorMessage(String sumary, String detail) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, sumary, detail);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public void addInfoMessage(String sumary) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, sumary, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public void addWarnMessage(String sumary) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, sumary, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public void addMessage(FacesMessage.Severity sev, String sumary, String detail) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, sumary, detail);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    /**
     * @return the paginationPageSize
     */
    public int getPaginationPageSize() {
        return paginationPageSize;
    }

    /**
     * @param paginationPageSize the paginationPageSize to set
     */
    public void setPaginationPageSize(int paginationPageSize) {
        this.paginationPageSize = paginationPageSize;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findAll(entityClass), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findAll(entityClass), true);
    }
    
    public List getItemsAvailableForSelect() {
        return getJpaController().findAll(getEntityClass());
    }

    protected UserSessionBean getUserSessionBean() {
        return (UserSessionBean) JsfUtil.getManagedBean("UserSessionBean");
    }

//    protected CasoController getCasoControllerBean() {
//        return (CasoController) JsfUtil.getManagedBean("casoController");
//    }
    /**
     * @return the selected
     */
    public E getSelected() {
        return current;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(E selected) {
        this.current = selected;
    }

    /**
     * @return the selectedItems
     */
    public List<E> getSelectedItems() {
        return selectedItems;
    }

    /**
     * @param selectedItems the selectedItems to set
     */
    public void setSelectedItems(List<E> selectedItems) {
        this.selectedItems = selectedItems;
    }

    /**
     * @return the entityClass
     */
    public Class<E> getEntityClass() {
        return entityClass;
    }

    public String applyViewFilter(Vista vista) {

        setQuery(null);

        try {
            Vista copy = new Vista();
            copy.setIdVista(vista.getIdVista());
            copy.setBaseEntityType(vista.getBaseEntityType());
//            copy.setIdUsuarioCreadaPor(userSessionBean.getCurrent());
            copy.setDescripcion(vista.getDescripcion());
            copy.setNombre(vista.getNombre());
            copy.setVisibleToAll(vista.getVisibleToAll());
            if (copy.getFiltrosVistaList() == null) {
                copy.setFiltrosVistaList(new ArrayList<FiltroVista>());
            }
            //Crearemos una copia para que al guardar no pisar la existente.
            int i = 1;
            if (vista.getFiltrosVistaList() != null) {
                for (FiltroVista f : vista.getFiltrosVistaList()) {
                    FiltroVista fCopy = new FiltroVista();
                    fCopy.setIdFiltro(i);
                    i++; //This is an ugly patch to solve issue when removing a filter from the view, if TODO: Warning - this method won't work in the case the id fields are not set
                    fCopy.setIdCampo(f.getIdCampo());
                    fCopy.setIdComparador(f.getIdComparador());
                    fCopy.setValor(f.getValor());
                    fCopy.setValorLabel(f.getValorLabel());
                    fCopy.setValor2(f.getValor2());
                    fCopy.setValor2Label(f.getValor2Label());
                    fCopy.setIdVista(copy);
                    copy.getFiltrosVistaList().add(fCopy);
                    ////System.out.println("added filtro " + fCopy);
                }
            }

            if (copy.getFiltrosVistaList().isEmpty()) {
                copy.addNewFiltroVista();
            }
            setVista(copy);
//        this.setVista(copy);
//        ////System.out.println("Vista copy set:" + copy);
            recreatePagination();
//            recreateModel();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prepareList();//"inbox";

    }
    
    public boolean isMobileClient(){
        return isThisRequestCommingFromAMobileDevice(getRequest());
    }

    /**
     * mothod to determine the browser agent of client!
     *
     * @param request
     * @return
     */
    protected boolean isThisRequestCommingFromAMobileDevice(HttpServletRequest request) {

        // http://www.hand-interactive.com/m/resources/detect-mobile-java.htm
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        String httpAccept = request.getHeader(HttpHeaders.ACCEPT);

        UAgentInfo detector = new UAgentInfo(userAgent, httpAccept);

        if (detector.detectMobileQuick()) {
            return true;
        }

        if (detector.detectTierTablet()) {
            return true;
        }

        return false;
    }

    protected UAgentInfo getUAgentInfo(HttpServletRequest request) {

        // http://www.hand-interactive.com/m/resources/detect-mobile-java.htm
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        String httpAccept = request.getHeader(HttpHeaders.ACCEPT);

        UAgentInfo detector = new UAgentInfo(userAgent, httpAccept);
        return detector;

    }

    /**
     * ejemplo: redirectToView("customer/ticket");
     *
     * @param viewId
     */
    protected void redirectToView(String viewId) {
//        ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
//        nav.performNavigation(viewId);
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String path_ticket = request.getContextPath() + request.getServletPath() + "/" + viewId + ".xhtml";
            FacesContext.getCurrentInstance().getExternalContext().redirect(path_ticket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * redirecciona un request ajax a una pagina especifica al parecer solo
     * funciona desde un filter no desde un managed bean =(
     *
     * @param request
     * @param page
     * @return
     */
    public String xmlPartialRedirectToPage(String page) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version='1.0' encoding='UTF-8'?>");
        sb.append("<partial-response><redirect url=\"").append(request.getContextPath()).append(request.getServletPath()).append(page).append("\"/></partial-response>");
        Logger.getLogger(AbstractManagedBean.class.getName()).log(Level.SEVERE, "xmlPartialRedirectToPage:{0}", sb.toString());
        return sb.toString();
    }

    public String redirect(String page) {

        FacesContext ctx = FacesContext.getCurrentInstance();

        ExternalContext extContext = ctx.getExternalContext();
        String url = extContext.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, page));
        try {
            extContext.redirect(url);
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        }
        return null;
    }

    public String prettyDate(Date date) {

        if (date != null) {
            PrettyTime p = new PrettyTime(new Locale("es"));
            return p.format(date);
//            return PrettyDate.format(date);
        } else {
            return "";
        }
    }

    public String formatShortDate(Date date) {
        if (date != null) {

            if (DateUtils.isToday(date)) {
                return dayDateFormat.format(date);
            } else if (DateUtils.isThisYear(date)) {
                return monthDateFormat.format(date);
            } else {
                return yearDateFormat.format(date);
            }
        } else {
            return "";
        }
    }

    public String formatDate(Date date) {
        if (date != null) {
            return fullDateFormat.format(date);
        } else {
            return "";
        }
    }

    public String formatShortDateTime(Date date) {
        if (date != null) {

            if (DateUtils.isToday(date)) {
                return dayDateFormat.format(date);
            } else if (DateUtils.isThisYear(date)) {
                return monthDateFormatWTime.format(date);
            } else {
                return yearDateFormatWTime.format(date);
            }
        } else {
            return "";
        }
    }

    public String formatDateRange(Date start, Date end) {

        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", LOCALE_ES_CL);

        DateTime dtStart = new DateTime(start);
        DateTime dtEnd = new DateTime(end);

        String formattedDate = "";

        if (start != null) {
            if (dtStart.isAfter(dtEnd) || end == null) {
                //illegal!!
                formattedDate = formatShortDateTime(start);
            } else {
                if (dtStart.withTimeAtStartOfDay().isEqual(dtEnd.withTimeAtStartOfDay())) {//same day?
                    formattedDate = formatShortDateTime(start) + " - " + sdf2.format(end);
                } else {
                    formattedDate = formatShortDateTime(start) + " - " + formatShortDateTime(end);
                }
            }
        }

        return formattedDate;
    }

    public String formatDurationRange(Date start, Date end) {
        if (start == null || end == null) {
            return "";
        }
        Interval interval = new Interval(new DateTime(start), new DateTime(end));
        return interval.toDuration().toString();
    }

    public String prepareView(E entity) {
        if (entity == null) {
            addInfoMessage("Se requiere que seleccione un item para editar.");
            return null;
        }
        setSelected(entity);
        this.backOutcome = null;
        return getViewPage();
    }

    public String prepareView(E entity, String backOutcome) {
        if (entity == null) {
            addInfoMessage("Se requiere que seleccione un item para editar.");
            return null;
        }
        setSelected(entity);
        this.backOutcome = backOutcome;
        return getViewPage();
    }

    protected void beforeSetSelected() {

    }

    protected void beforePrepareList() {

    }

    protected void afterSetSelected() {

    }

    public String prepareEdit(E entity) {
        if (entity == null) {
            addInfoMessage("Se requiere que seleccione un item para editar.");
            return null;
        }

        beforeSetSelected();

        setSelected(entity);

        afterSetSelected();
        this.backOutcome = null;
        return getEditPage();
    }

    public String prepareEdit(E entity, String backOutcome) {
        prepareEdit(entity);
        this.backOutcome = backOutcome;
        return getEditPage();
    }

    public String prepareEditPK(Object entityPK) {
        try {
            if (entityPK == null) {
                return null;
            }
            E entity = getJpaController().find(getEntityClass(), entityPK);

            beforeSetSelected();

            setSelected(entity);

            afterSetSelected();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "prepareEdit (" + entityPK + ")", ex);
        } finally {
            this.backOutcome = null;
        }

        return getEditPage();
    }

    public String prepareEditPK(Object entityPK, String backOutcome) {

        prepareEditPK(entityPK);
        this.backOutcome = backOutcome;

        return getEditPage();
    }

    public String prepareList() {
        beforePrepareList();
        recreateModel();
        return getListPage();
    }

    /**
     * When done with experiment this method should be abstract
     *
     * @return the list page to outcome
     */
//    protected abstract String getListPage();
    protected String getEditPage() {
        return null;
    }

    /**
     * When done with experiment this method should be abstract
     *
     * @return the list page to outcome
     */
//    protected abstract String getListPage();
    protected String getViewPage() {
        return null;
    }

    /**
     * When done with experiment this method should be abstract
     *
     * @return the list page to outcome
     */
//    protected abstract String getListPage();
    protected String getListPage() {
        return null;
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return the filterViewToggle
     */
    public boolean isFilterViewToggle() {
        return filterViewToggle;
    }

    /**
     * @param filterViewToggle the filterViewToggle to set
     */
    public void setFilterViewToggle(boolean filterViewToggle) {
        this.filterViewToggle = filterViewToggle;
    }

    public int countItemsVista(Vista vista) {
        try {
            UserSessionBean userSessionBean = getUserSessionBean();
            final Long countEntities = getJpaController().countEntities(vista, userSessionBean.getCurrent(), null);
            return countEntities.intValue();
        } catch (Exception ex) {
            addErrorMessage(ex.getMessage());
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return 0;
        }

    }

    /**
     * @return the vista
     */
    public Vista getVista() {

        if (vista == null) {
            vista = new Vista(getEntityClass());
            if (vista.getFiltrosVistaList() == null || vista.getFiltrosVistaList().isEmpty()) {
                vista.addNewFiltroVista();
            }
        }
        return vista;

    }

    /**
     * @param vista the vista to set
     */
    public void setVista(Vista vista) {
        this.vista = vista;
    }

    /**
     * @param entityClass the entityClass to set
     */
    public void setEntityClass(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

}
