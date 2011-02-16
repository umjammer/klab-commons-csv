/*
 * $Id: CsvWriter.java 0 2008/01/24 14:17:10 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.spi;

import java.io.Closeable;
import java.io.IOException;


/**
 * CsvWriter.
 * <li> TODO java.io.Writer �ƌ݊�����ɂ��邩�ǂ����H
 * 
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public interface CsvWriter extends Closeable {

    /**
     * @param csv EOL should be included
     * TODO �Ƃ肠���� String �ɂ��Ă邯�ǁA�ǂ��Ȃ̂��H 
     */
    void writeLine(String csv) throws IOException;

    /** */
    void flush() throws IOException;
}

/* */
