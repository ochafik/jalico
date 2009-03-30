package com.ochafik.util.listenable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;


public class ListenableListTableModel<T> extends AbstractTableModel {
	private static final long serialVersionUID = -9033649750678420736L;
	
	public static abstract class ColumnAdapter<T> implements Adapter<T, Object> {
		String name;
		Class<?> type;
		public ColumnAdapter(String name, Class<?> type) {
			this.name = name;
			this.type = type;
		}
		public ColumnAdapter(String name) {
			this.name = name;
		}
		public String toString() {
			return name;
		}
		
		public Class<?> getType() {
			return type == null ? Object.class : type;
		}
	}
	
	ListenableList<T> list;
	List<ColumnAdapter<T>> columnAdapters = new ArrayList<ColumnAdapter<T>>();
	Map<String, Integer> nameToColIndex = new HashMap<String, Integer>();
	
	private CollectionListener<T> collectionListener = new SwingCollectionListener<T>(new CollectionListener<T>() {
		public void collectionChanged(CollectionEvent<T> e) {
			int firstRow = e.getFirstIndex(), lastRow = e.getLastIndex();
			switch (e.getType()) {
			case ADDED:
				//System.out.println("added "+firstRow + ", "+lastRow);
				fireTableRowsInserted(firstRow, lastRow);
				break;
			case REMOVED:
				//System.out.println("removed "+firstRow + ", "+lastRow);
				fireTableRowsDeleted(firstRow, lastRow);
				break;
			case UPDATED:
				//System.out.println("updated "+firstRow + ", "+lastRow);
				fireTableRowsUpdated(firstRow, lastRow);
				break;
			}
		}
	});
	
	public ListenableListTableModel(ListenableList<T> list) {
		setList(list);
	}
	public void setList(ListenableList<T> list) {
		if (this.list == list)
			return;
		
		if (this.list != null) {
			list.removeCollectionListener(collectionListener);
			if (!list.isEmpty())
				fireTableRowsDeleted(0, list.size() - 1);
		}

		this.list = list;
		if (list != null)
			list.addCollectionListener(collectionListener);
	}
	
	boolean upToDate = false;
	public void addColumnAdapter(ColumnAdapter<T> columnAdapter) {
		columnAdapters.add(columnAdapter);
		upToDate = false;
	}	
	protected void updateNameToColIndex() {
		if (upToDate) return;
		int i = 0;
		nameToColIndex.clear();
		for (ColumnAdapter<T> columnAdapter : columnAdapters) {
			nameToColIndex.put(columnAdapter.toString(), i++);
		}
		upToDate = true;
	}
	public int getColumn(String name) {
		updateNameToColIndex();
		return nameToColIndex.get(name);
	}
	public ListenableList<T> getList() { 
		return list; 
	}
	public int getRowCount() {
		return list == null ? 0 : list.size();
	}
	public int getColumnCount() {
		return columnAdapters.isEmpty() ? 1 : columnAdapters.size();
	}
	
	public String getColumnName(int column) {
		return columnAdapters.get(column).toString();
	}
	public Class getColumnClass(int column) {
		return columnAdapters.get(column).getType();
	}
	public Object getValueAt(int row, int column) {
		if (list == null)
			return null;
		
		T item = list.get(row);
		return column < 0 || columnAdapters.isEmpty() ? item : columnAdapters.get(column).adapt(item);
	}
}
