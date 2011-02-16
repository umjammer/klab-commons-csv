/*
 * $Id: CsvReader.java 0 2008/01/24 14:17:10 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.rfc4180;

import java.io.IOException;
import java.io.Reader;


/**
 * CSV を要素に区切って出力します。
 * <p>
 * <li>一行のカラム数は可変である。
 * <li>文字が入るべきカラムは必ず " (ダブルクォーテーション) で囲まれる。(文字が入っていなくても " で囲まれる。)
 * <li>一行は終端は改行で表される。 しかし、""の中で改行が存在してもそれらは一行の終端ではない。
 * <li> "" の中の " は " でエスケープされる。
 * </p>
 * 
 * @author <a href="mailto:kusanagi@klab.org">Tomonori Kusanagi</a> (kusanagi)
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 */
public class CsvReader {

    // 読み込み元 ForwardReader オブジェクト
    protected ForwardReader forwardReader;

    /**
     * Reader をセットします。
     * 
     * @param reader
     */
    public CsvReader(Reader reader) {
        this.forwardReader = new ForwardReader(reader);
    }

    /**
     * さらに行がある場合に true を返します。
     * 
     * @return さらに行がある場合に true
     */
    public boolean hasNext() throws IOException {
        // ストリームの終わりに達しているかどうかチェック
        if (forwardReader.check() == -1) {
            return false;
        }
        return true;
    }

    /**
     * 次の行を返します。
     * 
     * @return 次の行
     */
    public CsvTokenizer next() throws IOException {
        synchronized (forwardReader) {
            CsvTokenizer line = new CsvTokenizer(forwardReader);
            return line;
        }
    }
}

/* */
