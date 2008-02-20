package com.ochafik.util.listenable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class DefaultListenableCollection<T> implements ListenableCollection<T> {
	protected Collection<T> collection;
 	protected ListenableSupport<T> collectionSupport;
	
 	public void addCollectionListener(CollectionListener<T> l) {
 		collectionSupport.addCollectionListener(l);
 	}
 	public void removeCollectionListener(CollectionListener<T> l) {
 		collectionSupport.removeCollectionListener(l);
 	}
 	public DefaultListenableCollection(Collection<T> collection) {
		this(collection,new ListenableSupport<T>());
	}
	public DefaultListenableCollection(Collection<T> collection, ListenableSupport<T> collectionSupport) {
		this.collection = collection;
		this.collectionSupport = collectionSupport;
	}
	public boolean add(T o) {
		boolean added = collection.add(o);
		if (added) {
			collectionSupport.fireAdded(this,Collections.singleton(o));
		} else {
			collectionSupport.fireUpdated(this,Collections.singleton(o));
		}
		return added;
	}
	public boolean addAll(Collection<? extends T> c) {
		boolean addedAny = false;
		int max = c.size();
		Collection<T> addedElements = new ArrayList<T>(max), updatedElements = new ArrayList<T>(max);
		for (T t : c) {
			boolean added = collection.add(t); 
			(added ? addedElements : updatedElements).add(t);
			addedAny = added || addedAny;
		}
		collectionSupport.fireAdded(this, addedElements);
		collectionSupport.fireUpdated(this, updatedElements);

		return addedAny;
	}
	public void clear() {
		Collection<T> copy = new ArrayList<T>(this);
		collection.clear();
		collectionSupport.fireRemoved(this, copy);
	}
	public boolean contains(Object o) {
		return collection.contains(o);
	}
	public boolean containsAll(Collection<?> c) {
		return collection.containsAll(c);
	}
	@Override
	public boolean equals(Object obj) {
		return collection.equals(obj);
	}
	@Override
	public int hashCode() {
		return collection.hashCode();
	}
	public boolean isEmpty() {
		return collection.isEmpty();
	}
	public Iterator<T> iterator() {
		final class ListenableIterator implements Iterator<T> {
			Iterator<T> iterator;
			T lastValue;
			DefaultListenableCollection<T> listenableCollection;
			public ListenableIterator(DefaultListenableCollection<T> listenableCollection,Iterator<T> iterator) {
				this.iterator = iterator;
				this.listenableCollection = listenableCollection;
			}
			public boolean hasNext() {
				return iterator.hasNext();
			}
			public T next() {
				lastValue = iterator.next();
				return lastValue;
			}
			public void remove() {
				iterator.remove();
				collectionSupport.fireRemoved(listenableCollection,Collections.singleton(lastValue));
			}
		};
		return new ListenableIterator(this,collection.iterator());
	}
	@SuppressWarnings("unchecked")
	public boolean remove(Object o) {
		boolean removed = collection.remove(o);
		if (removed) {
			collectionSupport.fireRemoved(this,Collections.singleton((T)o));
		}
		return removed;
	}
	@SuppressWarnings("unchecked")
	public boolean removeAll(Collection<?> c) {
		boolean removedAny = false;
		int max = c.size();
		Collection<T> removedElements = new ArrayList<T>(max);
		for (Object t : c) {
			boolean removed = collection.remove(t);
			if (removed) removedElements.add((T)t);
			removedAny = removed || removedAny;
		}
		collectionSupport.fireRemoved(this, removedElements);
		
		return removedAny;
	}
	public boolean retainAll(Collection<?> c) {
		boolean removedAny = false;
		int max = c.size();
		Collection<T> removedElements = new ArrayList<T>(max);
		
		for (Iterator<T> it = iterator(); it.hasNext();) {
			T e = it.next();
			if (!c.contains(e)) {
				it.remove();
				removedElements.add(e);
				removedAny = true;
			}
		}
		collectionSupport.fireRemoved(this, removedElements);
		return removedAny;
	}
	public int size() {
		return collection.size();
	}
	public Object[] toArray() {
		return collection.toArray();
	}
	public <V> V[] toArray(V[] a) {
		return collection.toArray(a);
	}
	@Override
	public String toString() {
		return collection.toString();
	}
}
