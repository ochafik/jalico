package com.ochafik.util.listenable;

public interface Adapter<U,V> {
	public V adapt(U value);
}
