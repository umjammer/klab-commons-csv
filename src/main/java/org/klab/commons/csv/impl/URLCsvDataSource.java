/*
 * Copyright (c) 2016 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;


/**
 * URLCsvDataSource.
 * <p>
 * If you specify this class to {@link org.klab.commons.csv.CsvEntity#dataSource()},
 * You need to specify url at {@link org.klab.commons.csv.CsvEntity#url()}.
 * </p>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2016/03/07 umjammer initial version <br>
 */
public class URLCsvDataSource<T> extends AbstractCsvFactory<String, T> {

    /* @see org.klab.commons.csv.CsvDataSource#getInputStream() */
    @Override
    public InputStream getInputStream() throws IOException {
        return new URL(source).openStream();
    }

    /* @see org.klab.commons.csv.CsvDataSource#getOutputStream() */
    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException("this class is raed only");
    }

    /** */
    @Override
    public String toString() {
        return source.toString();
    }
}

/* */
