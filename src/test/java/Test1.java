/*
 * Copyright (c) 2014 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.util.List;

import org.junit.jupiter.api.Test;

import org.klab.commons.csv.CsvColumn;
import org.klab.commons.csv.CsvEntity;
import org.klab.commons.csv.GeneratedValue;
import org.klab.commons.csv.dao.CsvDaoBase;
import org.klab.commons.csv.impl.FileCsvDataSource;

import vavi.util.StringUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test1.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2014/03/14 umjammer initial version <br>
 */
class Test1 {

    @CsvEntity(encoding = "MS932", provider = "org.klab.commons.csv.rfc4180.Rfc4180CsvProvider")
    public static class A implements org.klab.commons.csv.dao.CsvEntity<Integer> {
        @GeneratedValue
        int id;
        @CsvColumn(sequence = 2)
        String title;
        @CsvColumn(sequence = 3)
        String url;
        @CsvColumn(sequence = 4)
        int x;
        @CsvColumn(sequence = 5)
        String date;
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
    void test1() throws Exception {
        CsvDaoBase<A, Integer> csvDao = new CsvDaoBase<>();
        csvDao.setEntityClass(A.class);
        FileCsvDataSource<A> csvFactory = new FileCsvDataSource<>();
        csvFactory.setSource("src/test/resources/test.csv");
        csvDao.setCsvDataSource(csvFactory);

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
        FileCsvDataSource<A> csvFactory = new FileCsvDataSource<>();
        csvFactory.setSource("src/test/resources/test.csv");
        csvDao.setCsvDataSource(csvFactory);

        List<A> result = csvDao.findAll();
        result.forEach(System.err::println);
    }
}

/* */
