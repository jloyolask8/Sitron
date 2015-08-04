/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.jsf;

import com.sitron.persistence.entities.EstadoCaso;
import java.io.Serializable;
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
@Named("estadoCasoController")
@SessionScoped
public class EstadoCasoController extends AbstractManagedBean<EstadoCaso> implements Serializable {

    public EstadoCasoController() {
    }

    @Override
    protected Class getDataModelImplementationClass() {
        return ListDataModel.class;
    }

    /**
     *
     * @author jonathan
     */
    @FacesConverter(forClass = EstadoCaso.class)
    public static class EstadoCasoConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
            if (string == null || string.length() == 0) {
                return null;
            }
            String id = string;
            EstadoCasoController controller = (EstadoCasoController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "estadoCasoController");
            return controller.getJpaController().find(EstadoCaso.class, id);
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof EstadoCaso) {
                EstadoCaso o = (EstadoCaso) object;
                return o.getIdEstado() == null ? "" : o.getIdEstado();
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: com.sitron.persistence.entities.EstadoCaso");
            }
        }

    }

}
