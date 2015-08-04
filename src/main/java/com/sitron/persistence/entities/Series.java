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
import javax.persistence.Lob;
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
    @NamedQuery(name = "Series.findAll", query = "SELECT s FROM Series s")})
public class Series implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Long pk;
    @Basic(optional = false)
    @NotNull
    private int availability;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "body_part")
    private String bodyPart;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
//    @Basic(optional = false)
//    @NotNull
//    @Lob
//    @Column(name = "series_attrs")
//    private byte[] seriesAttrs;
    @Size(max = 255)
    @Column(name = "ext_retr_aet")
    private String extRetrAet;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String institution;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String department;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String laterality;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String modality;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_instances")
    private int numInstances;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_instances_a")
    private int numInstancesA;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pps_cuid")
    private String ppsCuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pps_iuid")
    private String ppsIuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pps_start_date")
    private String ppsStartDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pps_start_time")
    private String ppsStartTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "perf_phys_fn_sx")
    private String perfPhysFnSx;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "perf_phys_gn_sx")
    private String perfPhysGnSx;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "perf_phys_i_name")
    private String perfPhysIName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "perf_phys_name")
    private String perfPhysName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "perf_phys_p_name")
    private String perfPhysPName;
    @Size(max = 255)
    @Column(name = "retrieve_aets")
    private String retrieveAets;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "series_custom1")
    private String seriesCustom1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "series_custom2")
    private String seriesCustom2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "series_custom3")
    private String seriesCustom3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "series_desc")
    private String seriesDesc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "series_iuid")
    private String seriesIuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "series_no")
    private String seriesNo;
    @Size(max = 255)
    @Column(name = "src_aet")
    private String srcAet;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "station_name")
    private String stationName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTime;
    @OneToMany(mappedBy = "seriesFk", fetch = FetchType.LAZY)
    private List<Instance> instanceList;
    @JoinColumn(name = "inst_code_fk", referencedColumnName = "pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private Code instCodeFk;
    @JoinColumn(name = "study_fk", referencedColumnName = "pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private Study studyFk;

    public Series() {
    }

    public Series(Long pk) {
        this.pk = pk;
    }

    public Series(Long pk, int availability, String bodyPart, Date createdTime, byte[] seriesAttrs, String institution, String department, String laterality, String modality, int numInstances, int numInstancesA, String ppsCuid, String ppsIuid, String ppsStartDate, String ppsStartTime, String perfPhysFnSx, String perfPhysGnSx, String perfPhysIName, String perfPhysName, String perfPhysPName, String seriesCustom1, String seriesCustom2, String seriesCustom3, String seriesDesc, String seriesIuid, String seriesNo, String stationName, Date updatedTime) {
        this.pk = pk;
        this.availability = availability;
        this.bodyPart = bodyPart;
        this.createdTime = createdTime;
//        this.seriesAttrs = seriesAttrs;
        this.institution = institution;
        this.department = department;
        this.laterality = laterality;
        this.modality = modality;
        this.numInstances = numInstances;
        this.numInstancesA = numInstancesA;
        this.ppsCuid = ppsCuid;
        this.ppsIuid = ppsIuid;
        this.ppsStartDate = ppsStartDate;
        this.ppsStartTime = ppsStartTime;
        this.perfPhysFnSx = perfPhysFnSx;
        this.perfPhysGnSx = perfPhysGnSx;
        this.perfPhysIName = perfPhysIName;
        this.perfPhysName = perfPhysName;
        this.perfPhysPName = perfPhysPName;
        this.seriesCustom1 = seriesCustom1;
        this.seriesCustom2 = seriesCustom2;
        this.seriesCustom3 = seriesCustom3;
        this.seriesDesc = seriesDesc;
        this.seriesIuid = seriesIuid;
        this.seriesNo = seriesNo;
        this.stationName = stationName;
        this.updatedTime = updatedTime;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

//    public byte[] getSeriesAttrs() {
//        return seriesAttrs;
//    }
//
//    public void setSeriesAttrs(byte[] seriesAttrs) {
//        this.seriesAttrs = seriesAttrs;
//    }

    public String getExtRetrAet() {
        return extRetrAet;
    }

    public void setExtRetrAet(String extRetrAet) {
        this.extRetrAet = extRetrAet;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLaterality() {
        return laterality;
    }

    public void setLaterality(String laterality) {
        this.laterality = laterality;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public int getNumInstances() {
        return numInstances;
    }

    public void setNumInstances(int numInstances) {
        this.numInstances = numInstances;
    }

    public int getNumInstancesA() {
        return numInstancesA;
    }

    public void setNumInstancesA(int numInstancesA) {
        this.numInstancesA = numInstancesA;
    }

    public String getPpsCuid() {
        return ppsCuid;
    }

    public void setPpsCuid(String ppsCuid) {
        this.ppsCuid = ppsCuid;
    }

    public String getPpsIuid() {
        return ppsIuid;
    }

    public void setPpsIuid(String ppsIuid) {
        this.ppsIuid = ppsIuid;
    }

    public String getPpsStartDate() {
        return ppsStartDate;
    }

    public void setPpsStartDate(String ppsStartDate) {
        this.ppsStartDate = ppsStartDate;
    }

    public String getPpsStartTime() {
        return ppsStartTime;
    }

    public void setPpsStartTime(String ppsStartTime) {
        this.ppsStartTime = ppsStartTime;
    }

    public String getPerfPhysFnSx() {
        return perfPhysFnSx;
    }

    public void setPerfPhysFnSx(String perfPhysFnSx) {
        this.perfPhysFnSx = perfPhysFnSx;
    }

    public String getPerfPhysGnSx() {
        return perfPhysGnSx;
    }

    public void setPerfPhysGnSx(String perfPhysGnSx) {
        this.perfPhysGnSx = perfPhysGnSx;
    }

    public String getPerfPhysIName() {
        return perfPhysIName;
    }

    public void setPerfPhysIName(String perfPhysIName) {
        this.perfPhysIName = perfPhysIName;
    }

    public String getPerfPhysName() {
        return perfPhysName;
    }

    public void setPerfPhysName(String perfPhysName) {
        this.perfPhysName = perfPhysName;
    }

    public String getPerfPhysPName() {
        return perfPhysPName;
    }

    public void setPerfPhysPName(String perfPhysPName) {
        this.perfPhysPName = perfPhysPName;
    }

    public String getRetrieveAets() {
        return retrieveAets;
    }

    public void setRetrieveAets(String retrieveAets) {
        this.retrieveAets = retrieveAets;
    }

    public String getSeriesCustom1() {
        return seriesCustom1;
    }

    public void setSeriesCustom1(String seriesCustom1) {
        this.seriesCustom1 = seriesCustom1;
    }

    public String getSeriesCustom2() {
        return seriesCustom2;
    }

    public void setSeriesCustom2(String seriesCustom2) {
        this.seriesCustom2 = seriesCustom2;
    }

    public String getSeriesCustom3() {
        return seriesCustom3;
    }

    public void setSeriesCustom3(String seriesCustom3) {
        this.seriesCustom3 = seriesCustom3;
    }

    public String getSeriesDesc() {
        return seriesDesc;
    }

    public void setSeriesDesc(String seriesDesc) {
        this.seriesDesc = seriesDesc;
    }

    public String getSeriesIuid() {
        return seriesIuid;
    }

    public void setSeriesIuid(String seriesIuid) {
        this.seriesIuid = seriesIuid;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getSrcAet() {
        return srcAet;
    }

    public void setSrcAet(String srcAet) {
        this.srcAet = srcAet;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @XmlTransient
    public List<Instance> getInstanceList() {
        return instanceList;
    }

    public void setInstanceList(List<Instance> instanceList) {
        this.instanceList = instanceList;
    }

    public Code getInstCodeFk() {
        return instCodeFk;
    }

    public void setInstCodeFk(Code instCodeFk) {
        this.instCodeFk = instCodeFk;
    }

    public Study getStudyFk() {
        return studyFk;
    }

    public void setStudyFk(Study studyFk) {
        this.studyFk = studyFk;
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
        if (!(object instanceof Series)) {
            return false;
        }
        Series other = (Series) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Series[ pk=" + pk + " ]";
    }
    
}
