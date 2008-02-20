/*
   Copyright 2008 Olivier Chafik

   Licensed under the Apache License, Version 2.0 (the License);
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an AS IS BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   This file is part of the Jalico project (Java Listenable Collections).
*/
package com.ochafik.util.listenable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.ochafik.util.Filter;


public class FilteredListenableSet<T> implements ListenableSet<T>, CollectionListener<T> {
	ListenableSet<T> listenableSet;
	Filter<T> filter;
	Set<T> lastFilteredSet;
	
	ListenableSupport<T> collectionSupport = new ListenableSupport<T>();
	public FilteredListenableSet(ListenableSet<T> listenableSet,Filter<T> filter) {
		this.listenableSet = listenableSet;
		this.filter = filter;
		synchronized (listenableSet) {
			lastFilteredSet = new HashSet<T>(listenableSet.size());
			for (T element : listenableSet) {
				if (filter == null || filter.acceptValue(element)) {
					lastFilteredSet.add(element);
				}
			}
			listenableSet.addCollectionListener(this);
		}
	}
	public void collectionChanged(CollectionEvent<T> e) {
		CollectionEvent.EventType type = e.getType();
		Collection<T> effectiveElements = new ArrayList<T>(e.getElements().size());
		for (T element : e.getElements()) {
			if (filter == null || filter.acceptValue(element)) {
				switch (type) {
				case ADDED:
					if (lastFilteredSet.add(element)) {
						effectiveElements.add(element);
					}
					break;
				case REMOVED:
					if (lastFilteredSet.remove(element)) {
						effectiveElements.add(element);
					}
					break; 
				case UPDATED:
					break;
				}	
			}
			collectionSupport.fireEvent(this, effectiveElements, type, -1, -1);
		}
	}
	public void setFilter(Filter<T> filter) {
		int max = listenableSet.size();
		Collection<T> addedElements = new ArrayList<T>(max), removedElements = new ArrayList<T>(max);
		synchronized (listenableSet) {
			for (T element : listenableSet) {
				if (filter == null || filter.acceptValue(element)) {
					if (!(this.filter == null || this.filter.acceptValue(element))) {
						// newly accepted
						if (lastFilteredSet.add(element)) {
							addedElements.add(element);
						}
					}
				} else if (this.filter == null || this.filter.acceptValue(element)) {
					// newly refused
					if (lastFilteredSet.remove(element)) {
						removedElements.add(element);
					}
				}
			}
		}
		this.filter = filter;
		collectionSupport.fireAdded(this, addedElements);
		collectionSupport.fireRemoved(this, removedElements);
	}
	public boolean add(T o) {
		throw new UnsupportedOperationException();
	}
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}
	public void addCollectionListener(CollectionListener<T> l) {
		collectionSupport.addCollectionListener(l);
	}
	public void clear() {
		throw new UnsupportedOperationException();
	}
	public boolean contains(Object o) {
		return lastFilteredSet.contains(o);
	}
	public boolean containsAll(Collection<?> c) {
		return lastFilteredSet.containsAll(c);
	}
	@Override
	public boolean equals(Object obj) {
		return lastFilteredSet.equals(obj);
	}
	@Override
	public int hashCode() {
		return lastFilteredSet.hashCode();
	}
	public boolean isEmpty() {
		return lastFilteredSet.isEmpty();
	}
	public Iterator<T> iterator() {
		return Collections.unmodifiableCollection(lastFilteredSet).iterator();
	}
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	public void removeCollectionListener(CollectionListener<T> l) {
		collectionSupport.removeCollectionListener(l);
	}
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	public int size() {
		return lastFilteredSet.size();
	}
	public Object[] toArray() {
		return lastFilteredSet.toArray();
	}
	public <V> V[] toArray(V[] a) {
		return lastFilteredSet.toArray(a);
	}
	
}
