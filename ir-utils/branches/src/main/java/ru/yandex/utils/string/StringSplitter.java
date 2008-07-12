package ru.yandex.utils.string;

/**
 * Class that split string into tokens. Use Acceptors to determine character classes of the tokens.
 *
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public class StringSplitter {
	private final String s;
	private final AcceptC[] acceptors;

	private int tokenClass;
	private int begin, end;

	public StringSplitter(final String s, AcceptC... acceptors) {
		this.s = s;
		this.acceptors = acceptors;

		begin = end = 0;
	}

	/**
	 * Move to next token. Return true iff there is token in string.
	 * @return true iff there is token
	 */
	public boolean next() {
		if (s.length() <= end)
			return false;

		begin = end;
		tokenClass = detectClass(s.charAt(end++));
		for (;end < s.length(); end++)
			if (tokenClass != detectClass(s.charAt(end)))
				break;
		return true;
	}

	/** Return begin position (inclusive) of current token. */
	public int begin() {
		return begin;
	}

	/** Return end position (exclusive) of current token. */
	public int end() {
		return end;
	}

	/** Return class of current token. Class of the token is the index of Acceptor,
	 * that accepts chars of this token, or 0 iff there is no suitable acceptors.
	 */
	public int tokenClass() {
		return tokenClass;
	}

	private int detectClass(final char ch) {
		for (int i = 0; i < acceptors.length; i++)
			if (acceptors[i].accept(ch))
				return i;
		return -1;
	}
}