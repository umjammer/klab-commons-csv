/*
 * $Id: CsvDialect.java 0 2008/01/24 14:17:10 sano-n $
 *
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv;

import java.lang.reflect.Field;


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
     * object to string conversion. when field is annotated by {@link Dialectal}
     * @return string from field value
     */
    String toCsvColumn(Field field, Object bean, Object value);

    /**
     * string to object conversion. when field is annotated by {@link Dialectal}
     * @return object from column string
     */
    Object toFieldValue(Field field, Object bean, String column);

    /**
     * quote, escape quotation character, etc.
     * @param column not null
     */
    String formatString(String column);
}

/* */
