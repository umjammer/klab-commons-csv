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
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * CSV の IO を定義する型です。
 * <p>
 * JPA の EntityManager みたいなもの。
 * CsvDataSource とかの方が分かりやすいかも？
 * </p>
 * (original)
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 070207 sano-n initial version <br>
 */
public interface CsvFactory<T> {

    /** */
    void setSource(T source);

    /** CSV を実際に読み込むストリーム */
    InputStream getInputStream() throws IOException;

    /** CSV を実際に書き出すストリーム */
    OutputStream getOutputStream() throws IOException;

    /**
     * {@link WholeCsvReader#readAll(Class)} と {@link WholeCsvWriter#writeAll(Collection, Class)}
     * 内で一行ごとに起こるエラーハンドリングと全体が終了した場合のエラーハンドリングを定義する
     * 型です。
     * <p>
     * 使用例としては {@link ExceptionHandler#handleEachLine(Exception, int, Object, CsvFactory)}
     * で行単位で起こった例外を溜めておいて、{@link ExceptionHandler#handleWhenDone(Collection)}
     * で例外をまとめて発生させるとか。
     * </p>
     */
    interface ExceptionHandler {
        /** TODO 引数考える */
        void handleEachLine(Exception e, int lineNumber, Object line, CsvFactory<?> csvFactory);
        /** */
        void handleWhenDone(Collection<Exception> exceptions);
    }

    /** 一行ごとにログするだけ */
    class DefaultExceptionHandler implements ExceptionHandler {
        private static Logger logger = Logger.getLogger(DefaultExceptionHandler.class.getName());
        @Override
        public void handleEachLine(Exception e, int lineNumber, Object line, CsvFactory<?> csvFactory) {
e.printStackTrace(System.err);
            logger.log(Level.SEVERE, "csv: line " + lineNumber + ": " + csvFactory, e.getCause());
        }
        @Override
        public void handleWhenDone(Collection<Exception> exceptions) {
            if (exceptions.size() > 0) {
                throw (RuntimeException) new IllegalStateException("There are some exceptions.").initCause(new Exception("exceptions") {{
                    exceptions.forEach(this::addSuppressed);
                }});
            }
        }
    }

    /** CSV 全体を読み込むクラスの型です。 */
    interface WholeCsvReader {
        /**
         * @return CSV から読み込んだオブジェクトのリスト
         */
        <T> List<T> readAll(Class<T> entityClass) throws IOException;
    }

    /** */
    WholeCsvReader getWholeCsvReader();

    /** CSV 全体を書き出すクラスの型です。 */
    interface WholeCsvWriter {
        /**
         * @param entities CSV に書き出すオブジェクトのコレクション
         */
        <T> void writeAll(Collection<T> entities, Class<T> entityClass) throws IOException;
    }

    /** */
    WholeCsvWriter getWholeCsvWriter();
}

/* */
