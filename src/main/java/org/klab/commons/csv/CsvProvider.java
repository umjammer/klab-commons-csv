/*
 * $Id: CsvProvider.java 0 2008/01/24 14:38:23 sano-n $
 *
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.klab.commons.csv.spi.CsvReader;
import org.klab.commons.csv.spi.CsvWriter;


/**
 * CsvProvider.
 * <li> (jca)
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 */
public interface CsvProvider {

    /** TODO 引数 */
    CsvConverter getCsvConverter(Class<?> entityClass);

    /** Reader の提供 */
    CsvReader getCsvReader(InputStream is, String encoding) throws IOException;

    /** Writer の提供 */
    CsvWriter getCsvWriter(OutputStream os, String encoding) throws IOException;
}

/* */
