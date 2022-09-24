/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.apache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.logging.Level;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.klab.commons.csv.spi.CsvLine;
import org.klab.commons.csv.spi.CsvReader;
import vavi.util.Debug;


/**
 * ApacheCsvReader.
 *
 * @author <a href="mailto:umjammerngmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-24 nsano initial version <br>
 */
public class ApacheCsvReader implements CsvReader {

    /** */
    private CSVParser parser;

    /** */
    private Iterator<CSVRecord> iterator;

    /** */
    public ApacheCsvReader(InputStream is, String encoding, String delimiter, boolean hasTitle, Character commentMarker) throws IOException {
        CSVFormat.Builder builder = CSVFormat.DEFAULT.builder();
        if (delimiter != null) {
            builder = builder.setDelimiter(delimiter);
        }
        if (commentMarker != null) {
            builder = builder.setCommentMarker(commentMarker);
        }
        if (hasTitle) {
            builder = builder.setHeader().setSkipHeaderRecord(hasTitle);
        }
        CSVFormat format = builder.build();
Debug.println(Level.FINE, format);
        parser = format.parse(new BufferedReader(new InputStreamReader(is, encoding)));
        iterator = parser.iterator();
    }

    /** */
    public boolean hasNextLine() {
        return iterator.hasNext();
    }

    /** */
    public CsvLine nextLine() {
        CSVRecord line = iterator.next();
        return new ApacheCsvLine(line);
    }

    @Override
    public void close() throws IOException {
        parser.close();
    }
}

/* */
