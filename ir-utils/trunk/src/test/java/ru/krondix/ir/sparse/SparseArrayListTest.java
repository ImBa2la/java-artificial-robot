package ru.krondix.ir.sparse;
/**
 * @author Egor Azanov, <a href="mailto:krondix@yandex-team.ru">
 */

import junit.framework.TestCase;
import org.springframework.util.StopWatch;

import java.util.*;

public class SparseArrayListTest extends TestCase {

    public void testSetAndGet() throws Exception {
        int length = 10;
        SparseArrayList<Integer> list = createList(length);
        for (int i = 0; i < length; i++) {
            assertEquals(i, (int)list.get(i));
        }

        list.set(5, 6);
        for (int i = 0; i < length; i++) {
            assertEquals(i != 5 ? i : 6, (int) list.get(i));
        }
    }

    private SparseArrayList<Integer> createList(int length) {
        SparseArrayList<Integer> list = new SparseArrayList<Integer>();
        for (int i = 0; i < length; i++) {
            if (i == 5) {
                continue;
            }
            list.set(i, i);
        }
        list.set(5, 5);
        return list;
    }

    public void testIterate() throws Exception {
        int length = 10;
        SparseArrayList<Integer> list = createList(length);
        int i = 0;
        for (SparseArrayList.Entry<Integer> entry : list) {
            assertEquals(i, entry.getIndex());
            assertEquals(i, (int)entry.getValue());
            i++;
        }
    }

    public static List<Double> classList;
    public static List<double[]> arrayList;
    public static double[] array;

    public void testMemory2() {
        classList = new ArrayList<Double>();
        arrayList = new ArrayList<double[]>();
        int LENGTH = 100000;
        array = new double[LENGTH];

        for (int i = 0; i < LENGTH; i++) {
            double random = Math.random();
            classList.add(random);
            //arrayList.add(new double[]{random});
            array[i] = random;
        }
    }

    public static List<SparseArrayList<Double>> list;
    public static List<Map<Integer, Double>> map;

    public void testMemory() {
        list = new ArrayList<SparseArrayList<Double>>();
        map = new ArrayList<Map<Integer, Double>>();
        int LENGTH = 10000;
        int VECTOR_LENGTH = 100000;
        int USED_LENGTH = 100;
        for (int i = 0; i < LENGTH; i++) {
            // list.add(createList(VECTOR_LENGTH, USED_LENGTH));
            map.add(createMap(VECTOR_LENGTH, USED_LENGTH));
        }
    }

    private Map<Integer, Double> createMap(int vector_length, int used_length) {
        Map<Integer, Double> map = new HashMap<Integer, Double>();

        List<Integer> used = new ArrayList<Integer>();
        for (int i = 0; i < vector_length; i++) {
            Double random = Math.random();
            if (used.size() < used_length && random > 0.999) {
                map.put(i, random);
                used.add(i);
            }
        }
        return map;
    }

    private SparseArrayList<Double> createList(int vector_length, int used_length) {
        SparseArrayList<Double> list = new SparseArrayList<Double>();
        List<Integer> used = new ArrayList<Integer>();
        for (int i = 0; i < vector_length; i++) {
            Double random = Math.random();
            if (used.size() < used_length && random > 0.999) {
                list.set(i, random);
                used.add(i);
            }
        }
        return list;
    }


    public void testPerformance() {
        SparseArrayList<Double> list = new SparseArrayList<Double>();
        SortedMap<Integer, Double> map = new TreeMap<Integer, Double>();

        int LENGTH = 1000000;
        int USED_LENGTH = 50;
        List<Integer> used = new ArrayList<Integer>();
        for (int i = 0; i < LENGTH; i++) {
            Double random = Math.random();
            if (used.size() < USED_LENGTH && random > 0.9) {
                list.set(i, random);
                map.put(i, random);
                used.add(i);
            }
        }
        System.out.println(used.size());

        StopWatch watch = new StopWatch();
        watch.start("SparseArrayList");
        for (int i = 0; i < 100; i++) {
            for (SparseArrayList.Entry<Double> entry : list) {}
        }
        watch.stop();

        watch.start("HashMap");
        for (int i = 0; i < 100; i++) {
            Set<Integer> indexes = map.keySet();
            for (Integer index : indexes) {
                map.get(index);
            }
        }
        watch.stop();
        System.out.println(watch.prettyPrint());
    }

}
