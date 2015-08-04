/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jonathan
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Instance.findAll", query = "SELECT i FROM Instance i")})
public class Instance implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Long pk;
    @Basic(optional = false)
    @NotNull
    private boolean archived;
    @Basic(optional = false)
    @NotNull
    private int availability;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sr_complete")
    private String srComplete;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "content_date")
    private String contentDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "content_time")
    private String contentTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
//    @Basic(optional = false)
//    @NotNull
//    @Lob
//    @Column(name = "inst_attrs")
//    private byte[] instAttrs;
    @Size(max = 255)
    @Column(name = "ext_retr_aet")
    private String extRetrAet;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "inst_custom1")
    private String instCustom1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "inst_custom2")
    private String instCustom2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "inst_custom3")
    private String instCustom3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "inst_no")
    private String instNo;
    @Basic(optional = false)
    @NotNull
    private boolean replaced;
    @Size(max = 255)
    @Column(name = "retrieve_aets")
    private String retrieveAets;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sop_cuid")
    private String sopCuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sop_iuid")
    private String sopIuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sr_verified")
    private String srVerified;
    @JoinColumn(name = "reject_code_fk", referencedColumnName = "pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private Code rejectCodeFk;
    @JoinColumn(name = "srcode_fk", referencedColumnName = "pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private Code srcodeFk;
    @JoinColumn(name = "series_fk", referencedColumnName = "pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private Series seriesFk;

    public Instance() {
    }

    public Instance(Long pk) {
        this.pk = pk;
    }

    public Instance(Long pk, boolean archived, int availability, String srComplete, String contentDate, String contentTime, Date createdTime, byte[] instAttrs, String instCustom1, String instCustom2, String instCustom3, String instNo, boolean replaced, String sopCuid, String sopIuid, Date updatedTime, String srVerified) {
        this.pk = pk;
        this.archived = archived;
        this.availability = availability;
        this.srComplete = srComplete;
        this.contentDate = contentDate;
        this.contentTime = contentTime;
        this.createdTime = createdTime;
//        this.instAttrs = instAttrs;
        this.instCustom1 = instCustom1;
        this.instCustom2 = instCustom2;
        this.instCustom3 = instCustom3;
        this.instNo = instNo;
        this.replaced = replaced;
        this.sopCuid = sopCuid;
        this.sopIuid = sopIuid;
        this.updatedTime = updatedTime;
        this.srVerified = srVerified;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public boolean getArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getSrComplete() {
        return srComplete;
    }

    public void setSrComplete(String srComplete) {
        this.srComplete = srComplete;
    }

    public String getContentDate() {
        return contentDate;
    }

    public void setContentDate(String contentDate) {
        this.contentDate = contentDate;
    }

    public String getContentTime() {
        return contentTime;
    }

    public void setContentTime(String contentTime) {
        this.contentTime = contentTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

//    public byte[] getInstAttrs() {
//        return instAttrs;
//    }
//
//    public void setInstAttrs(byte[] instAttrs) {
//        this.instAttrs = instAttrs;
//    }

    public String getExtRetrAet() {
        return extRetrAet;
    }

    public void setExtRetrAet(String extRetrAet) {
        this.extRetrAet = extRetrAet;
    }

    public String getInstCustom1() {
        return instCustom1;
    }

    public void setInstCustom1(String instCustom1) {
        this.instCustom1 = instCustom1;
    }

    public String getInstCustom2() {
        return instCustom2;
    }

    public void setInstCustom2(String instCustom2) {
        this.instCustom2 = instCustom2;
    }

    public String getInstCustom3() {
        return instCustom3;
    }

    public void setInstCustom3(String instCustom3) {
        this.instCustom3 = instCustom3;
    }

    public String getInstNo() {
        return instNo;
    }

    public void setInstNo(String instNo) {
        this.instNo = instNo;
    }

    public boolean getReplaced() {
        return replaced;
    }

    public void setReplaced(boolean replaced) {
        this.replaced = replaced;
    }

    public String getRetrieveAets() {
        return retrieveAets;
    }

    public void setRetrieveAets(String retrieveAets) {
        this.retrieveAets = retrieveAets;
    }

    public String getSopCuid() {
        return sopCuid;
    }

    public void setSopCuid(String sopCuid) {
        this.sopCuid = sopCuid;
    }

    public String getSopIuid() {
        return sopIuid;
    }

    public void setSopIuid(String sopIuid) {
        this.sopIuid = sopIuid;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getSrVerified() {
        return srVerified;
    }

    public void setSrVerified(String srVerified) {
        this.srVerified = srVerified;
    }

    public Code getRejectCodeFk() {
        return rejectCodeFk;
    }

    public void setRejectCodeFk(Code rejectCodeFk) {
        this.rejectCodeFk = rejectCodeFk;
    }

    public Code getSrcodeFk() {
        return srcodeFk;
    }

    public void setSrcodeFk(Code srcodeFk) {
        this.srcodeFk = srcodeFk;
    }

    public Series getSeriesFk() {
        return seriesFk;
    }

    public void setSeriesFk(Series seriesFk) {
        this.seriesFk = seriesFk;
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
        if (!(object instanceof Instance)) {
            return false;
        }
        Instance other = (Instance) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.sopIuid;
    }
    
}
