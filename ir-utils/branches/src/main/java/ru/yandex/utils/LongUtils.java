package ru.yandex.utils;

import java.util.Random;


/**
 * Utility class for longs.
 *
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public class LongUtils {
	/** Take random array elements to [fromIndex, toIndex) interval of array. */
	public static void shuffle(final long a[], final int fromIndex, final int toIndex) {
		Random rnd = new Random();
		for (int i = fromIndex; i < toIndex; i++)
			swap(a, i, rnd.nextInt(a.length));
	}

	/** Shuffle array */
	public static void shuffle(final long a[]) {
		shuffle(a, 0, a.length);
	}

	/** Swap a[i1] <-> a[i2] */
	public static void swap(final long[] a, final int i1, final int i2) {
		final long t = a[i1];
		a[i1] = a[i2];
		a[i2] = t;
	}
}
