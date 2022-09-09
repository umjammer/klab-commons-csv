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
import org.klab.commons.csv.CsvFactory;
import org.klab.commons.csv.CsvProvider;
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
public abstract class AbstractCsvFactory<T> implements CsvFactory<T> {

    /** */
    protected T source;

    /** */
    public void setSource(T source) {
        this.source = source;
logger.fine("csv source: " + source);
    }

    /** */
    private static Logger logger = Logger.getLogger(AbstractCsvFactory.class.getName());

    /** */
    protected ExceptionHandler readExceptionHandler = new DefaultExceptionHandler();

    /** 設定されなければ {@link org.klab.commons.csv.CsvFactory.DefaultExceptionHandler} になります。 */
    public void setReadExceptionHandler(ExceptionHandler readExceptionHandler) {
        this.readExceptionHandler = readExceptionHandler;
    }

    /** */
    protected ExceptionHandler writeExceptionHandler = new DefaultExceptionHandler();

    /** 設定されなければ {@link org.klab.commons.csv.CsvFactory.DefaultExceptionHandler} になります。 */
    public void setWriteExceptionHandler(ExceptionHandler writeExceptionHandler) {
        this.writeExceptionHandler = writeExceptionHandler;
    }

    /**
     * <li> TODO こいつの場所がわからない、CsvConverter か？
     */
    class DefaultWholeCsvReader implements WholeCsvReader {
        /** */
        private List<?> cache;
        /**
         * {@link #readExceptionHandler} が実装されています。
         * キャッシュを行います。
         * @param entityClass {@link org.klab.commons.csv.CsvEntity} でアノテーションされたクラス
         */
        @SuppressWarnings("unchecked")
        @Override
        public <E> List<E> readAll(Class<E> entityClass) throws IOException {
            CsvProvider csvProvider = org.klab.commons.csv.CsvEntity.Util.getCsvProvider(entityClass);
logger.fine("csvProvider: " + csvProvider.getClass().getName());
            String encoding = org.klab.commons.csv.CsvEntity.Util.getEncoding(entityClass);
logger.fine("encoding: " + encoding);
            boolean cached = org.klab.commons.csv.CsvEntity.Util.isCached(entityClass);
logger.fine("cached: " + cached);

            CsvConverter csvConverter = csvProvider.getCsvConverter(entityClass);

            // cache
            if (!cached || cache == null) {
logger.fine("cache off or first read: cache: " + cached);
                this.cache = findAllInternal(csvConverter, csvProvider.getCsvReader(getInputStream(), encoding));
            }
            return (List<E>) cache;
        }
        /**
         * Reads each csv line.
         */
        protected List<?> findAllInternal(CsvConverter csvConverter, CsvReader reader) throws IOException {
            List<Object> results = new ArrayList<>();
            List<Exception> exceptions = new ArrayList<>();

            Integer id = 0;
            while (reader.hasNextLine()) {
                CsvLine csv = null;
                try {
                    csv = reader.nextLine();
if (csv.toString().isEmpty()) {
 logger.warning("line " + id + " is empty, skiped");
} else {
                    Object entity = csvConverter.toEntity(csv);
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
    protected WholeCsvReader wholeCsvReader = new DefaultWholeCsvReader();

    /** {@link DefaultWholeCsvReader} を返します。 */
    @Override
    public WholeCsvReader getWholeCsvReader() {
        return wholeCsvReader;
    }

    /**
     * アウタークラスの CsvFactory を使用している。
     * <li> TODO こいつの場所がわからない、CsvConverter か？
     */
    class DefaultWholeCsvWriter implements WholeCsvWriter {
        /**
         * {@link #writeExceptionHandler} が実装されています。
         * @param entityClass {@link org.klab.commons.csv.CsvEntity} でアノテーションされたクラス
         */
        @Override
        public <E> void writeAll(Collection<E> entities, Class<E> entityClass) throws IOException {
            List<Exception> exceptions = new ArrayList<>();

            CsvProvider csvProvider = org.klab.commons.csv.CsvEntity.Util.getCsvProvider(entityClass);
logger.fine("csvProvider: " + csvProvider.getClass().getName());
            String encoding = org.klab.commons.csv.CsvEntity.Util.getEncoding(entityClass);
logger.fine("encoding: " + encoding);

            CsvConverter csvConverter = csvProvider.getCsvConverter(entityClass);

            //
            CsvWriter writer = csvProvider.getCsvWriter(getOutputStream(), encoding); // TODO use factory pattern
            int id = 0;
            for (Object entity : entities) {
                try {
                    String csv = csvConverter.toCsv(entity);
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
    protected WholeCsvWriter wholeCsvWriter = new DefaultWholeCsvWriter();

    /** {@link DefaultWholeCsvWriter} を返します。 */
    @Override
    public WholeCsvWriter getWholeCsvWriter() {
        return wholeCsvWriter;
    }
}

/* */
