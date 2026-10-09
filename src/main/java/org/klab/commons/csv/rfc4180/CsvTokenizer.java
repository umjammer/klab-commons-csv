/*
 * $Id: CsvTokenizer.java 0 2008/01/24 14:17:10 sano-n $
 *
 * Copyright (C) 2008 KLab Inc. All Rights Reserved.
 */

package org.klab.commons.csv.rfc4180;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.System.getLogger;


/**
 * This represents a single line in a CVS file.
 *
 * @author <a href="mailto:kusanagi@klab.org">Tomonori Kusanagi</a> (kusanagi)
 * @author <a href="mailto:sano-n@klab.org">Naohide Sano</a> (sano-n)
 * @version $Revision: 1.0 $ $Date: 2008/01/24 14:38:23 $ $Author: sano-n $
 * @see "http://www.kasai.fm/wiki/rfc4180jp"
 */
public class CsvTokenizer implements Iterable<String> {

    private static final Logger logger = getLogger(CsvTokenizer.class.getName());

    /** General CSV token */
    private static final int TYPE_GENERAL = 0;

    /** String CSV token */
    private static final int TYPE_STRING = 1;

    /** CSV delimiter */
    private static final char SEPARATOR_CHAR = ',';

    /** quotation marks */
    private static final char QUOTE_CHAR = '"';

    /** Quote escape character */
    private static final char ESCAPE_CHAR = '"';

    /** Source CharacterBuffer object */
    protected ForwardReader forwardReader;

    /** A flag indicating that the end of the line has been reached. */
    protected boolean endOfLine = false;

    /** A flag indicating that the end of the stream has been reached. */
    protected boolean endOfStream = false;

    /** */
    protected Iterator<String> iterator;

    /**
     * Constructor.
     */
    protected CsvTokenizer(ForwardReader forwardReader) throws IOException {
        this.forwardReader = forwardReader;
        parse();
    }

    /** */
    private final List<String> parsedTokens = new ArrayList<>();

    /**
     * This parses a single line from a CSV file.
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
logger.log(Level.DEBUG, "parsedTokens: " + parsedTokens.size());
        iterator = parsedTokens.iterator();
    }

    /**
     * This returns whether there is still a CSV token in that row.
     *
     * @return If the target row still contains a CSV token, the result is true.
     */
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * The following CSV token will be returned.
     *
     * @throws NoSuchElementException no next token
     */
    public String next() {
        return iterator.next();
    }

    /**
     * Read the following CSV token from Reader:
     *
     * @return When the end of a column in a CSV file is reached, it returns null.
     * @throws IllegalStateException
     */
    protected String nextToken() throws IOException {

        // Initialization
        int type = TYPE_GENERAL;
        StringBuilder sb = new StringBuilder();

        // parse start
        int c;
        // Skip the space immediately following the comma.
        do {
            c = forwardReader.read();
        } while (c == ' ' || c == '\t');

        // Type identification
        switch (c) {
        case QUOTE_CHAR:
            // Beginning of string type
            type = TYPE_STRING;
            break;
        case SEPARATOR_CHAR:
            // Suddenly, the end of the CSV token.
            return "";
        case '\n':
            // End of CSV row
            endOfLine = true;
            return "";
        default:
            // The beginning of the general type
            sb.append((char) c);
        }

        // Main unit parse
        if (type == TYPE_GENERAL) {
            // General type
            return parseGeneralElement(sb);
        } else if (type == TYPE_STRING) {
            return parseStringElement(sb);
        } else {
            throw new IllegalStateException("Illegal csv element type: " + type);
        }
    }

    /**
     * The following general CSV token will be loaded.
     *
     * @param sb Starting from the second character
     * @return The following general CSV token
     */
    protected String parseGeneralElement(StringBuilder sb) throws IOException {
        String result = null;
        while (true) {
            int c = forwardReader.read();
            switch (c) {
            case SEPARATOR_CHAR:
                // End of element
                result = sb.toString();
                return result;
            case '\r':
                continue;
            case '\n':
                // End of CSV row
                endOfLine = true;
                result = sb.toString();
                return result;
            case -1:
                // End of stream
                endOfLine = true;
                endOfStream = true;
                result = sb.toString();
                return result;
            default:
                // Add anything else
                sb.append((char) c);
            }
        }
    }

    /**
     * The following string CSV token will be read.
     *
     * @param sb Starting from the second character
     * @return The following string is a CSV token, nullable.
     * @throws IllegalArgumentException The text came after the closing "
     * @throws IllegalArgumentException Before closing with " EOF
     */
    protected String parseStringElement(StringBuilder sb) throws IOException {
        // string type
        boolean inElement = true;
        while (inElement) {
            int c = forwardReader.read();
            switch (c) {
            case QUOTE_CHAR:
                int cc = forwardReader.read();
                if (cc == ESCAPE_CHAR) {
                    // It was an escaped "
                    sb.append((char) c);
                } else if (cc == SEPARATOR_CHAR) {
                    // End of element
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
                    // End of CSV row or end of file
                    inElement = false;
                    endOfLine = true;
                    return sb.toString();
                } else {
                    // If text appears after closing with ", it results in an error.
                    throw new IllegalArgumentException("extra character(s) after closing quotation. first is " + cc);
                }
                break;
            case -1:
                throw new IllegalArgumentException("quotation is not closed at the end of stream.");

            default:
                // Add anything else
                sb.append((char) c);
            }
        }
        return null;
    }

    @Override
    public Iterator<String> iterator() {
        return parsedTokens.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String token : parsedTokens) {
            sb.append(token);
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
