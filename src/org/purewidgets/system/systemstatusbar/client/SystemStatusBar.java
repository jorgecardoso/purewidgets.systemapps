/**
 * 
 */
package org.purewidgets.system.systemstatusbar.client;

import java.util.ArrayList;
import java.util.Iterator;

import org.purewidgets.client.im.WidgetManager;
import org.purewidgets.shared.logging.Log;

import org.purewidgets.shared.im.Application;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author "Jorge C. S. Cardoso"
 */
public class SystemStatusBar implements EntryPoint {

	Timer timer;
	
	int currentApp = 0;
	ArrayList<Application> apps;
	
	long lastApplicationsUpdate = -1;
	
	@Override
	public void onModuleLoad() {
		
	}

	

	
}
