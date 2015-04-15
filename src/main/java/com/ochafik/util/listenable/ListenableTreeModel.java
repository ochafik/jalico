package com.ochafik.util.listenable;

import java.io.InvalidClassException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Preliminary code for listenable tree model
 * @author ochafik
 */
public class ListenableTreeModel implements TreeModel {
	Collection<TreeModelListener> listeners = new ArrayList<TreeModelListener>();
	Object root;
	
	public void addTreeModelListener(TreeModelListener l) {
		listeners.add(l);
	}
	
	public ListenableTreeModel(Object root) {
		this.root = new ValueHolder(root);
	}

	@SuppressWarnings("unchecked")
	public ValueHolder getChild(Object parent, int index) {
		Collection col = asCollection(parent);
		return col == null ? null : new ValueHolder(Nth(col, index));
	}

	@SuppressWarnings("unchecked")
	public int getChildCount(Object parent) {
		Collection col = asCollection(parent);
		return col == null ? 0 : col.size();
	}
	
	static <T> int indexOf(Collection<T> list, Object o) {
		if (list instanceof List)
			return ((List<T>)list).indexOf(o);
		
		int i = 0;
		for (Object e : list) {
			if (e.equals(o))
				return i;
			i++;
		}
		return -1;
	}
	
	static <T> T Nth(Collection<T> list, int index) {
		if (list instanceof List)
			return ((List<T>)list).get(index);
		
		int i = 0;
		for (T e : list) {
			if (i == index)
				return e;
			i++;
		}
		throw new IndexOutOfBoundsException(index+"");
	}

	static Object extractValue(Object object) {
		while (object instanceof ValueHolder)
			object = ((ValueHolder)object).getValue();
		return object;
	}
	
	@SuppressWarnings("unchecked")
	public int getIndexOfChild(Object parent, Object child) {
		Collection col = asCollection(parent);
		return col == null ? -1 : indexOf(col, extractValue(child));
	}

	public Object getRoot() {
		return root;
	}

	public class ValueHolder {
		private final Object value;
		
		public ValueHolder(Object value) {
			this.value = extractValue(value);
		}
		public Object getValue() { return value; }
		
		@SuppressWarnings("unchecked")
		public String toString() {
			String string = null;
			try {
				string = stringifier == null ? null : stringifier.adapt(value);
			} catch (Throwable th) {
				th.printStackTrace();
			}
			if (string == null) {
				if (isLeaf(value))
					string = value.toString();
				else if (value instanceof Map.Entry)
					string = ((Map.Entry)value).getKey().toString();
				else if (value instanceof Pair)
					string = ((Pair)value).getFirst().toString();
			}

			return string;
		}
	}
	
	Adapter<Object, String> stringifier;
	
	@SuppressWarnings("unchecked")
	static Collection asCollection(Object object) {
		object = extractValue(object);
		
		if (object instanceof Map.Entry)
			object = ((Map.Entry)object).getValue();
		
		if (object instanceof Pair)
			object = ((Pair)object).getSecond();
		
		if (object instanceof Collection)
			return (Collection)object;
		
		if (object instanceof Map)
			return ((Map)object).entrySet();
		
		Class c = object.getClass();
		if (c.isArray())
			return Arrays.asList((Object[])object);
		
		return null;
	}
	
	public boolean isLeaf(Object parent) {
		return asCollection(parent) == null;
	}

	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(l);
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Map<String, Object> root = new TreeMap<String, Object>();
		root.put("Un", "ok");
		root.put("Deux", "ok");
		root.put("Trois", "ok");
		root.put("Tableau", new String[] {"", ")"});
		root.put("RŽcursion !", root);
		
		f.getContentPane().add("Center", new JTree(new ListenableTreeModel(new Pair("Root", root))));
		f.pack();
		f.setVisible(true);
	}
	
}
