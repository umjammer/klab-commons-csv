/*
 * Copyright (c) 2007 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.impl;

import java.io.InputStream;
import java.io.OutputStream;

import org.klab.commons.csv.CsvFactory;


/**
 * IOStreamCsvFactory.
 * <p>
 * currently not working with {@link org.klab.commons.csv.CsvEntity#io()} 
 * </p>
 * <li> (original)
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 070207 sano-n initial version <br>
 */
public class IOStreamCsvFactory extends AbstractCsvFactory<IOStreamCsvFactory.IO> implements CsvFactory<IOStreamCsvFactory.IO> {

    /** */
    public static class IO {
        InputStream inputStream;
        OutputStream outputStream;
    }

    @Override
    public InputStream getInputStream() {
        return source.inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return source.outputStream;
    }

    @Override
    public String toString() {
        return "IOStreamCsvFactory: " +
            source.inputStream.getClass().getSimpleName() + ", " +
            source.outputStream.getClass().getSimpleName();
    }
}

/* */
