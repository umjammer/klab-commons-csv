/*
 * $Id: SimpleCsvReader.java 0 2008/01/24 14:17:10 sano-n $
 *
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.simple;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.klab.commons.csv.spi.CsvLine;
import org.klab.commons.csv.spi.CsvReader;


/**
 * SimpleCsvReader.
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public class SimpleCsvReader implements CsvReader {

    /** */
    private Scanner scanner;

    /** */
    public SimpleCsvReader(InputStream is, String encoding) {
        this.scanner = new Scanner(is, encoding);
    }

    /** */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /** */
    public CsvLine nextLine() {
        String line = scanner.nextLine();
        return new SimpleCsvLine(line);
    }

    @Override
    public void close() throws IOException {
        scanner.close();
    }
}

/* */
