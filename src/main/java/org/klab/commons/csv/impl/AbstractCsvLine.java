/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.impl;

import java.util.ArrayList;
import java.util.List;

import org.klab.commons.csv.CsvDialect;
import org.klab.commons.csv.spi.CsvLine;


/**
 * AbstractCsvLine.
 *
 * @author <a href="mailto:umjammerngmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-24 nsano initial version <br>
 */
public abstract class AbstractCsvLine<T> implements CsvLine {

    /** */
    protected List<String> columns;

    /** */
    protected CsvDialect dialect;

    /** */
    protected T entity;

    protected AbstractCsvLine(CsvDialect dialect) {
        columns = new ArrayList<>();
        this.dialect = dialect;
    }

    protected AbstractCsvLine(T entity) {
        this.entity = entity;
    }

    @Override
    public void add(String column) {
        columns.add(column);
    }

    @Override
    public String toString() {
         if (entity != null) {
             // for read
             return entity.toString();
         } else {
             // for write
             return String.join(",", columns) + dialect.getEndOfLine();
         }
    }
}

/* */
