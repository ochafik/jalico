package com.ochafik.util.listenable;

import java.util.Map;

import com.ochafik.lang.SyntaxUtils;

public class Pair<U, V> implements Comparable<Pair<U, V>>, Map.Entry<U, V> {
	private U first;
	private V second;
	
	public Pair(U first, V second) {
		this.first = first;
		this.second = second;
	}
	
	public U getFirst() {
		return first;
	}
	
	public V getSecond() {
		return second;
	}
	
	public void setFirst(U first) {
		this.first = first;
	}
	
	public void setSecond(V second) {
		this.second = second;
	}
	
	@SuppressWarnings("unchecked")
	public int compareTo(Pair<U, V> o) {
		Comparable<U> cu = (Comparable<U>)getFirst();
		int d = cu.compareTo(o.getFirst());
		if (d != 0)
			return d;
		
		Comparable<V> cv = (Comparable<V>)getSecond();
		return cv.compareTo(o.getSecond());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Pair))
			return false;
		
		Pair other = (Pair)obj;
		if (!SyntaxUtils.equal(getFirst(), other.getFirst()))
			return false;
		return SyntaxUtils.equal(getSecond(), other.getSecond());
	}
	
	@Override
	public int hashCode() {
		int i1 = getFirst() == null ? 0 : getFirst().hashCode();
		int i2 = getSecond() == null ? 0 : getSecond().hashCode();
		return i1 ^ i2;
	}
	
	@Override
	public String toString() {
		return "Pair("+first+", "+second+")";
	}

	public U getKey() {
		return first;
	}

	public V getValue() {
		return second;
	}

	public V setValue(V value) {
		V oldValue = second;
		second = value;
		return oldValue;
	}
}
