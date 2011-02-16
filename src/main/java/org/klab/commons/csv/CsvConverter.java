/*
 * $Id: CsvDialect.java 0 2008/01/24 14:17:10 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv;

import org.klab.commons.csv.spi.CsvLine;


/**
 * CsvConverter.
 * <li> (spi?)
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public interface CsvConverter {

    /**
     * �G���e�B�e�B�� CSV �̈�s�ɕϊ����܂��B
     */
    String toCsv(Object entity);

    /**
     *  CSV �̈�s���G���e�B�e�B�ɕϊ����܂��B
     */
    Object toEntity(CsvLine csv);
}

/* */
