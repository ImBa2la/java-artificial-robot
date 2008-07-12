package ru.styskin.dict;

import java.util.ArrayList;
import java.util.List;

public class Dict {

	private List<String> words = new ArrayList<String>();

	
	public Dict(List<String> words) {
		this.words = words;
	}


	public List<String> getWords() {
		return words;
	}
	
	
}
