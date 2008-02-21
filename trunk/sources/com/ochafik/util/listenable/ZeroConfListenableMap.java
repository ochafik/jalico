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
/**
 * 
 */
package com.ochafik.util.listenable;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class ZeroConfListenableMap extends DefaultListenableMap<String, ServiceInfo> implements ServiceListener {
	String typeString;
	
	public ZeroConfListenableMap(final String typeString) {
		super(new HashMap<String, ServiceInfo>());
		this.typeString = typeString;
		getJmDNS().addServiceListener(typeString, this);
		new Thread() { public void run() {
			getJmDNS().requestServiceInfo(typeString, null);
		}}.start();
	}
	
	public synchronized void unregister() {
		getJmDNS().removeServiceListener(typeString, this);
	}
	
	public void serviceAdded(final ServiceEvent event) {
		new Thread() { public void run() {
			getJmDNS().requestServiceInfo(typeString, event.getName());
		}}.start();
	}
	
	public synchronized void serviceRemoved(final ServiceEvent event) {
		remove(event.getName());
	}
	
	public synchronized void serviceResolved(ServiceEvent event) {
		put(event.getName(), event.getInfo());
	}
	
	static JmDNS jmDNS;
	public static JmDNS getJmDNS() {
		if (jmDNS == null) {
			try {
				jmDNS = new JmDNS();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return jmDNS;
	}
	
	static ListenableMap<String, ServiceInfo> advertisedServices;
	public static ListenableMap<String, ServiceInfo> getAdvertisedServiceInfos() {
		if (advertisedServices == null) {
			advertisedServices = new DefaultListenableMap<String, ServiceInfo>(new HashMap<String, ServiceInfo>());
			advertisedServices.keySet().addCollectionListener(new CollectionListener<String>() {
				Map<String, ServiceInfo> oldValues = new HashMap<String, ServiceInfo>();
				public void collectionChanged(CollectionEvent<String> e) {
					for (String name : e.getElements()) {
						try {
							switch (e.getType()) {
							case UPDATED:
								getJmDNS().unregisterService(oldValues.remove(name));
								
							case ADDED:
								ServiceInfo info = advertisedServices.get(name);
								getJmDNS().registerService(info);
								oldValues.put(name, info);
								break;
							case REMOVED:
								getJmDNS().unregisterService(oldValues.remove(name));
								break;
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			Runtime.getRuntime().addShutdownHook(new Thread() { public void run() {
				advertisedServices.clear();
			}});
		}
		return advertisedServices;
	}
	
	public static void main(String[] args) {
		String key = "_zOlive._tcp.local.";
		ZeroConfListenableMap map = new ZeroConfListenableMap(key);
		JFrame f = new JFrame(key);
		f.getContentPane().add("Center", new JScrollPane(new JList(new ListenableListModel<String>(ListenableCollections.asList(map.keySet())))));
		f.setSize(200, 600);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				e.getWindow().setVisible(false);
				if (jmDNS != null) 
					jmDNS.close();
				System.exit(0);
			}
		});
		f.setVisible(true);
		
		Random random = new Random();
		for (int i = 5; i-- != 0;) {
			String name = "Agent " + random.nextInt();
			getAdvertisedServiceInfos().put(name, new ServiceInfo(key, name, 11, "fuck !"));
		}
	}
}