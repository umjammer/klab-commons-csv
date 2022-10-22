/*
 * $Id: CsvConverter.java 0 2008/01/24 14:17:10 sano-n $
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
public interface CsvConverter<T> {

    /**
     * エンティティを CSV の一行に変換します。
     */
    CsvLine toCsv(T entity);

    /**
     *  CSV の一行をエンティティに変換します。
     */
    T toEntity(CsvLine csv);
}

/* */
