/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.jsf;

import com.sitron.persistence.entities.Instance;
import java.io.Serializable;
import javax.faces.bean.SessionScoped;
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

@Named("instanceController")
@SessionScoped
public class InstanceController extends AbstractManagedBean<Instance> implements Serializable {

    public InstanceController() {

    }

    @Override
    protected Class getDataModelImplementationClass() {
        return ListDataModel.class;
    }

    /**
     *
     * @author jonathan
     */
     @FacesConverter( forClass = Instance.class)
    public static class InstanceConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
            if (string == null || string.length() == 0) {
                return null;
            }
            System.out.println("getAsObject:"+string);
            Long id = new Long(string);
//            InstanceController controller = (InstanceController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "instanceController");
            
             StudyController controller = (StudyController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "studyController");
             
            return controller.getJpaController().find(Instance.class, id);
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Instance) {
                Instance o = (Instance) object;
                return o.getPk() == null ? "" : o.getPk().toString();
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: com.sitron.persistence.entities.Instance");
            }
        }

    }

}
