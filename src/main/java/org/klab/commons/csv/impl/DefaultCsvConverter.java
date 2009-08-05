/*
 * $Id: CsvDialect.java 0 2008/01/24 14:17:10 sano-n $
 * 
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.klab.commons.csv.CsvColumn;
import org.klab.commons.csv.CsvConverter;
import org.klab.commons.csv.CsvDialect;
import org.klab.commons.csv.Dialectal;
import org.klab.commons.csv.Enumerated;
import org.klab.commons.csv.spi.CsvLine;

import vavi.beans.BeanUtil;


/**
 * DefaultCsvConverter.
 * <li> (spi, jca)
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (nsano)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public class DefaultCsvConverter implements CsvConverter {

    private static Log logger = LogFactory.getLog(DefaultCsvConverter.class);

    /** */
    protected Class<?> entityClass;

    /** for convenience */
    protected Set<Field> fields;

    /** */
    protected CsvDialect csvDialect;

    /**
     * @param entityClass should be {@link CsvColumn} annotated
     */
    public DefaultCsvConverter(Class<?> entityClass, CsvDialect csvDialect) {
        this.entityClass = entityClass;
        this.fields = CsvColumn.Util.getFields(entityClass);
        this.csvDialect = csvDialect;
    }

    /**
     * @param entity {@link org.klab.commons.csv.CsvEntity} annotated object
     * @return includes {@link CsvDialect#getEndOfLine() end of line}, String, Date type are '"' quoted 
     * @see CsvConverter#toCsv(Object)
     */
    @Override
    public String toCsv(Object entity) {
        StringBuilder sb = new StringBuilder(); 

        for (Field field : fields) {
            CsvLine columns = getFieldValueAsString(field, entity);
            for (String column : columns) {
                sb.append(column);
                sb.append(',');
            }
        }
//logger.debug("csv: " + sb.substring(0, sb.length() - 1));
        return sb.substring(0, sb.length() - 1) + csvDialect.getEndOfLine();
    }

    /**
     * {@link #fields field の定義}優先。
     * @return entity {@link org.klab.commons.csv.CsvEntity} annotated object
     * @see CsvConverter#toEntity(CsvLine)
     * @throws IllegalStateException when entity initialize failed
     */
    @Override
    public Object toEntity(CsvLine columns) {
        Object entity = null;
        try {
            entity = entityClass.newInstance();
        } catch (Exception e) {
            throw (RuntimeException) new IllegalStateException().initCause(e);
        }

        for (Field field : fields) {
            if (columns.hasNext()) {
                setFieldValueByStrings(field, entity, columns);
            } else {
logger.error("line: [" + columns + "]");
                throw new IndexOutOfBoundsException("columns < " + fields.size());
            }
        }
if (columns.hasNext()) {
 logger.warn("columns > " + fields.size());
}
//logger.debug("entity: " + ToStringBuilder.reflectionToString(entity));
        return entity;
    }

    /**
     * Object to String conversion.
     *
     * @param field @{@link CsvColumn} annotated field.
     * @param bean bean
     * @return only {@link Iterable} is guaranteed
     */
    @SuppressWarnings("unchecked")
    protected CsvLine getFieldValueAsString(Field field, Object bean) {
        Class<?> fieldClass = field.getType();
        Object fieldValue = BeanUtil.getFieldValue(field, bean);
        if (Dialectal.Util.isDialectal(field)) {
            return csvDialect.toCsvLine(field, bean, fieldValue);
        } else {
            List<String> columns = new ArrayList<String>();
            if (fieldClass.isEnum() && Enumerated.Util.isEnumetated(field)) {
                columns.add(Enumerated.Util.toCsvString(field, Enum.class.cast(fieldValue)));
            } else if (fieldClass.equals(String.class)) {
                columns.add(csvDialect.formatString(fieldValue == null ? "" : fieldValue.toString()));
            } else {
logger.debug("unhandled class: " + fieldClass.getName());
                columns.add(fieldValue == null ? "" : fieldValue.toString());
            }
            return (CsvLine) columns;
        }
    }

    /**
     * String to Object conversion.
     *
     * @param field @{@link CsvColumn} annotated field.
     * @param bean bean
     * @param columns column が null or empty の場合、
     *        設定先がプリミティブなら 0, false、ラッパークラスならば null
     */
    @SuppressWarnings("unchecked")
    protected void setFieldValueByStrings(Field field, Object bean, CsvLine columns) {
//logger.debug("field: " + ToStringBuilder.reflectionToString(field));
        Class<?> fieldClass = field.getType();
        if (Dialectal.Util.isDialectal(field)) {
            BeanUtil.setFieldValue(field, bean, csvDialect.toFieldValue(field, bean, columns));
        } else {
            String column = columns.next();
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
 logger.debug("unhandled class: " + fieldClass.getName());
}
                BeanUtil.setFieldValue(field, bean, column);
            }
        }
    }
}

/* */
