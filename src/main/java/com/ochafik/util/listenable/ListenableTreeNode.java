package com.ochafik.util.listenable;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

//@SuppressWarnings("unchecked")
public class ListenableTreeNode implements TreeNode {
	ListenableList list;
	
	private static class NamedEntry<K, V> implements Map.Entry<K, V> {
		Map.Entry<K, V> entry;

		public NamedEntry(Map.Entry<K, V> entry) {
			this.entry = entry;
		}
		public K getKey() {
			return entry.getKey();
		}

		public V getValue() {
			return entry.getValue();
		}

		public V setValue(V value) {
			return entry.setValue(value);
		}
		
		public String toString() {
			K key = entry.getKey();
			return key == null ? null : key.toString();
		}
		
		public boolean equals(Object o) {
			return entry.equals(o);
		}
	}
	
	public <K, V> ListenableTreeNode(Map<K, V> map) {
		this(ListenableCollections.asList(
			new AdaptedCollection<Map.Entry<K,V>, Map.Entry<K,V>>(
				map.entrySet(),
				new Adapter<Map.Entry<K,V>, Map.Entry<K,V>>() {
					public Map.Entry<K,V> adapt(Map.Entry<K,V> value) {
						return new NamedEntry<K, V>(value);
					}
				},
				new Adapter<Map.Entry<K,V>, Map.Entry<K,V>>() {
					public Map.Entry<K,V> adapt(Map.Entry<K,V> value) {
						return value;
					}
				}
			)
		));
	}
	
	@SuppressWarnings("unchecked")
	public ListenableTreeNode(Collection collection) {
		this(ListenableCollections.asList(ListenableCollections.listenableCollection(collection)));
	}
	
	public <T> ListenableTreeNode(ListenableList<T> list) {
		this.list = list;
		list.addCollectionListener(new CollectionListener<T>() {
			public void collectionChanged(CollectionEvent<T> e) {
				
			}
			
		});
	}

	public Enumeration children() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getAllowsChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	public TreeNode getChildAt(int childIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getChildCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getIndex(TreeNode node) {
		// TODO Auto-generated method stub
		return 0;
	}

	public TreeNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
