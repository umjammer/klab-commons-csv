/*
 * Copyright (c) 2014 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.util.List;

import org.klab.commons.csv.dao.CsvDaoBase;
import org.klab.commons.csv.dao.CsvEntity;
import org.klab.commons.csv.impl.FileCsvFactory;


/**
 * Test1. 
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2014/03/14 umjammer initial version <br>
 */
public class Test1 {

    static class A implements CsvEntity<Integer> {
        @Override
        public Integer getId() {
            return null;
        }
        @Override
        public void setId(Integer id) {
        }
        
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        CsvDaoBase<A, Integer> csvDao = new CsvDaoBase<A, Integer>();
        csvDao.setEntityClass(A.class);
        FileCsvFactory csvFactory = new FileCsvFactory();
        csvFactory.setFileName("/test.csv");
        csvDao.setCsvFactory(csvFactory);

        List<A> result = csvDao.findAll();        
    }
}

/* */
