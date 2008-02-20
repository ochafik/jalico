package com.ochafik.util.listenable;

import java.util.Map;


public interface ListenableMap<K,V> extends Map<K,V> {
	public ListenableSet<K> keySet();
	//public ListenableSet<Map.Entry<K,V>> entrySet();
}
