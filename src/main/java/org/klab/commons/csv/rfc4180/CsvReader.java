/*
 * $Id: CsvReader.java 0 2008/01/24 14:17:10 sano-n $
 *
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.rfc4180;

import java.io.IOException;
import java.io.Reader;


/**
 * This outputs the CSV file, separated into its elements.
 * <p>
 * <li>The number of columns in a row is variable.</li>
 * <li>Columns that should contain text are always enclosed in double quotes ("").
 *     (Even if they don't contain text, they are still enclosed in double quotes.)</li>
 * <li>A line is marked with a newline character at the end. However,
 *     even if a newline character exists within quotation marks (""), it does not mark the end of a line.</li>
 * <li>The " inside the "" is escaped with ".</li>
 * </p>
 *
 * @author <a href="mailto:kusanagi@klab.org">Tomonori Kusanagi</a> (kusanagi)
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 */
public class CsvReader {

    /** Source object: ForwardReader object */
    protected ForwardReader forwardReader;

    /**
     * Sets the reader.
     *
     * @param reader
     */
    public CsvReader(Reader reader) {
        this.forwardReader = new ForwardReader(reader);
    }

    /**
     * Returns true if there are further rows.
     *
     * @return True if there are more rows
     */
    public boolean hasNext() throws IOException {
        // Check if the end of the stream has been reached.
        if (forwardReader.check() == -1) {
            return false;
        }
        return true;
    }

    /**
     * Create the following line.
     *
     * @return Next line
     */
    public CsvTokenizer next() throws IOException {
        synchronized (forwardReader) {
            CsvTokenizer line = new CsvTokenizer(forwardReader);
            return line;
        }
    }
}
