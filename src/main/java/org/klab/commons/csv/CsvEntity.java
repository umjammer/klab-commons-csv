/*
 * Copyright (c) 2008 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.klab.commons.csv.impl.DefaultCsvProvider;


/**
 * CsvEntity. 
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (nsano)
 * @version $Revision: 1.0 $ $Date: 2008/01/11 23:45:45 $ $Author: sano-n $
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvEntity {

    /** */
    Class<? extends CsvProvider> provider() default DefaultCsvProvider.class;

    /** */
    String encoding() default "Windows-31J";

    /** */
    boolean cached() default false;

    /** */
    class Util {

        /**
         * @throws IllegalArgumentException bean is not annotated with {@link CsvEntity}
         */
        public static boolean isCached(Class<?> beanClass) {
            //
            CsvEntity entity = beanClass.getAnnotation(CsvEntity.class);
            if (entity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }

            //
            return entity.cached();
        }

        /** 
         * @throws IllegalArgumentException bean is not annotated with {@link CsvEntity}
         */
        public static String getEncoding(Class<?> beanClass) {
            //
            CsvEntity entity = beanClass.getAnnotation(CsvEntity.class);
            if (entity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }

            //
            return entity.encoding();
        }

        /**
         * @return annotated {@link CsvProvider}
         * @throws IllegalArgumentException bean is not annotated with {@link CsvEntity}
         */
        public static CsvProvider getCsvProvider(Class<?> beanClass) {
            //
            CsvEntity entity = beanClass.getAnnotation(CsvEntity.class);
            if (entity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }
//logger.debug("provider: " + entity.provider());
            //
            try {
                return entity.provider().newInstance();
            } catch (Exception e) {
                throw (RuntimeException) new IllegalStateException().initCause(e);
            }
        }
    }
}

/* */
