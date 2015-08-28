/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.entities;

import com.sitron.persistence.jsf.filterbuilder.EnumFieldType;
import com.sitron.persistence.jsf.filterbuilder.annotations.FilterField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jonathan
 */
@Entity
@Table(name = "study_case")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudyCase.findAll", query = "SELECT s FROM StudyCase s")})
public class StudyCase implements Serializable {

    private static final long serialVersionUID = 1L;
    @FilterField(fieldTypeId = EnumFieldType.NUMBER, label = "id caso", fieldIdFull = "idRequest", fieldTypeFull = Integer.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_request")
    private Integer idRequest;

    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "asunto", fieldIdFull = "subject", fieldTypeFull = String.class)
    @Size(max = 2147483647)
    private String subject;

    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "descripción", fieldIdFull = "description", fieldTypeFull = String.class)
    @Size(max = 2147483647)
    private String description;

    @FilterField(fieldTypeId = EnumFieldType.CALENDAR, label = "fecha creación", fieldIdFull = "createdAt", fieldTypeFull = Date.class)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @FilterField(fieldTypeId = EnumFieldType.CALENDAR, label = "fecha Asignación", fieldIdFull = "fechaAsignacion", fieldTypeFull = Date.class)
    @Column(name = "fechaAsignacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAsignacion;

    @FilterField(fieldTypeId = EnumFieldType.CALENDAR, label = "fecha ult. modif.", fieldIdFull = "modifAt", fieldTypeFull = Date.class)
    @Column(name = "modif_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifAt;
//    @FilterField(fieldTypeId = EnumFieldType.CALENDAR, label = "fecha cierre", fieldIdFull = "closedAt", fieldTypeFull = Date.class)
//    @Column(name = "closed_at")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date closedAt;

    @FilterField(fieldTypeId = EnumFieldType.CALENDAR, label = "fecha informe", fieldIdFull = "informedAt", fieldTypeFull = Date.class)
    @Column(name = "informed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date informedAt;

    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "informe", fieldIdFull = "informText", fieldTypeFull = String.class)
    @Size(max = 2147483647)
    @Column(name = "inform_text")
    private String informText;

//    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "statusInWorkflow", fieldIdFull = "statusInWorkflow", fieldTypeFull = String.class)
//    @Size(max = 40)
//    @Column(name = "status_in_workflow")
//    private String statusInWorkflow;
    @Column(name = "review_update")
    private Boolean reviewUpdate;

    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "studyUid", fieldIdFull = "studyUid", fieldTypeFull = String.class)
    @Size(max = 2147483647)
    @Column(name = "study_uid")
    private String studyUid;

    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Radiólogo asignado", fieldIdFull = "assignee.idUsuario", fieldTypeFull = String.class)
    @JoinColumn(name = "assignee", referencedColumnName = "id_usuario")
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario assignee;

    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Solicitante", fieldIdFull = "createdByUser.idUsuario", fieldTypeFull = String.class)
    @JoinColumn(name = "created_by_user", referencedColumnName = "id_usuario")
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario createdByUser;

    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Doctor", fieldIdFull = "doctor.idUsuario", fieldTypeFull = String.class)
    @JoinColumn(name = "doctor", referencedColumnName = "id_usuario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario doctor;

    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Prioridad", fieldIdFull = "priority.pk", fieldTypeFull = String.class)
    @JoinColumn(name = "priority_pk", referencedColumnName = "pk")
    @ManyToOne
    private Priority priority;

    @Basic
    private String rxType;

    @Basic
    private String piezasDent;

    @JoinColumn(name = "patient_fk", referencedColumnName = "pk")
    @ManyToOne(fetch = FetchType.EAGER)
    private Patient patientFk;

    //
    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Estado", fieldIdFull = "idEstado.idEstado", fieldTypeFull = String.class)
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne(fetch = FetchType.EAGER)
    private EstadoCaso idEstado;

    @FilterField(fieldTypeId = EnumFieldType.SELECTONE_ENTITY, label = "Estado de Alerta", fieldIdFull = "estadoAlerta.idalerta", fieldTypeFull = Integer.class)
    @JoinColumn(name = "estado_alerta", referencedColumnName = "idalerta")
    @ManyToOne
    private TipoAlerta estadoAlerta;

    //to know what images needs to be studied
    @JoinColumn(name = "study_pk", referencedColumnName = "pk")
    @ManyToOne(fetch = FetchType.EAGER)
    private Study studyPk;

//    @Transient
//    private Series serieSelected;
    @JoinColumn(name = "series_fk", referencedColumnName = "pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private Series serieSelected;

//    @Transient
//    private List<Instance> imagesSelected;
//    @ElementCollection(fetch = FetchType.EAGER)
//    private Collection<String> images;
    @ManyToMany
    @JoinTable(name = "studycase_images",
            joinColumns = {
                @JoinColumn(name = "id_request", referencedColumnName = "id_request")},
            inverseJoinColumns = {
                @JoinColumn(name = "pk", referencedColumnName = "pk")})
    private List<Instance> images;

    public StudyCase() {
    }

    public StudyCase(Integer idRequest) {
        this.idRequest = idRequest;
    }

    public Instance getFirstImage() {
        return images != null ? images.get(0) : null;
    }

    public Integer getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(Integer idRequest) {
        this.idRequest = idRequest;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifAt() {
        return modifAt;
    }

    public void setModifAt(Date modifAt) {
        this.modifAt = modifAt;
    }

//    public Date getClosedAt() {
//        return closedAt;
//    }
//
//    public void setClosedAt(Date closedAt) {
//        this.closedAt = closedAt;
//    }
    public Date getInformedAt() {
        return informedAt;
    }

    public void setInformedAt(Date informedAt) {
        this.informedAt = informedAt;
    }

    public String getInformText() {
        return informText;
    }
    
    public String getInformTextHtml() {        
        return informText.replace("\n", "<br/>").replace("\r", "<br/>");
    }

    public void setInformText(String informText) {
        this.informText = informText;
    }

//    public String getStatusInWorkflow() {
//        return statusInWorkflow;
//    }
//
//    public void setStatusInWorkflow(String statusInWorkflow) {
//        this.statusInWorkflow = statusInWorkflow;
//    }
    public Boolean getReviewUpdate() {
        return reviewUpdate;
    }

    public void setReviewUpdate(Boolean reviewUpdate) {
        this.reviewUpdate = reviewUpdate;
    }

    public String getStudyUid() {
        return studyUid;
    }

    public void setStudyUid(String studyUid) {
        this.studyUid = studyUid;
    }

    public Study getStudyPk() {
        return studyPk;
    }

    public void setStudyPk(Study studyPk) {
        this.studyPk = studyPk;
    }

    public Usuario getAssignee() {
        return assignee;
    }

    public void setAssignee(Usuario assignee) {
        this.assignee = assignee;
    }

    public Usuario getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(Usuario createdByUser) {
        this.createdByUser = createdByUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRequest != null ? idRequest.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StudyCase)) {
            return false;
        }
        StudyCase other = (StudyCase) object;
        if ((this.idRequest == null && other.idRequest != null) || (this.idRequest != null && !this.idRequest.equals(other.idRequest))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StudyCase[ idRequest=" + idRequest + " ]";
    }

    /**
     * @return the priority
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * @return the doctor
     */
    public Usuario getDoctor() {
        return doctor;
    }

    /**
     * @param doctor the doctor to set
     */
    public void setDoctor(Usuario doctor) {
        this.doctor = doctor;
    }

    /**
     * @return the RxType
     */
    public String getRxType() {
        return rxType;
    }

    /**
     * @param RxType the RxType to set
     */
    public void setRxType(String RxType) {
        this.rxType = RxType;
    }

    /**
     * @return the idEstado
     */
    public EstadoCaso getIdEstado() {
        return idEstado;
    }

    /**
     * @param idEstado the idEstado to set
     */
    public void setIdEstado(EstadoCaso idEstado) {
        this.idEstado = idEstado;
    }

    /**
     * @return the estadoAlerta
     */
    public TipoAlerta getEstadoAlerta() {
        return estadoAlerta;
    }

    /**
     * @param estadoAlerta the estadoAlerta to set
     */
    public void setEstadoAlerta(TipoAlerta estadoAlerta) {
        this.estadoAlerta = estadoAlerta;
    }

    /**
     * @return the serieSelected
     */
    public Series getSerieSelected() {
        return serieSelected;
    }

    /**
     * @param serieSelected the serieSelected to set
     */
    public void setSerieSelected(Series serieSelected) {
        this.serieSelected = serieSelected;
    }

    @XmlTransient
    public List<Instance> getInstanceList() {

        if (serieSelected != null) {
            return serieSelected.getInstanceList();
        } else if (studyPk != null) {
            List<Instance> ins = new ArrayList<>();
            for (Series serie : studyPk.getSeriesList()) {
                ins.addAll(serie.getInstanceList());
            }
            return ins;
        } else if (patientFk != null) {
            List<Instance> ins = new ArrayList<>();
            for (Study study : patientFk.getStudyList()) {
                for (Series serie : study.getSeriesList()) {
                    ins.addAll(serie.getInstanceList());
                }
            }
            return ins;
        }

        return Collections.EMPTY_LIST;
    }

//    /**
//     * @return the imagesSelected
//     */
//    public List<Instance> getImagesSelected() {
//        return imagesSelected;
//    }
//
//    /**
//     * @param imagesSelected the imagesSelected to set
//     */
//    public void setImagesSelected(List<Instance> imagesSelected) {
//        this.imagesSelected = imagesSelected;
//    }
    /**
     * @return the piezasDent
     */
    public String getPiezasDent() {
        return piezasDent;
    }

    /**
     * @param piezasDent the piezasDent to set
     */
    public void setPiezasDent(String piezasDent) {
        this.piezasDent = piezasDent;
    }

    /**
     * @return the patientFk
     */
    public Patient getPatientFk() {
        return patientFk;
    }

    /**
     * @param patientFk the patientFk to set
     */
    public void setPatientFk(Patient patientFk) {
        this.patientFk = patientFk;
    }

    /**
     * @param images the images to set
     */
    public void setImages(List<Instance> images) {
        this.images = images;
    }

    /**
     * @return the images
     */
    public List<Instance> getImages() {
        return images;
    }

    /**
     * @return the fechaAsignacion
     */
    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    /**
     * @param fechaAsignacion the fechaAsignacion to set
     */
    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

}
