package ru.yandex.utils.string;

/**
 * Utility class-factory to instantiate acceptors
 *
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public final class CharacterAcceptors {
	private CharacterAcceptors() { }

	private static final AcceptC ALPHA_ACCEPTOR_A = new AlphaA();
	private static final AcceptC DIGITS_ACCEPTOR_A = new DigitsA();
	private static final AcceptC EVERYTHING_ACCEPTOR_A = new EverythingA();

	/** Create Acceptor that accepts the only letter characters */
	public static AcceptC createAlphaA() {
		return ALPHA_ACCEPTOR_A;
	}

	/** Create Acceptor that accepts the only digit characters */
	public static AcceptC createDigitsA() {
		return DIGITS_ACCEPTOR_A;
	}

	/** Create Acceptor that accepts the characters contained in given string */
	public static AcceptC createDelimitersA(final String delimiters) {
		return new DelimitersA(delimiters);
	}
	
	public static AcceptC createEverythingA() {
		return EVERYTHING_ACCEPTOR_A;
	}	

	/** Letter characters acceptor */
	public static class AlphaA implements AcceptC {
		public boolean accept(final char ch) {
			return Character.isLetter(ch);
		}
	}
	
	/** Letter characters acceptor */
	public static class RussianA implements AcceptC {
		public boolean accept(final char ch) {			
			return (ch >= 'A' && ch <= 'z') || (ch >= 'À' && ch <= 'ÿ');
		}
	}

	/** Digit characters acceptor */
	public static class DigitsA implements AcceptC {
		public boolean accept(final char ch) {
			return Character.isDigit(ch);
		}
	}

	/** Accept characters from contained in given stirng */
	public static class DelimitersA implements AcceptC {
		private final String s;

		public DelimitersA(final String s) {
			this.s = s;
		}

		public boolean accept(final char ch) {
			return -1 != s.indexOf(ch);
		}
	}
	
	/** Digit characters acceptor */
	public static class EverythingA implements AcceptC {
		public boolean accept(final char ch) {
			return true;
		}
	}

}