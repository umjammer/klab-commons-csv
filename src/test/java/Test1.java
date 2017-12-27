/*
 * Copyright (c) 2014 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.util.List;

import org.junit.Test;

import org.klab.commons.csv.CsvEntity;
import org.klab.commons.csv.GeneratedValue;
import org.klab.commons.csv.dao.CsvDaoBase;
import org.klab.commons.csv.impl.FileCsvFactory;

import vavi.util.StringUtil;

import static org.junit.Assert.assertEquals;


/**
 * Test1.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2014/03/14 umjammer initial version <br>
 */
public class Test1 {

    @CsvEntity
    public static class A implements org.klab.commons.csv.dao.CsvEntity<Integer> {
        @GeneratedValue
        int id;
        @Override
        public Integer getId() {
            return id;
        }
        @Override
        public void setId(Integer id) {
            this.id = id;
        }
        public String toString() {
            return StringUtil.paramString(this);
        }
    }

    @Test
    public void test1() throws Exception {
        CsvDaoBase<A, Integer> csvDao = new CsvDaoBase<>();
        csvDao.setEntityClass(A.class);
        FileCsvFactory csvFactory = new FileCsvFactory();
        csvFactory.setSource("src/test/resources/test.csv");
        csvDao.setCsvFactory(csvFactory);

        List<A> result = csvDao.findAll();
        result.forEach(System.err::println);

        assertEquals(3, result.size());
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        CsvDaoBase<A, Integer> csvDao = new CsvDaoBase<>();
        csvDao.setEntityClass(A.class);
        FileCsvFactory csvFactory = new FileCsvFactory();
        csvFactory.setSource("src/test/resources/test.csv");
        csvDao.setCsvFactory(csvFactory);

        List<A> result = csvDao.findAll();
        result.forEach(System.err::println);
    }
}

/* */
