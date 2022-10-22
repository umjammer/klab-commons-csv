/*
 * $Id: CsvReader.java 0 2008/01/24 14:17:10 sano-n $
 *
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.spi;

import java.io.Closeable;
import java.io.IOException;


/**
 * CsvReader.
 * <li> TODO java.io.Reader と互換ありにするかどうか？
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public interface CsvReader extends Closeable {

    /** */
    boolean hasNextLine() throws IOException;

    /** */
    CsvLine nextLine() throws IOException;
}

/* */
