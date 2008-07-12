package ru.yandex.ir.util;

import junit.framework.TestCase;
import ru.yandex.ir.util.TwoPrioritySetQueue;

/**
 * XXX: write some tests that test with many threads
 *
 * @author yozh
 */
public class TwoPrioritySetQueueTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TwoPrioritySetQueueTest.class);

    public void testSimple() throws Exception {
        TwoPrioritySetQueue<Integer> q = new TwoPrioritySetQueue<Integer>();
        q.add(1);
        q.add(2);
        q.addToFaster(3);
        q.addToFaster(2);
        q.add(4);
        assertEquals(3, (int) q.take());
        assertEquals(2, (int) q.take());
        assertEquals(1, (int) q.take());
        assertEquals(4, (int) q.poll());
        assertNull(q.poll());
    }
} //~
