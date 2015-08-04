/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
    @NamedQuery(name = "Code.findAll", query = "SELECT c FROM Code c")})
public class Code implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Long pk;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "code_meaning")
    private String codeMeaning;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "code_value")
    private String codeValue;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "code_designator")
    private String codeDesignator;
    @Size(max = 255)
    @Column(name = "code_version")
    private String codeVersion;
    @ManyToMany(mappedBy = "codeList", fetch = FetchType.LAZY)
    private List<Study> studyList;
    @OneToMany(mappedBy = "rejectCodeFk", fetch = FetchType.LAZY)
    private List<Instance> instanceList;
    @OneToMany(mappedBy = "srcodeFk", fetch = FetchType.LAZY)
    private List<Instance> instanceList1;
    @OneToMany(mappedBy = "instCodeFk", fetch = FetchType.LAZY)
    private List<Series> seriesList;

    public Code() {
    }

    public Code(Long pk) {
        this.pk = pk;
    }

    public Code(Long pk, String codeMeaning, String codeValue, String codeDesignator) {
        this.pk = pk;
        this.codeMeaning = codeMeaning;
        this.codeValue = codeValue;
        this.codeDesignator = codeDesignator;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getCodeMeaning() {
        return codeMeaning;
    }

    public void setCodeMeaning(String codeMeaning) {
        this.codeMeaning = codeMeaning;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getCodeDesignator() {
        return codeDesignator;
    }

    public void setCodeDesignator(String codeDesignator) {
        this.codeDesignator = codeDesignator;
    }

    public String getCodeVersion() {
        return codeVersion;
    }

    public void setCodeVersion(String codeVersion) {
        this.codeVersion = codeVersion;
    }

    @XmlTransient
    public List<Study> getStudyList() {
        return studyList;
    }

    public void setStudyList(List<Study> studyList) {
        this.studyList = studyList;
    }

    @XmlTransient
    public List<Instance> getInstanceList() {
        return instanceList;
    }

    public void setInstanceList(List<Instance> instanceList) {
        this.instanceList = instanceList;
    }

    @XmlTransient
    public List<Instance> getInstanceList1() {
        return instanceList1;
    }

    public void setInstanceList1(List<Instance> instanceList1) {
        this.instanceList1 = instanceList1;
    }

    @XmlTransient
    public List<Series> getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(List<Series> seriesList) {
        this.seriesList = seriesList;
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
        if (!(object instanceof Code)) {
            return false;
        }
        Code other = (Code) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sitron.persistence.entities.Code[ pk=" + pk + " ]";
    }
    
}
