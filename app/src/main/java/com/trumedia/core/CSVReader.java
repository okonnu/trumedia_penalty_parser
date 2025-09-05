package com.trumedia.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader
{
    public static final char DEFAULT_DELIM = ',';
    public static final char DEFAULT_QUOTE = '"';
    public static final String DEFAULT_CHARSET = "utf-8";
    public static final byte[] DEFAULT_BOM_UTF8_PREFIX = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };

    private enum QuoteState { IN, NONE, BEFORE, AFTER }

    private final BufferedReader m_reader;
    private final char m_delimChar;
    private final char m_quoteChar;
    private final String[] m_forcedHeader;

    public CSVReader(File file) throws FileNotFoundException, UnsupportedEncodingException
    {
        this(new FileInputStream(file), DEFAULT_CHARSET, DEFAULT_DELIM, DEFAULT_QUOTE, null);
    }

    public CSVReader(File file, String[] header) throws FileNotFoundException, UnsupportedEncodingException
    {
        this(new FileInputStream(file), DEFAULT_CHARSET, DEFAULT_DELIM, DEFAULT_QUOTE, header);
    }

    public CSVReader(File file, char delimChar, char quoteChar) throws FileNotFoundException, UnsupportedEncodingException
    {
        this(new FileInputStream(file), DEFAULT_CHARSET, delimChar, quoteChar, null);
    }

    public CSVReader(File file, String charSet) throws FileNotFoundException, UnsupportedEncodingException
    {
        this(new FileInputStream(file), charSet, DEFAULT_DELIM, DEFAULT_QUOTE, null);
    }

    public CSVReader(File file, String charSet, char delimChar, char quoteChar) throws FileNotFoundException, UnsupportedEncodingException
    {
        this(new FileInputStream(file), charSet, delimChar, quoteChar, null);
    }

    public CSVReader(InputStream stream) throws UnsupportedEncodingException
    {
        this(stream, DEFAULT_CHARSET, DEFAULT_DELIM, DEFAULT_QUOTE, null);
    }

    public CSVReader(InputStream stream, String[] forcedHeader) throws UnsupportedEncodingException
    {
        this(stream, DEFAULT_CHARSET, DEFAULT_DELIM, DEFAULT_QUOTE, forcedHeader);
    }

    public CSVReader(InputStream stream, char delimChar, char quoteChar) throws UnsupportedEncodingException
    {
        this(stream, DEFAULT_CHARSET, delimChar, quoteChar, null);
    }

    public CSVReader(InputStream stream, String charSet) throws UnsupportedEncodingException
    {
        this(stream, charSet, DEFAULT_DELIM, DEFAULT_QUOTE, null);
    }

    public CSVReader(InputStream stream, String charSet, char delimChar, char quoteChar) throws UnsupportedEncodingException
    {
        this(stream, charSet, delimChar, quoteChar, null);
    }

    public CSVReader(InputStream stream, String charSet, char delimChar, char quoteChar, String[] forcedHeader) throws UnsupportedEncodingException
    {
        m_reader = new BufferedReader(new InputStreamReader(stream, charSet));
        m_delimChar = delimChar;
        m_quoteChar = quoteChar;
        m_forcedHeader = forcedHeader;
    }

    public CSVData readAll(boolean closeReader) throws IOException, ParseException
    {
        try
        {
            // read in the header
            String[] header;
            if (m_forcedHeader == null || m_forcedHeader.length <= 0)
            {
                header = readLine();
                if (header == null)
                    return null;
            }
            else
            {
                header = m_forcedHeader;
            }

            // init the CSV object
            CSVData csv = new CSVData(header);
            String[] line = readLine();
            while (line != null)
            {
                csv.addLine(line);
                line = readLine();
            }
            return csv;
        }
        finally
        {
            if (closeReader)
            {
                close();
            }
        }
    }

    public String[] readLine() throws IOException, ParseException
    {
        // get next line
        String prevLineStr = "";
        String lineStr = m_reader.readLine();
        while (true)
        {
            if (lineStr == null)
                return null;
            lineStr = lineStr.trim();
            if (lineStr.length() > 0)
            {
                int quotChars = countQuotes(prevLineStr + lineStr);
                if (quotChars % 2 == 0)
                    break;
                else
                    prevLineStr += lineStr;
            }
            lineStr = m_reader.readLine();
        }

        // get each column value
        return parseLine(prevLineStr + lineStr, m_quoteChar, m_delimChar);
    }

    public void close() throws IOException
    {
        m_reader.close();
    }

    public static String[] parseLine(String line, char quoteChar, char delimChar) throws ParseException
    {
        List<String> parts = new ArrayList<String>();
        StringBuilder cur = new StringBuilder();
        QuoteState quoteState = QuoteState.BEFORE;
        for (int i = 0; i < line.length(); i++)
        {
            char c = line.charAt(i);
            switch (quoteState)
            {
                case IN:
                {
                    if (c == quoteChar)
                    {
                        quoteState = QuoteState.AFTER;
                    }
                    else
                    {
                        cur.append(c);
                    }
                    break;
                }
                case NONE:
                {
                    if (c == delimChar)
                    {
                        parts.add(cur.toString().trim());
                        cur = new StringBuilder();
                        quoteState = QuoteState.BEFORE;
                    }
                    else
                    {
                        cur.append(c);
                    }
                    break;
                }
                case BEFORE:
                {
                    if (c == delimChar)
                    {
                        parts.add("");
                    }
                    else if (c == quoteChar)
                    {
                        quoteState = QuoteState.IN;
                    }
                    else if (!Character.isWhitespace(c))
                    {
                        quoteState = QuoteState.NONE;
                        cur.append(c);
                    }
                    break;
                }
                case AFTER:
                {
                    if (c == quoteChar)
                    {
                        quoteState = QuoteState.IN;
                        cur.append(quoteChar);
                    }
                    else if (c == delimChar)
                    {
                        parts.add(cur.toString());
                        cur = new StringBuilder();
                        quoteState = QuoteState.BEFORE;
                    }
                    else if (!Character.isWhitespace(c))
                    {
                        throw new ParseException(line, i);
                    }
                    break;
                }
            }
        }
        parts.add(cur.toString());
        return parts.toArray(new String[parts.size()]);
    }

    protected int countQuotes(String line)
    {
        int count = 0;
        for (int i = 0; i < line.length(); i++)
        {
            char c = line.charAt(i);
            if (c == m_quoteChar)
                count++;
        }
        return count;
    }
}
