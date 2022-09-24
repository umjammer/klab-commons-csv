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
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vavi.net.www.protocol.URLStreamHandlerUtil;


/**
 * CsvEntity.
 * <p>
 * TODO make type {@link #url()} and {@link CsvDataSource}'s generics type of {@link #dataSource()} generics.
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

    /** Specifies I/O handler, default {@link URLCsvDataSource} */
    Class<? extends CsvDataSource> dataSource() default org.klab.commons.csv.impl.URLCsvDataSource.class;

    /** */
    class Util {

        /** */
        private static final Logger logger = Logger.getLogger(Util.class.getName());

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
logger.fine("use default encoding: " + encoding);
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
logger.finer("provider: " + entity.provider());
            //
            try {
                return entity.provider().getDeclaredConstructor().newInstance();
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
         * @return annotated {@link CsvDataSource}
         * @throws IllegalArgumentException bean is not annotated with {@link CsvEntity}
         */
        @SuppressWarnings("unchecked")
        public static <T> CsvDataSource<Object, T> getCsvDataSource(Class<T> beanClass) {
            //
            CsvEntity entity = beanClass.getAnnotation(CsvEntity.class);
            if (entity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }
logger.finer("provider: " + entity.provider());
            //
            try {
                return (CsvDataSource<Object, T>) entity.dataSource().getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        /**
         * TODO annotation for method
         * @return only {@link CsvColumn} annotated fields, sorted by {@link CsvColumn#sequence()}
         * @throws IllegalArgumentException bean is not annotated with {@link CsvEntity}
         */
        public static List<Field> getFields(Class<?> beanClass) {
            //
            CsvEntity entity = beanClass.getAnnotation(CsvEntity.class);
            if (entity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }

            // {@link Field} set annotated {@link Column}
            List<Field> columnFields = new ArrayList<>();

            Class<?> clazz = beanClass;
            while (clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    CsvColumn column = field.getAnnotation(CsvColumn.class);
                    if (column == null) {
logger.fine("not @CsvColumn: " + field.getName());
                        continue;
                    }
logger.fine("field[" + column.sequence() + "]: " + field.getName());
                    columnFields.add(field);
                }
                clazz = clazz.getSuperclass();
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
logger.info(key + " is not replaceable");
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

            csvFactory.setSource(replaceWithEnvOrProps(getUrl(type)));
            CsvDataSource<Object, T> csvDataSource = getCsvDataSource(type);
            return csvDataSource.getWholeCsvReader().readAll(type);
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

            csvFactory.setSource(replaceWithEnvOrProps(getUrl(type)));
            CsvDataSource<Object, T> csvDataSource = getCsvDataSource(type);
            csvDataSource.getWholeCsvWriter().writeAll(list, type);
        }
    }
}

/* */
