/*
 * Copyright (c) 2016 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.klab.commons.csv.CsvColumn;
import org.klab.commons.csv.CsvEntity;
import org.klab.commons.csv.Dialectal;
import org.klab.commons.csv.EnumType;
import org.klab.commons.csv.Enumerated;
import org.klab.commons.csv.GeneratedValue;
import org.klab.commons.csv.impl.FileCsvDataSource;

import vavi.util.StringUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * TestCase.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2016/03/07 umjammer initial version <br>
 */
public class TestCase {

    @CsvEntity(url = "classpath:test.csv", encoding = "Windows-31J",
            provider = "org.klab.commons.csv.rfc4180.Rfc4180CsvProvider")
    public static class Test01 {
        public enum E { // *** CAUTION *** enum should be public when you read csv.
            N0, N1, N2, N3, N4, N5, N6
        }
        @GeneratedValue
        @CsvColumn(sequence = 1)
        int id;
        @CsvColumn(sequence = 2)
        String title;
        @CsvColumn(sequence = 3)
        String html;
        @CsvColumn(sequence = 4)
        @Enumerated(EnumType.ORDINAL)
        E no;
        @CsvColumn(sequence = 5)
        @Dialectal
        Date date;
        public String toString() {
            return StringUtil.paramString(this);
        }
    }

    @Test
    public void test() throws Exception {
        List<Test01> result = CsvEntity.Util.read(Test01.class);
        result.forEach(System.err::println);
        assertEquals(3, result.size());
    }

    @CsvEntity(url = "tmp/out.csv", dataSource = FileCsvDataSource.class,
            provider = "org.klab.commons.csv.simple.ApacheCsvProvider")
    public static class Test02 {
        public enum E { // should be public for read
            A, B, C, D, E
        }
        public Test02() {} // needed for read
        public Test02(int id, String title, String html, E no, Date date) {
            this.id = id;
            this.title = title;
            this.html = html;
            this.no = no;
            this.date = date;
        }
        @GeneratedValue
        @CsvColumn(sequence = 1)
        int id;
        @CsvColumn(sequence = 2)
        String title;
        @CsvColumn(sequence = 3)
        String html;
        @CsvColumn(sequence = 4)
        @Enumerated(EnumType.ORDINAL)
        E no;
        @CsvColumn(sequence = 5)
        @Dialectal
        Date date;
    }

    @Test
    public void test2() throws Exception {
        Path dir = Paths.get("tmp");
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        List<Test02> list = new ArrayList<>();
        Date date = new Date();
        list.add(new Test02(1, "title1", "contents1", Test02.E.A, date));
        list.add(new Test02(2, "title2", "contents2", Test02.E.B, date));
        list.add(new Test02(3, "title3", "contents3", Test02.E.C, date));
        list.add(new Test02(4, "title4", "contents4", Test02.E.D, date));
        list.add(new Test02(5, "title5", "contents5", Test02.E.E, date));
        CsvEntity.Util.write(list, Test02.class);

        List<Test02> list2 = CsvEntity.Util.read(Test02.class);
//        assertEquals(date, list2.get(0).date); // TODO second is sounded by the excel dialect
        assertEquals(3, list2.get(3).id);
    }

    @CsvEntity(url = "http://www.sample-videos.com/csv/Sample-Spreadsheet-500000-rows.csv", encoding = "ISO8859-1")
    public static class Test03 {
        @CsvColumn(sequence = 1)
        String name;
        @CsvColumn(sequence = 2)
        String owner;
        @CsvColumn(sequence = 3)
        int n1;
        @CsvColumn(sequence = 4)
        float n2;
        @CsvColumn(sequence = 5)
        float n3;
        @CsvColumn(sequence = 6)
        float n4;
        @CsvColumn(sequence = 7)
        String status;
        @CsvColumn(sequence = 8)
        String category;
        @CsvColumn(sequence = 9)
        float n5;
    }

    @Test
    @Disabled // TODO give me suitable online csv sample!
    public void test3() throws Exception {
        List<Test03> result = CsvEntity.Util.read(Test03.class);
        assertEquals(500000, result.size());
    }

    public static class Test04Super {
        @GeneratedValue
        @CsvColumn(sequence = 1)
        int id;
        @CsvColumn(sequence = 2)
        String title;
    }

    @CsvEntity(url = "classpath:test.csv", encoding = "Windows-31J",
            provider = "org.klab.commons.csv.rfc4180.Rfc4180CsvProvider")
    public static class Test04 extends Test04Super {
        public enum E { // *** CAUTION *** enum should be public when you read csv.
            N0, N1, N2, N3, N4, N5, N6
        }
        @CsvColumn(sequence = 3)
        String html;
        @CsvColumn(sequence = 4)
        @Enumerated(EnumType.ORDINAL)
        E no;
        @CsvColumn(sequence = 5)
        @Dialectal
        Date date;
        public String toString() {
            return StringUtil.paramString(this);
        }
    }

    @Test
    public void test4() throws Exception {
        List<Test04> result = CsvEntity.Util.read(Test04.class);
        result.forEach(System.err::println);
        assertEquals(1, result.get(1).id);
        assertNotNull(result.get(0).title);
        assertEquals(3, result.size());
    }
}

/* */
