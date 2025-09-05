package com.trumedia.core;

import java.util.ArrayList;
import java.util.List;

public class StringUtil
{
    public static boolean isEmpty(String str)
    {
        return (str == null || str.length() == 0 || str.trim().length() == 0);
    }

    public static boolean isIn(String str, String ... options)
    {
        if (str == null || options == null)
            return false;
        for (String o : options)
        {
            if (str.equalsIgnoreCase(o))
                return true;
        }
        return false;
    }

    public static List<String> splitAndTrim(String line, String delim)
    {
        return splitAndTrim(line, delim, Integer.MAX_VALUE);
    }

    public static List<String> splitAndTrim(String line, String delim, int maxParts)
    {
        List<String> parts = new ArrayList<String>();
        if (!StringUtil.isEmpty(line))
        {
            int startIndex = 0;
            int endIndex = line.indexOf(delim);
            while (endIndex >= 0 && parts.size() < maxParts - 1)
            {
                // extract part
                String str = line.substring(startIndex, endIndex).trim();
                if (str.length() > 0)
                    parts.add(str);

                // next item
                startIndex = endIndex + delim.length();
                endIndex = line.indexOf(delim, startIndex);
            }

            // extract last part
            String str = line.substring(startIndex).trim();
            if (str.length() > 0)
                parts.add(str);
        }
        return parts;
    }
}
