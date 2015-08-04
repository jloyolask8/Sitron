/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.jsf.filterbuilder;

import java.io.Serializable;

/**
 * Belongs to future jonathan's JPA Filter builder API. 
 * @author jonathan
 */
public class ComparableField implements Serializable, Comparable<ComparableField>{

    private static final long serialVersionUID = 1L;
    private Class baseClass;
    private String idCampo;
    private Class tipo;
    private String idCampoFullPath;
    private Class tipoCampoFullPath;
    private FieldType fieldTypeId;
    private String label;
    private String descripcion;
    private String listParameterFieldId;
    private Class listGenericType;

    public ComparableField() {
    }

  

    public Class getBaseClass() {
        return baseClass;
    }

    public void setBaseClass(Class baseClass) {
        this.baseClass = baseClass;
    }

    public String getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(String idCampo) {
        this.idCampo = idCampo;
    }

    public Class getTipo() {
        return tipo;
    }

    public void setTipo(Class tipo) {
        this.tipo = tipo;
    }

    public String getIdCampoFullPath() {
        return idCampoFullPath;
    }

    public void setIdCampoFullPath(String idCampoFullPath) {
        this.idCampoFullPath = idCampoFullPath;
    }

    public Class getTipoCampoFullPath() {
        return tipoCampoFullPath;
    }

    public void setTipoCampoFullPath(Class tipoCampoFullPath) {
        this.tipoCampoFullPath = tipoCampoFullPath;
    }

    public FieldType getFieldTypeId() {
        return fieldTypeId;
    }

    public void setFieldTypeId(FieldType fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.baseClass != null ? this.baseClass.hashCode() : 0);
        hash = 59 * hash + (this.idCampo != null ? this.idCampo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComparableField other = (ComparableField) obj;
        if (this.baseClass != other.baseClass && (this.baseClass == null || !this.baseClass.equals(other.baseClass))) {
            return false;
        }
        if ((this.idCampo == null) ? (other.idCampo != null) : !this.idCampo.equals(other.idCampo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ComparableField [");
        builder.append("baseClass=");
        builder.append(baseClass);
        builder.append(", descripcion=");
        builder.append(descripcion);
        builder.append(", fieldTypeId=");
        builder.append(fieldTypeId);
        builder.append(", idCampo=");
        builder.append(idCampo);
        builder.append(", idCampoFullPath=");
        builder.append(idCampoFullPath);
        builder.append(", label=");
        builder.append(label);
        builder.append(", listGenericType=");
        builder.append(listGenericType);
        builder.append(", listParameterFieldId=");
        builder.append(listParameterFieldId);
        builder.append(", serialVersionUID=");
        builder.append(serialVersionUID);
        builder.append(", tipo=");
        builder.append(tipo);
        builder.append(", tipoCampoFullPath=");
        builder.append(tipoCampoFullPath);
        builder.append("]");
        return builder.toString();
    }

   

    /**
     * @return the listParameterFieldId
     */
    public String getListParameterFieldId() {
        return listParameterFieldId;
    }

    /**
     * @param listParameterFieldId the listParameterFieldId to set
     */
    public void setListParameterFieldId(String listParameterFieldId) {
        this.listParameterFieldId = listParameterFieldId;
    }

    /**
     * @return the listGenericType
     */
    public Class getListGenericType() {
        return listGenericType;
    }

    /**
     * @param listGenericType the listGenericType to set
     */
    public void setListGenericType(Class listGenericType) {
        this.listGenericType = listGenericType;
    }

    @Override
    public int compareTo(ComparableField o) {
        return this.getLabel().compareTo(o.getLabel());
    }
    
    
}
