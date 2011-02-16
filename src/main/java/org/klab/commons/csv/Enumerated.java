/*
 * $Id: Enumerated.java 0 2008/01/24 16:03:20 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Enumerated.
 * 
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 16:03:20 $ $Author: sano-n $
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Enumerated {

    /** */
    EnumType value();

    /**
     * TODO アノテーションがメソッド指定の場合 
     */
    static class Util {

        /** */
        public static boolean isEnumetated(Field field) {
            return field.getAnnotation(Enumerated.class) != null;
        }

        /**
         * 
         * @param field @{@link CsvColumn} annotated field.
         * @param fieldValue enum value
         * @throws NullPointerException when field is not annotated by {@link Enumerated}
         */
        public static <E extends Enum<E>> String toCsvString(Field field, E fieldValue) {
            Enumerated enumerated = field.getAnnotation(Enumerated.class);
            switch (enumerated.value()) {
            case ORDINAL:
                return fieldValue == null ? "" : String.valueOf(fieldValue.ordinal());
            case STRING:
            default:
                return fieldValue == null ? "" : fieldValue.name(); 
            }
        }

        /**
         * 
         * @param field @{@link CsvColumn} annotated field.
         * @param column string enum value
         * @throws NullPointerException when field is not annotated by {@link Enumerated}
         */
        @SuppressWarnings("unchecked")
        public static <E extends Enum<E>> E toFieldValue(Field field, String column) {
            Enumerated enumerated = field.getAnnotation(Enumerated.class);
            switch (enumerated.value()) {
            case ORDINAL:
                try {
                    if (column != null && !column.isEmpty()) {
                        Method method = field.getType().getDeclaredMethod("values");
                        Object[] values = (Object[]) method.invoke(null);
                        return (E) values[Integer.parseInt(column)];
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    throw (RuntimeException) new IllegalStateException().initCause(e); 
                }
            case STRING:
            default:
                return column != null && !column.isEmpty() ? Enum.valueOf((Class<E>) field.getType(), column) : null;
            }
        }
    }
}

/* */
