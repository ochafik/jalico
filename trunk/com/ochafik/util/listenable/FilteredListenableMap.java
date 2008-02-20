package com.ochafik.util.listenable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public class FilteredListenableMap<K, V> implements ListenableMap<K, V> {
	ListenableMap<K, V> listenableMap;

	
	public FilteredListenableMap(ListenableMap<K, V> listenableMap) {
		this.listenableMap = listenableMap;
	}

	public void clear() {
		listenableMap.clear();
	}

	public boolean containsKey(Object key) {
		
		return listenableMap.containsKey(key);
	}

	public boolean containsValue(Object value) {
		
		return listenableMap.containsValue(value);
	}

	public Set<Entry<K, V>> entrySet() {
		
		return listenableMap.entrySet();
	}

	public V get(Object key) {
		
		return listenableMap.get(key);
	}

	public boolean isEmpty() {
		
		return listenableMap.isEmpty();
	}

	public ListenableSet<K> keySet() {
		
		return listenableMap.keySet();
	}

	public V put(K key, V value) {
		
		return listenableMap.put(key, value);
	}

	public void putAll(Map<? extends K, ? extends V> t) {
		listenableMap.putAll(t);
		
	}

	public V remove(Object key) {
		
		return listenableMap.remove(key);
	}

	public int size() {
		
		return listenableMap.size();
	}

	public Collection<V> values() {
		
		return listenableMap.values();
	}

	public ListenableSet<K> listenableKeySet() {
		
		return listenableMap.keySet();
	}
	
	
}
