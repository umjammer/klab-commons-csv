/*
 * $Id: CsvRow.java 0 2008/01/24 14:17:10 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.rfc4180;

import java.util.Iterator;

import org.klab.commons.csv.spi.CsvLine;


/**
 * Rfc4180CsvLine.
 * 
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public class Rfc4180CsvLine implements CsvLine {

    private CsvTokenizer csvTokenizer;

    public Rfc4180CsvLine(CsvTokenizer csvTokenizer) {
        this.csvTokenizer = csvTokenizer;
    }

    @Override
    public boolean hasNext() {
        return csvTokenizer.hasNext();
    }

    @Override
    public String next() {
        return csvTokenizer.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<String> iterator() {
        return csvTokenizer.iterator();
    }

    @Override
    public String toString() {
        return csvTokenizer.toString();
    }
}

/* */
