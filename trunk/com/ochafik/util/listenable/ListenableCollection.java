package com.ochafik.util.listenable;

import java.util.Collection;

public interface ListenableCollection<T> extends Collection<T>{
	public void addCollectionListener(CollectionListener<T> l);
	public void removeCollectionListener(CollectionListener<T> l);

}
