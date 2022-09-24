/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.apache;

import java.util.Iterator;

import org.apache.commons.csv.CSVRecord;
import org.klab.commons.csv.CsvDialect;
import org.klab.commons.csv.impl.AbstractCsvLine;


/**
 * ApacheCsvLine.
 *
 * @author <a href="mailto:umjammerngmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-24 nsano initial version <br>
 */
public class ApacheCsvLine extends AbstractCsvLine<CSVRecord> {

    public ApacheCsvLine(CsvDialect csvDialect) {
        super(csvDialect);
    }

    public ApacheCsvLine(CSVRecord csv) {
        super(csv);
    }

    @Override
    public Iterator<String> iterator() {
        return entity.iterator();
    }
}

/* */
