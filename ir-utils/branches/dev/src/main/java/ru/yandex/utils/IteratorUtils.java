package ru.yandex.utils;

import it.unimi.dsi.fastutil.ints.IntIterator;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;


/**
 * Utilities functions, such as map(), addAll(), repeat().
 *
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public final class IteratorUtils {

	private static final Logger logger = Logger.getLogger(IteratorUtils.class);

	private IteratorUtils() {
	}

	public static <F, T> Iterator<T> map(Iterable<F> list, Transform<F, T> mapper) {
		return map(list.iterator(), mapper);
	}

	public static <F, T> Iterator<T> map(Iterator<F> list, Transform<F, T> mapper) {
		return new Mapper<F, T>(list, mapper);
	}

	public static <F, T> Iterable<T> mapI(Iterable<F> list, Transform<F, T> mapper) {
		return createrIterable(map(list.iterator(), mapper));
	}

	public static <T> Iterator<T> map(IntIterator list, TransformI2O<T> mapper) {
		return new MapperI2O<T>(list, mapper);
	}
	
	public static <T> T reduce(Binar<T, T, T> transfom, Iterator<T> it, T init) {
		T current = init;
		while(it.hasNext()) {
			current = transfom.transform(current, it.next());
		}
		return current;
	}
	
	public static <T> T reduce(Binar<T, T, T> transfom, Iterable<T> it, T init) {
		return reduce(transfom, it.iterator(), init);		
	}
	
	public static <T> T reduce(Binar<T, T, T> transfom, Iterator<T> it) {
		if(!it.hasNext())
			return null;		
		T current = it.next();
		while(it.hasNext()) {
			current = transfom.transform(current, it.next());
		}
		return current;
	}
	
	public static <T> T reduce(Binar<T, T, T> transfom, Iterable<T> it) {
		return reduce(transfom, it.iterator());		
	}
	
	public static String joinMap(IntIterator list, TransformI2O<String> mapper, final String delimiter) {
		StringBuilder b = new StringBuilder();
		if (list.hasNext())
			b.append(mapper.transform(list.nextInt()));
		while (list.hasNext())
			b.append(delimiter).append(mapper.transform(list.nextInt()));
		return b.toString();
	}

	public static String joinMap(int a[], TransformI2O<String> mapper, final String delimiter) {
		StringBuilder b = new StringBuilder();
		final int l = a.length;
		if (0 < l)
			b.append(mapper.transform(a[0]));
		for (int i = 1; i < l; i++)
			b.append(delimiter).append(mapper.transform(a[i]));
		return b.toString();
	}

	public static <F, T> Iterable<T> mapI(Iterator<F> list, Transform<F, T> mapper) {
		return createrIterable(map(list, mapper));
	}

	public static <F> Iterator<F> filter(Iterable<F> list, Accept<F> accept) {
		return filter(list.iterator(), accept);
	}

	public static <F> Iterator<F> filter(Iterator<F> list, Accept<F> accept) {
		return new Filter<F>(list, accept);
	}

	public static <F> Iterable<F> filterI(Iterator<F> list, Accept<F> accept) {
		return createrIterable(filter(list, accept));
	}

	public static <F> Iterable<F> filterI(Iterable<F> list, Accept<F> accept) {
		return createrIterable(filter(list, accept));
	}

	public static class SimpleIterable<F> implements Iterable<F> {
		private Iterator<F> it;

		private SimpleIterable(Iterator<F> it) {
			this.it = it;
		}

		public Iterator<F> iterator() {
			return it;
		}
	}

	public static <F> Iterable<F> createrIterable(Iterator<F> it) {
		return new SimpleIterable<F>(it);
	}

	public static <F> List<F> toList(Iterator<F> it) {
		List<F> list = new ArrayList<F>();
		while(it.hasNext()) {
			list.add(it.next());
		}
		return list;
	}

	public static <F, T> void addAll(List<T> to, List<F> from, Transform<F, T> transform) {
		for (F val : from)
			to.add(transform.transform(val));
	}

	public static <F, T> void addAll(Consumer<F> consumer, List<F> from) {
		for (F val : from)
			consumer.add(val);
	}

	public static <T> void addAll(Collection<T> to, Iterator<T> from) {
		while (from.hasNext())
			to.add(from.next());
	}

	public static <T> void addAll(Consumer<T> to, Iterator<T> from) {
		while (from.hasNext())
			to.add(from.next());
	}

	public static <T> void addAll(CloseableConsumer<T> to, Iterator<T> from) {
		try {
			addAll((Consumer<T>) to, from);
		} finally {
			silentClose(to);
		}
	}

	public static <T> void addAll(Collection<T> to, CloseableIterator<T> from) {
		try {
			addAll(to, (Iterator<T>) from);
		} finally {
			silentClose(from);
		}
	}

	public static <T> void addAll(Consumer<T> to, CloseableIterator<T> from) {
		try {
			addAll(to, (Iterator<T>) from);
		} finally {
			silentClose(from);
		}
	}

	public static <T> void addAll(CloseableConsumer<T> to, CloseableIterator<T> from) {
		try {
			addAll((Consumer<T>) to, (Iterator<T>) from);
		} finally {
			silentClose(from);
			silentClose(to);
		}
	}

	public static <T> T head(Iterable<T> list) {
		Iterator<T> iter = list.iterator();
		if (!iter.hasNext())
			return null;
		return iter.next();
	}
	
	public static <T> T tail(List<T> list) {
		return list.get(list.size() - 1);
	}
	
	public static <T> T tail(SortedSet<T> list) {
		return list.last();
	}	

	public static <T> Iterator<T> repeat(T value, final int count) {
		return new RepeatIterator<T>(value, count);
	}

	public static <T> Iterator<T> head(final int count, Iterator<T> list) {
		return new HeadIterator<T>(count, list);
	}

	public static void silentClose(Closeable o) {
		try {
			if (null != o)
				o.close();
		} catch (Exception e) {
			logger.error("On close", e);
		}
	}

	public static Object[] oarr(Object... a) {
		return a;
	}

	public static String[] sarr(String... a) {
		return a;
	}
	
	public static <T> T[] arr(T... a) {
		return a;
	}
	
	public static <F> List<F> olist(F... a) {
		return Arrays.asList(a);
	}

	private static class Mapper<F, T> implements Iterator<T> {
		private final Iterator<F> list;
		private final Transform<F, T> transformer;

		public Mapper(Iterator<F> list, Transform<F, T> transformer) {
			this.list = list;
			this.transformer = transformer;
		}

		public boolean hasNext() {
			return list.hasNext();
		}

		public T next() {
			return transformer.transform(list.next());
		}

		public void remove() {
			list.remove();
		}
	}

	private static class MapperI2O<T> implements Iterator<T> {
		private final IntIterator list;
		private final TransformI2O<T> transformer;

		public MapperI2O(IntIterator list, TransformI2O<T> transformer) {
			this.list = list;
			this.transformer = transformer;
		}

		public boolean hasNext() {
			return list.hasNext();
		}

		public T next() {
			return transformer.transform(list.nextInt());
		}

		public void remove() {
			list.remove();
		}
	}

	private static class Filter<F> implements Iterator<F> {
		private final Iterator<F> list;
		private final Accept<F> acceptor;

		private F current;
		private boolean took = true;

		public Filter(Iterator<F> list, Accept<F> acceptor) {
			this.list = list;
			this.acceptor = acceptor;
			hasNext();
		}

		public boolean hasNext() {
			while(took && list.hasNext()) {
				F o = list.next();
				if(acceptor.accept(o)) {
					current = o;
					took = false;
					break;
				}
			}
			return !took;
		}

		public F next() {
			if(hasNext()) {
				took = true;
				return current;
			}
			return null;
		}

		public void remove() {}
	}

	private static class HeadIterator<T> implements Iterator<T> {
		private final Iterator<T> list;
		private int count;

		public HeadIterator(final int count, Iterator<T> list) {
			this.count = count;
			this.list = list;
		}

		public boolean hasNext() {
			return list.hasNext() && (0 < count);
		}

		public T next() {
			if (0 < count) {
				--count;
				return list.next();
			} else {
				return null;
			}
		}

		public void remove() {
			list.remove();
		}
	}

	private static class RepeatIterator<T> implements Iterator<T> {
		private final T value;
		private int count;

		public RepeatIterator(final T value, final int count) {
			this.count = count;
			this.value = value;
		}

		public boolean hasNext() {
			return 0 < count;
		}

		public T next() {
			if (0 < count) {
				--count;
				return value;
			} else {
				return null;
			}
		}

		public void remove() {
		}
	}
	
	public static final Binar<Double, Double, Double> DOUBLE_SUM = new Binar<Double, Double, Double>() {
		public Double transform(Double a, Double b) {
			return a + b;
		}
	};
	
	public static final Binar<Integer, Integer, Integer> INT_SUM = new Binar<Integer, Integer, Integer>() {
		public Integer transform(Integer a, Integer b) {
			return a + b;
		}
	};
}