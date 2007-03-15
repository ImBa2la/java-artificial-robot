package ru.styskin.poetry.utils;

import java.util.StringTokenizer;

/**
 * User: smargolin
 * Date: 13.04.2005
 * Time: 11:32:45
 */
public final class StringUtils {
    public static final String DELIMITERS = "qwertyuiopasdfghjklzxcvbnm0123456789 .#*%^$\"\t\n\r:;+,?!()\\/<>'&_|{}�[]`�����-��" + (char) 160;

    public static final String REMOVE_CHARS = "-";
    private static final String EMPTY_STRING = "";

    private StringUtils() {

    }

    /**
     * �������� ��� ������� � ������, ������� ����� ���� ���������������� ��� ��������� � ��������
     *
     * @param string ������
     * @return ������-�������
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
     * ������� ������ �������� ������, �� ������������ ���������� ���������
     *
     * @param string     ������
     * @param delimiters �����������
     * @return ������ �������� �� �����
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
     * ������� �� {@link String#replaceFirst(String,String)} � ��� ��� �� ������������ ���������� ���������
     *
     * @param where ��� ��������
     * @param what  ��� ��������
     * @param with  �� ��� ��������
     * @return ��������� ������
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
