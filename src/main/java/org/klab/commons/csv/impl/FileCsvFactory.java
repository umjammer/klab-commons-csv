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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.klab.commons.csv.CsvFactory;


/**
 * FileCsvFactory.
 * <li> (original)
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 070207 sano-n initial version <br>
 */
public class FileCsvFactory extends AbstractCsvFactory implements CsvFactory {

    private static Log logger = LogFactory.getLog(FileCsvFactory.class);

    /** */
    private String fileName;

    /**
     * for DI
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
logger.debug("fileName: " + fileName);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(fileName);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(fileName);
    }

    /** */
    @Override
    public String toString() {
        return fileName;
    }
}

/* */
