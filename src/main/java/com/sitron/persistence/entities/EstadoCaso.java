/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.entities;

import com.sitron.persistence.jsf.filterbuilder.EnumFieldType;
import com.sitron.persistence.jsf.filterbuilder.annotations.FilterField;
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
@Table(name = "ESTADO_CASO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoCaso.findAll", query = "SELECT s FROM EstadoCaso s"),
    @NamedQuery(name = "EstadoCaso.findAllByQuery", query = "SELECT o FROM EstadoCaso o WHERE (LOWER(o.nombre) LIKE CONCAT(LOWER(:q), '%')) "),
    @NamedQuery(name = "EstadoCaso.findByIdEstado", query = "SELECT s FROM EstadoCaso s WHERE s.idEstado = :idEstado"),
    @NamedQuery(name = "EstadoCaso.findByNombre", query = "SELECT s FROM EstadoCaso s WHERE s.nombre = :nombre")
})
public class EstadoCaso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "id_estado")
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Estado", fieldIdFull = "idEstado", fieldTypeFull = String.class)
    private String idEstado;
    @Basic(optional = false)
    @NotNull    
     @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Nombre", fieldIdFull = "nombre", fieldTypeFull = String.class)
    private String nombre;
    @FilterField(fieldTypeId = EnumFieldType.TEXT, label = "Descripción", fieldIdFull = "descripcion", fieldTypeFull = String.class)
    private String descripcion;
  

    @Basic(optional = false)
    @NotNull
    @Size(min = 3, max = 6)
    @Column(name = "font_color")
    private String fontColor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 3, max = 6)
    @Column(name = "background_color")
    private String backgroundColor;
   

    @NotNull
    private boolean first;

    public EstadoCaso() {
    }

    public EstadoCaso(String idEstado) {
        this.idEstado = idEstado;
    }

    public EstadoCaso(String idEstado, String nombre, String descripcion, String fontColor, String backgroundColor, boolean first) {
        this.idEstado = idEstado;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
        this.first = first;
    }

    public EstadoCaso(String idEstado, String nombre) {
        this.idEstado = idEstado;
        this.nombre = nombre;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstado != null ? idEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoCaso)) {
            return false;
        }
        EstadoCaso other = (EstadoCaso) object;
        if ((this.idEstado == null && other.idEstado != null) || (this.idEstado != null && !this.idEstado.equals(other.idEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

   

    /**
     * @return the fontColor
     */
    public String getFontColor() {
        return fontColor;
    }

    /**
     * @param fontColor the fontColor to set
     */
    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * @return the backgroundColor
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

 

    /**
     * @return the first
     */
    public boolean isFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(boolean first) {
        this.first = first;
    }
}
