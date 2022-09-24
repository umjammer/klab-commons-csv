/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.apache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.klab.commons.csv.CsvConverter;
import org.klab.commons.csv.CsvDialect;
import org.klab.commons.csv.spi.CsvLine;
import org.klab.commons.csv.spi.CsvProvider;
import org.klab.commons.csv.spi.CsvReader;
import org.klab.commons.csv.spi.CsvWriter;


/**
 * ApacheCsvProvider.
 *
 * @author <a href="mailto:umjammerngmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-24 nsano initial version <br>
 */
public class ApacheCsvProvider<T> implements CsvProvider<T> {

    /** */
    private final CsvDialect csvDialect = new org.klab.commons.csv.impl.ExcelCsvDialect();

    @Override
    public CsvDialect getCsvDialect() {
        return csvDialect;
    }

    @Override
    public CsvConverter<T> newCsvConverter(Class<T> entityClass) {
        return new org.klab.commons.csv.impl.DefaultCsvConverter<>(entityClass, this);
    }

    @Override
    public CsvReader newCsvReader(InputStream is, String encoding, String delimiter, boolean hasTitle, Character commentMarker) throws IOException {
        return new ApacheCsvReader(is, encoding, delimiter, hasTitle, commentMarker);
    }

    @Override
    public CsvWriter newCsvWriter(OutputStream os, String encoding) throws IOException {
        return new ApacheCsvWriter(os, encoding);
    }

    @Override
    public CsvLine newCsvLine() {
        return new ApacheCsvLine(csvDialect);
    }
}

/* */
