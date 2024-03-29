/*
 * Copyright (c) 2007 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.impl;

import java.io.InputStream;
import java.io.OutputStream;


/**
 * ResourceCsvDataSource.
 * <p>
 * If you specify this class to {@link org.klab.commons.csv.CsvEntity#dataSource()},
 * You need to specify path at {@link org.klab.commons.csv.CsvEntity#url()}.
 * </p>
 * <li> (original)
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 070207 sano-n initial version <br>
 */
public class ResourceCsvDataSource<T> extends AbstractCsvFactory<String, T> {

    /**
     * @return null if {@link #source} is not found.
     */
    @Override
    public InputStream getInputStream() {
        return ResourceCsvDataSource.class.getResourceAsStream(source);
    }

    @Override
    public OutputStream getOutputStream() {
        throw new UnsupportedOperationException("this class is raed only");
    }

    @Override
    public String toString() {
        return source;
    }
}

/* */
