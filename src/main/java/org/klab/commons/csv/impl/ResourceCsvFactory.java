/*
 * Copyright (c) 2007 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.impl;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.klab.commons.csv.CsvFactory;


/**
 * ResourceCsvFactory.
 * <li> (original)
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (nsano)
 * @version 0.00 070207 nsano initial version <br>
 */
public class ResourceCsvFactory extends AbstractCsvFactory implements CsvFactory {

    private static Log logger = LogFactory.getLog(ResourceCsvFactory.class);

    /** */
    private String resourceName;

    /** for DI */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
logger.debug("CSV resourceName: " + resourceName);
    }

    @Override
    public InputStream getInputStream() {
        return ResourceCsvFactory.class.getResourceAsStream(resourceName);
    }

    @Override
    public OutputStream getOutputStream() {
        throw new UnsupportedOperationException("this class is raed only");
    }

    @Override
    public String toString() {
        return resourceName;
    }
}

/* */
