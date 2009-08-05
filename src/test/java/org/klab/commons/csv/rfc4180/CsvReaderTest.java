/*
 * Created on 2004/12/16
 */

package org.klab.commons.csv.rfc4180;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.klab.commons.csv.rfc4180.CsvReader;
import org.klab.commons.csv.rfc4180.CsvTokenizer;
import org.klab.commons.csv.rfc4180.ForwardReader;

import junit.framework.TestCase;


/**
 * 
 * @author <a href="mailto:kusanagi@klab.co.jp">Tomonori Kusanagi</a> (kusanagi)
 */
public class CsvReaderTest extends TestCase {

    /**
     * �����񂩂�ǂݍ��݂܂��B
     */
    public void testRead() throws Exception {
        String testStr = "this is a test line.\n����̓e�X�g�ł��B";

        CsvReader tokenizer = new CsvReader(new StringReader(testStr));
        ForwardReader forwardReader = tokenizer.forwardReader;
        int c;
        while ((c = forwardReader.read()) != -1) {
            System.out.println((char) c);
        }
    }

    /**
     * �t�@�C������ǂݍ��݂܂��B(Windows-31J)
     */
    public void testReadFromFile() throws Exception {
        final String filename = "/test.csv";
        Reader fReader = new InputStreamReader(CsvReaderTest.class.getResourceAsStream(filename), "Windows-31J");

        CsvReader tokenizer = new CsvReader(fReader);
        ForwardReader charBuffer = tokenizer.forwardReader;
        int c;
        while ((c = charBuffer.read()) != -1) {
            System.out.println((char) c);
        }
    }

    /**
     * ���ۂ̎g�p���@�������Ă��܂��B
     */
    public void testParse1() throws Exception {
        final String filename = "/test.csv";
        Reader fReader = new InputStreamReader(CsvReaderTest.class.getResourceAsStream(filename), "Windows-31J");

        // ���ۂɂ͈ȉ��̂悤�ɂ��Ďg���B
        CsvReader reader = new CsvReader(fReader);
        int i = 1;
        while (reader.hasNext()) {
            System.out.println("======" + i + "=====");
            CsvTokenizer line = reader.next();
            while (line.hasNext()) {
                String token = line.next();
                System.out.print("[[" + token + "]]");
            }
            System.out.println();
            i++;
        }
    }
}
