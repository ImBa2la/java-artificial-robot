package ru.yandex.ir.util;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import ru.yandex.ir.util.SpringUtils;

/**
 * @author yozh
 */
public class SpringUtilsTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SpringUtilsTest.class);

    public void testGetApplicationContext() {
        ApplicationContext ctx1 = SpringUtils.getApplicationContext("main/configurable.xml");
        ApplicationContext ctx2 = SpringUtils.getApplicationContext("main/configurable.xml");
        assertSame("expecting same context returned two times", ctx1, ctx2);
    }
} //~
