/*
 * Copyright (c) 2003 by K Laboratory Co., Ltd., All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.csv.simple;

import java.util.Enumeration;
import java.util.NoSuchElementException;


/**
 * A class that parses a single line of data in CSV format and breaks it down into its individual fields.
 * Something like {@link java.util.StringTokenizer} that supports CSV format.
 *
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 */
public class CsvTokenizer implements Enumeration<String> {
    /** Target string */
    private final String source;
    /** Next read position */
    private int currentPosition;
    /** */
    private final int maxPosition;

    /**
     * Create an instance of CSVTokenizer to parse lines in CSV format.
     *
     * @param line CSV format string TODO: Does not include newline characters.
     */
    public CsvTokenizer(String line) {
        source = line;
        currentPosition = 0;
        maxPosition = line.length();
    }

    /**
     * Returns the position of the next comma.
     * If there are no remaining commas, then nextComma() == maxPosition.
     * Also, if the last item is empty, nextComma() == maxPosition.
     *
     * @param ind Start location for search
     * @return The position of the next comma. If there is no comma, the value represents the length of the string.
     */
    private int nextComma(int ind) {
        boolean inquote = false;
        while (ind < maxPosition) {
            char ch = source.charAt(ind);
            if (!inquote && ch == ',') {
                break;
            }
            else if ('"' == ch) {
                inquote = !inquote; // This also handles the "" part correctly.
            }
            ind ++;
        }
        return ind;
    }

    /**
     * Returns the number of items included.
     *
     * @return Number of items included
     */
    public int countTokens() {
        int i = 0;
        int ret = 1;
        while ((i = nextComma(i)) < maxPosition) {
            i ++;
            ret ++;
        }
        return ret;
    }

    /**
     * Returns the string for the next item.
     *
     * @return Next item
     * @exception NoSuchElementException When there are no items left
     */
    public String nextToken() {
        // The last item cannot be processed correctly using ">=".
        // An exception will occur if the last item is empty (the line ends with a comma).
        if (currentPosition > maxPosition) {
            throw new NoSuchElementException(this + "#nextToken");
        }

        int st = currentPosition;
        currentPosition = nextComma(currentPosition);

        StringBuilder strb = new StringBuilder();
        while (st < currentPosition) {
            char ch = source.charAt(st++);
            if (ch == '"') {
                // When " appears alone, do nothing.
                if ((st < currentPosition) && (source.charAt(st) == '"')) {
                    strb.append(ch);
                    st ++;
                }
            } else {
                strb.append(ch);
            }
        }
        currentPosition ++;
        return new String(strb);
    }

    /**
     * Similar to the <code>nextToken</code> method, it returns the string for the next item.<br>
     * However, the return value is of type Object, not String.<br>
     * This method exists because it implements {@link Enumeration}.
     *
     * @return Next item
     * @exception NoSuchElementException When there are no items left
     * @see java.util.Enumeration
     * @see #nextElement()
     */
    public String nextElement() {
        return nextToken();
    }

    /**
     * Check if there are any items left.
     *
     * @return If there are still items remaining, then it's true.
     */
    public boolean hasMoreTokens() {
        // Using "<" instead of "<=" will not correctly process the last item.
        return (nextComma(currentPosition) <= maxPosition);
    }

    /**
     * Similar to the <code>hasMoreTokens</code> method, this checks if there are still items remaining.<br>
     * This method exists because it implements {@link Enumeration}.
     *
     * @return If there are still items remaining, then it's true.
     * @see java.util.Enumeration
     * @see #hasMoreTokens()
     */
    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    /**
     * Returns a string representation of the instance.
     * TODO
     * @return The string representation of the instance.
     */
    @Override
    public String toString() {
        return source;
    }
}
