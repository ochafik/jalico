package com.ochafik.util.listenable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;


class UnmodifiableListenableMap<K, V> extends FilteredListenableMap<K, V>{

	
	public UnmodifiableListenableMap(ListenableMap<K, V> listenableMap) {
		super(listenableMap);
	}
	@Override
	public void clear() {
		throw new UnsupportedOperationException("Unmodifiable map !");
	}
	@Override
	public Set<Entry<K, V>> entrySet() {
		return Collections.unmodifiableSet(listenableMap.entrySet());
	}
	@Override
	public ListenableSet<K> keySet() {
		return new UnmodifiableListenableSet<K>(listenableMap.keySet());
		//return Collections.unmodifiableSet(listenableMap.keySet());
	}
	@Override
	public ListenableSet<K> listenableKeySet() {
		return new UnmodifiableListenableSet<K>(listenableMap.keySet());
	}
	@Override
	public V put(K key, V value) {
		throw new UnsupportedOperationException("Unmodifiable map !");
	}
	@Override
	public void putAll(Map<? extends K, ? extends V> t) {
		throw new UnsupportedOperationException("Unmodifiable map !");
	}
	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException("Unmodifiable map !");
	}
}
