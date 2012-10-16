/*
 * $Id: SimpleCsvLine.java 0 2008/01/24 14:17:10 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.simple;

import java.util.Iterator;

import org.klab.commons.csv.spi.CsvLine;


/**
 * SimpleCsvLine.
 * 
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public class SimpleCsvLine implements CsvLine {

    private CsvTokenizer csvTokenizer;

    public SimpleCsvLine(String csv) {
        this.csvTokenizer = new CsvTokenizer(csv);
    }

    @Override
    public boolean hasNext() {
        return csvTokenizer.hasMoreElements();
    }

    @Override
    public String next() {
        return csvTokenizer.nextElement();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<String> iterator() {
        return this; // TODO 仕様と違う気が ArrayList は毎回 new
    }

    @Override
    public String toString() {
        return csvTokenizer.toString();
    }
}

/* */
