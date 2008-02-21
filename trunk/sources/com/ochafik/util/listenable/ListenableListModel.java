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

   This file comes from the Jalico project (Java Listenable Collections)

       http://jalico.googlecode.com/.
*/
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
