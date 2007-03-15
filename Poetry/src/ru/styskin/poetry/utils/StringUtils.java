package ru.styskin.poetry.utils;

import java.util.StringTokenizer;

/**
 * User: smargolin
 * Date: 13.04.2005
 * Time: 11:32:45
 */
public final class StringUtils {
    public static final String DELIMITERS = "qwertyuiopasdfghjklzxcvbnm0123456789 .#*%^$\"\t\n\r:;+,?!()\\/<>'&_|{}∞[]`СЃЩЂї-ИХ" + (char) 160;

    public static final String REMOVE_CHARS = "-";
    private static final String EMPTY_STRING = "";

    private StringUtils() {

    }

    /**
     * Ёскейпит все символы в строке, которые могут быть интерпретированы как служебные в регэкспе
     *
     * @param string строка
     * @return строка-регэксп
     */
    public static String string2regexp(String string) {
        StringBuilder stringBuilder = new StringBuilder(string.length() * 2);
        for (char c : string.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                stringBuilder.append(c);
            } else {
                stringBuilder.append("\\").append(c);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Ѕыстра€ замена обычному сплиту, не использующа€ регул€рных выражений
     *
     * @param string     строка
     * @param delimiters разделители
     * @return строка разбита€ на части
     */
    public static String[] fastSplit(String string, String delimiters) {
        if (string == null) {
            return null;
        }
        StringTokenizer tokenizer = new StringTokenizer(string, delimiters);
        String[] parts = new String[tokenizer.countTokens()];
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            parts[i++] = tokenizer.nextToken();
        }
        return parts;
    }


    /**
     * ќтличие от {@link String#replaceFirst(String,String)} в том что не используютс€ регул€рные выражение
     *
     * @param where где замен€ем
     * @param what  что замен€ем
     * @param with  на что замен€ем
     * @return результат замены
     */
    public static String secureReplace(String where, String what, String with) {
        return where.replaceFirst(string2regexp(what), with);
    }

    public static String toString(String[] tokens) {
        String str = null;
        for (String token : tokens) {
            str = (str == null) ? token : (!EMPTY_STRING.equals(token)) ? str + " " + token : str;
        }
        return str;
    }

    public static String secureReplaceAll(String where, String what, String with) {
        return where.replaceAll(string2regexp(what), with);
    }

    public static String fastReplace(String string, String what, String with) {
        int index = string.indexOf(what);
        if (index == -1) {
            return string;
        }
        return string.substring(0, index) + with + string.substring(index + what.length());
    }

    public static int matchFromEnd(SingletonString a, SingletonString b) {
    	return matchFromEnd(a.getString(), b.getString());    	
    }
    
    public static int matchFromEnd(String a, String b) {
    	int result = 0;
    	int A = a.length()-1;
    	int B = b.length()-1;
    	while(A >= 0 && B >= 0 && a.charAt(A) == b.charAt(B)) {
    		A --;
    		B --;
    		result ++;
    	}
    	return result;    	
    }
}
