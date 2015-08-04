package com.sitron.persistence.jsf;

import com.sitron.persistence.entities.Series;
import com.sitron.persistence.entities.Study;
import com.sitron.persistence.entities.StudyCase;
import com.sitron.persistence.entities.enums.EnumEstadoCaso;
import com.sitron.persistence.entities.enums.EnumTipoAlerta;
import com.sitron.persistence.jpa.AbstractJPAController;
import com.sitron.persistence.jsf.filterbuilder.EnumTipoComparacion;
import com.sitron.persistence.jsf.filterbuilder.FiltroVista;
import com.sitron.persistence.jsf.filterbuilder.JPAFilterHelper;
import com.sitron.persistence.jsf.filterbuilder.OrderBy;
import com.sitron.persistence.jsf.filterbuilder.Vista;
import com.sitron.persistence.jsf.util.JsfUtil;
import com.sitron.reports.ReportsManager;
import java.io.ByteArrayInputStream;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named("studyCaseController")
@SessionScoped
public class StudyCaseController extends AbstractManagedBean<StudyCase> implements Serializable {

    private int stepNewCasoIndex;

    public StudyCaseController() {
    }

    @PostConstruct
    protected void initialize() {
        setEntityClass(StudyCase.class);
    }
    
    public void handlePatientSelectionOnCreate() {
        if(this.getSelected().getPatientFk() != null){
            final List<Study> studyList = this.getSelected().getPatientFk().getStudyList();
            if(studyList != null && !studyList.isEmpty()){
                getSelected().setStudyPk(studyList.get(0));
                handleStudySelection();
            }
        }
    }

    public void handleStudySelection() {
        if(this.getSelected().getStudyPk()!= null){
            final List<Series> seriesList = this.getSelected().getStudyPk().getSeriesList();
            if(seriesList != null && !seriesList.isEmpty()){
                getSelected().setSerieSelected(seriesList.get(0));
            }
        }
//        ((StudyCase) getSelected()).setRxType(getSelected().getStudyPk().getModsInStudy());
    }
    
   

    @Override
    public OrderBy getDefaultOrderBy() {
        return new OrderBy("createdAt", OrderBy.OrderType.DESC);
    }

    public String prepareMisTareasList() {
        vista = new Vista(getEntityClass());
        vista.setNombre("Tareas del Radiólogo");
        vista.setAllMustMatch(false);

        FiltroVista assignee = new FiltroVista();
        assignee.setIdCampo("assignee");
        assignee.setIdComparador(EnumTipoComparacion.EQ.getTipoComparacion());
        assignee.setValor(getUserSessionBean().getCurrent().getIdUsuario());
        assignee.setValorLabel(getUserSessionBean().getCurrent().getIdUsuario());
        assignee.setIdVista(vista);

        vista.getFiltrosVistaList().add(assignee);

        FiltroVista createdByUser = new FiltroVista();
        createdByUser.setIdCampo("createdByUser");
        createdByUser.setIdComparador(EnumTipoComparacion.EQ.getTipoComparacion());
        createdByUser.setValor(getUserSessionBean().getCurrent().getIdUsuario());
        createdByUser.setValorLabel(getUserSessionBean().getCurrent().getIdUsuario());
        createdByUser.setIdVista(vista);

        vista.getFiltrosVistaList().add(createdByUser);

        recreateModel();
        return getListPage();
    }

    public String prepareTodasTareasList() {
        vista = new Vista(getEntityClass());
        vista.setNombre("Todas las Tareas");
        recreateModel();
        recreatePagination();
        return getListPage();
    }

    public String prepareTareasDispoiblesList() {
        vista = new Vista(getEntityClass());
        vista.setNombre("Tareas disponibles");

        FiltroVista f1 = new FiltroVista();
        f1.setIdCampo("assignee");
        f1.setIdComparador(EnumTipoComparacion.EQ.getTipoComparacion());
        f1.setValor(AbstractJPAController.PLACE_HOLDER_NULL);
        f1.setValorLabel(JPAFilterHelper.PLACE_HOLDER_NULL_LABEL);
        f1.setIdVista(vista);

        vista.getFiltrosVistaList().add(f1);

        FiltroVista f_status = new FiltroVista();
        f_status.setIdCampo("idEstado.idEstado");
        f_status.setIdComparador(EnumTipoComparacion.EQ.getTipoComparacion());
        f_status.setValor(EnumEstadoCaso.ESPERA_INFORME.getEstado().getIdEstado());
        f_status.setValorLabel(EnumEstadoCaso.ESPERA_INFORME.getEstado().getNombre());
        f_status.setIdVista(vista);

        vista.getFiltrosVistaList().add(f_status);

        recreateModel();
        recreatePagination();
        return getListPage();
    }

    @Override
    protected String getEditPage() {
        return "/cruds/studyRequest/Edit";
    }

    @Override
    protected String getViewPage() {
        return "/cruds/studyRequest/View";
    }

    @Override
    protected String getListPage() {
        return "/cruds/studyRequest/List";
    }

    public String assignMe(StudyCase entity) {
        try {
            if (entity == null) {
                addInfoMessage("Se requiere que seleccione un item para assignar.");
                return null;
            }

            entity.setFechaAsignacion(new Date());
            entity.setAssignee(getUserSessionBean().getCurrent());
            getJpaController().merge(entity);
            setSelected(entity);
            JsfUtil.addSuccessMessage("Tarea asignada exitósamente.");
            this.backOutcome = null;
            return getViewPage();
        } catch (Exception ex) {
            Logger.getLogger(StudyCaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String destroy() {
        current = (StudyCase) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();

        return prepareList();
    }

    private void performDestroy() {
        try {
            getJpaController().remove(StudyCase.class, current.getIdRequest());
            JsfUtil.addSuccessMessage("Tarea eliminada exitósamente.");
            recreatePagination();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    /**
     * override this to get data based on the user rol, technical guys see the
     * cases they created Radiologist see cases they have assigned, Promise:
     * another list show list the cases not assigned that could be taken by
     * radiologist
     *
     * @return
     */
    @Override
    public Vista getVista() {

        if (getUserSessionBean().getCurrent().isRadiologist()) {
            if (vista == null) {
                vista = new Vista(getEntityClass());
                vista.setNombre("Tareas del Radiólogo");

                FiltroVista assignee = new FiltroVista();
                assignee.setIdCampo("assignee");
                assignee.setIdComparador(EnumTipoComparacion.EQ.getTipoComparacion());
                assignee.setValor(getUserSessionBean().getCurrent().getIdUsuario());
                assignee.setValorLabel(getUserSessionBean().getCurrent().getIdUsuario());
                assignee.setIdVista(vista);

                vista.getFiltrosVistaList().add(assignee);

            }
            return vista;
        } else if (getUserSessionBean().getCurrent().isTechnical()) {
            if (vista == null) {
                vista = new Vista(getEntityClass());
                vista.setNombre("Tareas creadas por el Técnico de imagenes");

                FiltroVista createdByUser = new FiltroVista();
                createdByUser.setIdCampo("createdByUser");
                createdByUser.setIdComparador(EnumTipoComparacion.EQ.getTipoComparacion());
                createdByUser.setValor(getUserSessionBean().getCurrent().getIdUsuario());
                createdByUser.setValorLabel(getUserSessionBean().getCurrent().getIdUsuario());
                createdByUser.setIdVista(vista);

                vista.getFiltrosVistaList().add(createdByUser);

            }
            return vista;
        } else if (getUserSessionBean().getCurrent().isAdmin()) {
            //no matter if it is admin, this is the default view which shows asigned to me or created by me
            if (vista == null) {
                vista = new Vista(getEntityClass());
                vista.setNombre("Todas las tareas");
                vista.setAllMustMatch(false);

                FiltroVista createdByUser = new FiltroVista();
                createdByUser.setIdCampo("createdByUser");
                createdByUser.setIdComparador(EnumTipoComparacion.EQ.getTipoComparacion());
                createdByUser.setValor(getUserSessionBean().getCurrent().getIdUsuario());
                createdByUser.setValorLabel(getUserSessionBean().getCurrent().getIdUsuario());
                createdByUser.setIdVista(vista);

                vista.getFiltrosVistaList().add(createdByUser);

                FiltroVista assignee = new FiltroVista();
                assignee.setIdCampo("assignee");
                assignee.setIdComparador(EnumTipoComparacion.EQ.getTipoComparacion());
                assignee.setValor(getUserSessionBean().getCurrent().getIdUsuario());
                assignee.setValorLabel(getUserSessionBean().getCurrent().getIdUsuario());
                assignee.setIdVista(vista);

                vista.getFiltrosVistaList().add(assignee);
            }
            return vista;

        } else {
            //Error no rol
            return null;
        }

    }

    @Override
    protected Class getDataModelImplementationClass() {
        return ListDataModel.class;
    }

//
    public String prepareCreate() {
        this.stepNewCasoIndex = 1;
        current = new StudyCase();
//        selectedItemIndex = -1;
        return "/cruds/studyRequest/Create";
    }

    public String update() {
        try {
            getJpaController().merge(current);
            JsfUtil.addSuccessMessage("Tarea actualizada exitosamente.");
            return getListPage();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String informar() {
        try {
            current.setIdEstado(EnumEstadoCaso.INFORMADO.getEstado());
            current.setInformedAt(new Date());
            getJpaController().merge(current);
            JsfUtil.addSuccessMessage("Tarea actualizada exitosamente.");
            return getListPage();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String observar() {
        try {
            current.setIdEstado(EnumEstadoCaso.RAD_CON_OBSERVACION.getEstado());
            current.setModifAt(new Date());
            getJpaController().merge(current);
            JsfUtil.addSuccessMessage("Tarea actualizada exitosamente.");
            return getListPage();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String repetir() {
        try {
            current.setIdEstado(EnumEstadoCaso.REPETIR_RADIOGRAFIA.getEstado());
            current.setModifAt(new Date());
            getJpaController().merge(current);
            JsfUtil.addSuccessMessage("Tarea actualizada exitosamente.");
            return getListPage();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String create() {
        try {
            current.setCreatedAt(new Date());
            current.setModifAt(new Date());
            current.setEstadoAlerta(EnumTipoAlerta.TIPO_ALERTA_PENDIENTE.getTipoAlerta());
            current.setIdEstado(EnumEstadoCaso.ESPERA_INFORME.getEstado());
            current.setCreatedByUser(getUserSessionBean().getCurrent());

            getJpaController().persist(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("StudyCaseCreated"));
            recreatePagination();
            recreateModel();
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
//
//
//    public SelectItem[] getItemsAvailableSelectMany() {
//        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
//    }
//
//    public SelectItem[] getItemsAvailableSelectOne() {
//        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
//    }

    public StudyCase getStudyCase(java.lang.Integer id) {
        return getJpaController().find(StudyCase.class, id);
    }

    /**
     * @return the stepNewCasoIndex
     */
    public int getStepNewCasoIndex() {
        return stepNewCasoIndex;
    }

    /**
     * @param stepNewCasoIndex the stepNewCasoIndex to set
     */
    public void setStepNewCasoIndex(int stepNewCasoIndex) {
        this.stepNewCasoIndex = stepNewCasoIndex;
    }

    public String goBackCreateCasoByStep() {
        switch (getStepNewCasoIndex()) {
            case 2:
                setStepNewCasoIndex(getStepNewCasoIndex() - 1);
                break;
            case 3:
                setStepNewCasoIndex(getStepNewCasoIndex() - 1);
                break;
            case 4:
                setStepNewCasoIndex(getStepNewCasoIndex() - 1);
                break;
        }
        return null;
    }

    public String createCasoByStep() {

        switch (getStepNewCasoIndex()) {
            case 1:

                setStepNewCasoIndex(getStepNewCasoIndex() + 1);
                break;
            case 2:
                setStepNewCasoIndex(getStepNewCasoIndex() + 1);
                break;
            case 3:
                setStepNewCasoIndex(getStepNewCasoIndex() + 1);
                break;
            case 4:
                return create();

        }

        return null;

    }

    /**
     * Generate Report
     *
     * @return
     */
    public StreamedContent exportarInformeAPDF() {
        try {
            byte[] data = ReportsManager.createInformePdf(getStudyCase(getSelected().getIdRequest()));
            return new DefaultStreamedContent(
                    new ByteArrayInputStream(data), "application/pdf", "informe_" + getSelected().getPatientFk().getPatName() + ".pdf");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e, e.getMessage());
            return new DefaultStreamedContent();
        }
    }

    @FacesConverter(forClass = StudyCase.class)
    public static class StudyCaseControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StudyCaseController controller = (StudyCaseController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "studyCaseController");
            return controller.getStudyCase(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof StudyCase) {
                StudyCase o = (StudyCase) object;
                return getStringKey(o.getIdRequest());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + StudyCase.class.getName());
            }
        }

    }

}
