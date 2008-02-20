package com.ochafik.util.listenable;

import javax.swing.SwingUtilities;

public class SwingCollectionListener<T> implements CollectionListener<T> {
	final CollectionListener<T> listener;
	
	public SwingCollectionListener(CollectionListener<T> listener) {
		this.listener = listener;
	}

	public void collectionChanged(final CollectionEvent<T> e) {
		Runnable r = new Runnable() {
			public void run() {
				listener.collectionChanged(e);
			};
		};
		if (SwingUtilities.isEventDispatchThread()) {
			r.run();
		} else {
			SwingUtilities.invokeLater(r);
		}
	}	
}
