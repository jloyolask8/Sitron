/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.jsf.filterbuilder;

/**
 *
 * @author jonathan
 */
public class FieldType {
    
    private static final long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    private String fieldTypeId;
    private String name;
    private String description;
    private Boolean isCustomField;
    private String javaType;

    public FieldType() {
    }

    public FieldType(String fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
    }

    public FieldType(String fieldTypeId, String name) {
        this.fieldTypeId = fieldTypeId;
        this.name = name;
    }
    
    public FieldType(String fieldTypeId, String name, Boolean isCustomField) {
        this.fieldTypeId = fieldTypeId;
        this.name = name;
        this.isCustomField = isCustomField;
    }
    
    /**
     * @return the fieldTypeId
     */
    public String getFieldTypeId() {
        return fieldTypeId;
    }

    /**
     * @param fieldTypeId the fieldTypeId to set
     */
    public void setFieldTypeId(String fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the isCustomField
     */
    public Boolean getIsCustomField() {
        return isCustomField;
    }

    /**
     * @param isCustomField the isCustomField to set
     */
    public void setIsCustomField(Boolean isCustomField) {
        this.isCustomField = isCustomField;
    }

    /**
     * @return the javaType
     */
    public String getJavaType() {
        return javaType;
    }

    /**
     * @param javaType the javaType to set
     */
    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
    
}
