/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.jsf.filterbuilder;

/**
 *
 * @author jonathan
 */
public class OrderBy {
    
    private String fieldName;
    private OrderType orderType;

    /**
     * Default ASC order
     * @param fieldName 
     */
    public OrderBy(String fieldName) {
        this.fieldName = fieldName;
        this.orderType = OrderType.ASC;
    }
    
    public OrderBy(String fieldName, OrderType orderType) {
        this.fieldName = fieldName;
        this.orderType = orderType;
    }

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @return the orderType
     */
    public OrderType getOrderType() {
        return orderType;
    }
    
        
    public enum OrderType{
        ASC, DESC;
    }
}
