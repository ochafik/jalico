package com.ochafik.util.listenable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class DefaultListenableMap<K,V> implements ListenableMap<K,V> {
	Map<K,V> map;
 	ListenableSupport<K> collectionSupport;
 	ListenableSet<K> listenableKeySet;
 	
 	public ListenableSet<K> keySet() {
 		if (listenableKeySet == null) {
 			listenableKeySet = new DefaultListenableSet<K>(map.keySet(),collectionSupport);
 		}
 		return listenableKeySet;
 	}
	public DefaultListenableMap(Map<K,V> map) {
		this.map = map;
		collectionSupport = new ListenableSupport<K>();
	}
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}
	public Set<Entry<K, V>> entrySet() {
		return Collections.unmodifiableSet(map.entrySet());
	}
	public int size() {
		return map.size();
	}
	@Override
	public String toString() {
		return map.toString();
	}
	public void clear() {
		for (Iterator<K> it = keySet().iterator(); it.hasNext();) {
			it.next();
			it.remove();
		}
	}
	@Override
	public boolean equals(Object obj) {
		return map.equals(obj);
	}
	public V get(Object key) {
		return map.get(key);
	}
	@Override
	public int hashCode() {
		return map.hashCode();
	}
	public boolean isEmpty() {
		return map.isEmpty();
	}
	public V put(K key, V value) {
		V v = map.put(key,value);
		if (v !=null) {
			collectionSupport.fireUpdated(keySet(), Collections.singleton(key));
		} else {
			collectionSupport.fireAdded(keySet(), Collections.singleton(key));
		}
		return v;
	} 
	public void putAll(Map<? extends K, ? extends V> t) {
		Collection<K> added = new ArrayList<K>(t.size());
		for (Map.Entry<? extends K, ? extends V> e : t.entrySet()) {
			K key = e.getKey();
			if (map.put(key, e.getValue()) == null)
				added.add(key);
			//put(e.getKey(),e.getValue());
		}
		if (!added.isEmpty())
			collectionSupport.fireAdded(keySet(), added);
	}
	@SuppressWarnings("unchecked")
	public V remove(Object key) {
		V v = map.remove(key);
		if (v !=null) {
			collectionSupport.fireRemoved(keySet(),Collections.singleton((K)key));
		}
		return v;
	}
	public Collection<V> values() {
		return Collections.unmodifiableCollection(map.values());
	}
	
}
