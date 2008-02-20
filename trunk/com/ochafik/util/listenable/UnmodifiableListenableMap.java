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
