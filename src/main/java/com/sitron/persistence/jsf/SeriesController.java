/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.jsf;

import com.sitron.persistence.entities.Series;
import com.sitron.persistence.entities.Study;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import javax.inject.Named;

/**
 *
 * @author jonathan
 */
@Named("seriesController")
@SessionScoped
public class SeriesController extends AbstractManagedBean<Series> implements Serializable {

    public SeriesController() {
    }

    @PostConstruct
    protected void initialize() {
        setEntityClass(Series.class);
    }

    @Override
    protected String getEditPage() {
        return "/cruds/admin/series/Edit";
    }

    @Override
    protected String getViewPage() {
        return "/cruds/admin/series/View";
    }

    @Override
    protected String getListPage() {
        return "/cruds/admin/series/List";
    }

    public Study getStudy(java.lang.Long id) {
        return getJpaController().find(Study.class, id);
    }

    @Override
    protected Class getDataModelImplementationClass() {
        return ListDataModel.class;
    }

    /**
     *
     * @author jonathan
     */
    @FacesConverter(forClass = Series.class)
    public static class SeriesConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
            if (string == null || string.length() == 0) {
                return null;
            }
            Long id = new Long(string);
            SeriesController controller = (SeriesController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "seriesController");
            return controller.getJpaController().find(Series.class, id);
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Series) {
                Series o = (Series) object;
                return o.getPk() == null ? "" : o.getPk().toString();
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: com.sitron.persistence.entities.Series");
            }
        }

    }

}
