package ru.yandex.ir.util;

import javolution.text.Text;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

public class StringUtilsTest extends TestCase {
    private static final Logger logger = Logger.getLogger(StringUtilsTest.class);

    public void testNormalize() throws Exception {
        String string = "������  �    ����!";
        assertEquals("�pebe� � �aka", StringUtils.normalize(string));

        string = "������  13�234   23����";
        assertEquals("�pebe� 13 � 234 23 �aka", StringUtils.normalize(string));

        assertEquals("", StringUtils.normalize(null));
    }

//    public void testAppend() {
//        int count = 10000000;
//
//        int size = 100;
//
//        StopWatch watch = new StopWatch();
//
//        String test = "dsfasdgsdfgsfdfdsgfdghdxc m,nkjhi3ur32rsdfesdht";
//
//        watch.start("fastReplace");
//        int j;
//        for (j = 0; j < count; j++) {
//            for (char c : test.toCharArray()) {
//                char x = c;
//            }
//        }
//        watch.stop();
//
//        System.out.println("First done: " + j);
//
//        Text testTex = Text.intern(test);
//        // StackContext.enter();
//        watch.start("fastReplaceNew");
//        for (j = 0; j < count; j++) {
//            for (int i = 0; i < testTex.length(); i++) {
//                char x = testTex.charAt(i);
//            }
//        }
//        watch.stop();
//        // StackContext.exit();
//        System.out.println("Second done: " + j);
//
//        System.out.println(watch.prettyPrint());
//    }

    public void testNormalizeSlash() throws Exception {
        String string = "2/2";
        assertEquals("2 2", StringUtils.normalize(string));
    }

    public void testNormalizeRus2En() throws Exception {
        String toEnglish = "�����������";
        assertEquals("ceopakyx�tm", StringUtils.normRus2En2(toEnglish));

        String russian = "���������������������";
        assertEquals(russian, StringUtils.normRus2En2(russian));
    }

    public void testNormalizeToNumeric() throws Exception {
        String numbers = "32x43 x34 34x 23xxx";
        assertEquals("32 x 43 x 34 34 x 23 xxx", StringUtils.normalize(numbers));

        numbers = "3 x";
        assertEquals("3 x", StringUtils.normalize(numbers));

        numbers = "3";
        assertEquals("3", StringUtils.normalize(numbers));
    }

//    public void testGenerateAliases() throws Exception {
//        String string = "������  �   ����!";
//        Set<String> aliases = StringUtils.generateAliases(string);
//        assertEquals(3, aliases.size());
//
//        Iterator it = aliases.iterator();
//        assertEquals("�pe�e� � �aka", it.next());
//        assertEquals("�pe�e� ��aka", it.next());
//        assertEquals("�pe�e�� �aka", it.next());
//    }
//
//    public void testGenerateAliasesAndNormalize() throws Exception {
//        String string = "������  13�234   23����!";
//        String normalizedString = StringUtils.normalize(string);
//
//        Set<String> normilizedFirst = StringUtils.generateAliases(normalizedString);
//        Set<String> raw = StringUtils.generateAliases(string);
//
//        assertEquals(raw, normilizedFirst);
//    }
//
//    public void testFastSplit() throws Exception {
//        String string = "������  �   ����!";
//        String[] array = StringUtils.fastSplit(string, " ");
//        assertEquals(3, array.length);
//        assertEquals("������", array[0]);
//        assertEquals("�", array[1]);
//        assertEquals("����!", array[2]);
//
//        string = "������\n �\t \n����!";
//        array = StringUtils.fastSplit(string, " \n\t");
//        assertEquals(3, array.length);
//        assertEquals("������", array[0]);
//        assertEquals("�", array[1]);
//        assertEquals("����!", array[2]);
//
//        assertNull(StringUtils.fastSplit(null, " "));
//    }
//
//    public void testFastSplitMemory() throws InterruptedException {
//        Thread thread1 = new Thread() {
//            public void run() {
//                while (true) {
//                    String word = generateRandomWord();
//                    for (int i = 0; i < Math.random() * 100; i++) {
//                        StringUtils.fastSplit(word, " ");
//                    }
//
//                }
//            }
//        };
//        thread1.start();
//        
//        for (int i = 0; i < 1000; i++) {
//            new Thread() {
//                public void run() {
//                    while (true) {
//                        String word = generateRandomWord();
//                        for (int i = 0; i < Math.random() * 100; i++) {
//                            StringUtils.fastSplit(word, " ");
//                        }
//
//                    }
//                }
//            }.start();
//        }
//        thread1.join();
//    }
//
//    char[] letters = "qwertyuiop[]         asdfghjkl;'zxcvbnm,./".toCharArray();
//
//    private String generateRandomWord() {
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < 20; i++) {
//            builder.append(letters[new Double(Math.random() * letters.length).intValue()]);
//        }
//        return builder.toString();
//    }
//
//
//    public void testSecureReplace() throws Exception {
//        assertEquals("������ x ����", StringUtils.secureReplace("������ � ����", "�", "x"));
//    }

//    public void testReplaceCommasInRegion() throws Exception {
//        String regexp = "([^\\.,&&[\\D]])([0-9]+),([0-9]+)([^\\.,&&[\\D]])";
//
//        String string = "������9,1 ����, ����";
//        assertEquals("������9.1 ����, ����", StringUtils.replaceCommasInRegion(string, regexp));
//
//        string = "PM740-1.73,512,60,15.4\"WSXGA+,ATi X300(128)";
//        assertEquals("PM740-1.73,512,60,15.4\"WSXGA+,ATi X300(128)", StringUtils.replaceCommasInRegion(string, regexp));
//
//        string = "Dothan-1,6/512/60/";
//        assertEquals("Dothan-1.6/512/60/", StringUtils.replaceCommasInRegion(string, regexp));
//
//        string = "Turion 64 MT-32 (1,8GHz) / 2x256MB / ";
//        assertEquals("Turion 64 MT-32 (1.8GHz) / 2x256MB / ", StringUtils.replaceCommasInRegion(string, regexp));
//    }
//
//    public void testString2regexp() throws Exception {
//        String string = "[x123\\t.*34[],";
//        assertEquals("\\[x123\\\\t\\.\\*34\\[\\]\\,", StringUtils.string2regexp(string));
//    }

    public void testNotebookNormalize() throws Exception {
        String string = "������  � |_   ����!";
        assertEquals("�pebe� � �aka", StringUtils.normalize(string));

        string = "������  13�234,23����!";
        assertEquals("�pebe� 13 � 234.23 �aka", StringUtils.normalize(string));

        assertEquals("", StringUtils.normalize(null));
    }

    public void testComplexNotebooksNormalize() {
        String result = StringUtils.normalize("Latitude 110L i910GML CM-360 1.4,512,60,15");
        assertEquals("latitude 110 l i 910 gml cm 360 1.4 512 60 15", result);
        
        result = StringUtils.normalize("15,,3");
        assertEquals("15 3", result);
    }

//    public void testGetFirstWords() {
//        String result = StringUtils.getFirstWords("preved kisa kuku paka", 3);
//        assertEquals("preved kisa kuku", result);
//    }

    public void testRemoveDotsAfterWords() throws Exception {
        assertEquals("a", StringUtils.normalize("a."));
    }

//    public void testFastReplace() throws Exception {
//        assertEquals("index preved", StringUtils.fastReplace("index nbsp preved", " nbsp ", " "));
//    }
//
//    public void testRemoveAllStrangeSymbols() {
//        System.out.println(StringUtils.removeAllStrangeSymbols("������()\\/<>'&_|{}�[paka12.12"));
//        assertEquals("������              paka12.12", StringUtils.removeAllStrangeSymbols("������()\\/<>'&_|{}�[paka12.12"));
//    }
}