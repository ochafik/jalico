package com.ochafik.util.listenable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Helper class that keeps track of registered CollectionListener instances and eases up the firing of CollectionEvent.
 * @author Olivier Chafik
 * @param <T> type of the collections
 */
public class ListenableSupport<T> {
	protected Collection<CollectionListener<T>> listeners;
	
	public void addCollectionListener(CollectionListener<T> l) {
		if (listeners == null)
			listeners = new ArrayList<CollectionListener<T>>(1);
		
		listeners.add(l);
	}
	
	public void removeCollectionListener(CollectionListener<T> l) {
		if (listeners == null)
			return;
		
		listeners.remove(l);
	}

	public boolean hasListeners() {
		return !listeners.isEmpty();
	}
	
	public void fireEvent(ListenableCollection<T> listenableCollection, Collection<T> elements, CollectionEvent.EventType type, int firstIndex, int lastIndex) {
		if (listeners == null || listeners.isEmpty() || elements.isEmpty()) 
			return;
		
		CollectionEvent<T> event = new CollectionEvent<T>(listenableCollection,elements,type, firstIndex, lastIndex);
		for (CollectionListener<T> listener : listeners) {
			listener.collectionChanged(event);
		}
	}
	
	public void fireAdded(ListenableCollection<T> listenableCollection, Collection<T> elements) {
		fireEvent(listenableCollection, elements, CollectionEvent.EventType.ADDED, -1, -1);
	}
	public void fireAdded(ListenableCollection<T> listenableCollection, Collection<T> elements, int firstIndex, int lastIndex) {
		fireEvent(listenableCollection, elements, CollectionEvent.EventType.ADDED, firstIndex, lastIndex);
	}
	
	public void fireRemoved(ListenableCollection<T> listenableCollection, Collection<T> elements) {
		fireEvent(listenableCollection, elements, CollectionEvent.EventType.REMOVED, -1, -1);
	}
	public void fireRemoved(ListenableCollection<T> listenableCollection, Collection<T> elements, int firstIndex, int lastIndex) {
		fireEvent(listenableCollection, elements, CollectionEvent.EventType.REMOVED, firstIndex, lastIndex);
	}
	
	public void fireUpdated(ListenableCollection<T> listenableCollection, Collection<T> elements) {
		fireEvent(listenableCollection, elements, CollectionEvent.EventType.UPDATED, -1, -1);
	}
	public void fireUpdated(ListenableCollection<T> listenableCollection, Collection<T> elements, int firstIndex, int lastIndex) {
		fireEvent(listenableCollection, elements, CollectionEvent.EventType.UPDATED, firstIndex, lastIndex);
	}
}
