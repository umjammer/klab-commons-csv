/*
 * $Id: ExcelCsvDialect.java 0 2008/01/24 14:17:10 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.impl;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.klab.commons.csv.CsvDialect;

import vavi.beans.BeanUtil;


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
    public String toCsvLine(Field field, Object bean, Object value) {
        if (field.getType() == Date.class) {
            return value == null ? "" : "\"" + new SimpleDateFormat(sdf).format(Date.class.cast(value).getTime()) + "\"";
        } else {
            throw new IllegalArgumentException(field.getName());
        }
    }

    /**
     * @param field when type is Date only converted 
     */
    @Override
    public Object toFieldValue(Field field, Object bean, String column) {
        if (field.getType() == Date.class) {
            try {
                Object value = column == null ? null : new SimpleDateFormat(sdf).parse(column);
                BeanUtil.setFieldValue(field, bean, value);
                return value;
            } catch (ParseException e) {
                throw new IllegalStateException(e);
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
