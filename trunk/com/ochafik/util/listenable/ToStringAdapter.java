package com.ochafik.util.listenable;

public class ToStringAdapter<U> implements Adapter<U, String> {
	public String adapt(U value) {
		return String.valueOf(value);
	}
}
