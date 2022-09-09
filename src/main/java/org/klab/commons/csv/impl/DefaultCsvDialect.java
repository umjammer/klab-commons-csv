/*
 * $Id: CsvProvider.java 0 2008/01/24 14:38:23 sano-n $
 *
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.impl;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import org.klab.commons.csv.CsvColumn;
import org.klab.commons.csv.CsvDialect;
import org.klab.commons.csv.Enumerated;

import vavi.beans.BeanUtil;


/**
 * DefaultCsvDialect.
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 */
public class DefaultCsvDialect implements CsvDialect {

    private static Logger logger = Logger.getLogger(DefaultCsvDialect.class.getName());

    /**
     * String to Object conversion.
     *
     * @param field @{@link CsvColumn} annotated field.
     * @param bean bean
     * @param column null or empty の場合、
     *        設定先がプリミティブなら 0, false、ラッパークラスならば null
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object toFieldValue(Field field, Object bean, String column) {
//logger.debug("field: " + ToStringBuilder.reflectionToString(field));
        Class<?> fieldClass = field.getType();
        if (fieldClass.isEnum() && Enumerated.Util.isEnumetated(field)) {
            BeanUtil.setFieldValue(field, bean, Enumerated.Util.<Enum>toFieldValue(field, column));
        } else if (fieldClass.equals(Boolean.class)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? null : Boolean.parseBoolean(column));
        } else if (fieldClass.equals(Boolean.TYPE)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? false : Boolean.parseBoolean(column));
        } else if (fieldClass.equals(Integer.class)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? null : Integer.parseInt(column));
        } else if (fieldClass.equals(Integer.TYPE)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? 0 : Integer.parseInt(column));
        } else if (fieldClass.equals(Short.class)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? null : Short.parseShort(column));
        } else if (fieldClass.equals(Short.TYPE)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? 0 : Short.parseShort(column));
        } else if (fieldClass.equals(Byte.class)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? null : Byte.parseByte(column));
        } else if (fieldClass.equals(Byte.TYPE)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? 0 : Byte.parseByte(column));
        } else if (fieldClass.equals(Long.class)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? null : Long.parseLong(column));
        } else if (fieldClass.equals(Long.TYPE)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? 0 : Long.parseLong(column));
        } else if (fieldClass.equals(Float.class)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? null : Float.parseFloat(column));
        } else if (fieldClass.equals(Float.TYPE)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? 0 : Float.parseFloat(column));
        } else if (fieldClass.equals(Double.class)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? null : Double.parseDouble(column));
        } else if (fieldClass.equals(Double.TYPE)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? 0 : Double.parseDouble(column));
        } else if (fieldClass.equals(Character.class)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? null : Character.valueOf(column.charAt(0))); // TODO ???
        } else if (fieldClass.equals(Character.TYPE)) {
            BeanUtil.setFieldValue(field, bean, column == null || column.isEmpty() ? 0 : Character.valueOf(column.charAt(0))); // TODO ???
        } else {
if (!fieldClass.equals(String.class)) {
 logger.fine("unhandled class: " + fieldClass.getName());
}
            BeanUtil.setFieldValue(field, bean, column);
        }

        return BeanUtil.getFieldValue(field, bean);
    }

    /**
     * Object to String conversion.
     *
     * @param field @{@link CsvColumn} annotated field.
     * @param value field value
     * @return only {@link Iterable} is guaranteed
     */
    @SuppressWarnings("unchecked")
    @Override
    public String toCsvLine(Field field, Object bean, Object value) {
        Class<?> fieldClass = field.getType();
        String column = null;
        if (fieldClass.isEnum() && Enumerated.Util.isEnumetated(field)) {
            column = Enumerated.Util.toCsvString(field, Enum.class.cast(value));
        } else if (fieldClass.equals(String.class)) {
            column = formatString(value == null ? "" : value.toString());
        } else {
logger.fine("unhandled class: " + fieldClass.getName());
            column = value == null ? "" : value.toString();
        }
        return column;
    }

    @Override
    public String getEndOfLine() {
        return "\r\n";
    }

    @Override
    public String formatString(String column) {
        String stage1 = column.replace("\"", "\"\"");
        String stage2 = "\"" + stage1 + "\"";
        return stage2;
    }
}

/* */
