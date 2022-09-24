/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.klab.commons.csv.CsvConverter;
import org.klab.commons.csv.CsvDialect;
import org.klab.commons.csv.CsvProvider;
import org.klab.commons.csv.spi.CsvReader;
import org.klab.commons.csv.spi.CsvWriter;


/**
 * SimpleCsvProvider.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-24 nsano initial version <br>
 */
public class DefaultCsvProvider implements CsvProvider {

    private CsvDialect csvDiarect = new org.klab.commons.csv.impl.ExcelCsvDialect();
    /** */

    /* */
    @Override
    public CsvConverter getCsvConverter(Class<?> entityClass) {
        return new org.klab.commons.csv.impl.DefaultCsvConverter(entityClass, csvDiarect);
    }

    /* TODO impl の DI */
    @Override
    public CsvReader getCsvReader(InputStream is, String encoding) throws IOException {
        return new org.klab.commons.csv.rfc4180.Rfc4180CsvReader(is, encoding);
    }

    /* TODO impl の DI */
    @Override
    public CsvWriter getCsvWriter(OutputStream os, String encoding) throws IOException {
        return new org.klab.commons.csv.rfc4180.Rfc4180CsvWriter(os, encoding);
    }
}

/* */
