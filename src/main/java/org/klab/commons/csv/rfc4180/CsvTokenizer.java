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
 * CVS �̈�s��\���܂��B
 * 
 * @author <a href="mailto:kusanagi@klab.org">Tomonori Kusanagi</a> (kusanagi)
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 * @see "http://www.kasai.fm/wiki/rfc4180jp"
 */
public class CsvTokenizer implements Iterable<String> {

    private static Log logger = LogFactory.getLog(CsvTokenizer.class);

    /** ��� CSV �g�[�N�� */
    private static final int TYPE_GENERAL = 0;

    /** ������ CSV �g�[�N�� */
    private static final int TYPE_STRING = 1;

    /** CSV �̋�؂蕶�� */
    private static final char SEPARATOR_CHAR = ',';

    /** ���p�� */
    private static final char QUOTE_CHAR = '"';

    /** ���p���̃G�X�P�[�v���� */
    private static final char ESCAPE_CHAR = '"';

    /** �ǂݍ��݌� CharacterBuffer �I�u�W�F�N�g */
    protected ForwardReader forwardReader;

    /** �s���ɒB�������Ƃ������t���O */
    protected boolean endOfLine = false;

    /** �X�g���[���̏I���ɒB�������Ƃ������t���O */
    protected boolean endOfStream = false;

    /** */
    protected Iterator<String> iterator;

    /**
     * �R���X�g���N�^
     */
    protected CsvTokenizer(ForwardReader forwardReader) throws IOException {
        this.forwardReader = forwardReader;
        parse();
    }

    /** */
    private List<String> parsedTokens = new ArrayList<String>();

    /**
     * CSV �̈�s���p�[�X���܂��B
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
     * ���̍s�ɂ܂� CSV �g�[�N�������邩�ǂ�����Ԃ��܂��B
     * 
     * @return �Ώۍs�ɂ܂� CSV �g�[�N�����c���Ă���� true
     */
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * ���� CSV �g�[�N����Ԃ��܂��B
     * 
     * @throws NoSuchElementException
     */
    public String next() {
        return iterator.next();
    }

    /**
     * Reader ���玟�� CSV �g�[�N����ǂݍ��݂܂��B
     * 
     * @param forwardReader
     * @return CSV �̗�̖����ɒB�����Ƃ��� null�B
     * @throws IllegalStateException
     */
    protected String nextToken() throws IOException {

        // ������
        int type = TYPE_GENERAL;
        StringBuilder sb = new StringBuilder();

        // �p�[�X�J�n
        int c;
        // �R���}����̋󔒂�ǂݔ�΂�
        do {
            c = forwardReader.read();
        } while (c == ' ' || c == '\t');

        // �^�C�v����
        switch (c) {
        case QUOTE_CHAR:
            // ������^�̎n�܂�
            type = TYPE_STRING;
            break;
        case SEPARATOR_CHAR:
            // �����Ȃ�CSV �g�[�N���̏I���
            return "";
        case '\n':
            // CSV �s�̏I��
            endOfLine = true;
            return "";
        default:
            // ��ʌ^�̎n�܂�
            sb.append((char) c);
        }

        // ========== �{�̂̃p�[�X
        if (type == TYPE_GENERAL) {
            // ��ʌ^
            return parseGeneralElement(sb);
        } else if (type == TYPE_STRING) {
            return parseStringElement(sb);
        } else {
            throw new IllegalStateException("Illegal csv element type: " + type);
        }
    }

    /**
     * ���� ��� CSV �g�[�N����ǂݍ��݂܂��B
     * 
     * @param sb 2�����ڂ���
     * @return ���� ��� CSV �g�[�N��
     */
    protected String parseGeneralElement(StringBuilder sb) throws IOException {
        String result = null;
        while (true) {
            int c = forwardReader.read();
            switch (c) {
            case SEPARATOR_CHAR:
                // �v�f�̏I��
                result = sb.toString();
                return result;
            case '\r':
                continue;
            case '\n':
                // CSV�s�̏I��
                endOfLine = true;
                result = sb.toString();
                return result;
            case -1:
                // �X�g���[���̏I��
                endOfLine = true;
                endOfStream = true;
                result = sb.toString();
                return result;
            default:
                // ����ȊO�͒ǉ�
                sb.append((char) c);
            }
        }
    }

    /**
     * ���̕����� CSV �g�[�N����ǂݍ��݂܂��B
     * 
     * @param sb 2�����ڂ���
     * @return ���̕����� CSV �g�[�N��, nullable
     * @throws IllegalArgumentException " �ŕ�����ɕ���������
     * @throws IllegalArgumentException " �ŕ���܂��� EOF
     */
    protected String parseStringElement(StringBuilder sb) throws IOException {
        // ������^
        boolean inElement = true;
        while (inElement) {
            int c = forwardReader.read();
            switch (c) {
            case QUOTE_CHAR:
                int cc = forwardReader.read();
                if (cc == ESCAPE_CHAR) {
                    // �G�X�P�[�v���ꂽ " ������
                    sb.append((char) c);
                } else if (cc == SEPARATOR_CHAR) {
                    // �v�f�̏I��
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
                    // CSV�s�̏I�肩�t�@�C���̏I��
                    inElement = false;
                    endOfLine = true;
                    return sb.toString();
                } else {
                    // " �ŕ�����ɕ������������G���[
                    throw new IllegalArgumentException("extra character(s) after closing quotation. first is " + cc);
                }
                break;
            case -1:
                throw new IllegalArgumentException("quotation is not closed at the end of stream.");

            default:
                // ����ȊO�͒ǉ�
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
