/*
 * $Id: CsvProvider.java 0 2008/01/24 14:38:23 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
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
 * DefaultCsvProvider.
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 */
public class DefaultCsvProvider implements CsvProvider {

    /** TODO impl の DI */
    private CsvDialect csvDiarect = new org.klab.commons.csv.impl.ExcelCsvDialect();

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
