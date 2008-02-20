package com.ochafik.util.listenable;


class UnmodifiableListenableSet<T> extends UnmodifiableListenableCollection<T> implements ListenableSet<T> {

	public UnmodifiableListenableSet(ListenableCollection<T> listenableCollection) {
		super(listenableCollection);
	}
	
}
