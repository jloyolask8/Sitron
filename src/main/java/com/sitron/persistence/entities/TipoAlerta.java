/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "TIPO_ALERTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoAlerta.findAll", query = "SELECT t FROM TipoAlerta t"),
    @NamedQuery(name = "TipoAlerta.findAllByQuery", query = "SELECT o FROM TipoAlerta o WHERE (LOWER(o.nombre) LIKE CONCAT(LOWER(:q), '%')) "),
    @NamedQuery(name = "TipoAlerta.findByIdalerta", query = "SELECT t FROM TipoAlerta t WHERE t.idalerta = :idalerta"),
    @NamedQuery(name = "TipoAlerta.findByNombre", query = "SELECT t FROM TipoAlerta t WHERE t.nombre = :nombre")})
public class TipoAlerta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer idalerta;
    @Size(max = 50)
    private String nombre;
    @Lob
    @Size(max = 2147483647)
    private String descripcion;
//    @OneToMany(mappedBy = "estadoAlerta")
//    private List<Caso> casoList;

    public TipoAlerta() {
    }

    public TipoAlerta(Integer idalerta) {
        this.idalerta = idalerta;
    }

    public TipoAlerta(Integer idalerta, String nombre, String descripcion) {
        this.idalerta = idalerta;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getIdalerta() {
        return idalerta;
    }

    public void setIdalerta(Integer idalerta) {
        this.idalerta = idalerta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

//    @XmlTransient
//    public List<Caso> getCasoList() {
//        return casoList;
//    }
//
//    public void setCasoList(List<Caso> casoList) {
//        this.casoList = casoList;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idalerta != null ? idalerta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoAlerta)) {
            return false;
        }
        TipoAlerta other = (TipoAlerta) object;
        if ((this.idalerta == null && other.idalerta != null) || (this.idalerta != null && !this.idalerta.equals(other.idalerta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
