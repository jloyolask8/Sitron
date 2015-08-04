package com.sitron.persistence.jsf;

import com.sitron.persistence.entities.Priority;
import com.sitron.persistence.jsf.util.JsfUtil;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("priorityController")
@SessionScoped
public class PriorityController extends AbstractManagedBean<Priority> implements Serializable {

    

    public PriorityController() {
    }


    public String prepareCreate() {
        current = new Priority();
        return "Create";
    }

    public String create() {
        try {
            getJpaController().persist(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PriorityCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

   

    public String update() {
        try {
            getJpaController().merge(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PriorityUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Priority) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

   
    private void performDestroy() {
        try {
            getJpaController().remove(Priority.class, current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PriorityDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    @Override
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findAll(Priority.class), false);
    }

    @Override
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findAll(Priority.class), true);
    }

    public Priority getPriority(java.lang.String id) {
        return getJpaController().find(Priority.class, id);
    }

    @Override
    protected Class getDataModelImplementationClass() {
       return ListDataModel.class;
    }

    @FacesConverter(forClass = Priority.class)
    public static class PriorityControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PriorityController controller = (PriorityController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "priorityController");
            return controller.getPriority(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Priority) {
                Priority o = (Priority) object;
                return getStringKey(o.getPk());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Priority.class.getName());
            }
        }

    }

}
