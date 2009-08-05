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
 * <li> (original)
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (nsano)
 * @version 0.00 070207 nsano initial version <br>
 */
public class IOStreamCsvFactory extends AbstractCsvFactory implements CsvFactory {

    /** */
    private InputStream inputStream;

    /** */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    /** */
    private OutputStream outputStream;

    /** */
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public String toString() {
        return "IOStreamCsvFactory: " +
            inputStream.getClass().getSimpleName() + ", " +
            outputStream.getClass().getSimpleName();
    }
}

/* */
