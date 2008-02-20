package com.ochafik.util.listenable;

import java.util.Collection;
import java.util.Iterator;

public class FilteredListenableCollection<T> implements ListenableCollection<T> {
	protected final ListenableCollection<T> listenableCollection;

	public FilteredListenableCollection(ListenableCollection<T> listenableCollection) {
		this.listenableCollection = listenableCollection;
	}

	public void addCollectionListener(CollectionListener<T> l) {
		listenableCollection.addCollectionListener(l);
	}

	public void removeCollectionListener(CollectionListener<T> l) {
		listenableCollection.removeCollectionListener(l);
	}

	public boolean add(T o) {
		return listenableCollection.add(o);
	}

	public boolean addAll(Collection<? extends T> c) {
		return listenableCollection.addAll(c);
	}
	
	public void clear() {
		listenableCollection.clear();		
	}

	public boolean contains(Object o) {
		return listenableCollection.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return listenableCollection.containsAll(c);
	}

	public boolean isEmpty() {
		return listenableCollection.isEmpty();
	}
	static class FilteredIterator<T> implements Iterator<T> {
		Iterator<T> iterator;
		
		public FilteredIterator(Iterator<T> iterator) {
			this.iterator = iterator;
		}
		public boolean hasNext() {
			return iterator.hasNext();
		}
		public T next() {
			return iterator.next();
		}
		public void remove() {
			iterator.remove();
		}
	}
	public Iterator<T> iterator() {
		return new FilteredIterator<T>(listenableCollection.iterator());
	}

	public boolean remove(Object o) {
		return listenableCollection.remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return listenableCollection.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return listenableCollection.retainAll(c);
	}

	public int size() {
		return listenableCollection.size();
	}

	public Object[] toArray() {
		return listenableCollection.toArray();
	}

	@SuppressWarnings("hiding")
	public <T> T[] toArray(T[] a) {
		return listenableCollection.toArray(a);
	}
		
}
