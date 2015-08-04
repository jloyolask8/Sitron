/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
    @NamedQuery(name = "Study.findAll", query = "SELECT s FROM Study s")})
public class Study implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Long pk;
//    @Size(max = 255)
//    @Column(name = "access_control_id")
//    private String accessControlId;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 255)
//    @Column(name = "accession_no")
//    private String accessionNo;
    @Basic(optional = false)
    @NotNull
    private int availability;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
//    @Basic(optional = false)
//    @NotNull
//    @Lob
//    @Column(name = "study_attrs")
//    private byte[] studyAttrs;
//    @Size(max = 255)
//    @Column(name = "ext_retr_aet")
//    private String extRetrAet;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 255)
//    @Column(name = "accno_issuer")
//    private String accnoIssuer;
    @Size(max = 255)
    @Column(name = "mods_in_study")
    private String modsInStudy;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_instances")
    private int numInstances;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "num_instances_a")
//    private int numInstancesA;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_series")
    private int numSeries;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "num_series_a")
//    private int numSeriesA;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 255)
//    @Column(name = "ref_phys_fn_sx")
//    private String refPhysFnSx;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 255)
//    @Column(name = "ref_phys_gn_sx")
//    private String refPhysGnSx;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 255)
//    @Column(name = "ref_phys_i_name")
//    private String refPhysIName;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 255)
//    @Column(name = "ref_physician")
//    private String refPhysician;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 255)
//    @Column(name = "ref_phys_p_name")
//    private String refPhysPName;
//    @Size(max = 255)
//    @Column(name = "retrieve_aets")
//    private String retrieveAets;
    @Size(max = 255)
    @Column(name = "cuids_in_study")
    private String cuidsInStudy;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "study_custom1")
    private String studyCustom1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "study_custom2")
    private String studyCustom2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "study_custom3")
    private String studyCustom3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "study_date")
    private String studyDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "study_desc")
    private String studyDesc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "study_id")
    private String studyId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "study_iuid")
    private String studyIuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "study_time")
    private String studyTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTime;
    @JoinTable(name = "rel_study_pcode", joinColumns = {
        @JoinColumn(name = "study_fk", referencedColumnName = "pk")}, inverseJoinColumns = {
        @JoinColumn(name = "pcode_fk", referencedColumnName = "pk")})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Code> codeList;
    @JoinColumn(name = "patient_fk", referencedColumnName = "pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patientFk;
    @OneToMany(mappedBy = "studyFk", fetch = FetchType.LAZY)
    private List<Series> seriesList;
    @OneToMany(mappedBy = "studyPk", fetch = FetchType.LAZY)
    private List<StudyCase> studyRequestList;

    public Study() {
    }

    public Study(Long pk) {
        this.pk = pk;
    }

    public Study(Long pk, String accessionNo, int availability, Date createdTime,  String accnoIssuer, int numInstances, int numInstancesA, int numSeries, int numSeriesA, String refPhysFnSx, String refPhysGnSx, String refPhysIName, String refPhysician, String refPhysPName, String studyCustom1, String studyCustom2, String studyCustom3, String studyDate, String studyDesc, String studyId, String studyIuid, String studyTime, Date updatedTime) {
        this.pk = pk;
//        this.accessionNo = accessionNo;
        this.availability = availability;
        this.createdTime = createdTime;
//        this.studyAttrs = studyAttrs;
//        this.accnoIssuer = accnoIssuer;
        this.numInstances = numInstances;
//        this.numInstancesA = numInstancesA;
        this.numSeries = numSeries;
//        this.numSeriesA = numSeriesA;
//        this.refPhysFnSx = refPhysFnSx;
//        this.refPhysGnSx = refPhysGnSx;
//        this.refPhysIName = refPhysIName;
//        this.refPhysician = refPhysician;
//        this.refPhysPName = refPhysPName;
        this.studyCustom1 = studyCustom1;
        this.studyCustom2 = studyCustom2;
        this.studyCustom3 = studyCustom3;
        this.studyDate = studyDate;
        this.studyDesc = studyDesc;
        this.studyId = studyId;
        this.studyIuid = studyIuid;
        this.studyTime = studyTime;
        this.updatedTime = updatedTime;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

//    public String getAccessControlId() {
//        return accessControlId;
//    }
//
//    public void setAccessControlId(String accessControlId) {
//        this.accessControlId = accessControlId;
//    }

//    public String getAccessionNo() {
//        return accessionNo;
//    }
//
//    public void setAccessionNo(String accessionNo) {
//        this.accessionNo = accessionNo;
//    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

//    public byte[] getStudyAttrs() {
//        return studyAttrs;
//    }
//
//    public void setStudyAttrs(byte[] studyAttrs) {
//        this.studyAttrs = studyAttrs;
//    }

//    public String getExtRetrAet() {
//        return extRetrAet;
//    }
//
//    public void setExtRetrAet(String extRetrAet) {
//        this.extRetrAet = extRetrAet;
//    }
//
//    public String getAccnoIssuer() {
//        return accnoIssuer;
//    }
//
//    public void setAccnoIssuer(String accnoIssuer) {
//        this.accnoIssuer = accnoIssuer;
//    }

    public String getModsInStudy() {
        return modsInStudy;
    }

    public void setModsInStudy(String modsInStudy) {
        this.modsInStudy = modsInStudy;
    }

    public int getNumInstances() {
        return numInstances;
    }

    public void setNumInstances(int numInstances) {
        this.numInstances = numInstances;
    }

//    public int getNumInstancesA() {
//        return numInstancesA;
//    }
//
//    public void setNumInstancesA(int numInstancesA) {
//        this.numInstancesA = numInstancesA;
//    }

    public int getNumSeries() {
        return numSeries;
    }

    public void setNumSeries(int numSeries) {
        this.numSeries = numSeries;
    }

//    public int getNumSeriesA() {
//        return numSeriesA;
//    }
//
//    public void setNumSeriesA(int numSeriesA) {
//        this.numSeriesA = numSeriesA;
//    }
//
//    public String getRefPhysFnSx() {
//        return refPhysFnSx;
//    }
//
//    public void setRefPhysFnSx(String refPhysFnSx) {
//        this.refPhysFnSx = refPhysFnSx;
//    }
//
//    public String getRefPhysGnSx() {
//        return refPhysGnSx;
//    }
//
//    public void setRefPhysGnSx(String refPhysGnSx) {
//        this.refPhysGnSx = refPhysGnSx;
//    }
//
//    public String getRefPhysIName() {
//        return refPhysIName;
//    }
//
//    public void setRefPhysIName(String refPhysIName) {
//        this.refPhysIName = refPhysIName;
//    }
//
//    public String getRefPhysician() {
//        return refPhysician;
//    }
//
//    public void setRefPhysician(String refPhysician) {
//        this.refPhysician = refPhysician;
//    }
//
//    public String getRefPhysPName() {
//        return refPhysPName;
//    }
//
//    public void setRefPhysPName(String refPhysPName) {
//        this.refPhysPName = refPhysPName;
//    }
//
//    public String getRetrieveAets() {
//        return retrieveAets;
//    }
//
//    public void setRetrieveAets(String retrieveAets) {
//        this.retrieveAets = retrieveAets;
//    }

    public String getCuidsInStudy() {
        return cuidsInStudy;
    }

    public void setCuidsInStudy(String cuidsInStudy) {
        this.cuidsInStudy = cuidsInStudy;
    }

    public String getStudyCustom1() {
        return studyCustom1;
    }

    public void setStudyCustom1(String studyCustom1) {
        this.studyCustom1 = studyCustom1;
    }

    public String getStudyCustom2() {
        return studyCustom2;
    }

    public void setStudyCustom2(String studyCustom2) {
        this.studyCustom2 = studyCustom2;
    }

    public String getStudyCustom3() {
        return studyCustom3;
    }

    public void setStudyCustom3(String studyCustom3) {
        this.studyCustom3 = studyCustom3;
    }

    public String getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(String studyDate) {
        this.studyDate = studyDate;
    }

    public String getStudyDesc() {
        return studyDesc;
    }

    public void setStudyDesc(String studyDesc) {
        this.studyDesc = studyDesc;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public String getStudyIuid() {
        return studyIuid;
    }

    public void setStudyIuid(String studyIuid) {
        this.studyIuid = studyIuid;
    }

    public String getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @XmlTransient
    public List<Code> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<Code> codeList) {
        this.codeList = codeList;
    }

    public Patient getPatientFk() {
        return patientFk;
    }

    public void setPatientFk(Patient patientFk) {
        this.patientFk = patientFk;
    }

    @XmlTransient
    public List<Series> getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(List<Series> seriesList) {
        this.seriesList = seriesList;
    }

    @XmlTransient
    public List<StudyCase> getStudyCaseList() {
        return studyRequestList;
    }

    public void setStudyCaseList(List<StudyCase> studyRequestList) {
        this.studyRequestList = studyRequestList;
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
        if (!(object instanceof Study)) {
            return false;
        }
        Study other = (Study) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sitron.persistence.entities.Study[ pk=" + pk + " ]";
    }
    
}
