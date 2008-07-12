package ru.yandex.utils.string;

import java.util.List;

public class ComplexStringProcessor implements StringProcessor {

	private List<StringProcessor> processors;
	
	public String process(String s) {
		for(StringProcessor p : processors) {
			s = p.process(s);
		}
		return s;
	}

	public void setProcessors(List<StringProcessor> processors) {
		this.processors = processors;
	}

}
