/*
 * $Id: Rfc4180CsvLine.java 0 2008/01/24 14:17:10 sano-n $
 *
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.rfc4180;

import java.util.Iterator;

import org.klab.commons.csv.CsvDialect;
import org.klab.commons.csv.impl.AbstractCsvLine;


/**
 * Rfc4180CsvLine.
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public class Rfc4180CsvLine extends AbstractCsvLine<CsvTokenizer> {

    public Rfc4180CsvLine(CsvDialect csvDialect) {
        super(csvDialect);
    }

    public Rfc4180CsvLine(CsvTokenizer csvTokenizer) {
        super(csvTokenizer);
    }

    @Override
    public Iterator<String> iterator() {
        return entity.iterator();
    }
}

/* */
