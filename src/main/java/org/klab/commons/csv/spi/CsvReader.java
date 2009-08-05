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
 * <li> TODO java.io.Reader Ç∆å›ä∑Ç†ÇËÇ…Ç∑ÇÈÇ©Ç«Ç§Ç©ÅH
 * 
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (nsano)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public interface CsvReader extends Closeable {

    /** */
    boolean hasNextLine() throws IOException;

    /** TODO Ç∆ÇËÇ†Ç¶Ç∏ CsvLine Ç…ÇµÇƒÇ›ÇΩÇØÇ« get/set Ç≈ëŒè∆ê´Ç™Ç»Ç¢ */
    CsvLine nextLine() throws IOException;
}

/* */
