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

   This file comes from the Jalico project (Java Listenable Collections)

       http://jalico.googlecode.com/.
*/
package com.ochafik.util.listenable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Sub-view of a listenable set, which only contains elements of the original set that were accepted by a Filter.<br/>
 * The set of elements that passed the filter test is cached to improve speed at the expense of memory consumption. 
 * @see com.ochafik.util.listenable.Filter
 * @author ochafik
 * @param <T> type of the elements of the set
 */
public class FilteredListenableSet<T> implements ListenableSet<T>, CollectionListener<T> {
	ListenableSet<T> set;
	Filter<T> filter;
	Set<T> lastFilteredSet;
	
	ListenableSupport<T> collectionSupport = new ListenableSupport<T>();
	public FilteredListenableSet(ListenableSet<T> set) {
		this(set, null);
	}
	public FilteredListenableSet(ListenableSet<T> set, Filter<T> filter) {
		this.set = set;
		this.filter = filter;
		
		for (T element : set) {
			if (filter == null || filter.accept(element)) {
				lastFilteredSet.add(element);
			}
		}
	}
	public void collectionChanged(CollectionEvent<T> e) {
		CollectionEvent.EventType type = e.getType();
		Collection<T> filteredElements = new ArrayList<T>(e.getElements().size());
		for (T element : e.getElements()) {
			if (filter == null || filter.accept(element)) {
				switch (type) {
				case ADDED:
					if (lastFilteredSet.add(element)) {
						filteredElements.add(element);
					}
					break;
				case REMOVED:
					if (lastFilteredSet.remove(element)) {
						filteredElements.add(element);
					}
					break; 
				case UPDATED:
					break;
				}	
			}
			collectionSupport.fireEvent(this, filteredElements, type, -1, -1);
		}
	}
	public void setFilter(Filter<T> filter) {
		int max = set.size();
		Collection<T> addedElements = new ArrayList<T>(max), removedElements = new ArrayList<T>(max);
		synchronized (set) {
			Filter<T> oldFilter = this.filter;
			for (T element : set) {
				if (oldFilter == null || oldFilter.accept(element)) {
					if (filter == null || filter.accept(element)) {
						// still accepted : nothing to do
					} else {
						// not accepted anymore
						if (lastFilteredSet.remove(element)) 
							removedElements.add(element);
					}
				} else {
					if (filter == null || filter.accept(element)) {
						// now accepted
						if (lastFilteredSet.add(element))
							addedElements.add(element);
					} else {
						// still not accepted : nothing to do
					}
				}
			}
		}
		this.filter = filter;
		collectionSupport.fireAdded(this, addedElements);
		collectionSupport.fireRemoved(this, removedElements);
	}
	public boolean add(T o) {
		return set.add(o);
	}
	public boolean addAll(Collection<? extends T> c) {
		return set.addAll(c);
	}
	public void addCollectionListener(CollectionListener<T> l) {
		collectionSupport.addCollectionListener(l);
	}
	public void clear() {
		set.clear();
	}
	public boolean contains(Object o) {
		//return set.contains(o) && (filter == null || filter.accept((T)o));
		return lastFilteredSet.contains(o);
	}
	public boolean containsAll(Collection<?> c) {
		return lastFilteredSet.containsAll(c);
		/*if (!set.containsAll(c))
			return false;
		
		if (filter == null)
			return true;
		
		for (Object o : c) {
			if (!filter.accept((T)o))
				return false;
		}
		return true;*/
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		
		return lastFilteredSet.equals(obj);
		/*if (obj == null || !(obj instanceof Set))
			return false;
		
		Set s = (Set)obj;
		return set.containsAll(s) && s.size() == size();*/
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
		if (lastFilteredSet.contains(o)) {
			return set.remove(o);
		}
		return false;
		//return (filter == null || filter.accept((T)o)) && set.remove(o);
	}
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for (Object o : c) {
			if (remove(o))
				changed = true;
		}
		return changed;
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
