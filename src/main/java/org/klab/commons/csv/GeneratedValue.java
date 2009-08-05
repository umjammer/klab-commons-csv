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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import vavi.beans.BeanUtil;


/**
 * GeneratedValue. 
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (nsano)
 * @version $Revision: 1.0 $ $Date: 2008/01/11 23:45:45 $ $Author: sano-n $
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratedValue {

    // TODO strategy

    /** */
    class Util {

        /** */
        private static Log logger = LogFactory.getLog(GeneratedValue.class);

        /**
         * TODO エンティティに対して一つとみなしていいのか？
         * {@link GeneratedValue} がフィールドに無い場合は無視されます。
         */
        public static <I> void setGenerateId(Object bean, I id) {

            Field generatedValueField = null;
            for (Field field : bean.getClass().getDeclaredFields()) {
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

            if (generatedValueField == null) {
logger.debug("no @GeneratedValue");
                return;
            }

            BeanUtil.setFieldValue(generatedValueField, bean, id);
logger.debug("set @GeneratedValue: " + id);
        }
    }
}

/* */
