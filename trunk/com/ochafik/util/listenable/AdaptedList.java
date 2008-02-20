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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.ochafik.util.Adapter;

public class AdaptedList<U, V> extends AdaptedCollection<U,V> implements ListenableList<V> {
	protected final List<U> list;
	
	public AdaptedList(List<U> list, Adapter<U, V> forwardAdapter) {
		this(list, forwardAdapter, null);
	}
	
	public AdaptedList(List<U> list, Adapter<U, V> forwardAdapter, Adapter<V, U> backwardAdapter) {
		super(list, forwardAdapter, backwardAdapter);
		this.list = list;
	}
	
	public void add(int index, V value) {
		if (backwardAdapter == null)
			throw new UnsupportedOperationException("No backward adapter in this AdapterList");
		
		try {
			currentlyCausingChange = true;
			list.add(index, backwardAdapter.adapt(value));
			collectionSupport.fireAdded(this, (Collection<V>)Collections.singleton(value), index, index);
		} finally {
			currentlyCausingChange = false;
		}
	}
	
	public V remove(int index) {
		try {
			currentlyCausingChange = true;
			V adapted = forwardAdapter.adapt(list.remove(index));
			collectionSupport.fireRemoved(this, Collections.singleton(adapted), index, index);
			return adapted;
		} finally {
			currentlyCausingChange = false;
		}
	}

	public V set(int index, V element) {
		if (backwardAdapter == null)
			throw new UnsupportedOperationException("No backward adapter in this AdapterList");
		
		try {
			currentlyCausingChange = true;
			V adapted = forwardAdapter.adapt(list.set(index, backwardAdapter.adapt(element)));
			collectionSupport.fireUpdated(this, Collections.singleton(adapted), index, index);
			return adapted;
		} finally {
			currentlyCausingChange = false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected boolean removeWithoutBackWardAdapter(Object value) {
		int i = list.indexOf(backwardAdapter.adapt((V)value));
		if (i < 0)
			return false;
		
		try {
			currentlyCausingChange = true;
			list.remove(i);
			collectionSupport.fireRemoved(this, (Collection<V>)Collections.singleton(value), i, i);
			return true;
		} finally {
			currentlyCausingChange = false;
		}
	}

	@Override
	public boolean add(V value) {
		add(size(), value);
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean addAll(int index, Collection<? extends V> values) {
		if (values.isEmpty())
			return false;
		
		if (backwardAdapter == null)
			throw new UnsupportedOperationException("No backward adapter in this AdapterList");
		
		try {
			currentlyCausingChange = true;
			int initialSize = list.size();
			if (list.addAll(index, new AdaptedCollection<V, U>((Collection<V>)values, backwardAdapter, forwardAdapter))) {
				collectionSupport.fireAdded(this, (Collection<V>)values, index, index + values.size() - 1);
				return true;
			} else if (list.size() > initialSize){
				throw new UnsupportedOperationException("Not handling wrapped collections that do not handle addAll atomically !");
			} else {
				return false;
			}
		} finally {
			currentlyCausingChange = false;
		}
	}

	public V get(int index) {
		return forwardAdapter.adapt(list.get(index));
	}

	@SuppressWarnings("unchecked")
	public int indexOf(Object o) {
		if (backwardAdapter == null)
			throw new UnsupportedOperationException("No backward adapter in this AdapterList");
		
		return list.indexOf(backwardAdapter.adapt((V)o));
	}

	@SuppressWarnings("unchecked")
	public int lastIndexOf(Object o) {
		if (backwardAdapter == null)
			throw new UnsupportedOperationException("No backward adapter in this AdapterList");
		
		return list.lastIndexOf(backwardAdapter.adapt((V)o));
	}

	protected class ListIteratorAdapter implements ListIterator<V> {
		protected ListIterator<U> listIterator;
		protected V lastValue;
		
		public ListIteratorAdapter(ListIterator<U> listIterator) {
			this.listIterator = listIterator;
		}
		
		public boolean hasNext() {
			return listIterator.hasNext();
		}
		
		public V next() {
			return lastValue = forwardAdapter.adapt(listIterator.next());
		}
		
		public V previous() {
			return lastValue = forwardAdapter.adapt(listIterator.previous());
		}

		public void remove() {
			try {
				currentlyCausingChange = true;
				listIterator.remove();
				int index = listIterator.previousIndex();
				collectionSupport.fireRemoved(AdaptedList.this, Collections.singleton(lastValue), index, index);
			} finally {
				currentlyCausingChange = false;
			}	
		}
		
		public void add(V o) {
			if (backwardAdapter == null)
				throw new UnsupportedOperationException("No backward adapter in this iterator's AdapterList");
			
			listIterator.add(backwardAdapter.adapt((V)o));
		}

		public boolean hasPrevious() {
			return listIterator.hasPrevious();
		}

		public int nextIndex() {
			return listIterator.nextIndex();
		}

		public int previousIndex() {
			return listIterator.previousIndex();
		}

		public void set(V o) {
			if (backwardAdapter == null)
				throw new UnsupportedOperationException("No backward adapter in this iterator's AdapterList");
			
			listIterator.set(backwardAdapter.adapt((V)o));
		}
		
	}
	public ListIterator<V> listIterator() {
		return new ListIteratorAdapter(list.listIterator());
	}

	public ListIterator<V> listIterator(int index) {
		return new ListIteratorAdapter(list.listIterator(index));
	}

	public List<V> subList(int fromIndex, int toIndex) {
		return new AdaptedList<U, V>(list.subList(fromIndex, toIndex), forwardAdapter, backwardAdapter);
	}

}
