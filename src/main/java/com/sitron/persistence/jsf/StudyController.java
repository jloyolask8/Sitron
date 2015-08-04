package com.sitron.persistence.jsf;

import com.sitron.persistence.entities.Study;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;

@Named("studyController")
@SessionScoped
public class StudyController extends AbstractManagedBean<Study> implements Serializable {

    public StudyController() {
    }
     @PostConstruct
    protected void initialize() {
        setEntityClass(Study.class);
    }
    
    @Override
    protected String getEditPage() {
        return "/cruds/admin/study/Edit";
    }

    @Override
    protected String getViewPage() {
        return "/cruds/admin/study/View";
    }

    @Override
    protected String getListPage() {
        return "/cruds/admin/study/List";
    }
    
    public Study getStudy(java.lang.Long id) {
        return getJpaController().find(Study.class, id);
    }
    
    @Override
    protected Class getDataModelImplementationClass() {
        return ListDataModel.class;
    }

    @FacesConverter(forClass = Study.class)
    public static class StudyControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StudyController controller = (StudyController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "studyController");
            return controller.getStudy(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Study) {
                Study o = (Study) object;
                return getStringKey(o.getPk());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Study.class.getName());
            }
        }

    }

}
