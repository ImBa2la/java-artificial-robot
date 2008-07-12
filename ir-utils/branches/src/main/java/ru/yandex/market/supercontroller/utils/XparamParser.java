package ru.yandex.market.supercontroller.utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;
import java.util.StringTokenizer;

public class XparamParser {
	public static final String XPARAMS_TYPE = "Type";

	private XparamParser() { }

	public static Map<String, String> parseXparams(String params) {
		Map<String, String> map = new Object2ObjectOpenHashMap<String, String>();
		if(params == null) {
			return map;
		}
		StringTokenizer st = new StringTokenizer(params, "|");
		while(st.hasMoreTokens()) {
			String s = st.nextToken();
			int delim = s.indexOf(':');
			if(delim >= 0) {
				map.put(s.substring(0, delim), s.substring(delim + 1));
			}
		}
		return map;
	}
	
	public static String compileXparams(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, String> entry : params.entrySet()) {
			sb.append('|').append(entry.getKey()).append(':').append(entry.getValue());
		}
		return sb.toString();
	}
}
