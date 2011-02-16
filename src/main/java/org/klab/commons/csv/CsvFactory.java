/*
 * Copyright (c) 2007 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * CSV �� IO ���`����^�ł��B
 * <p>
 * JPA �� EntityManager �݂����Ȃ��́B
 * CsvDataSource �Ƃ��̕���������₷�������H
 * </p>
 * (original)
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 070207 sano-n initial version <br>
 */
public interface CsvFactory {

    /** CSV �����ۂɓǂݍ��ރX�g���[�� */
    InputStream getInputStream() throws IOException;

    /** CSV �����ۂɏ����o���X�g���[�� */
    OutputStream getOutputStream() throws IOException;

    /**
     * {@link WholeCsvReader#readAll(Class)} �� {@link WholeCsvWriter#writeAll(Collection, Class)}
     * ���ň�s���ƂɋN����G���[�n���h�����O�ƑS�̂��I�������ꍇ�̃G���[�n���h�����O���`����
     * �^�ł��B
     * <p>
     * �g�p��Ƃ��Ă� {@link ExceptionHandler#handleEachLine(Exception, int, Object, CsvFactory)}
     * �ōs�P�ʂŋN��������O�𗭂߂Ă����āA{@link ExceptionHandler#handleWhenDone(Collection)}
     * �ŗ�O���܂Ƃ߂Ĕ���������Ƃ��B
     * </p>
     */
    interface ExceptionHandler {
        /** TODO �����l���� */
        void handleEachLine(Exception e, int lineNumber, Object line, CsvFactory csvFactory);
        /** */
        void handleWhenDone(Collection<?> entities);
    }

    /** ��s���ƂɃ��O���邾�� */
    class DefaultExceptionHandler implements ExceptionHandler {
        private static Log logger = LogFactory.getLog(DefaultExceptionHandler.class);
        @Override
        public void handleEachLine(Exception e, int lineNumber, Object line, CsvFactory csvFactory) {
logger.error("csv: line " + lineNumber + ": " + csvFactory, e);
        }
        @Override
        public void handleWhenDone(Collection<?> entities) {
        }
    }

    /** CSV �S�̂�ǂݍ��ރN���X�̌^�ł��B */
    interface WholeCsvReader {
        /**
         * @return CSV ����ǂݍ��񂾃I�u�W�F�N�g�̃��X�g 
         */
        <T> List<T> readAll(Class<T> entityClass) throws IOException;
    }

    /** */
    WholeCsvReader getWholeCsvReader();

    /** CSV �S�̂������o���N���X�̌^�ł��B */
    interface WholeCsvWriter {
        /**
         * @param CSV �ɏ����o���I�u�W�F�N�g�̃R���N�V���� 
         */
        <T> void writeAll(Collection<T> entities, Class<T> entityClass) throws IOException;
    }

    /** */
    WholeCsvWriter getWholeCsvWriter();
}

/* */
