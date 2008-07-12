package ru.yandex.utils.string;

import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class StringEncodingUtils {
	
	private static final Logger logger = Logger.getLogger(StringEncodingUtils.class);
	
	private static final String WINDOWS1251_ENCODING = "windows-1251";
	private static final String LATIL1_ENCODING = "latin1";
	
	public static String encode(byte[] s) {
		return new String(s);	
	}
	
	public static String encode(byte[] s, String encoding) {
		try {
			return s != null ? new String(s, encoding) : null;
		} catch (UnsupportedEncodingException e) {
			logger.fatal(e);			
		}
		assert false;
		return null;
	}
	public static byte[] encode(String s) {
		return s != null ? s.getBytes() : null;
	}
	
	public static byte[] encode(String s, String encoding) {
		try {
			return s != null? s.getBytes(encoding) : null;
		} catch (UnsupportedEncodingException e) {
			logger.fatal(e);			
		}
		assert false;
		return null;
	}
	
	public static String getLatin(byte[] s) {
		return encode(s, LATIL1_ENCODING);
	}
	
	public static String getWindows(byte[] s) {
		return encode(s, WINDOWS1251_ENCODING);
	}

	public static byte[] getWindowsBytes(String s) {
		return encode(s, WINDOWS1251_ENCODING);				
	}

	public static byte[] getLatinBytes(String s) {
		return encode(s, LATIL1_ENCODING);				
	}
	
	public static final String DELIMITERS = " \n\r";
	
	public static String normalize(String categoryName) {
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(categoryName, DELIMITERS);
		if(st.hasMoreTokens()) {
			sb.append(st.nextToken());		
			while(st.hasMoreTokens()) {
				sb.append(' ').append(st.nextToken());
			}
		}
		return sb.toString();
	}
	
}
