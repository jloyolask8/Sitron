/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.webapp;

import com.auth0.Auth0User;
import com.sitron.persistence.entities.Usuario;
import com.sitron.persistence.jsf.AbstractManagedBean;
import com.sitron.persistence.jsf.util.UtilesRut;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Jonathan
 */
@ManagedBean(name = "UserSessionBean")
@SessionScoped
public class UserSessionBean extends AbstractManagedBean<Usuario> implements Serializable {

//    @ManagedProperty(value = "#{applicationBean}")
//    private ApplicationBean applicationBean;
//    private final String DEFAULT_THEME = "bootstrap";
    private Usuario sessionUser;
    private String channel;
    private int activeIndexOfMyAccount = 0;

    public UserSessionBean() {
        super(Usuario.class);
    }

    public boolean isValidatedSession() {
        return this.getCurrent() != null;
    }

    public Usuario getCurrent() {
//        return sessionUser;
        if ((sessionUser == null)) {
            final Auth0User auth0User = getAuth0User();
            if (auth0User != null) {
                final Usuario usuario = getJpaController().find(Usuario.class, auth0User.getUserId());
                setCurrent(usuario);
            }

        }

        return sessionUser;
    }

    public void formateaRut() {
        if (getCurrent().getRut() != null && !StringUtils.isEmpty(getCurrent().getRut())) {
            String rutFormateado = UtilesRut.formatear(getCurrent().getRut());
            getCurrent().setRut(rutFormateado);
        }
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String c) {
        this.channel = c;
    }

    public void setCurrent(Usuario current) {
        if ((sessionUser == null) && (current != null)) {
//            applicationBean.init();
        }
        this.sessionUser = current;
    }

    public String updateUsuario() {
        try {

//            String rutFormateado = UtilesRut.formatear(sessionUser.getRut());
//            sessionUser.setRut(rutFormateado);
            getJpaController().merge(sessionUser);
            addInfoMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdated"));

        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }

        return null;
    }

    /**
     * @return the activeIndexOfMyAccount
     */
    public int getActiveIndexOfMyAccount() {
        return activeIndexOfMyAccount;
    }

    /**
     * @param activeIndexOfMyAccount the activeIndexOfMyAccount to set
     */
    public void setActiveIndexOfMyAccount(int activeIndexOfMyAccount) {
        this.activeIndexOfMyAccount = activeIndexOfMyAccount;
    }

    @Override
    public Class getDataModelImplementationClass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param applicationBean the applicationBean to set
     */
//    public void setApplicationBean(ApplicationBean applicationBean) {
////        this.applicationBean = applicationBean;
//    }
    @PostConstruct
    public void init() {
    }

}
