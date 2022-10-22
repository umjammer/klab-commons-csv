/*
 * Copyright (c) 2007 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.klab.commons.csv.impl.AbstractCsvFactory;


/**
 * FileCsvDataSource.
 * <p>
 * If you specify this class to {@link org.klab.commons.csv.CsvEntity#dataSource()},
 * You need to specify file name at {@link org.klab.commons.csv.CsvEntity#url()}.
 * </p>
 * <li> (original)
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 070207 sano-n initial version <br>
 */
public class FileCsvDataSource<T> extends AbstractCsvFactory<String, T> {

    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(Paths.get(source));
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return Files.newOutputStream(Paths.get(source));
    }

    /** */
    @Override
    public String toString() {
        return source;
    }
}

/* */
