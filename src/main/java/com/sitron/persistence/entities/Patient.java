/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.entities;

import com.sitron.persistence.jsf.filterbuilder.EnumFieldType;
import com.sitron.persistence.jsf.filterbuilder.annotations.FilterField;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jonathan
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Patient.findAll", query = "SELECT p FROM Patient p")})
public class Patient implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Long pk;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
//    @Basic(optional = false)
//    @NotNull
//    @Lob
//    @Column(name = "pat_attrs")
//    private byte[] patAttrs;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pat_id_issuer")
    private String patIdIssuer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pat_birthdate")
    private String patBirthdate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pat_custom1")
    private String patCustom1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pat_custom2")
    private String patCustom2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pat_custom3")
    private String patCustom3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pat_fn_sx")
    private String patFnSx;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pat_gn_sx")
    private String patGnSx;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pat_id")
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Patient id", fieldIdFull = "patId", fieldTypeFull = String.class)
    private String patId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pat_i_name")
    private String patIName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pat_name")
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Patient name", fieldIdFull = "patName", fieldTypeFull = String.class)
    private String patName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pat_p_name")
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Patient pname", fieldIdFull = "patPName", fieldTypeFull = String.class)
    private String patPName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pat_sex")
    private String patSex;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTime;
    @OneToMany(mappedBy = "patientFk", fetch = FetchType.LAZY)
    private List<Study> studyList;
    @OneToMany(mappedBy = "mergeFk", fetch = FetchType.LAZY)
    private List<Patient> patientList;
    @JoinColumn(name = "merge_fk", referencedColumnName = "pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private Patient mergeFk;

    public Patient() {
    }

    public Patient(Long pk) {
        this.pk = pk;
    }

    public Patient(Long pk, Date createdTime, byte[] patAttrs, String patIdIssuer, String patBirthdate, String patCustom1, String patCustom2, String patCustom3, String patFnSx, String patGnSx, String patId, String patIName, String patName, String patPName, String patSex, Date updatedTime) {
        this.pk = pk;
        this.createdTime = createdTime;
//        this.patAttrs = patAttrs;
        this.patIdIssuer = patIdIssuer;
        this.patBirthdate = patBirthdate;
        this.patCustom1 = patCustom1;
        this.patCustom2 = patCustom2;
        this.patCustom3 = patCustom3;
        this.patFnSx = patFnSx;
        this.patGnSx = patGnSx;
        this.patId = patId;
        this.patIName = patIName;
        this.patName = patName;
        this.patPName = patPName;
        this.patSex = patSex;
        this.updatedTime = updatedTime;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

//    public byte[] getPatAttrs() {
//        return patAttrs;
//    }
//
//    public void setPatAttrs(byte[] patAttrs) {
//        this.patAttrs = patAttrs;
//    }

    public String getPatIdIssuer() {
        return patIdIssuer;
    }

    public void setPatIdIssuer(String patIdIssuer) {
        this.patIdIssuer = patIdIssuer;
    }

    public String getPatBirthdate() {
        return patBirthdate;
    }

    public void setPatBirthdate(String patBirthdate) {
        this.patBirthdate = patBirthdate;
    }

    public String getPatCustom1() {
        return patCustom1;
    }

    public void setPatCustom1(String patCustom1) {
        this.patCustom1 = patCustom1;
    }

    public String getPatCustom2() {
        return patCustom2;
    }

    public void setPatCustom2(String patCustom2) {
        this.patCustom2 = patCustom2;
    }

    public String getPatCustom3() {
        return patCustom3;
    }

    public void setPatCustom3(String patCustom3) {
        this.patCustom3 = patCustom3;
    }

    public String getPatFnSx() {
        return patFnSx;
    }

    public void setPatFnSx(String patFnSx) {
        this.patFnSx = patFnSx;
    }

    public String getPatGnSx() {
        return patGnSx;
    }

    public void setPatGnSx(String patGnSx) {
        this.patGnSx = patGnSx;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getPatIName() {
        return patIName;
    }

    public void setPatIName(String patIName) {
        this.patIName = patIName;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    public String getPatPName() {
        return patPName;
    }

    public void setPatPName(String patPName) {
        this.patPName = patPName;
    }

    public String getPatSex() {
        return patSex;
    }

    public void setPatSex(String patSex) {
        this.patSex = patSex;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @XmlTransient
    public List<Study> getStudyList() {
        return studyList;
    }

    public void setStudyList(List<Study> studyList) {
        this.studyList = studyList;
    }

    @XmlTransient
    public List<Patient> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<Patient> patientList) {
        this.patientList = patientList;
    }

    public Patient getMergeFk() {
        return mergeFk;
    }

    public void setMergeFk(Patient mergeFk) {
        this.mergeFk = mergeFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pk != null ? pk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Patient)) {
            return false;
        }
        Patient other = (Patient) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Patient[ " + this.patName + " ]";
    }
    
//     public String getCapitalName() {
//        String capitalName = "";
//        try {
//            String[] names = new String[]{this. == null ? "" : nombres.split(" ")[0], apellidos == null ? "" : apellidos.split(" ")[0]};
//            for (int i = 0; i < names.length; i++) {
//                if (!(names[i].trim().isEmpty())) {
//                    if (i > 0) {
//                        capitalName += " ";
//                    }
//                    if (!names[i].isEmpty()) {
//                        capitalName += names[i].substring(0, 1).toUpperCase() + names[i].substring(1).toLowerCase();
//                    }
//                }
//            }
//        } catch (Exception e) {
//            capitalName = nombres + " " + apellidos;
//        }
//        return capitalName;
//    }
    
}
