package ru.styskin.poetry.utils;

import java.util.ArrayList;
import java.util.List;

public class DefaultStringProcessor {
	
	public DefaultStringProcessor() {
	}
	
    public List<String> splitString(String document) {
    	return splitString(document, StringUtils.DELIMITERS);    	
    }
	

    public List<SingletonString> split(String document) {
    	return split(document, StringUtils.DELIMITERS);    	
    }
    
    public List<String> splitString(String document, String delimiters ) {
        String[] tokens = StringUtils.fastSplit(document, delimiters);
        List<String> singletonTokens = new ArrayList<String>();
        for (String token : tokens) {
       		singletonTokens.add(token.toLowerCase());
        }
        return singletonTokens;
    }
    
	
    public List<SingletonString> split(String document, String delimiters ) {
        String[] tokens = StringUtils.fastSplit(document, delimiters);
        List<SingletonString> singletonTokens = new ArrayList<SingletonString>();
        for (String token : tokens) {
       		singletonTokens.add(SingletonString.getInstance(token.toLowerCase()));
        }
        return singletonTokens;
    }
	
    private static DefaultStringProcessor defaultStringProcessor;    
    static {
    	defaultStringProcessor = new DefaultStringProcessor();
    }    
    
    public static DefaultStringProcessor getProcessor() {
    	return defaultStringProcessor;    	    	
    }

}
