/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.entities;

import com.sitron.persistence.jsf.filterbuilder.EnumFieldType;
import com.sitron.persistence.jsf.filterbuilder.annotations.FilterField;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jonathan
 */
@Entity
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Usuario.findAll", query = "SELECT o FROM Usuario o"),
    @NamedQuery(name = "Usuario.findByUserId", query = "SELECT u FROM Usuario u WHERE u.idUsuario = :userId"),
    @NamedQuery(name = "Usuario.findByEmail", query = "SELECT u FROM Usuario u WHERE u.email = :email")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "id_usuario")
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "idUsuario", fieldIdFull = "idUsuario", fieldTypeFull = String.class)
    private String idUsuario;
    @Size(max = 100)
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "nombres", fieldIdFull = "nombres", fieldTypeFull = String.class)
    private String nombres;
    @Size(max = 100)
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "apellidos", fieldIdFull = "apellidos", fieldTypeFull = String.class)
    private String apellidos;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "email", fieldIdFull = "email", fieldTypeFull = String.class)
    private String email;
    @Size(max = 40)
    @Column(name = "tel_fijo")
    private String telFijo;
    @Size(max = 40)
    @Column(name = "tel_movil")
    private String telMovil;
    @Basic(optional = false)
    @NotNull
    private boolean activo;
    @Basic(optional = false)
    private String pass;
    @Size(max = 14)
    private String rut;
    @Basic(optional = false)
    @NotNull
    private boolean editable;
    @Size(max = 40)
    private String theme;
    @Size(max = 2147483647)
    @Column(name = "page_layout_state")
    private String pageLayoutState;
    @Column(name = "notify_when_ticket_assigned")
    private Boolean notifyWhenTicketAssigned;
    @Column(name = "notify_when_new_ticket_in_group")
    private Boolean notifyWhenNewTicketInGroup;
    @Column(name = "notify_when_ticket_alert")
    private Boolean notifyWhenTicketAlert;
    @Column(name = "notify_when_ticket_is_updated")
    private Boolean notifyWhenTicketIsUpdated;
    @Column(name = "email_notifications_enabled")
    private Boolean emailNotificationsEnabled;
    @Column(name = "desktop_notifications_enabled")
    private Boolean desktopNotificationsEnabled;
    @Column(name = "prefer_firma_enabled")
    private Boolean preferFirmaEnabled;
    @Size(max = 2147483647)
    private String firma;
    @Size(max = 64)
    @Column(name = "template_theme")
    private String templateTheme;
    @Column(name = "main_menu_changepos")
    private Boolean mainMenuChangepos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String rol;
    @OneToMany(mappedBy = "supervisor", fetch = FetchType.LAZY)
    private List<Usuario> usuarioList;
    @JoinColumn(name = "supervisor", referencedColumnName = "id_usuario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario supervisor;
    @OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY)
    private List<StudyCase> studyRequestList;
    @OneToMany(mappedBy = "createdByUser", fetch = FetchType.LAZY)
    private List<StudyCase> studyRequestList1;

    private String aboutMe;
    private String picture;
    private String especialidad;

    @Basic
    private String creationDate;
    @Basic
    private String nickname;
    @Basic
    private String locale;
    @Basic
    private String genre;
    
    @Basic
    @Column(name = "email_verified")
    private boolean emailVerified = false;
    
    @Size(max = 2147483647)
    private String signature;

    public Usuario() {
        this.rol = "Technical";
    }

    @Transient
    public boolean isRadiologist() {
        return getRol() != null ? "Radiologist".equals(getRol()) : false;
    }

    @Transient
    public boolean isTechnical() {
        return getRol() != null ? "Technical".equals(getRol()) : false;
    }

    @Transient
    public boolean isAdmin() {
        return getRol() != null ? "admin".equals(getRol()) : false;
    }

    public Usuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario(String idUsuario, boolean activo, String pass, boolean editable, String rol) {
        this.idUsuario = idUsuario;
        this.activo = activo;
        this.pass = pass;
        this.editable = editable;
        this.rol = rol;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelFijo() {
        return telFijo;
    }

    public void setTelFijo(String telFijo) {
        this.telFijo = telFijo;
    }

    public String getTelMovil() {
        return telMovil;
    }

    public void setTelMovil(String telMovil) {
        this.telMovil = telMovil;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getPageLayoutState() {
        return pageLayoutState;
    }

    public void setPageLayoutState(String pageLayoutState) {
        this.pageLayoutState = pageLayoutState;
    }

    public Boolean getNotifyWhenTicketAssigned() {
        return notifyWhenTicketAssigned;
    }

    public void setNotifyWhenTicketAssigned(Boolean notifyWhenTicketAssigned) {
        this.notifyWhenTicketAssigned = notifyWhenTicketAssigned;
    }

    public Boolean getNotifyWhenNewTicketInGroup() {
        return notifyWhenNewTicketInGroup;
    }

    public void setNotifyWhenNewTicketInGroup(Boolean notifyWhenNewTicketInGroup) {
        this.notifyWhenNewTicketInGroup = notifyWhenNewTicketInGroup;
    }

    public Boolean getNotifyWhenTicketAlert() {
        return notifyWhenTicketAlert;
    }

    public void setNotifyWhenTicketAlert(Boolean notifyWhenTicketAlert) {
        this.notifyWhenTicketAlert = notifyWhenTicketAlert;
    }

    public Boolean getNotifyWhenTicketIsUpdated() {
        return notifyWhenTicketIsUpdated;
    }

    public void setNotifyWhenTicketIsUpdated(Boolean notifyWhenTicketIsUpdated) {
        this.notifyWhenTicketIsUpdated = notifyWhenTicketIsUpdated;
    }

    public Boolean getEmailNotificationsEnabled() {
        return emailNotificationsEnabled;
    }

    public void setEmailNotificationsEnabled(Boolean emailNotificationsEnabled) {
        this.emailNotificationsEnabled = emailNotificationsEnabled;
    }

    public Boolean getDesktopNotificationsEnabled() {
        return desktopNotificationsEnabled;
    }

    public void setDesktopNotificationsEnabled(Boolean desktopNotificationsEnabled) {
        this.desktopNotificationsEnabled = desktopNotificationsEnabled;
    }

    public Boolean getPreferFirmaEnabled() {
        return preferFirmaEnabled;
    }

    public void setPreferFirmaEnabled(Boolean preferFirmaEnabled) {
        this.preferFirmaEnabled = preferFirmaEnabled;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getTemplateTheme() {
        return templateTheme;
    }

    public void setTemplateTheme(String templateTheme) {
        this.templateTheme = templateTheme;
    }

    public Boolean getMainMenuChangepos() {
        return mainMenuChangepos;
    }

    public void setMainMenuChangepos(Boolean mainMenuChangepos) {
        this.mainMenuChangepos = mainMenuChangepos;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public Usuario getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Usuario supervisor) {
        this.supervisor = supervisor;
    }

    @XmlTransient
    public List<StudyCase> getStudyCaseList() {
        return studyRequestList;
    }

    public void setStudyCaseList(List<StudyCase> studyRequestList) {
        this.studyRequestList = studyRequestList;
    }

    @XmlTransient
    public List<StudyCase> getStudyCaseList1() {
        return studyRequestList1;
    }

    public void setStudyCaseList1(List<StudyCase> studyRequestList1) {
        this.studyRequestList1 = studyRequestList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final String capitalName = getCapitalName();
        if(!StringUtils.isEmpty(capitalName)){
            return capitalName;
        }else{
            return email;
        }
            
        
    }

    public String getCapitalName() {
        String capitalName = "";
        try {
            String[] names = new String[]{nombres == null ? "" : nombres.split(" ")[0], apellidos == null ? "" : apellidos.split(" ")[0]};
            for (int i = 0; i < names.length; i++) {
                if (!(names[i].trim().isEmpty())) {
                    if (i > 0) {
                        capitalName += " ";
                    }
                    if (!names[i].isEmpty()) {
                        capitalName += names[i].substring(0, 1).toUpperCase() + names[i].substring(1).toLowerCase();
                    }
                }
            }
        } catch (Exception e) {
            capitalName = nombres + " " + apellidos;
        }
        if(StringUtils.isEmpty(capitalName)){
            return email;
        }
        return capitalName;
    }

    /**
     * @return the picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * @param picture the picture to set
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * @return the aboutMe
     */
    public String getAboutMe() {
        return aboutMe;
    }

    /**
     * @param aboutMe the aboutMe to set
     */
    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    /**
     * @return the especialidad
     */
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * @param especialidad the especialidad to set
     */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * @return the creationDate
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * @return the emailVerified
     */
    public boolean isEmailVerified() {
        return emailVerified;
    }

    /**
     * @param emailVerified the emailVerified to set
     */
    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    /**
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

}
