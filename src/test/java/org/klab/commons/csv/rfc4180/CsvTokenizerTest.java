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
 * @author <a href="mailto:kusanagi@klab.org">Tomonori Kusanagi</a> (kusanagi)
 */
public class CsvTokenizerTest extends TestCase {

    /**
     * 一行パースします。
     */
    public void testParseLine() throws IOException{
        String testStr = "0,127,ts.1101801508,da.2004.11.30,da.2004.11.30,tm.10.30.0,tm.12.0.0,,,,,1,0,\"\",\"資料作成\",\"\"\"第2回 IT効果測定報告会\"\"にむけた資料の作成\r\n\r\n サーバーディスクの容量を計測する\",,,,,,,,0,16\r\n";
        CsvReader tokenizer = new CsvReader(new StringReader(testStr));
        ForwardReader forwardReader = tokenizer.forwardReader;
        
        CsvTokenizer line = new CsvTokenizer(forwardReader);
        while (line.hasNext()) {
            System.out.print("[[" + line.next() + "]]");
        }
    }
}

/* */
