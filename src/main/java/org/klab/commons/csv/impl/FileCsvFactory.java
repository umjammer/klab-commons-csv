/*
 * Copyright (c) 2007 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.klab.commons.csv.CsvFactory;


/**
 * FileCsvFactory.
 * <p>
 * If you specify this class to {@link org.klab.commons.csv.CsvEntity#io()},
 * You need to specify file name at {@link org.klab.commons.csv.CsvEntity#url()}.
 * </p>
 * <li> (original)
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 070207 sano-n initial version <br>
 */
public class FileCsvFactory extends AbstractCsvFactory<String> implements CsvFactory<String> {

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(source);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(source);
    }

    /** */
    @Override
    public String toString() {
        return source;
    }
}

/* */
