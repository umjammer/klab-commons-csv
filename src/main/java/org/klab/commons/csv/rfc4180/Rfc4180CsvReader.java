/*
 * $Id: SimpleCsvReader.java 0 2008/01/24 14:17:10 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.rfc4180;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.klab.commons.csv.spi.CsvLine;


/**
 * Rfc4180CsvReader.
 * 
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public class Rfc4180CsvReader implements org.klab.commons.csv.spi.CsvReader {

    /** */
    private CsvReader reader;

    /** */
    public Rfc4180CsvReader(InputStream is, String encoding) throws IOException {
        this.reader = new CsvReader(new InputStreamReader(is, encoding));
    }

    /** */
    public boolean hasNextLine() throws IOException {
        return reader.hasNext();
    }

    /** */
    public CsvLine nextLine() throws IOException {
        CsvTokenizer line = reader.next();
        return new Rfc4180CsvLine(line);
    }

    @Override
    public void close() throws IOException {
        // TODO implement
    }
}

/* */
