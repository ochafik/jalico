package com.ochafik.util.listenable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;


public class DefaultListenableList<T> extends DefaultListenableCollection<T> implements ListenableList<T>{
	List<T> list;
	public DefaultListenableList(List<T> list) {
		super(list);
		this.list = list;
	}
	public DefaultListenableList(List<T> list, ListenableSupport<T> collectionSupport) {
		super(list,collectionSupport);
		this.list = list;
	}
	public boolean add(T o) {
		add(size(), o);
		return true;
	}
	public void add(int index, T element) {
		list.add(index, element);
		//System.out.println("add "+index);
		
		collectionSupport.fireAdded(this,Collections.singleton(element), index, index);
	}
	public boolean addAll(int index, Collection<? extends T> c) {
		int initSize = list.size();
		list.addAll(index, c);
		//System.out.println("addAll "+initSize);
		collectionSupport.fireAdded(this, new ArrayList<T>(c), initSize, initSize + c.size() - 1);
		return true;
	}
	public T get(int index) {
		return list.get(index);
	}
	public int indexOf(Object o) {
		return list.indexOf(o);
	}
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}
	public T set(int index, T element) {
		T value = list.set(index, element);
		//System.out.println("set "+index);
		collectionSupport.fireUpdated(this, Collections.singleton(value), index, index);
		return value;
	}
	public List<T> subList(int fromIndex, int toIndex) {
		return new DefaultListenableList<T>(list.subList(fromIndex, toIndex),collectionSupport);
	}
	public ListIterator<T> listIterator(int index) {
		throw new UnsupportedOperationException();
	}
	public T remove(int index) {
		T removed = list.remove(index);
		if (removed != null) {
			collectionSupport.fireRemoved(this,Collections.singleton(removed), index, index);
		}
		return removed;
	}
	public boolean remove(Object o) {
		int i = indexOf(o);
		return i >= 0 && remove(i) != null;
	}
	public void clear() {
		Collection<T> copy = new ArrayList<T>(this);
		collection.clear();
		collectionSupport.fireRemoved(this, copy, 0, copy.size() - 1);
	}
	
}
