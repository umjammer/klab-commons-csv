/*
 * $Id: CsvProvider.java 0 2008/01/24 14:38:23 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import org.klab.commons.csv.CsvConverter;
import org.klab.commons.csv.CsvDialect;
import org.klab.commons.csv.CsvProvider;
import org.klab.commons.csv.rfc4180.Rfc4180CsvReader;
import org.klab.commons.csv.rfc4180.Rfc4180CsvWriter;
import org.klab.commons.csv.spi.CsvLine;
import org.klab.commons.csv.spi.CsvReader;
import org.klab.commons.csv.spi.CsvWriter;


/**
 * CsvProviderBase.
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 */
public abstract class CsvProviderBase implements CsvProvider {

    /** TODO impl ‚Ì DI */
    protected CsvDialect defaultCsvDiarect = new ExcelCsvDialect();

    /** */
    private CsvDialect csvDiarect = new CsvDialect() {
        @Override
        public Object toFieldValue(Field field, Object bean, CsvLine columns) {
            return CsvProviderBase.this.toFieldValue(field, bean, columns);
        }
        @Override
        public String formatString(String column) {
            return defaultCsvDiarect.formatString(column);
        }
        @Override
        public String getEndOfLine() {
            return defaultCsvDiarect.getEndOfLine();
        }
        @Override
        public CsvLine toCsvLine(Field field, Object bean, Object value) {
            return defaultCsvDiarect.toCsvLine(field, bean, value);
        }
    };

    /* */
    @Override
    public CsvConverter getCsvConverter(Class<?> entityClass) {
        return new DefaultCsvConverter(entityClass, csvDiarect);
    }

    /** implement dialectal */
    protected abstract Object toFieldValue(Field field, Object bean, CsvLine columns);

    /* TODO impl ‚Ì DI */
    @Override
    public CsvReader getCsvReader(InputStream is, String encoding) throws IOException {
        return new Rfc4180CsvReader(is, encoding);
    }

    /* TODO impl ‚Ì DI */
    @Override
    public CsvWriter getCsvWriter(OutputStream os, String encoding) throws IOException {
        return new Rfc4180CsvWriter(os, encoding);
    }
}

/* */
