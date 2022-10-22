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
import java.util.logging.Logger;

import vavi.beans.BeanUtil;


/**
 * GeneratedValue.
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/11 23:45:45 $ $Author: sano-n $
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratedValue {

    // TODO strategy

    /** */
    class Util {

        /** */
        private static Logger logger = Logger.getLogger(GeneratedValue.class.getName());

        private Util() {
        }

        /**
         * TODO エンティティに対して一つとみなしていいのか？
         * {@link GeneratedValue} がフィールドに無い場合は無視されます。
         */
        public static <I> void setGenerateId(Object bean, I id) {

            Field generatedValueField = null;

            Class<?> clazz = bean.getClass();
            while (clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
//logger.debug("field: " + field.getName());
                    GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
                    if (generatedValue == null) {
//logger.debug("not @GeneratedValue: " + field.getName());
                        continue;
                    } else {
                        generatedValueField = field;
                        break;
                    }
                }
                clazz = clazz.getSuperclass();
            }

            if (generatedValueField == null) {
logger.finer("no @GeneratedValue");
                return;
            }

            BeanUtil.setFieldValue(generatedValueField, bean, id);
logger.fine("set @GeneratedValue: " + id);
        }
    }
}

/* */
