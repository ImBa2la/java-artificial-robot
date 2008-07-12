package ru.yandex.utils;

public interface Binar<A,B,F> {
	
	F transform(A a, B b);
}
