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
