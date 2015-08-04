/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jonathan
 */
@Entity
@Table(name = "rx_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RxType.findAll", query = "SELECT r FROM RxType r"),
    @NamedQuery(name = "RxType.findByPk", query = "SELECT r FROM RxType r WHERE r.pk = :pk"),
    @NamedQuery(name = "RxType.findByName", query = "SELECT r FROM RxType r WHERE r.name = :name"),
    @NamedQuery(name = "RxType.findByDescription", query = "SELECT r FROM RxType r WHERE r.description = :description")})
public class RxType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "pk")
    private String pk;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "name")
    private String name;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;

    public RxType() {
    }

    public RxType(String pk) {
        this.pk = pk;
    }

    public RxType(String pk, String name) {
        this.pk = pk;
        this.name = name;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof RxType)) {
            return false;
        }
        RxType other = (RxType) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sitron.persistence.entities.RxType[ pk=" + pk + " ]";
    }
    
}
