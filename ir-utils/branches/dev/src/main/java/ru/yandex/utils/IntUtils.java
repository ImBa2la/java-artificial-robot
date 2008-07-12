package ru.yandex.utils;

/**
 * Utility class for int and int arrays manipulation.
 *
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public final class IntUtils {
	private IntUtils() {
	}

	/** Return index of minimal element. */
	public static int argmin2(int a0, int a1) {
		return a0 <= a1 ? 0 : 1;
	}

	/** Return index of maximal element. */
	public static int argmax2(int a0, int a1) {
		return a0 > a1 ? 0 : 1;
	}

	/** Return value of minimal element. */
	public static int min2(int a0, int a1) {
		return a0 <= a1 ? a0 : a1;
	}

	/** Return value of maximal element. */
	public static int max2(int a0, int a1) {
		return a0 > a1 ? a0 : a1;
	}

	/** Return index of minimal element. */
	public static int argmin3(int a0, int a1, int a2) {
		return a0 <= a1 ? a0 <= a2 ? 0 : 2 : a1 <= a2 ? 1 : 2;
	}

	/** Return index of maximal element. */
	public static int argmax3(int a0, int a1, int a2) {
		return a0 > a1 ? a0 > a2 ? 0 : 2 : a1 > a2 ? 1 : 2;
	}

	/** Return index of minimal element. */
	public static int argmin(int... a) {
		if (0 == a.length)
			return -1;
		int mi = 0;
		for (int i = 1; i < a.length; i++)
			if (a[i] < a[mi])
				mi = i;
		return mi;
	}

	/** Check if all fields of the mask is set */
	public static boolean isSet(final int v, final int mask) {
		return mask == (v & mask);
	}

	/** Return true is array is null or empty */
	public static boolean isEmpty(int a[]) {
		return null == a || 0 == a.length;
	}

	/** Return array of integer values */
	public static int[] iarr(int... a) {
		return a;
	}
}
