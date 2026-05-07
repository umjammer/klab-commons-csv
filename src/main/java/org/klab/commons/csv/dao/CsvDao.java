/*
 * Copyright (c) 2008 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * CsvDao.
 * <li> (commons-persistence like)
 * <li> TODO Fine-tuned operation, perhaps?
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 080117 nsano initial version <br>
 */
public interface CsvDao<E extends CsvEntity<I>, I extends Serializable> {

    /** Import the entire CSV file. */
    List<E> findAll();

    /** Export all CSV files. */
    void updateAll(Collection<E> entities);
}
