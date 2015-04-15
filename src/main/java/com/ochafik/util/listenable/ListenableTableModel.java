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

import sun.awt.shell.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;

/**
 * Swing table model (for use by {@link javax.swing.JTable}) that dynamically reflects the contents of a listenable list.<br/>
 * The model sets up one list item per row, and requires column adapters for each column.<br/>
 * A column adapter defines how to get the value a particular row and column from the item of this row (it <i>adapts</i> the item to a cell value).<br/>
 * For convenience, an extra hidden column is added at column index -1, which value is the item of the row itself.
 * <p>
 * For instance, if you want to display a list of files, you might do the following :<br/>
 * <code><pre>
 * 	ListenableList&lt;File&gt; filesList = new DefaultListenableList&lt;File&gt;(new ArrayList&lt;File&gt;(Arrays.asList(new File(".").listFiles())));
 * 	ListenableTableModel&lt;File&gt; tableModel = new ListenableTableModel&lt;File&gt;(filesList);
 * 	tableModel.columns.add(new Adapter&lt;File,Object&gt;() {
 * 		public String toString() { return "File Name"; } 
 * 		public Object adapt(File file) { return file.getName(); }
 * 	});
 * 	tableModel.columns.add(new Adapter&lt;File,Object&gt;() {
 * 		public String toString() { return "Size"; } 
 * 		public Object adapt(File file) { return file.length(); }
 * 	});
 * 	tableModel.columns.add(new Adapter&lt;File,Object&gt;() {
 * 		public String toString() { return "Last Modification"; } 
 *		public Object adapt(File file) { return new Date(file.lastModified()); }
 *	});
 *	JFrame frame = new JFrame();
 *	frame.getContentPane().add("Center", new JScrollPane(new JTable(tableModel)));
 *	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 *	frame.pack();
 *	frame.setVisible(true);
 * </pre></code>
 * This model safely propagates events from the listenable list to any registered ListDataListener within the event dispatch thread, even if the events were received from an other thread.
 * @see javax.swing.event.ListDataListener
 * @see javax.swing.JList
 * @see com.ochafik.util.listenable.SwingCollectionListener 
 * @author Olivier Chafik
 * @param <T> Type of the elements of the list
 */
public class ListenableTableModel<T> extends AbstractTableModel {
	private static final long serialVersionUID = -9033649750678420736L;

	public final ListenableList<T> list;
	public final ListenableList<Adapter<T, Object>> columns = ListenableCollections.listenableList(new ArrayList<Adapter<T, Object>>());
	
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
		columns.addCollectionListener(new CollectionListener<Adapter<T,Object>>() { public void collectionChanged(com.ochafik.util.listenable.CollectionEvent<com.ochafik.util.listenable.Adapter<T,Object>> e) {
			fireTableStructureChanged();
		}});
	}
	
	public int getRowCount() {
		return list.size();
	}
	
	public int getColumnCount() {
		return columns.isEmpty() ? 1 : columns.size();
	}

	public String getColumnName(int column) {
		return columns.get(column).toString();
	}
	
	public Object getValueAt(int row, int column) {
		T item = list.get(row);
		return column < 0 || columns.isEmpty() ? item : columns.get(column).adapt(item);
	}

	public static void main(String[] args) {
		ListenableList<File> filesList = new DefaultListenableList<File>(new ArrayList<File>(Arrays.asList(new File(".").listFiles())));
		ListenableTableModel<File> tableModel = new ListenableTableModel<File>(filesList);
		
		abstract class Column implements Adapter<File,Object> {
			String name;
			public Column(String name) { this.name = name; }
			public String toString() { return name; }
		};
		tableModel.columns.add(new Column("Ic™ne") { 
            public Object adapt(File file) { return FileSystemView.getFileSystemView().getSystemIcon(file); }
        });
		tableModel.columns.add(new Column("File Name") { 
			public Object adapt(File file) { return file.getName(); }
		});
		tableModel.columns.add(new Column("Size") { 
			public Object adapt(File file) { return file.length(); }
		});
		tableModel.columns.add(new Column("Last Modification") { 
			public Object adapt(File file) { return new Date(file.lastModified()); }
		});
		
		JFrame frame = new JFrame();
		frame.getContentPane().add("Center", new JScrollPane(new JTable(tableModel)));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);		
	}
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		for (T item : list) {
			Object value = columns.get(columnIndex).adapt(item);
			if (value != null) {
				Class<?> c = value.getClass();
				if (Icon.class.isAssignableFrom(c))
					c = Icon.class;
				return c;
			}
		}
		return super.getColumnClass(columnIndex);
	}
}
