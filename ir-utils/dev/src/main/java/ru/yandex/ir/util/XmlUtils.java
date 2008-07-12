package ru.yandex.ir.util;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * User: smargolin
 * Date: 13.04.2005
 * Time: 11:52:53
 */
public class XmlUtils {
    public static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"Windows-1251\"?>\n";

    public static String escape(String s) {
        return StringEscapeUtils.escapeXml(s);
    }

    public static String open(String elementName) {
        return open(elementName, null, "");
    }

    public static String open(String elementName, Object c) {
        return open(elementName, c, "");
    }

    public static String open(String elementName, Object c, String paramStr) {
        if (paramStr == null || paramStr.length() == 0)
            return "<" + elementName + ">";
        else
            return "<" + elementName + " " + paramStr + ">";
    }

    public static String close(String elementName) {
        return "</" + elementName + ">\n";
    }

    public static String descape(String s) {
        return StringEscapeUtils.unescapeXml(s);
    }

    public static String getXml(String elementName, String value) {
        return open(elementName) + value + close(elementName);
    }

    public static String getXml(String elementName, int value) {
        return getXml(elementName, "" + value);
    }
}
