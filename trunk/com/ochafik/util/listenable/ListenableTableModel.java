package com.ochafik.util.listenable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class ListenableTableModel<T> extends AbstractTableModel {
	private static final long serialVersionUID = -9033649750678420736L;
	
	public static abstract class AbstractNamedAdapter<T> implements Adapter<T, Object> {
		String name;
		public AbstractNamedAdapter(String name) {
			this.name = name;
		}
		public String toString() {
			return name;
		}
	}
	
	ListenableList<T> list;
	List<Adapter<T, Object>> columnAdapters = new ArrayList<Adapter<T, Object>>();
	Map<String, Integer> nameToColIndex = new HashMap<String, Integer>();
	
	public ListenableTableModel(ListenableList<T> list) {
		this.list = list;
		list.addCollectionListener(new SwingCollectionListener<T>(new CollectionListener<T>() {
			public void collectionChanged(CollectionEvent<T> e) {
				int firstRow = e.getFirstIndex(), lastRow = e.getLastIndex();
				switch (e.getType()) {
				case ADDED:
					fireTableRowsInserted(firstRow, lastRow);
					break;
				case REMOVED:
					fireTableRowsDeleted(firstRow, lastRow);
					break;
				case UPDATED:
					fireTableRowsUpdated(firstRow, lastRow);
					break;
				}
			}
		}));
	}
	
	boolean upToDate = false;
	public void addColumnAdapter(Adapter<T, Object> columnAdapter) {
		columnAdapters.add(columnAdapter);
		upToDate = false;
	}	
	protected void updateNameToColIndex() {
		if (upToDate) return;
		int i = 0;
		nameToColIndex.clear();
		for (Adapter<T, Object> columnAdapter : columnAdapters) {
			nameToColIndex.put(columnAdapter.toString(), i++);
		}
		upToDate = true;
	}
	public int getColumn(String name) {
		updateNameToColIndex();
		return nameToColIndex.get(name);
	}
	public ListenableList<T> getList() { return list; }
	public int getRowCount() {
		return list.size();
	}
	public int getColumnCount() {
		return columnAdapters.isEmpty() ? 1 : columnAdapters.size();
	}
	
	public String getColumnName(int column) {
		return columnAdapters.get(column).toString();
	}
	public Object getValueAt(int row, int column) {
		T item = list.get(row);
		return column < 0 || columnAdapters.isEmpty() ? item : columnAdapters.get(column).adapt(item);
	}
}
