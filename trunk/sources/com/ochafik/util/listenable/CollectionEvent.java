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

import java.util.Collection;


public class CollectionEvent<T> {
	ListenableCollection<T> listenableCollection;
	public ListenableCollection<T> getListenableCollection() {
		return listenableCollection;
	}
	public enum EventType {
		ADDED, REMOVED, UPDATED
	}
	
	EventType type;
	public EventType getType() {
		return type;
	}
	Collection<T> elements;
	public Collection<T> getElements() {
		return elements;
	}
	
	int firstIndex = -1, lastIndex = -1;
	public int getFirstIndex() {
		return firstIndex;
	}
	public int getLastIndex() {
		return lastIndex;
	}
	public CollectionEvent(ListenableCollection<T> listenableCollection,Collection<T> elements, EventType type) {
		this.elements = elements;
		this.type = type;
		this.listenableCollection = listenableCollection;
	}
	public CollectionEvent(ListenableCollection<T> listenableCollection,Collection<T> elements, EventType type, int firstIndex, int lastIndex) {
		this.elements = elements;
		this.type = type;
		this.firstIndex = firstIndex;
		this.lastIndex = lastIndex;
		this.listenableCollection = listenableCollection;
	}
	
}
