/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.jpa;

import java.util.Iterator;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

/**
 *
 * @author jonathan
 */
public class ConstraintViolationExceptionHelper {

    public static void handleError(Exception ex) {

        if (ex instanceof ConstraintViolationException) {
            printOutContraintViolation((ConstraintViolationException) ex);
        }else if(ex.getCause() instanceof ConstraintViolationException){
            printOutContraintViolation((ConstraintViolationException) ex.getCause());
        }else{
            System.out.println("not a ConstraintViolationException...");
        }

//        if (ex.getCause() instanceof ConstraintViolationException) {
//            printOutContraintViolation((ConstraintViolationException) (ex.getCause()));
//        }

    }

    private static void printOutContraintViolation(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> set = (ex).getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : set) {
            System.out.println("\nleafBean class: " + constraintViolation.getLeafBean().getClass());
            Iterator<Path.Node> iter = constraintViolation.getPropertyPath().iterator();
            System.out.println("\nconstraintViolation.getPropertyPath(): ");
            while (iter.hasNext()) {
                System.out.print(iter.next().getName() + "/");
            }
            System.out.println("\nAnotacion: " + constraintViolation.getConstraintDescriptor().getAnnotation().toString() + " value:" + constraintViolation.getInvalidValue());
        }
    }

}
