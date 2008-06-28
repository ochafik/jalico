package com.ochafik.lang;

public class SyntaxUtils {
	public static <T> T[] array(T... elements) {
		return elements;
	}
	
	public static <T> boolean equal(T a, T b) {
		if (a == null)
			return b == null;
		if (b == null)
			return false;
		return a.equals(b);
	}

	public static <T> T as(Object node, Class<T> c) {
		if (node == null)
			return null;
		if (c.isAssignableFrom(node.getClass()))
			return c.cast(node);
		return null;
	}
	
}
