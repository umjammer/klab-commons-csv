/*
 * $Id: CsvDialect.java 0 2008/01/24 14:17:10 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv;

import java.lang.reflect.Field;

import org.klab.commons.csv.spi.CsvLine;


/**
 * CsvDialect.
 * <li> (spi?)
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public interface CsvDialect {

    /** */
    String getEndOfLine();

    /**
     * object to strings conversion. when field is annotated by {@link Dialectal} 
     */
    CsvLine toCsvLine(Field field, Object bean, Object value);

    /**
     * strings to object conversion. when field is annotated by {@link Dialectal} 
     */
    Object toFieldValue(Field field, Object bean, CsvLine columns);

    /**
     * quote, escape quotation character, etc.
     * @param column not null
     */
    String formatString(String column);
}

/* */
