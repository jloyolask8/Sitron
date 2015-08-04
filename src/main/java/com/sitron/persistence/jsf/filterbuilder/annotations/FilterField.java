/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.jsf.filterbuilder.annotations;

import com.sitron.persistence.jsf.filterbuilder.EnumFieldType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author jonathan
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterField {

    EnumFieldType fieldTypeId();

    String label();

    String fieldIdFull();

    Class fieldTypeFull();

    String listGenericTypeFieldId() default "";
}
