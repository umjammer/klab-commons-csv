/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.apache;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.klab.commons.csv.spi.CsvLine;
import org.klab.commons.csv.spi.CsvWriter;


/**
 * ApacheCsvWriter.
 *
 * @author <a href="mailto:umjammerngmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-24 nsano initial version <br>
 */
public class ApacheCsvWriter implements CsvWriter {

    /** TODO use apache writer */
    private final Writer writer;

    /** */
    public ApacheCsvWriter(OutputStream os, String encoding) throws IOException {
        this.writer = new OutputStreamWriter(os, encoding);
    }

    /** */
    public void writeLine(CsvLine csv) throws IOException {
        writer.write(csv.toString());
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
