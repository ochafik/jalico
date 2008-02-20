package com.ochafik.util.listenable;

import java.util.Collection;

class SynchronizedListenableSet<T> extends SynchronizedListenableCollection<T> implements ListenableSet<T> {

	public SynchronizedListenableSet(Collection<T> collection, ListenableSupport<T> collectionSupport, Object mutex) {
		super(collection, collectionSupport, mutex);
	}

	public SynchronizedListenableSet(Collection<T> collection, Object mutex) {
		super(collection, mutex);
	}

	public SynchronizedListenableSet(Collection<T> collection) {
		super(collection);
	}
	
}
