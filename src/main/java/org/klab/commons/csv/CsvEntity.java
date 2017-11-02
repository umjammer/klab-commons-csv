/*
 * Copyright (c) 2008 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import vavi.net.www.protocol.URLStreamHandlerUtil;


/**
 * CsvEntity.
 * <p>
 * TODO make type {@link #url()} and {@link CsvFactory}'s generics type of {@link #io()} generics.
 * </p>
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/11 23:45:45 $ $Author: sano-n $
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvEntity {

    /** default {@link org.klab.commons.csv.impl.DefaultCsvProvider} */
    Class<? extends CsvProvider> provider() default org.klab.commons.csv.impl.DefaultCsvProvider.class;

    /** If you don't set, use <code>System.egtProperty("file.encoding")</code> */
    String encoding() default "";

    /** */
    boolean cached() default false;

    /** Specifies resource URL */
    String url() default "";

    /** Specifies I/O handler, default {@link org.klab.commons.csv.impl.URLCsvFactory} */
    Class<? extends CsvFactory<String>> io() default org.klab.commons.csv.impl.URLCsvFactory.class;

    /** */
    class Util {

        /** */
        private static Log logger = LogFactory.getLog(Util.class);

        private Util() {
        }

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
            String encoding = entity.encoding();
            if (encoding.isEmpty()) {
                encoding = System.getProperty("file.encoding");
System.err.println("use default encoding: " + encoding);
            }

            return encoding;
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
                throw new IllegalStateException(e);
            }
        }

        /** 
         * @throws IllegalArgumentException bean is not annotated with {@link CsvEntity}
         */
        public static String getUrl(Class<?> beanClass) {
            //
            CsvEntity entity = beanClass.getAnnotation(CsvEntity.class);
            if (entity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }

            String url = entity.url();
            if (url.isEmpty()) {
                throw new IllegalArgumentException("url cannot be null or empty");
            }

            return url;
        }

        /**
         * @return annotated {@link CsvFactory}
         * @throws IllegalArgumentException bean is not annotated with {@link CsvEntity}
         */
        public static CsvFactory<String> getCsvFactory(Class<?> beanClass) {
            //
            CsvEntity entity = beanClass.getAnnotation(CsvEntity.class);
            if (entity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }
//logger.debug("provider: " + entity.provider());
            //
            try {
                return entity.io().newInstance();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        /**
         * TODO メソッドにアノテーションされた場合
         * @return only {@link CsvColumn} annotated fields, sorted by {@link CsvColumn#sequence()} 
         * @throws IllegalArgumentException bean is not annotated with {@link CsvEntity}
         */
        public static List<Field> getFields(Class<?> beanClass) {
            //
            CsvEntity entity = beanClass.getAnnotation(CsvEntity.class);
            if (entity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }

            // {@link Column} でアノテートされた {@link Field} のセット
            List<Field> columnFields = new ArrayList<>();
            for (Field field : beanClass.getDeclaredFields()) {
                CsvColumn column = field.getAnnotation(CsvColumn.class);
                if (column == null) {
logger.debug("not @CsvColumn: " + field.getName());
                    continue;
                }
logger.debug("field[" + column.sequence() + "]: " + field.getName());
                columnFields.add(field);
            }

            Collections.sort(columnFields, new Comparator<Field>() {
                @Override
                public int compare(Field o1, Field o2) {
                    int s1 = CsvColumn.Util.getSequence(o1);
                    int s2 = CsvColumn.Util.getSequence(o2);
                    return s1 - s2;
                }
            });

            return columnFields;
        }

        /** replacing key pattern */
        private static final Pattern pattern = Pattern.compile("\\$\\{[\\w\\.]+\\}"); 

        /**
         * Replaces <code>${Foo}</code> with <code>System.getProperty("Foo")</code> or <code>System.getenv("Foo")</code>.
         */
        private static String replaceWithEnvOrProps(String url) {
            Matcher matcher = pattern.matcher(url);
            while (matcher.find()) {
               String key = matcher.group();
               String name = key.substring(2, key.length() - 1);
               String value = System.getenv(name);
               if (value == null) {
                   value = System.getProperty(name);
                   if (value == null) {
                       System.err.println(key + " is not replaceable");
                       continue;
                   }
               }
               url = url.replace(key, value);
            }
            return url;
        }

        /* */
        static {
            URLStreamHandlerUtil.loadService();
        }

        /**
         * Entry point for reading.
         *
         * @param args replace <code>"{#}"</code> (# is 0, 1, 2 ...)
         * <pre>
         * $ cat some.properties
         * foo.bar.buz=xxx
         * foo.bar.aaa=yyy
         *
         * @Property(name = "foo.bar.{0})
         * Foo bar;
         *
         *    :
         *
         * PropsEntity.Util.bind(bean, "buz");
         * assertEquals(bean.bar, "xxx");
         *
         * </pre>
         */
        public static <T> List<T> read(Class<T> type, String... args) throws IOException {
            //
            CsvEntity csvEntity = type.getAnnotation(CsvEntity.class);
            if (csvEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }

            CsvFactory<String> csvFactory = getCsvFactory(type);
            csvFactory.setSource(replaceWithEnvOrProps(getUrl(type)));
            return csvFactory.getWholeCsvReader().readAll(type);
        }

        /**
         * Entry point for writing.
         */
        public static <T> void write(List<T> list, Class<T> type) throws IOException {
            //
            CsvEntity csvEntity = type.getAnnotation(CsvEntity.class);
            if (csvEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }

            CsvFactory<String> csvFactory = getCsvFactory(type);
            csvFactory.setSource(replaceWithEnvOrProps(getUrl(type)));
            csvFactory.getWholeCsvWriter().writeAll(list, type);
        }
    }
}

/* */
