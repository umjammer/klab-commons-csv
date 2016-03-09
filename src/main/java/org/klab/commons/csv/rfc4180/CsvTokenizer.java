/*
 * $Id: ExcelCsvDialect.java 0 2008/01/24 14:17:10 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.rfc4180;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * CVS の一行を表します。
 * 
 * @author <a href="mailto:kusanagi@klab.org">Tomonori Kusanagi</a> (kusanagi)
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 * @see "http://www.kasai.fm/wiki/rfc4180jp"
 */
public class CsvTokenizer implements Iterable<String> {

    private static Log logger = LogFactory.getLog(CsvTokenizer.class);

    /** 一般 CSV トークン */
    private static final int TYPE_GENERAL = 0;

    /** 文字列 CSV トークン */
    private static final int TYPE_STRING = 1;

    /** CSV の区切り文字 */
    private static final char SEPARATOR_CHAR = ',';

    /** 引用符 */
    private static final char QUOTE_CHAR = '"';

    /** 引用符のエスケープ文字 */
    private static final char ESCAPE_CHAR = '"';

    /** 読み込み元 CharacterBuffer オブジェクト */
    protected ForwardReader forwardReader;

    /** 行末に達したことを示すフラグ */
    protected boolean endOfLine = false;

    /** ストリームの終わりに達したことを示すフラグ */
    protected boolean endOfStream = false;

    /** */
    protected Iterator<String> iterator;

    /**
     * コンストラクタ
     */
    protected CsvTokenizer(ForwardReader forwardReader) throws IOException {
        this.forwardReader = forwardReader;
        parse();
    }

    /** */
    private List<String> parsedTokens = new ArrayList<>();

    /**
     * CSV の一行をパースします。
     */
    private void parse() throws IOException {
        if (forwardReader.check() == '\r') {
            forwardReader.read();
            if (forwardReader.check() == '\n' || forwardReader.check() == -1) {
                parsedTokens.add("");
            }
        } else if (forwardReader.check() == '\n' || forwardReader.check() == -1) {
            parsedTokens.add("");
        } else {
            while (!endOfLine && !endOfStream) {
                parsedTokens.add(nextToken());
            }
        }
logger.debug("parsedTokens: " + parsedTokens.size());
        iterator = parsedTokens.iterator();
    }

    /**
     * その行にまだ CSV トークンがあるかどうかを返します。
     * 
     * @return 対象行にまだ CSV トークンが残っていれば true
     */
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * 次の CSV トークンを返します。
     * 
     * @throws NoSuchElementException
     */
    public String next() {
        return iterator.next();
    }

    /**
     * Reader から次の CSV トークンを読み込みます。
     * 
     * @param forwardReader
     * @return CSV の列の末尾に達したときは null。
     * @throws IllegalStateException
     */
    protected String nextToken() throws IOException {

        // 初期化
        int type = TYPE_GENERAL;
        StringBuilder sb = new StringBuilder();

        // パース開始
        int c;
        // コンマ直後の空白を読み飛ばし
        do {
            c = forwardReader.read();
        } while (c == ' ' || c == '\t');

        // タイプ判別
        switch (c) {
        case QUOTE_CHAR:
            // 文字列型の始まり
            type = TYPE_STRING;
            break;
        case SEPARATOR_CHAR:
            // いきなりCSV トークンの終わり
            return "";
        case '\n':
            // CSV 行の終り
            endOfLine = true;
            return "";
        default:
            // 一般型の始まり
            sb.append((char) c);
        }

        // ========== 本体のパース
        if (type == TYPE_GENERAL) {
            // 一般型
            return parseGeneralElement(sb);
        } else if (type == TYPE_STRING) {
            return parseStringElement(sb);
        } else {
            throw new IllegalStateException("Illegal csv element type: " + type);
        }
    }

    /**
     * 次の 一般 CSV トークンを読み込みます。
     * 
     * @param sb 2文字目から
     * @return 次の 一般 CSV トークン
     */
    protected String parseGeneralElement(StringBuilder sb) throws IOException {
        String result = null;
        while (true) {
            int c = forwardReader.read();
            switch (c) {
            case SEPARATOR_CHAR:
                // 要素の終り
                result = sb.toString();
                return result;
            case '\r':
                continue;
            case '\n':
                // CSV行の終り
                endOfLine = true;
                result = sb.toString();
                return result;
            case -1:
                // ストリームの終り
                endOfLine = true;
                endOfStream = true;
                result = sb.toString();
                return result;
            default:
                // それ以外は追加
                sb.append((char) c);
            }
        }
    }

    /**
     * 次の文字列 CSV トークンを読み込みます。
     * 
     * @param sb 2文字目から
     * @return 次の文字列 CSV トークン, nullable
     * @throws IllegalArgumentException " で閉じた後に文字が来た
     * @throws IllegalArgumentException " で閉じるまえに EOF
     */
    protected String parseStringElement(StringBuilder sb) throws IOException {
        // 文字列型
        boolean inElement = true;
        while (inElement) {
            int c = forwardReader.read();
            switch (c) {
            case QUOTE_CHAR:
                int cc = forwardReader.read();
                if (cc == ESCAPE_CHAR) {
                    // エスケープされた " だった
                    sb.append((char) c);
                } else if (cc == SEPARATOR_CHAR) {
                    // 要素の終り
                    inElement = false;
                    return sb.toString();
                } else if (cc == '\r') {
                    int ccc = forwardReader.read();
                    if (ccc == '\n' || ccc == -1) {
                        inElement = false;
                        endOfLine = true;
                        return sb.toString();
                    }
                } else if (cc == '\n' || cc == -1) {
                    // CSV行の終りかファイルの終り
                    inElement = false;
                    endOfLine = true;
                    return sb.toString();
                } else {
                    // " で閉じた後に文字が来た→エラー
                    throw new IllegalArgumentException("extra character(s) after closing quotation. first is " + cc);
                }
                break;
            case -1:
                throw new IllegalArgumentException("quotation is not closed at the end of stream.");

            default:
                // それ以外は追加
                sb.append((char) c);
            }
        }
        return null;
    }

    @Override
    public Iterator<String> iterator() {
        return parsedTokens.iterator();
    }

    /** */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String token : parsedTokens) {
            sb.append(token);
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }
}

/* */
