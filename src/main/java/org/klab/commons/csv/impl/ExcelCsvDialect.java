/*
 * $Id: ExcelCsvDialect.java 0 2008/01/24 14:17:10 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.impl;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.klab.commons.csv.CsvDialect;
import org.klab.commons.csv.spi.CsvLine;


/**
 * ExcelCsvDialect.
 * <li> (spi?)
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public class ExcelCsvDialect implements CsvDialect {

    /** */
    private static final String sdf = "yyyy/M/d H:mm";

    /**
     * @param field when type is Date only converted 
     */
    @Override
    public CsvLine toCsvLine(Field field, Object bean, Object value) {
        List<String> columns = new ArrayList<String>();
        if (field.getType() == Date.class) {
            columns.add(value == null ? "" : "\"" + new SimpleDateFormat(sdf).format(Date.class.cast(value).getTime()) + "\"");
        } else {
            throw new IllegalArgumentException(field.getName());
        }
        return (CsvLine) columns;
    }

    /**
     * @param field when type is Date only converted 
     */
    @Override
    public Object toFieldValue(Field field, Object bean, CsvLine columns) {
        String column = columns.next();
        if (field.getType() == Date.class) {
            try {
                return column == null ? null : new SimpleDateFormat(sdf).parse(column);
            } catch (ParseException e) {
                throw (RuntimeException) new IllegalStateException().initCause(e);
            }
        } else {
            throw new IllegalArgumentException(field.getName());
        }
    }

    /**
     * @return \r\n
     */
    @Override
    public String getEndOfLine() {
        return "\r\n";
    }

    /**
     * @return quoted by '"', '\'' is escaped by '\''
     */
    @Override
    public String formatString(String column) {
        String stage1 = column.replace("\"", "\"\"");
        String stage2 = "\"" + stage1 + "\"";
        return stage2;
    }
}

/* */
