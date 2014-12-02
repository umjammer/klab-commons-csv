/*
 * Created on 2004/12/19
 */

package org.klab.commons.csv.rfc4180;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author <a href="mailto:kusanagi@klab.org">Tomonori Kusanagi</a> (kusanagi)
 */
public class CsvTokenizerTest {

    static final String testStr = "0,127,ts.1101801508,da.2004.11.30,da.2004.11.30,tm.10.30.0,tm.12.0.0,,,,,1,0,\"\",\"資料作成\",\"\"\"第2回 IT効果測定報告会\"\"にむけた資料の作成\r\n\r\n サーバーディスクの容量を計測する\",,,,,,,,0,16\r\n";
    static final String[] resultStr = {
        "0", "127", "ts.1101801508", "da.2004.11.30", "da.2004.11.30", "tm.10.30.0", "tm.12.0.0", "", "", "", "", "1", "0",
        "", "資料作成", "\"第2回 IT効果測定報告会\"にむけた資料の作成\r\n\r\n サーバーディスクの容量を計測する",
        "", "", "", "", "", "", "", "0", "16"
    };

    /**
     * 一行パースします。
     */
    @Test
    public void testParseLine() throws IOException {
        CsvReader tokenizer = new CsvReader(new StringReader(testStr));
        ForwardReader forwardReader = tokenizer.forwardReader;
        
        CsvTokenizer line = new CsvTokenizer(forwardReader);
        int c = 0;
        while (line.hasNext()) {
            String result = line.next();
            System.out.println(c + "[" + result + "]");
            assertEquals(resultStr[c++], result);
        }
    }
}

/* */
