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
import java.util.List;
import java.util.Map;
import java.util.Set;



public class ListenableCollections {
	public static final <T> ListenableSet<T> unmodifiableSet(ListenableSet<T> set) {
		return new UnmodifiableListenableSet<T>(set);
	}
	
	public static final <T> ListenableCollection<T> unmodifiableCollection(ListenableCollection<T> col) {
		return new UnmodifiableListenableCollection<T>(col);
	}
	
	public static final <K,V> ListenableMap<K,V> unmodifiableMap(ListenableMap<K,V> map) {
		return new UnmodifiableListenableMap<K,V>(map);
	}
	
	public static final <T> ListenableCollection<T> listenableCollection(Collection<T> x) {
		return new DefaultListenableCollection<T>(x);
	}
	
	public static final <T> ListenableSet<T> listenableSet(Set<T> x) {
		return new DefaultListenableSet<T>(x);
	}
	
	public static final <K,V> ListenableMap<K,V> listenableMap(Map<K,V> x) {
		return new DefaultListenableMap<K,V>(x);
	}
	
	public static <T> ListenableList<T> asList(ListenableSet<T> source) {
		ListenableList<T> out = new DefaultListenableList<T>(new ArrayList<T>());
		bind(source, out);
		return out;
	}
	public static <T> void bind(final ListenableCollection<T> a, final ListenableCollection<T> b) {
		CollectionListener<T> listener = new CollectionListener<T>() {
			boolean currentlyPropagating = false;
			
			@SuppressWarnings("unchecked")
			public void collectionChanged(CollectionEvent<T> e) {
				// Avoid infinite propagation of events
				if (currentlyPropagating) 
					return;
				
				//System.out.println("Propagating "+e.getType() + " for elements "+e.getElements());
				try {
					currentlyPropagating = true;
					ListenableCollection<T> recipient = e.getListenableCollection() == a ? b : a;
					
					for (T t : e.getElements()) {
						switch (e.getType()) {
						case ADDED:
							recipient.add(t);
							break;
						case REMOVED:
							recipient.remove(t);
							break;
						case UPDATED:
							if (recipient instanceof List) {
								List<T> recipientList = (List<T>)recipient;
								int i = recipientList.indexOf(t);
								recipientList.set(i, t);
							} else if (recipient instanceof Set) {
								recipient.add(t);
							} else {
								// Add might do a duplicate, so first remove element.
								recipient.remove(t);
								recipient.add(t);
							}
							break;
						}
					}
				} finally {
					currentlyPropagating = false;
				}
			}
		};
		a.addCollectionListener(listener);
		b.addCollectionListener(listener);
	}
}
