/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author jonathan
 */
public class CriteriaQueryHelper {

   

    public static Expression<?> createExpression(Root<?> root, String field) {
        Path<?> path = null;
        String[] fields = field.split("\\.");

        for (String string : fields) {
            if (null == path) {
                path = root.get(string);
            } else {
                path = path.get(string);
            }
        }

        return path;
    }
    /*
     * 
     */

    public static Predicate addOrPredicate(Predicate predicate, Predicate localPredicate, CriteriaBuilder criteriaBuilder) {
        if (predicate == null) {
            predicate = localPredicate;
        } else {
            predicate = criteriaBuilder.or(predicate, localPredicate);
        }
        return predicate;
    }

    /*
     * 
     */
    public static Predicate addPredicate(Predicate predicate, Predicate localPredicate, CriteriaBuilder criteriaBuilder) {
        if (predicate == null) {
            predicate = localPredicate;
        } else {
            predicate = criteriaBuilder.and(predicate, localPredicate);
        }
        return predicate;
    }
//     public static Predicate addLikePredicate(Root<?> root, Predicate predicate, String field, String pattern) {
//        Predicate localPredicate = getCriteriaBuilder().like(root.<String>get(field), pattern);
//        return addPredicate(predicate, localPredicate);
//    }
}
