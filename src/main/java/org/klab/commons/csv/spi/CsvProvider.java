/*
 * $Id: CsvProvider.java 0 2008/01/24 14:38:23 sano-n $
 *
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.klab.commons.csv.CsvConverter;
import org.klab.commons.csv.CsvDialect;


/**
 * CsvProvider.
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 */
public interface CsvProvider<T> {

    /** same instance */
    CsvDialect getCsvDialect();

    /** TODO args */
    CsvConverter<T> newCsvConverter(Class<T> entityClass);

    /** provides new CsvReader instance */
    CsvReader newCsvReader(InputStream is, String encoding, String delimiter, boolean hasTitle, Character commentMarker) throws IOException;

    /** provides new CsvWriter instance */
    CsvWriter newCsvWriter(OutputStream os, String encoding) throws IOException;

    /** provides new CsvLine instance */
    CsvLine newCsvLine();
}

/* */
