/*
 * $Id: DefaultCsvConverter.java 0 2008/01/24 14:17:10 sano-n $
 *
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.klab.commons.csv.CsvColumn;
import org.klab.commons.csv.CsvConverter;
import org.klab.commons.csv.CsvDialect;
import org.klab.commons.csv.CsvEntity;
import org.klab.commons.csv.Dialectal;
import org.klab.commons.csv.spi.CsvLine;

import vavi.beans.BeanUtil;


/**
 * DefaultCsvConverter.
 * <li> (spi, jca)
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:17:10 $ $Author: sano-n $
 */
public class DefaultCsvConverter implements CsvConverter {

    private static final Logger logger = Logger.getLogger(DefaultCsvConverter.class.getName());

    /** */
    protected Class<?> entityClass;

    /** */
    private List<Field> fields;

    /** */
    protected CsvDialect defaultCsvDialect = new DefaultCsvDialect();

    /** */
    protected CsvDialect csvDialect;

    /**
     * @param entityClass should be {@link CsvColumn} annotated
     */
    public DefaultCsvConverter(Class<?> entityClass, CsvDialect csvDialect) {
        this.entityClass = entityClass;
        this.csvDialect = csvDialect;
        this.fields = CsvEntity.Util.getFields(entityClass);
    }

    /**
     * for write.
     * @param entity {@link org.klab.commons.csv.CsvEntity} annotated object
     * @return includes {@link CsvDialect#getEndOfLine() end of line}, String, Date type are '"' quoted
     * @see CsvConverter#toCsv(Object)
     */
    @Override
    public String toCsv(Object entity) {
        StringBuilder sb = new StringBuilder();

        List<String> columns = new ArrayList<>();

        for (Field field : fields) {
            // TODO when gap sequences
            try {
                Object fieldValue = BeanUtil.getFieldValue(field, entity);
                if (Dialectal.Util.isDialectal(field)) {
                    columns.add(csvDialect.toCsvLine(field, entity, fieldValue));
                } else {
                    columns.add(defaultCsvDialect.toCsvLine(field, entity, fieldValue));
                }
            } catch (Exception e) {
                throw new IllegalStateException("field[" + CsvColumn.Util.getSequence(field) + "]: " + field.getType().getSimpleName() + " " + field.getName(), e);
            }
        }
        for (String column : columns) {
            sb.append(column);
            sb.append(',');
        }
//logger.debug("csv: " + sb.substring(0, sb.length() - 1));
        return sb.substring(0, sb.length() - 1) + csvDialect.getEndOfLine();
    }

    /**
     * for read.
     * {@link #fields field definition} have priority
     * @return entity {@link org.klab.commons.csv.CsvEntity} annotated object
     * @see CsvConverter#toEntity(CsvLine)
     * @throws IllegalStateException when entity initialize failed
     */
    @Override
    public Object toEntity(CsvLine columns) {
        Object entity = null;
        try {
            entity = entityClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        List<String> csvColumns = new ArrayList<>();
        columns.forEachRemaining(csvColumns::add);

        for (Field field : fields) {
            int sequence = CsvColumn.Util.getSequence(field);
            if (sequence > 0 && sequence <= csvColumns.size()) {
                String column = csvColumns.get(sequence - 1);
                if (column != null && !column.isEmpty() && !column.equals("\r") && !column.equals("\n") && !column.equals("\r\n")) { // TODO performance
//Debug.println("field[" + CsvColumn.Util.getSequence(field) + "]: " + field.getType().getSimpleName() + " " + field.getName() + " = '" + column + "'\n" + StringUtil.getDump(column));
                    try {
                        if (Dialectal.Util.isDialectal(field)) {
                            csvDialect.toFieldValue(field, entity, column);
                        } else {
                            defaultCsvDialect.toFieldValue(field, entity, column);
                        }
                    } catch (Exception e) {
                        throw new IllegalStateException("field[" + CsvColumn.Util.getSequence(field) + "]: " + field.getType().getSimpleName() + " " + field.getName() + " = '" + column + "'", e);
                    }
                } else {
logger.warning("field[" + CsvColumn.Util.getSequence(field) + "]: " + field.getType().getSimpleName() + " " + field.getName() + " is empty");
                }
            } else {
logger.severe("line: [" + columns + "]");
                throw new IndexOutOfBoundsException("column " + sequence + "/" + csvColumns.size());
            }
        }
if (csvColumns.size() > fields.size()) {
 logger.fine("columns: pojo: " + fields.size() + " < csv: " + csvColumns.size());
}
//logger.debug("entity: " + ToStringBuilder.reflectionToString(entity));
        return entity;
    }
}

/* */
