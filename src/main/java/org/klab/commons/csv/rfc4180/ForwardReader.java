/*
 * $Id: ExcelCsvDialect.java 0 2008/01/24 14:17:10 sano-n $
 *
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.rfc4180;

import java.io.IOException;
import java.io.Reader;


/**
 * Reader から1文字ずつ読み込むためのクラスです。
 *
 * @author <a href="mailto:kusanagi@klab.org">Tomonori Kusanagi</a> (kusanagi)
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 */
public class ForwardReader {

    protected static final int NONE = -2;

    protected Reader reader;

    protected int bufferedChar = NONE;

    /**
     * コンストラクタ
     */
    public ForwardReader(Reader reader) {
        this.reader = reader;
    }

    /**
     * 現在の読み込みポイントの次の文字を読み込み、
     * 読み込みポイントをひとつ進めます。
     *
     * @return read character
     */
    protected synchronized int read() throws IOException {
        if (bufferedChar == NONE) {
            return reader.read();
        } else {
            int val = bufferedChar;
            // bufferedChar をクリア
            bufferedChar = NONE;
            return val;
        }
    }

    /**
     * 現在の読み込みポイントの次の文字を調べて返しますが、
     * 読み込みポイントは先に進めません。
     * read メソッドが実行されない限り、同じ文字を返し続けます。
     *
     * @return buffered character
     * @throws IOException
     */
    protected synchronized int check() throws IOException {
        if (bufferedChar == NONE) {
            bufferedChar = reader.read();
            return bufferedChar;
        } else {
            return bufferedChar;
        }
    }
}

/* */
