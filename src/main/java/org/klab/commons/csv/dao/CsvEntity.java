/*
 * Copyright (c) 2008 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.dao;

import java.io.Serializable;


/**
 * CsvEntity. 
 * <li> (commons-persistence like)
 * 
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 080117 nsano initial version <br>
 */
public interface CsvEntity<I extends Serializable> {

    /** */
    I getId();

    /** */
    void setId(I id);
}

/* */
