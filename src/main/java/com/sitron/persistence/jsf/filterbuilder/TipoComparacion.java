package com.sitron.persistence.jsf.filterbuilder;

import java.io.Serializable;

/**
 * the name of this should be Criteria operator or something like that
 * @author jonathan
 */
public class TipoComparacion implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idComparador;
    private String simbolo;
    private String nombre;
    private String descripcion;
    

    public TipoComparacion() {
    }

    public TipoComparacion(String idComparador) {
        this.idComparador = idComparador;
    }

    public TipoComparacion(String nombre, String idComparador, String simbolo, String descripcion)
    {
        this.idComparador = idComparador;
        this.simbolo = simbolo;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }
    
    

    public String getIdComparador() {
        return idComparador;
    }

    public void setIdComparador(String idComparador) {
        this.idComparador = idComparador;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
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
        hash += (idComparador != null ? idComparador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoComparacion)) {
            return false;
        }
        TipoComparacion other = (TipoComparacion) object;
        if ((this.idComparador == null && other.idComparador != null) || (this.idComparador != null && !this.idComparador.equals(other.idComparador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
