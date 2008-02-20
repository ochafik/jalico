package com.ochafik.util.listenable;

import java.util.Set;


public class DefaultListenableSet<T> extends DefaultListenableCollection<T> implements ListenableSet<T> {
	public DefaultListenableSet(Set<T> set, ListenableSupport<T> collectionSupport) {
		super(set,collectionSupport);
	}
	public DefaultListenableSet(Set<T> set) {
		super(set);
	}	
}
