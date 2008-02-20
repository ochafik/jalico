package com.ochafik.util.listenable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


class SynchronizedListenableMap<K,V> extends DefaultListenableMap<K,V> {
	Object mutex;
 	
	public SynchronizedListenableMap(Map<K, V> map) {
		super(map);
		mutex = this;
	}
	@Override
	public void clear() {
		synchronized (mutex) {
			super.clear();
		}
	}
 	@Override
 	protected Object clone() throws CloneNotSupportedException {
 		synchronized (mutex) {
 			return super.clone();
 		}
 	}
 	
 	@Override
 	public boolean containsKey(Object key) {
 		synchronized (mutex) {
 			return super.containsKey(key);
 		}
 	}
 	@Override
 	public boolean containsValue(Object value) {
 		synchronized (mutex) {
 			return super.containsValue(value);
 		}
 	}
 	@Override
 	public Set<Entry<K, V>> entrySet() {
 		synchronized (mutex) {
 			return new SynchronizedListenableSet<Entry<K,V>>(super.entrySet(), mutex);
 		}
 	}
 	@Override
 	public boolean equals(Object obj) {
 		synchronized (mutex) {
 			return super.equals(obj);
 		}
 	}
 	@Override
 	public V get(Object key) {
 		synchronized (mutex) {
 			return super.get(key);
 		}
 	}
 	@Override
 	public int hashCode() {
 		synchronized (mutex) {
 			return super.hashCode();
 		}
 	}
 	@Override
 	public boolean isEmpty() {
 		synchronized (mutex) {
 			return super.isEmpty();
 		}
 	}
 	
 	@Override
 	public ListenableSet<K> keySet() {
 		synchronized (mutex) {
 			return new SynchronizedListenableSet<K>(super.keySet());
 		}
 	}
 	@Override
 	public V put(K key, V value) {
 		synchronized (mutex) {
 			return super.put(key, value);
 		}
 	}
 	@Override
 	public void putAll(Map<? extends K, ? extends V> t) {
 		synchronized (mutex) {
 			super.putAll(t);
 		}
 	}
 	@Override
 	public V remove(Object key) {
 		synchronized (mutex) {
 			return super.remove(key);
 		}
 	}
 	@Override
 	public int size() {
 		synchronized (mutex) {
 			return super.size();
 		}
 	}
 	@Override
 	public String toString() {
 		synchronized (mutex) {
 			return super.toString();
 		}
 	}
 	@Override
 	public Collection<V> values() {
 		synchronized (mutex) {
 			return new SynchronizedListenableCollection<V>(super.values());
 		}
 	}
 	
}
