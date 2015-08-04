/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.jsf.filterbuilder;

import com.sitron.persistence.entities.Usuario;
import java.awt.geom.Area;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jonathan
 */
public class Vista implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idVista;
    private String nombre;
    private String descripcion;
    private boolean visibleToAll;
    private Usuario idUsuarioCreadaPor;
    private List<FiltroVista> filtrosVistaList;
    private String baseEntityType;

    private Date fechaCreacion;
    private Date fechaModif;
    
    private boolean allMustMatch = true;//false means any can match
    
    

    public Vista() {
    }

    public Vista(Class clazz) {
        this.baseEntityType = clazz.getName();
        filtrosVistaList = new ArrayList<>();
    }

    public Vista(Integer idVista) {
        this.idVista = idVista;
    }

    public Vista(Integer idVista, String nombre, boolean visibleToAll) {
        this.idVista = idVista;
        this.nombre = nombre;
        this.visibleToAll = visibleToAll;
    }

    public Integer getIdVista() {
        return idVista;
    }

    public void setIdVista(Integer idVista) {
        this.idVista = idVista;
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

    public boolean getVisibleToAll() {
        return visibleToAll;
    }

    public void setVisibleToAll(boolean visibleToAll) {
        this.visibleToAll = visibleToAll;
    }

    public Usuario getIdUsuarioCreadaPor() {
        return idUsuarioCreadaPor;
    }

    public void setIdUsuarioCreadaPor(Usuario idUsuarioCreadaPor) {
        this.idUsuarioCreadaPor = idUsuarioCreadaPor;
    }

    public List<FiltroVista> getFiltrosVistaList() {
        return filtrosVistaList;
    }

    public void setFiltrosVistaList(List<FiltroVista> filtrosVistaList) {
        this.filtrosVistaList = filtrosVistaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVista != null ? idVista.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vista)) {
            return false;
        }
        Vista other = (Vista) object;
        if ((this.idVista == null && other.idVista != null) || (this.idVista != null && !this.idVista.equals(other.idVista))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Vista [" + "Criterios: " + filtrosVistaList + " visibleToAll: " + visibleToAll + " creadaPor:" + this.idUsuarioCreadaPor + "]";
    }

    public void addFiltroVista(FiltroVista filtro) {
        if (this.getFiltrosVistaList() == null || this.getFiltrosVistaList().isEmpty()) {
            this.setFiltrosVistaList(new ArrayList<FiltroVista>());
        }
        this.getFiltrosVistaList().add(filtro);
        filtro.setIdVista(this);
    }

    public void addNewFiltroVista(FiltroVista filtroActual) {
        FiltroVista filtro = new FiltroVista();
        Random randomGenerator = new Random();
        int n = randomGenerator.nextInt();
        if (n > 0) {
            n = n * (-1);
        }
        filtro.setIdFiltro(n);//Ugly patch to solve identifier unknown when new items are added to the datatable.
        if (this.getFiltrosVistaList() == null || this.getFiltrosVistaList().isEmpty()) {
            this.setFiltrosVistaList(new LinkedList<FiltroVista>());
        }
        if (filtroActual != null) {
            int index = this.getFiltrosVistaList().indexOf(filtroActual);
            if (index >= 0) {
                this.getFiltrosVistaList().add(index + 1, filtro);
            } else {
                this.getFiltrosVistaList().add(filtro);
            }
        } else {
            this.getFiltrosVistaList().add(filtro);
        }
        filtro.setIdVista(this);
    }

    public void addNewFiltroVista() {
        addNewFiltroVista(null);
    }

    public boolean canRemoveFiltroVista() {
        if (this.getFiltrosVistaList() != null && !this.getFiltrosVistaList().isEmpty()) {
            return this.getFiltrosVistaList().size() > 1;
        }
        return false;
    }

    public void removeFiltroFromVista(FiltroVista filtro) {
        if (this.getFiltrosVistaList() != null) {
            if (this.getFiltrosVistaList().contains(filtro)) {
                this.getFiltrosVistaList().remove(filtro);
            }
            filtro.setIdVista(null);
        }
    }

    /**
     * @return the baseEntityType
     */
    public String getBaseEntityType() {
        return baseEntityType;
    }

    /**
     * @param baseEntityType the baseEntityType to set
     */
    public void setBaseEntityType(String baseEntityType) {
        this.baseEntityType = baseEntityType;
    }

    /**
     * @return the fechaCreacion
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * @param fechaCreacion the fechaCreacion to set
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * @return the fechaModif
     */
    public Date getFechaModif() {
        return fechaModif;
    }

    /**
     * @param fechaModif the fechaModif to set
     */
    public void setFechaModif(Date fechaModif) {
        this.fechaModif = fechaModif;
    }

    /**
     * @return the allMustMatch
     */
    public boolean isAllMustMatch() {
        return allMustMatch;
    }

    /**
     * @param allMustMatch the allMustMatch to set
     */
    public void setAllMustMatch(boolean allMustMatch) {
        this.allMustMatch = allMustMatch;
    }
}
