/*
 * Copyright (c) 2007 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.klab.commons.csv.CsvConverter;
import org.klab.commons.csv.CsvDataSource;
import org.klab.commons.csv.spi.CsvProvider;
import org.klab.commons.csv.GeneratedValue;
import org.klab.commons.csv.spi.CsvLine;
import org.klab.commons.csv.spi.CsvReader;
import org.klab.commons.csv.spi.CsvWriter;


/**
 * AbstractCsvFactory.
 * <li> (original)
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 070207 sano-n initial version <br>
 */
public abstract class AbstractCsvFactory<S, T> implements CsvDataSource<S, T> {

    /** */
    protected S source;

    /** */
    public void setSource(S source) {
        this.source = source;
logger.fine("csv source: " + source);
    }

    /** */
    private static Logger logger = Logger.getLogger(AbstractCsvFactory.class.getName());

    /** */
    protected ExceptionHandler readExceptionHandler = new DefaultExceptionHandler();

    /** if not set, {@link CsvDataSource.DefaultExceptionHandler} is used as default */
    public void setReadExceptionHandler(ExceptionHandler readExceptionHandler) {
        this.readExceptionHandler = readExceptionHandler;
    }

    /** */
    protected ExceptionHandler writeExceptionHandler = new DefaultExceptionHandler();

    /** if not set, {@link CsvDataSource.DefaultExceptionHandler} is used as default */
    public void setWriteExceptionHandler(ExceptionHandler writeExceptionHandler) {
        this.writeExceptionHandler = writeExceptionHandler;
    }

    /**
     * <li> TODO where is this class appropriate location, CsvConverter?
     */
    class DefaultWholeCsvReader implements WholeCsvReader<T> {
        /** */
        private List<T> cache;
        /**
         * implements {@link #readExceptionHandler}.
         * do cache also.
         * @param entityClass a class annotated by {@link org.klab.commons.csv.CsvEntity}
         */
        @Override
        public List<T> readAll(Class<T> entityClass) throws IOException {
            CsvProvider<T> csvProvider = org.klab.commons.csv.CsvEntity.Util.getCsvProvider(entityClass);
logger.fine("csvProvider: " + csvProvider.getClass().getName());
            String encoding = org.klab.commons.csv.CsvEntity.Util.getEncoding(entityClass);
logger.fine("encoding: " + encoding);
            String delimiter = org.klab.commons.csv.CsvEntity.Util.getDelimiter(entityClass);
logger.fine("delimiter: " + delimiter);
            Character commentMarker = org.klab.commons.csv.CsvEntity.Util.getCommentMarker(entityClass);
logger.fine("commentMarker: " + commentMarker);
            boolean hasTitle = org.klab.commons.csv.CsvEntity.Util.hasTitle(entityClass);
logger.fine("hasTitle: " + hasTitle);
            boolean cached = org.klab.commons.csv.CsvEntity.Util.isCached(entityClass);
logger.fine("cached: " + cached);

            CsvConverter<T> csvConverter = csvProvider.newCsvConverter(entityClass);

            // cache
            if (!cached || cache == null) {
logger.fine("cache off or first read: cache: " + cached);
                this.cache = findAllInternal(csvConverter, csvProvider.newCsvReader(getInputStream(), encoding, delimiter, hasTitle, commentMarker));
            }
            return cache;
        }
        /**
         * Reads each csv line.
         */
        protected List<T> findAllInternal(CsvConverter<T> csvConverter, CsvReader reader) throws IOException {
            List<T> results = new ArrayList<>();
            List<Exception> exceptions = new ArrayList<>();

            int id = 0;
            while (reader.hasNextLine()) {
                CsvLine csv = null;
                try {
                    csv = reader.nextLine();
if (csv.toString().isEmpty()) {
 logger.warning("line " + id + " is empty, skiped");
} else {
                    T entity = csvConverter.toEntity(csv);
                    GeneratedValue.Util.setGenerateId(entity, id); // TODO こんなんでいいのか？
                    results.add(entity);
//logger.debug(ToStringBuilder.reflectionToString(entity));
}
                } catch (Exception e) {
                    readExceptionHandler.handleEachLine(e, id + 1, csv, AbstractCsvFactory.this);
                    exceptions.add(e);
                }
                id++;
            }
            reader.close();

            readExceptionHandler.handleWhenDone(exceptions);

            return results;
        }
    }

    /** */
    protected WholeCsvReader<T> wholeCsvReader = new DefaultWholeCsvReader();

    /** Returns {@link DefaultWholeCsvReader} */
    @Override
    public WholeCsvReader<T> getWholeCsvReader() {
        return wholeCsvReader;
    }

    /**
     * using outer class's CsvDataSource
     * <li> TODO where is this class appropriate location, CsvConverter?
     */
    class DefaultWholeCsvWriter implements WholeCsvWriter<T> {
        /**
         * implements {@link #writeExceptionHandler}
         * @param entityClass a class annotated by {@link org.klab.commons.csv.CsvEntity}
         */
        @Override
        public void writeAll(Collection<T> entities, Class<T> entityClass) throws IOException {
            List<Exception> exceptions = new ArrayList<>();

            CsvProvider<T> csvProvider = org.klab.commons.csv.CsvEntity.Util.getCsvProvider(entityClass);
logger.fine("csvProvider: " + csvProvider.getClass().getName());
            String encoding = org.klab.commons.csv.CsvEntity.Util.getEncoding(entityClass);
logger.fine("encoding: " + encoding);

            CsvConverter<T> csvConverter = csvProvider.newCsvConverter(entityClass);

            //
            CsvWriter writer = csvProvider.newCsvWriter(getOutputStream(), encoding); // TODO use factory pattern
            int id = 0;
            for (T entity : entities) {
                try {
                    CsvLine csv = csvConverter.toCsv(entity);
                    writer.writeLine(csv);
                } catch (Exception e) {
                    writeExceptionHandler.handleEachLine(e, id + 1, entity, AbstractCsvFactory.this);
                    exceptions.add(e);
                }
                id++;
            }
            writer.flush();
            writer.close();

            writeExceptionHandler.handleWhenDone(exceptions);
        }
    }

    /** */
    protected WholeCsvWriter<T> wholeCsvWriter = new DefaultWholeCsvWriter();

    /** Returns {@link DefaultWholeCsvWriter} */
    @Override
    public WholeCsvWriter<T> getWholeCsvWriter() {
        return wholeCsvWriter;
    }
}

/* */
