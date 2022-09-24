/*
 * $Id: CsvProvider.java 0 2008/01/24 14:38:23 sano-n $
 *
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.dao;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.klab.commons.csv.CsvDataSource;


/**
 * CsvDaoBase.
 * <li> (commons-persistence like)
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 */
public class CsvDaoBase<E extends CsvEntity<I>, I extends Serializable> implements CsvDao<E, I> {

    /** TODO リフレクションで総称型取れれば要らないはず */
    private Class<E> entityClass;

    /** for DI */
    public void setEntityClass(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    /** data source */
    private CsvDataSource<?, E> csvDataSource;

    /** for DI */
    public void setCsvDataSource(CsvDataSource<?, E> csvDataSource) {
        this.csvDataSource = csvDataSource;
    }

    /** */
    @SuppressWarnings("cast")
    public List<E> findAll() {
        try {
            return csvDataSource.getWholeCsvReader().readAll(entityClass);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /** */
    public void updateAll(Collection<E> entities) {
        try {
            csvDataSource.getWholeCsvWriter().writeAll(entities, entityClass);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}

/* */
