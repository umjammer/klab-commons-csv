/*
 * $Id: SimpleCsvWriter.java 0 2008/01/24 14:17:10 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.rfc4180;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.klab.commons.csv.spi.CsvWriter;


/**
 * SimpleCsvWriter.
 * 
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (nsano)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public class Rfc4180CsvWriter implements CsvWriter {

    /** */
    private Writer writer;

    /** */
    public Rfc4180CsvWriter(OutputStream os, String encoding) throws IOException {
        this.writer = new OutputStreamWriter(os, encoding);
    }
    
    /** */
    public void writeLine(String csv) throws IOException {
        writer.write(csv);
    }

    /** */
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}

/* */
