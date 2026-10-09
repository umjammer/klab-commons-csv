/*
 * Copyright (c) 2007 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Collection;
import java.util.List;

import static java.lang.System.getLogger;


/**
 * This is the type that defines the I/O for CSV.
 * <p>
 * It's similar to JPA's EntityManager.
 * </p>
 * (original)
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 070207 sano-n initial version <br>
 */
public interface CsvDataSource<S, T> {

    /** */
    void setSource(S source);

    /** The stream that actually reads the CSV file. */
    InputStream getInputStream() throws IOException;

    /** The stream that actually writes the CSV file. */
    OutputStream getOutputStream() throws IOException;

    /**
     * This type defines error handling for each line and for when the entire process finishes,
     * within {@link WholeCsvReader#readAll(Class)} and {@link WholeCsvWriter#writeAll(Collection, Class)}.
     * <p>
     * One example of its use is to collect exceptions that occur line by line
     * using {@link ExceptionHandler#handleEachLine(Exception, int, Object, CsvDataSource)} and then
     * raise all the exceptions at once using {@link ExceptionHandler#handleWhenDone(Collection)}.
     * </p>
     */
    interface ExceptionHandler {
        /** TODO Consider the arguments */
        void handleEachLine(Exception e, int lineNumber, Object line, CsvDataSource<?, ?> csvDataSource);
        /** */
        void handleWhenDone(Collection<Exception> exceptions);
    }

    /** Just log each line. */
    class DefaultExceptionHandler implements ExceptionHandler {
        private static final Logger logger = getLogger(DefaultExceptionHandler.class.getName());
        @Override
        public void handleEachLine(Exception e, int lineNumber, Object line, CsvDataSource<?, ?> csvDataSource) {
e.printStackTrace(System.err);
            logger.log(Level.ERROR, "csv: line " + lineNumber + ": " + csvDataSource, e.getCause());
        }
        @Override
        public void handleWhenDone(Collection<Exception> exceptions) {
            if (!exceptions.isEmpty()) {
                throw new IllegalStateException("There are some exceptions.", new Exception("exceptions") {{
                    exceptions.forEach(this::addSuppressed);
                }});
            }
        }
    }

    /** This is the type of class that reads the entire CSV file. */
    interface WholeCsvReader<T> {
        /**
         * @return List of objects read from CSV
         */
        List<T> readAll(Class<T> entityClass) throws IOException;
    }

    /** */
    WholeCsvReader<T> getWholeCsvReader();

    /** This is the type of class that writes the entire CSV file. */
    interface WholeCsvWriter<T> {
        /**
         * @param entities A collection of objects to write to a CSV file.
         */
        void writeAll(Collection<T> entities, Class<T> entityClass) throws IOException;
    }

    /** */
    WholeCsvWriter<T> getWholeCsvWriter();
}
