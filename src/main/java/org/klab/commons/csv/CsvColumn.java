/*
 * Copyright (c) 2007 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import vavi.util.properties.annotation.Property;


/**
 * CsvColumn. 
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/11 23:45:45 $ $Author: sano-n $
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvColumn {

    /**
     * Sequence no of the CSV. Starts with 1.
     * <p>
     * ex.
     * <pre>
     *  1, 2...
     * </pre>
     */
    int sequence();

    /** */
    class Util {

        /**
         * @param field {@link Property} annotated
         * @return When {@link Property#name()} is not set, the field name will be return.
         */
        public static int getSequence(Field field) {
            CsvColumn target = field.getAnnotation(CsvColumn.class);
            if (target == null) {
                throw new IllegalArgumentException("bean is not annotated with @Property");
            }

            return target.sequence();
        }
    }
}

/* */
