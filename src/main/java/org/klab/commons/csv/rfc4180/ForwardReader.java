/*
 * $Id: ExcelCsvDialect.java 0 2008/01/24 14:17:10 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.rfc4180;

import java.io.IOException;
import java.io.Reader;


/**
 * Reader ����1�������ǂݍ��ނ��߂̃N���X�ł��B
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
     * �R���X�g���N�^
     */
    public ForwardReader(Reader reader) {
        this.reader = reader;
    }

    /**
     * ���݂̓ǂݍ��݃|�C���g�̎��̕�����ǂݍ��݁A
     * �ǂݍ��݃|�C���g���ЂƂi�߂܂��B
     * 
     * @return read character
     */
    protected synchronized int read() throws IOException {
        if (bufferedChar == NONE) {
            return reader.read();
        } else {
            int val = bufferedChar;
            // bufferedChar ���N���A
            bufferedChar = NONE;
            return val;
        }
    }

    /**
     * ���݂̓ǂݍ��݃|�C���g�̎��̕����𒲂ׂĕԂ��܂����A
     * �ǂݍ��݃|�C���g�͐�ɐi�߂܂���B
     * read ���\�b�h�����s����Ȃ�����A����������Ԃ������܂��B
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
