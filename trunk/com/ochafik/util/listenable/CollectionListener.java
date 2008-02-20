package com.ochafik.util.listenable;

public interface CollectionListener<T> {
	public void collectionChanged(CollectionEvent<T> e);
}
