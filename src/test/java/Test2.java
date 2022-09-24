/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */


import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.klab.commons.csv.CsvColumn;
import org.klab.commons.csv.CsvEntity;
import org.klab.commons.csv.Dialectal;
import org.klab.commons.csv.EnumType;
import org.klab.commons.csv.Enumerated;
import org.klab.commons.csv.GeneratedValue;
import vavi.util.Debug;
import vavi.util.StringUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test2.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022/09/24 nsano initial version <br>
 */
class Test2 {

    @CsvEntity(url = "classpath:test.csv", encoding = "Windows-31J",
            provider = "org.klab.commons.csv.apache.ApacheCsvProvider")
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
        TestCase.Test01.E no;
        @CsvColumn(sequence = 5)
        @Dialectal
        Date date;
        public String toString() {
            return StringUtil.paramString(this);
        }
    }

    @Test
    void test() throws Exception {
        List<Test01> result = CsvEntity.Util.read(Test01.class);
        result.forEach(System.err::println);
        assertEquals(3, result.size());
    }

    @CsvEntity(url = "classpath:test.csv", encoding = "Windows-31J")
    public static class Test02 {
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
        TestCase.Test01.E no;
        @CsvColumn(sequence = 5)
        @Dialectal
        Date date;
        public String toString() {
            return StringUtil.paramString(this);
        }
    }

    @Test
    void test2() throws Exception {
        List<Test02> result = CsvEntity.Util.read(Test02.class);
        result.forEach(System.err::println);
        assertEquals(3, result.size());
    }

    @CsvEntity(url = "classpath:test.tsv", delimiter = "\t", hasTitle = true)
    public static class Test03 {
        @CsvColumn(sequence = 1)
        String gender;
        @CsvColumn(sequence = 2)
        String age;
        @CsvColumn(sequence = 3)
        String area;
        @CsvColumn(sequence = 4)
        String hasCar;
        @CsvColumn(sequence = 5)
        String hasDigitalCamera;
        @CsvColumn(sequence = 6)
        String hasPC;
        @CsvColumn(sequence = 7)
        String work;
        public String toString() {
            return StringUtil.paramString(this);
        }
    }

    @Test
    void test3() throws Exception {
        List<Test03> result = CsvEntity.Util.read(Test03.class);
        result.forEach(System.err::println);
        assertEquals(34, result.size());
    }

    @CsvEntity(url = "classpath:testH.csv",  commentMarker = ";")
    public static class Test04 {
        @CsvColumn(sequence = 1)
        String gender;
        @CsvColumn(sequence = 2)
        String age;
        @CsvColumn(sequence = 3)
        String area;
        @CsvColumn(sequence = 4)
        String hasCar;
        @CsvColumn(sequence = 5)
        String hasDigitalCamera;
        @CsvColumn(sequence = 6)
        String hasPC;
        @CsvColumn(sequence = 7)
        String work;
        public String toString() {
            return StringUtil.paramString(this);
        }
    }

    @Test
    void test4() throws Exception {
        List<Test04> result = CsvEntity.Util.read(Test04.class);
        result.forEach(System.err::println);
        assertEquals(34, result.size());
    }

    @CsvEntity(url = "file:///Users/nsano/src/vavi/vavi-apps-aozora/src/main/resources/prc-unicode.tsv", delimiter = "\t", commentMarker = "#")
    public static class ConvertTable implements Serializable {
        @CsvColumn(sequence = 1)
        String prc;
        @CsvColumn(sequence = 2)
        String character;
        @CsvColumn(sequence = 3)
        String reserved1;
        @CsvColumn(sequence = 4)
        String reserved2;
    }

    @Test
    @Disabled("uses a file outside of this project")
    void testX() throws Exception {
        Map<String, Character> map;

long t = System.currentTimeMillis();

        Path ser = Paths.get("tmp/prc-unicode.ser");

        if (Files.exists(ser)) {
Debug.println("read from ser");
            InputStream is = Files.newInputStream(ser);
            ObjectInputStream ois = new ObjectInputStream(is);
            map = (Map<String, Character>) ois.readObject();
            ois.close();
        } else {
Debug.println("create new map");
            map = new HashMap<>();
            List<ConvertTable> table = CsvEntity.Util.read(ConvertTable.class);
            table.forEach(c -> map.put(c.prc, c.character.charAt(0)));
        }

        assertEquals('瘵', map.get("1-88-56"));
        assertEquals('ǽ', map.get("1-11-37"));

Debug.println("it took " + (System.currentTimeMillis() - t) + " ms");

        OutputStream os = Files.newOutputStream(ser);
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(map);
        oos.close();
    }
}

