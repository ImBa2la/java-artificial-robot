package ru.yandex.utils.string;

import java.util.ArrayList;
import java.util.List;

import ru.yandex.utils.string.aho.Builder;
import ru.yandex.utils.string.aho.Vertex;

public class DefaultStringProcessor implements StringProcessor {

	// TODO: from properties
	private final List<String> dict = new ArrayList<String>(), rep = new ArrayList<String>();  
	
	private final Vertex<Integer> root;
	
	{
		dict.add("amp;");		rep.add("&");
		dict.add("nbsp;");		rep.add(" ");
		dict.add("quot;");		rep.add("\"");		
		dict.add("gt;");		rep.add(">");		
		dict.add("lt;");		rep.add("<");		
		dict.add("eq;");		rep.add("=");		
		dict.add("&amp;");		rep.add("&");
		dict.add("&nbsp;");		rep.add(" ");
		dict.add("&quot;");		rep.add("\"");		
		dict.add("&gt;");		rep.add(">");		
		dict.add("&lt;");		rep.add("<");		
		dict.add("&eq;");		rep.add("=");	
		root = Builder.buildIndex(dict);
	}	

	public String process(String s) {
		return ModernStringUtils.fastReplace(s.trim(), root, dict, rep);
	}

}
