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
 * <li> TODO ���ߍׂ��ȑ���Ƃ��H
 * 
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version 0.00 080117 nsano initial version <br>
 */
public interface CsvDao<E extends CsvEntity<I>, I extends Serializable> {

    /** CSV �����ׂēǂݍ��݂܂��B */
    List<E> findAll();

    /** CSV �����ׂď����o���܂��B */
    void updateAll(Collection<E> entities);
}

/* */
