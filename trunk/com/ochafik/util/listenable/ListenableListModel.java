package com.ochafik.util.listenable;

import java.awt.Component;

import javax.swing.AbstractListModel;

public class ListenableListModel<T> extends AbstractListModel {
	private ListenableList<T> list;
	
	public ListenableListModel(ListenableList<T> list) {
		setList(list);
	}
	public ListenableListModel() {}
	
	CollectionListener<T> listener  =new SwingCollectionListener<T>(new CollectionListener<T>() {
		public void collectionChanged(CollectionEvent<T> e) {
			switch (e.getType()) {
			case ADDED:
				fireIntervalAdded(this, e.getFirstIndex(), e.getLastIndex());
				break;
			case REMOVED:
				fireIntervalRemoved(this, e.getFirstIndex(), e.getLastIndex());
				break;
			case UPDATED:
				fireContentsChanged(this, e.getFirstIndex(), e.getLastIndex());
				break;
			}
		}
	});
	public void setList(ListenableList<T> list) {
		ListenableList<T> oldList = this.list;
		if (oldList != null) {
			this.list = null;
			oldList.removeCollectionListener(listener);
			if (!oldList.isEmpty()) {
				fireIntervalRemoved(this, 0, oldList.size() - 1);
			}
		}
		this.list = list;
		if (list == null)
			return;
		
		list.addCollectionListener(listener);
		if (!list.isEmpty()) {
			fireIntervalAdded(this, 0, list.size() - 1);
		}
		
	}
	public Object getElementAt(int index) {
		return list.get(index);
	}

	public int getSize() {
		return list == null ? 0 : list.size();
	}
	public ListenableList<T> getList() {
		return list;
	}

}
