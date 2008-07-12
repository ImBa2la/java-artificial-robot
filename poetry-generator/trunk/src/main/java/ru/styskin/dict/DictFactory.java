package ru.styskin.dict;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.ClassPathResource;

public class DictFactory implements FactoryBean {
	
	private String encoding = "koi8-r";
	private String fileName;	

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Object getObject() throws Exception {
		List<String> words = new ArrayList<String>();		
		BufferedReader in = new BufferedReader( new InputStreamReader((new ClassPathResource(fileName)).getInputStream(), encoding));
		String s;
		in.readLine();
		while((s = in.readLine()) != null) {
			words.add(s);									
		}
		in.close();
		return new Dict(words);
	}

	public Class getObjectType() {
		return Dict.class;
	}

	public boolean isSingleton() {
		return true;
	}	
}
