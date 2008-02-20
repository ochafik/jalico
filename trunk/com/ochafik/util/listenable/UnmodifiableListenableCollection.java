package com.ochafik.util.listenable;

import java.util.Collection;
import java.util.Iterator;

class UnmodifiableListenableCollection<T> extends FilteredListenableCollection<T> {
	public UnmodifiableListenableCollection(ListenableCollection<T> listenableCollection) {
		super(listenableCollection);
	}
	@Override
	public boolean add(T o) {
		throw new UnsupportedOperationException("Unmodifiable listenable collection !");
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException("Unmodifiable listenable collection !");
	}
	
	@Override
	public void clear() {
		throw new UnsupportedOperationException("Unmodifiable listenable collection !");		
	}

	@Override
	public Iterator<T> iterator() {
		return new FilteredIterator<T>(listenableCollection.iterator()) {
			@Override
			public void remove() {
				throw new UnsupportedOperationException("Unmodifiable listenable collection !");
			}
		};
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Unmodifiable listenable collection !");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Unmodifiable listenable collection !");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Unmodifiable listenable collection !");
	}	
}