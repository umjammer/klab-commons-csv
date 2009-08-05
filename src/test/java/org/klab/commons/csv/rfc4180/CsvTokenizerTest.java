/*
 * Created on 2004/12/19
 */

package org.klab.commons.csv.rfc4180;

import java.io.IOException;
import java.io.StringReader;

import org.klab.commons.csv.rfc4180.CsvReader;
import org.klab.commons.csv.rfc4180.CsvTokenizer;
import org.klab.commons.csv.rfc4180.ForwardReader;

import junit.framework.TestCase;


/**
 * @author <a href="mailto:kusanagi@klab.co.jp">Tomonori Kusanagi</a> (kusanagi)
 */
public class CsvTokenizerTest extends TestCase {

    /**
     * ��s�p�[�X���܂��B
     */
    public void testParseLine() throws IOException{
        String testStr = "0,127,ts.1101801508,da.2004.11.30,da.2004.11.30,tm.10.30.0,tm.12.0.0,,,,,1,0,\"\",\"�����쐬\",\"\"\"��2�� IT���ʑ���񍐉�\"\"�ɂނ��������̍쐬\r\n\r\n �T�[�o�[�f�B�X�N�̗e�ʂ��v������\",,,,,,,,0,16\r\n";
        CsvReader tokenizer = new CsvReader(new StringReader(testStr));
        ForwardReader forwardReader = tokenizer.forwardReader;
        
        CsvTokenizer line = new CsvTokenizer(forwardReader);
        while (line.hasNext()) {
            System.out.print("[[" + line.next() + "]]");
        }
    }
}

/* */
