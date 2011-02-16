/*
 * $Id: CsvProvider.java 0 2008/01/24 14:38:23 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.impl;

import java.lang.reflect.Field;

import org.klab.commons.csv.spi.CsvLine;


/**
 * DefaultCsvProvider.
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 */
public class DefaultCsvProvider extends CsvProviderBase {

    /** */
    protected Object toFieldValue(Field field, Object bean, CsvLine columns) {
        return defaultCsvDiarect.toFieldValue(field, bean, columns);
    }
}

/* */
