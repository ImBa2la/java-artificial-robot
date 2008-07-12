package ru.yandex.utils.string;

import java.util.Iterator;


/**
 * Iterate string tokens produced by StringSplitter.
 *
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public class StringSplitterIterator implements Iterator<String> {
	private final String s;
	private final StringSplitter st;

	private String token;
	private final boolean skipWs;

	public StringSplitterIterator(
	    final String s, AcceptC... acceptors)
	{
		this(s, true, acceptors);
	}

	public StringSplitterIterator(
	    final String s, final boolean skipDelimiters, AcceptC... acceptors)
	{
		this.s = s;
		skipWs = skipDelimiters;
		st = new StringSplitter(s, acceptors);
		moveNext();
	}

	public boolean hasNext() {
		return null != token;
	}

	public String next() {
		if (!hasNext())
			return null;

		final String res = token;
		moveNext();
		return res;
	}

	public void remove() { }

	private void moveNext() {
		boolean hasNext;
		// skip delimiter tokens IFF skipWs is true
		while ((hasNext = st.next()) && (skipWs && -1==st.tokenClass()));
		token = hasNext ? s.substring(st.begin(), st.end()) : null;
	}
}