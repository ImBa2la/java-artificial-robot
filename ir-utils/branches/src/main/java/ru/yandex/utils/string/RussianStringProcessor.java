package ru.yandex.utils.string;

public class RussianStringProcessor implements StringProcessor {

	public String process(String s) {
		s = s.toLowerCase();
		return ModernStringUtils.normRus2En2(s);
	}

}
