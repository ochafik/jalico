package com.ochafik.util.listenable;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

class UnmodifiableListenableList<T> extends UnmodifiableListenableCollection<T> implements ListenableList<T> {
	final ListenableList<T> listenableList;
	public UnmodifiableListenableList(
			ListenableList<T> listenableList) {
		super(listenableList);
		this.listenableList = listenableList;
	}

	void ex() {
		throw new UnsupportedOperationException("Unmodifiable list");
	}
	@Override
	public void add(int index, T element) {
		ex();
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		ex();
		return false;
	}

	@Override
	public T get(int index) {
		return listenableList.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return listenableList.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return listenableList.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		ex();
		return null;
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		ex();
		return null;
	}

	@Override
	public T remove(int index) {
		ex();
		return null;
	}

	@Override
	public T set(int index, T element) {
		ex();
		return null;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		ex();
		return null;
	}
	
}
