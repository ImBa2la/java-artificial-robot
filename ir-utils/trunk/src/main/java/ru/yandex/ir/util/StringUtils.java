package ru.yandex.ir.util;

import javolution.text.Text;
import javolution.context.ObjectFactory;

import java.util.*;
import java.util.regex.Pattern;

/**
 * User: smargolin
 * Date: 13.04.2005
 * Time: 11:32:45
 */
public final class StringUtils {
    public static final String DELIMITERS = " @\"\t\n\r,.:;()/-*<>'&_�[]��`���+��" + (char) 160;
    public static final String NOTEBOOK_DELIMITERS = " @\"\t\n\r*:;,()/-��<>'&_|{}�[]+`�����" + (char) 160;

    public static final String CLASSIFICATOR_DELIMITERS = " ~@#*%^$\"\t\n\r:;+,?!()\\/<>'&_|{}�[]`�����-��������=��?��<>�" + (char) 160;

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
     * ����������� ������:<br>
     * 1. ������� ������ ���������� �������<br>
     * 2. ��������� � lowercase<br>
     * 3. ��������� ��������� �����<br>
     * 4. �������� ������� ������� �� ����� �� �� ��������� ���������� ({@link #RUS_2_EN_REPLACEMENTS})<br>
     *
     * @param string ������ ��� ������������
     * @return ��������������� ������, ���� string != null, ������ ������ � ��������� ������
     */
    public static String normalize(String string) {
        if (string == null) {
            return EMPTY_STRING;
        }
        StringTokenizer st = new StringTokenizer(string, DELIMITERS);
        String s = EMPTY_STRING;
        while (st.hasMoreTokens()) {
            if (s.length() > 0) {
                s = s + " ";
            }
            s = s + st.nextToken();
        }
        return normalizeRus2En(normalizeToNumeric(s.toLowerCase()));
    }

    public static final char RUS_2_EN_REPLACEMENTS[][] =
            {{'�', 'c'}, {'�', 'e'}, {'�', 'o'}, {'�', 'p'},
             {'�', 'a'}, {'�', 'k'}, {'�', 'y'}, {'�', 'x'},
             {'�', 'b'}, {'�', 't'}, {'�', 'm'}};// rus -> en

    /**
     * ����������� ������: �������� ������� ������� �� ����� �� �� ��������� ���������� ({@link #RUS_2_EN_REPLACEMENTS})
     *
     * @param string ������ ��� ������������
     * @return ��������������� ������
     */
    public static String normalizeRus2En(String string) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            buf.append(rusEscape(string.charAt(i)));
        }
        return buf.toString();
    }

    private static char rusEscape(char c) {
        for (char[] replace : RUS_2_EN_REPLACEMENTS) {
            if (replace[0] == c)
                return replace[1];
        }
        return c;
    }

    /**
     * ����������� ������: ��������� ��������� �����
     *
     * @param string ������ ��� ������������
     * @return ��������������� ������
     */
    public static String normalizeToNumeric(String string) {
        return normalizeToNumeric(string, new char[]{32});
    }

    public static String normalizeToNumeric(String string, char[] spaceSymbols) {
        if (string.length() < 2) {
            return string;
        }

        StringBuffer buf = new StringBuffer();
        char cur = string.charAt(0);
        buf.append(cur);

        char prev;
        for (int i = 1; i < string.length(); i++) {
            prev = cur;
            cur = string.charAt(i);
            if (isNum(cur) && !isNum(prev) && notInSymbols(prev, spaceSymbols))
                buf.append(" ").append(cur);
            else if (isNum(prev) && !isNum(cur) && notInSymbols(cur, spaceSymbols))
                buf.append(" ").append(cur);
            else
                buf.append(cur);
        }
        return buf.toString();
    }

    private static boolean notInSymbols(char c, char[] symbols) {
        for (char symbol : symbols) {
            if (c == symbol) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNum(char c) {
        return (58 > c && c > 47);
    }

    public static boolean isNum(String string) {
        for (char c : string.toCharArray()) {
            if (!isNum(c)) {
                return false;
            }
        }
        return true;
    }


    private static Map<String, String[]> splitted = Collections.synchronizedMap(new WeakHashMap<String, String[]>());

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
        
        String[] parts = splitted.get(string);
        if (parts != null) {
            return parts;
        }


        StringTokenizer tokenizer = new StringTokenizer(string, delimiters);
        parts = new String[tokenizer.countTokens()];
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            parts[i++] = tokenizer.nextToken();
        }
        splitted.put(string, parts);

        return parts;
    }

    /**
     * ������ ����� ��������� ��� �������� ������: �� ������� ������� ������� � ����������� ������ �������
     *
     * @param string ������
     * @return ����� ���������
     */
    public static Set<String> generateAliases(String string) {
        Set<String> withRemovedSpaces = applySpacesRemover(string);
        Set<String> aliases = new TreeSet<String>();
        for (String alias : withRemovedSpaces) {
            aliases.add(normalize(alias));
        }
        return aliases;
    }

    protected static Set<String> applySpacesRemover(String string) {
        Set<String> aliases = new HashSet<String>();
        aliases.add(string);
        String[] tokens = fastSplit(string, " ");
        for (int i = 0; i < tokens.length; i++) {
            if (i != 0 && i != tokens.length) {
                String s = removeSpace(tokens, i);
                if (s != null) {
                    aliases.add(s);
                }
            }
        }
        return aliases;
    }

    private static String removeSpace(String[] parts, int numSpace) {
        String s = EMPTY_STRING;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].length() == 0) {
                continue;
            }
            if (i == 0) {
                s = parts[i];
            } else {
                s = (i != numSpace) ? s + " " + parts[i] : s + parts[i];
            }
        }
        return s;
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

    private static Pattern notebooksRegionPattern = Pattern.compile("([^\\.,&&[\\D]])([0-9]+),([0-9]+)([^\\.,&&[\\D]])");

    public static String notebookNormalize(String string) {
        if (string == null) {
            return EMPTY_STRING;
        }
        string = replaceCommasInRegion(string, notebooksRegionPattern);
        string = removeDotsAfterWords(string);
        StringTokenizer st = new StringTokenizer(string, NOTEBOOK_DELIMITERS);
        String s = EMPTY_STRING;
        while (st.hasMoreTokens()) {
            if (s.length() > 0) {
                s = s + " ";
            }
            s = s + st.nextToken();
        }
        return normalizeRus2En(normalizeNotebooksToNumeric(s.toLowerCase()));
    }

    private static String normalizeNotebooksToNumeric(String norm) {
        return normalizeToNumeric(norm, new char[]{32, 46});
    }

    /**
     * �������� ������� �� ����� � ������ ������ ������� �������� ��� �������� �������
     *
     * @param string  ������ ��� ������
     * @param pattern �������
     * @return ���������� ������
     */
    public static String replaceCommasInRegion(String string, String pattern) {
        return replaceCommasInRegion(string, Pattern.compile(pattern));
    }

    public static String replaceCommasInRegion(String string, Pattern p) {
        StringBuilder builder = new StringBuilder(string.length());
        for (char c : string.toCharArray()) {
            if (c == '\\') {
                builder.append('/');
            } else {
                builder.append(c);
            }
        }
        string = builder.toString();
        java.util.regex.Matcher m = p.matcher(string);
        StringBuffer sb = new StringBuffer();
        boolean result = m.find();
        while (result) {
            m.appendReplacement(sb, string.substring(m.start(), m.end()).replaceAll(",", "."));
            result = m.find();
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static String removeDotsAfterWords(String str) {
        StringBuilder builder = new StringBuilder(str.length());
        char prev = 0;
        for (char c : str.toCharArray()) {
            if (c == '.' && Character.isLetter(prev)) {
                builder.append(' ');
            } else {
                builder.append(c);
            }
            prev = c;
        }
        return builder.toString();
    }

    public static String RENormalize(String str) {
        return str.toLowerCase().replaceAll("-", " ");
    }

    public static String getHostname() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown";
        }
    }

    public static boolean badAlias(String alias) {
        return (". \r".indexOf(alias) > -1 || alias.length() == 0);
    }

    public static String secureReplaceAll(String where, String what, String with) {
        return where.replaceAll(string2regexp(what), with);
    }

    public static String getFirstWords(String str, int wordsCount) {
        StringBuilder builder = new StringBuilder(str.length());
        int foundWords = 0;
        for (char c : str.toCharArray()) {
            if (c == ' ') {
                foundWords++;
            }
            if (foundWords == wordsCount) {
                break;
            }
            builder.append(c);
        }
        return builder.toString();
    }

    public static String removeAllDotsAfterWords(String token) {
        StringBuilder builder = new StringBuilder();
        boolean charStarted = false;
        for (int i = token.length() - 1; i >= 0; i--) {
            char c = token.charAt(i);
            if (!charStarted) {
                if (c != '.') {
                    charStarted = true;
                    builder.append(c);
                }
            } else {
                builder.append(c);
            }
        }
        return builder.reverse().toString();
    }

    public static String removeAllDotsBeforeWords(String token) {
        StringBuilder builder = new StringBuilder();
        boolean charStarted = false;
        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            if (!charStarted) {
                if (c != '.') {
                    charStarted = true;
                    builder.append(c);
                }
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public static String fastReplace(String string, String what, String with) {
        int index = string.indexOf(what);
        if (index == -1) {
            return string;
        }
        return string.substring(0, index) + with + string.substring(index + what.length());
    }

    public static Text fastReplaceNew(Text string, Text what, Text with) {
        int index = string.indexOf(what);
        if (index == -1) {
            return string;
        }
        return string.subtext(0, index).plus(with).plus(string.subtext(index + what.length()));
    }

    private static ObjectFactory<StringBuilder> STRING_BUILDER_FACTORY = new ObjectFactory<StringBuilder>() {
        protected StringBuilder create() {
            return new StringBuilder(100);
        }
    };

    public static String removeAllStrangeSymbols(String string) {
        StringBuilder builder = new StringBuilder();
        for (char c : string.toCharArray()) {
            if (Character.isLetter(c) || Character.isDigit(c) || c == '.' || c == ',') {
                builder.append(c);
            } else {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    public static String removeAllStrangeSymbolsNew(String string) {
        StringBuilder builder = STRING_BUILDER_FACTORY.object();
        for (char c : string.toCharArray()) {
            if (Character.isLetter(c) || Character.isDigit(c) || c == '.' || c == ',') {
                builder.append(c);
            } else {
                builder.append(" ");
            }
        }
        String result = builder.toString();
        STRING_BUILDER_FACTORY.recycle(builder);
        return result;
    }

    public static String nvl(final String s) {
        return null == s ? EMPTY_STRING : s;
    }
}
