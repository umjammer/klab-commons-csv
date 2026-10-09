/*
 * $Id: ForwardReader.java 0 2008/01/24 14:17:10 sano-n $
 *
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.rfc4180;

import java.io.IOException;
import java.io.Reader;


/**
 * This is a class for reading characters one by one from a Reader.
 *
 * @author <a href="mailto:kusanagi@klab.org">Tomonori Kusanagi</a> (kusanagi)
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 */
public class ForwardReader {

    protected static final int NONE = -2;

    protected Reader reader;

    protected int bufferedChar = NONE;

    /**
     * Constructor.
     */
    public ForwardReader(Reader reader) {
        this.reader = reader;
    }

    /**
     * Read the character following the current reading point,
     * and advance the reading point by one.
     *
     * @return read character
     */
    protected synchronized int read() throws IOException {
        if (bufferedChar == NONE) {
            return reader.read();
        } else {
            int val = bufferedChar;
            // Clear bufferedChar
            bufferedChar = NONE;
            return val;
        }
    }

    /**
     * This method checks and returns the character following the current reading point,
     * but it does not advance the reading point.
     * It will continue to return the same character unless the `read` method is executed again.
     *
     * @return buffered character
     * @throws IOException
     */
    protected synchronized int check() throws IOException {
        if (bufferedChar == NONE) {
            bufferedChar = reader.read();
            return bufferedChar;
        } else {
            return bufferedChar;
        }
    }
}
