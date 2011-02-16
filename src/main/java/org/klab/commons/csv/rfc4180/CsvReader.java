/*
 * $Id: CsvReader.java 0 2008/01/24 14:17:10 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.rfc4180;

import java.io.IOException;
import java.io.Reader;


/**
 * CSV ��v�f�ɋ�؂��ďo�͂��܂��B
 * <p>
 * <li>��s�̃J�������͉ςł���B
 * <li>����������ׂ��J�����͕K�� " (�_�u���N�H�[�e�[�V����) �ň͂܂��B(�����������Ă��Ȃ��Ă� " �ň͂܂��B)
 * <li>��s�͏I�[�͉��s�ŕ\�����B �������A""�̒��ŉ��s�����݂��Ă������͈�s�̏I�[�ł͂Ȃ��B
 * <li> "" �̒��� " �� " �ŃG�X�P�[�v�����B
 * </p>
 * 
 * @author <a href="mailto:kusanagi@klab.org">Tomonori Kusanagi</a> (kusanagi)
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 */
public class CsvReader {

    // �ǂݍ��݌� ForwardReader �I�u�W�F�N�g
    protected ForwardReader forwardReader;

    /**
     * Reader ���Z�b�g���܂��B
     * 
     * @param reader
     */
    public CsvReader(Reader reader) {
        this.forwardReader = new ForwardReader(reader);
    }

    /**
     * ����ɍs������ꍇ�� true ��Ԃ��܂��B
     * 
     * @return ����ɍs������ꍇ�� true
     */
    public boolean hasNext() throws IOException {
        // �X�g���[���̏I���ɒB���Ă��邩�ǂ����`�F�b�N
        if (forwardReader.check() == -1) {
            return false;
        }
        return true;
    }

    /**
     * ���̍s��Ԃ��܂��B
     * 
     * @return ���̍s
     */
    public CsvTokenizer next() throws IOException {
        synchronized (forwardReader) {
            CsvTokenizer line = new CsvTokenizer(forwardReader);
            return line;
        }
    }
}

/* */
