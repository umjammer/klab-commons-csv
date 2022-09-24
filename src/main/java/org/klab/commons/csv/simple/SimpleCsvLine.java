/*
 * $Id: SimpleCsvLine.java 0 2008/01/24 14:17:10 sano-n $
 *
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.simple;

import java.util.Iterator;

import org.klab.commons.csv.CsvDialect;
import org.klab.commons.csv.impl.AbstractCsvLine;


/**
 * SimpleCsvLine.
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public class SimpleCsvLine extends AbstractCsvLine<CsvTokenizer> {

    public SimpleCsvLine(CsvDialect dialect) {
        super(dialect);
    }

    public SimpleCsvLine(String csv) {
        super(new CsvTokenizer(csv));
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return entity.hasMoreTokens();
            }

            @Override
            public String next() {
                return entity.nextToken();
            }
        };
    }
}

/* */
