package ru.yandex.utils.string.aho;

import java.util.ArrayList;
import java.util.List;

import ru.yandex.utils.PairIO;

public class Search {
		
	public static <O> List<PairIO<O>> search(String str, Vertex<O> root) {
		List<PairIO<O>> answer = new ArrayList<PairIO<O>>();
		Vertex<O> current = root;
		outer: for(int i=0; i < str.length(); i++) {
			while(current.gotoFunction(str.charAt(i)) == null) {				
				if(current == current.fail()) {
					continue outer;					
				}
				current = current.fail();
			}
			current = current.gotoFunction(str.charAt(i));
			if(current.out() != null)
				for(O s : current.out())
					answer.add(new PairIO<O>(i, s));
		}
		return answer;
	}
	

}
