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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public abstract class AbstractCsvFactory implements CsvFactory {

    private static Log logger = LogFactory.getLog(AbstractCsvFactory.class);

    /** */
    protected ExceptionHandler readExceptionHandler = new DefaultExceptionHandler();

    /** �ݒ肳��Ȃ���� {@link DefaultExceptionHandler} �ɂȂ�܂��B */
    public void setReadExceptionHandler(ExceptionHandler readExceptionHandler) {
        this.readExceptionHandler = readExceptionHandler;
    }

    /** */
    protected ExceptionHandler writeExceptionHandler = new DefaultExceptionHandler();

    /** �ݒ肳��Ȃ���� {@link DefaultExceptionHandler} �ɂȂ�܂��B */
    public void setWriteExceptionHandler(ExceptionHandler writeExceptionHandler) {
        this.writeExceptionHandler = writeExceptionHandler;
    }

    /**
     * �A�E�^�[�N���X�� CsvFactory ���g�p���Ă���B
     * <li> TODO �����̏ꏊ���킩��Ȃ��ACsvConverter ���H
     */
    class DefaultWholeCsvReader implements WholeCsvReader {
        /** */
        private List<?> cache;
        /**
         * {@link #readExceptionHandler} ����������Ă��܂��B
         * �L���b�V�����s���܂��B
         * @param entityClass {@link CsvEntity} �ŃA�m�e�[�V�������ꂽ�N���X
         */
        @SuppressWarnings("unchecked")
        @Override
        public <E> List<E> readAll(Class<E> entityClass) throws IOException {
            CsvProvider csvProvider = org.klab.commons.csv.CsvEntity.Util.getCsvProvider(entityClass);
logger.debug("csvProvider: " + csvProvider.getClass().getName());
            String encoding = org.klab.commons.csv.CsvEntity.Util.getEncoding(entityClass);
logger.debug("encoding: " + encoding);
            boolean cached = org.klab.commons.csv.CsvEntity.Util.isCached(entityClass);
logger.debug("cached: " + cached);

            CsvConverter csvConverter = csvProvider.getCsvConverter(entityClass);
            
            // cache
            if (!cached || cache == null) {
logger.debug("cache off or first read: cache: " + cached);
                this.cache = findAllInternal(csvConverter, csvProvider.getCsvReader(getInputStream(), encoding));
            }
            return (List<E>) cache;
        }
        /**
         * @throws NullPointerException do {@link #setEntityClass(Class)}
         */
        protected List<?> findAllInternal(CsvConverter csvConverter, CsvReader reader) throws IOException {
            List<Object> results = new ArrayList<Object>();

            Integer id = 0;
            while (reader.hasNextLine()) {
                CsvLine csv = null;
                try {
                    csv = reader.nextLine();
if (csv.toString().isEmpty()) {
 logger.warn("line " + id + " is empty, skiped");
} else {
                    Object entity = csvConverter.toEntity(csv);
                    GeneratedValue.Util.setGenerateId(entity, id); // TODO ����Ȃ�ł����̂��H                    
                    results.add(entity);
//logger.debug(ToStringBuilder.reflectionToString(entity));
}
                } catch (Exception e) {
logger.error("read: " + e);
                    readExceptionHandler.handleEachLine(e, id + 1, csv, AbstractCsvFactory.this);
                }
                id++;
            }
            reader.close();

            readExceptionHandler.handleWhenDone(results);

            return results;
        }
    }

    /** */
    protected WholeCsvReader wholeCsvReader = new DefaultWholeCsvReader();

    /** {@link DefaultWholeCsvReader} ��Ԃ��܂��B */
    @Override
    public WholeCsvReader getWholeCsvReader() {
        return wholeCsvReader;
    }

    /**
     * �A�E�^�[�N���X�� CsvFactory ���g�p���Ă���B
     * <li> TODO �����̏ꏊ���킩��Ȃ��ACsvConverter ���H
     */
    class DefaultWholeCsvWriter implements WholeCsvWriter {
        /**
         * {@link #writeExceptionHandler} ����������Ă��܂��B
         * @param entityClass {@link CsvEntity} �ŃA�m�e�[�V�������ꂽ�N���X
         */
        @Override
        public <E> void writeAll(Collection<E> entities, Class<E> entityClass) throws IOException {
            CsvProvider csvProvider = org.klab.commons.csv.CsvEntity.Util.getCsvProvider(entityClass);
logger.debug("csvProvider: " + csvProvider.getClass().getName());
            String encoding = org.klab.commons.csv.CsvEntity.Util.getEncoding(entityClass);
logger.debug("encoding: " + encoding);

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
                }
                id++;
            }
            writer.flush();
            writer.close();

            writeExceptionHandler.handleWhenDone(entities);
        }
    }

    /** */
    protected WholeCsvWriter wholeCsvWriter = new DefaultWholeCsvWriter();

    /** {@link DefaultWholeCsvWriter} ��Ԃ��܂��B */
    @Override
    public WholeCsvWriter getWholeCsvWriter() {
        return wholeCsvWriter;
    }
}

/* */
