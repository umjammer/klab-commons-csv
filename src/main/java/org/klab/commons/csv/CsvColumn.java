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
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


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
     * CSV のカラム順
     */
    int sequence();

    /** */
    class Util {

        /** */
        private static Log logger = LogFactory.getLog(Util.class);

        /**
         * TODO メソッドにアノテーションされた場合
         * @return only {@link CsvColumn} annotated fields, sorted by {@link CsvColumn#sequence()} 
         * @throws IllegalArgumentException bean is not annotated with {@link CsvEntity}
         */
        public static Set<Field> getFields(Class<?> beanClass) {

            // {@link Column} でアノテートされた {@link Field} のセット
            Set<Field> columnFields = new TreeSet<Field>(new Comparator<Field>() {
                @Override
                public int compare(Field o1, Field o2) {
                    int s1 = o1.getAnnotation(CsvColumn.class).sequence();
                    int s2 = o2.getAnnotation(CsvColumn.class).sequence();
                    return s1 - s2;
                }
            });
            for (Field field : beanClass.getDeclaredFields()) {
logger.debug("field: " + field.getName());
                CsvColumn column = field.getAnnotation(CsvColumn.class);
                if (column == null) {
logger.debug("not @CsvColumn: " + field.getName());
                    continue;
                }
                columnFields.add(field);
            }

            return columnFields;
        }
    }
}

/* */
