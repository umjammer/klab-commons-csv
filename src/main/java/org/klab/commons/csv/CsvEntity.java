/*
 * Copyright (c) 2008 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Source;

import org.klab.commons.csv.impl.IOStreamCsvDataSource;
import org.klab.commons.csv.impl.URLCsvDataSource;
import org.klab.commons.csv.spi.CsvProvider;
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

    /** default {@link org.klab.commons.csv.apache.ApacheCsvProvider} */
    String provider() default "";

    /** If you don't set, use <code>System.egtProperty("file.encoding")</code> */
    String encoding() default "";

    /** */
    boolean cached() default false;

    /** */
    boolean hasTitle() default false;

    /** */
    String delimiter() default "";

    /** only 1st byte is effective */
    String commentMarker() default "";

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
        public static boolean hasTitle(Class<?> beanClass) {
            //
            CsvEntity entity = beanClass.getAnnotation(CsvEntity.class);
            if (entity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }

            //
            return entity.hasTitle();
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
         * @throws IllegalArgumentException bean is not annotated with {@link CsvEntity}
         */
        public static String getDelimiter(Class<?> beanClass) {
            //
            CsvEntity entity = beanClass.getAnnotation(CsvEntity.class);
            if (entity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }

            //
            String delimiter = entity.delimiter();
            if (delimiter.isEmpty()) {
                delimiter = null;
            }

            return delimiter;
        }

        /**
         * @throws IllegalArgumentException bean is not annotated with {@link CsvEntity}
         */
        public static Character getCommentMarker(Class<?> beanClass) {
            //
            CsvEntity entity = beanClass.getAnnotation(CsvEntity.class);
            if (entity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }

            //
            String commentMarker = entity.commentMarker();

            return !commentMarker.isEmpty() ? commentMarker.charAt(0) : null;
        }

        /**
         * @return annotated {@link CsvProvider}
         * @throws IllegalArgumentException bean is not annotated with {@link CsvEntity}
         */
        @SuppressWarnings("unchecked")
        public static <T> CsvProvider<T> getCsvProvider(Class<T> beanClass) {
            //
            CsvEntity entity = beanClass.getAnnotation(CsvEntity.class);
            if (entity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }
logger.finer("provider: " + entity.provider());
            //
            try {
                ServiceLoader<CsvProvider> serviceLoader = ServiceLoader.load(CsvProvider.class);
                for (CsvProvider<T> provider : serviceLoader) {
                    if (provider.getClass().getName().equals(entity.provider())) {
                        return provider;
                    }
                }
                return serviceLoader.iterator().next();
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

            columnFields.sort((o1, o2) -> {
                int s1 = CsvColumn.Util.getSequence(o1);
                int s2 = CsvColumn.Util.getSequence(o2);
                return s1 - s2;
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

        private static String replaceWithArgs(String url, String... args) throws IOException {
            int c = 0;
            for (String arg : args) {
                url = url.replace("{" + c + "}", URLEncoder.encode(arg, "utf-8"));
//System.err.println(url + ", " + arg);
                c++;
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
         * @param args replace url, inside string literal marker <code>"{#}"</code> (# is 0, 1, 2 ...)
         * <pre>
         * @CsvEntity(url = "https::/www.example.org/file{0}.txt")
         * class Foo {
         *
         *    :
         *
         * ... = CsvEntity.Util.bind(Foo.class, "1"); // retrieves from .../file1.txt
         *
         * </pre>
         * ${env.name}, ${system.property.name} will be replaced also.
         */
        public static <T> List<T> read(Class<T> type, String... args) throws IOException {
            //
            CsvEntity csvEntity = type.getAnnotation(CsvEntity.class);
            if (csvEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }

            CsvDataSource<Object, T> csvDataSource = getCsvDataSource(type);
            csvDataSource.setSource(replaceWithArgs(replaceWithEnvOrProps(getUrl(type)), args));
            return csvDataSource.getWholeCsvReader().readAll(type);
        }

        /** */
        public static <T> List<T> read(Class<T> type, URL url) throws IOException {
            //
            CsvEntity csvEntity = type.getAnnotation(CsvEntity.class);
            if (csvEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }

            CsvDataSource<String, T> csvDataSource = new URLCsvDataSource<>();
            csvDataSource.setSource(url.toString());
            return csvDataSource.getWholeCsvReader().readAll(type);
        }

        /** */
        public static <T> List<T> read(Class<T> type, InputStream is) throws IOException {
            //
            CsvEntity csvEntity = type.getAnnotation(CsvEntity.class);
            if (csvEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }

            CsvDataSource<IOStreamCsvDataSource.IO, T> csvDataSource = new IOStreamCsvDataSource<>();
            csvDataSource.setSource(new IOStreamCsvDataSource.IO(is));
            return csvDataSource.getWholeCsvReader().readAll(type);
        }

        /**
         * Entry point for writing.
         *
         * @param args see {@link #read}
         */
        public static <T> void write(List<T> list, Class<T> type, String... args) throws IOException {
            //
            CsvEntity csvEntity = type.getAnnotation(CsvEntity.class);
            if (csvEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @CsvEntity");
            }

            CsvDataSource<Object, T> csvDataSource = getCsvDataSource(type);
            csvDataSource.setSource(replaceWithArgs(replaceWithEnvOrProps(getUrl(type)), args));
            csvDataSource.getWholeCsvWriter().writeAll(list, type);
        }
    }
}

/* */
