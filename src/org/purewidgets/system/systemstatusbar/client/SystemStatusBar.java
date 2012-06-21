/**
 * 
 */
package org.purewidgets.system.systemstatusbar.client;

import java.util.ArrayList;
import java.util.Iterator;

import org.purewidgets.client.application.PublicDisplayApplication;
import org.purewidgets.client.application.PublicDisplayApplicationLoadedListener;
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
public class SystemStatusBar implements PublicDisplayApplicationLoadedListener, EntryPoint {

	Timer timer;
	
	int currentApp = 0;
	ArrayList<Application> apps;
	
	long lastApplicationsUpdate = -1;
	
	@Override
	public void onModuleLoad() {
		PublicDisplayApplication.load(this, "SystemStatusBar", true);
		WidgetManager.get().setAutomaticInputRequests(false);
	}

	
	protected void refresh() {
		
		/*
		 * Update the list of apps from the IM
		 */
		if ( System.currentTimeMillis()-this.lastApplicationsUpdate > 30*1000 ) {
			Log.debug(this, "Asking server for list of applications");
			final String placeId = "DefaultPlace";
			PublicDisplayApplication.getServerCommunicator().getApplicationsList(placeId, new AsyncCallback<ArrayList<Application>> () {

				@Override
				public void onSuccess(ArrayList<Application> applicationList) {
					SystemStatusBar.this.onApplicationList(placeId, applicationList);
				}

				@Override
				public void onFailure(Throwable exception) {
					Log.warn(SystemStatusBar.this, "Could not get application list: " + exception.getMessage());
				}
				
			});
			
			//WidgetManager.get().getApplicationsList("DefaultPlace", false);
			//this.lastApplicationsUpdate = System.currentTimeMillis();
		}
		
		
		
		/*
		 * Update the currently displayed features on the status bar
		 */
		Log.debug(this, "Updating features displayed");
		if ( null == this.apps || this.apps.size() == 0 ) {
			return;
		}
		if ( this.currentApp >= this.apps.size() ) {
			this.currentApp = 0;
		}
		Application app = this.apps.get(this.currentApp);
		this.currentApp++;
		
		/*
		 * Clear the panel that shows the features for this app
		 * and recreate it.
		 */
		RootPanel.get("features").clear();
		RootPanel.get("features").add(new Label("Application: " + app.getApplicationId()));
//		for ( Widget widget : app.getWidgets() ) {
//			if ( widget.isVolatileWidget() ) { 
//				continue;
//			}
//			
//			if ( widget.getWidgetOptions().size() == 1 ) {
//				RootPanel.get("features").add(new Label(widget.getShortDescription() + " [" + widget.getWidgetOptions().get(0).getReferenceCode()  + "] : " + widget.getLongDescription() + "<" + widget.getControlType() + ">"));
//			} else if ( widget.getWidgetOptions().size() > 1 ) { 
//				
//				
//			}
//		}
		
	}


	private void onApplicationList(String placeId, ArrayList<Application> applications) {
		Log.debug(this, "Received list of applications: " + applications.size() );
		/*
		 * Remove apps with no widgets
		 */
		Iterator<Application> it = applications.iterator();
		while ( it.hasNext() ) {
			Application ap = it.next();
			
			
//			if ( ap.getWidgets() != null ) {
//				boolean delete = true;
//				for (Widget w : ap.getWidgets() ) {
//					if ( !w.isVolatileWidget() ) {
//						delete = false;
//					}
//				}
//				if ( delete ) {
//					it.remove();
//				}
//			} else {
//				it.remove();
//			}
		}
		
		this.apps = applications;
		
		/*
		for ( Application app : applications ) {
			Log.debug(this, "Application: " + app.getApplicationId() );
			RootPanel.get().add(new Label(app.getApplicationId() + ":" ) );
			
			for ( Widget widget : app.getWidgets() ) {
				RootPanel.get().add(new Label(widget.getWidgetId() + " - " + widget.getShortDescription() + " [" + widget.getLongDescription() + "]"));
			}
		}*/
		
	}

	@Override
	public void onApplicationLoaded() {
		lastApplicationsUpdate = System.currentTimeMillis();
		
		timer = new Timer() {
			@Override
			public void run() {
				refresh();
			}
		};
		
		timer.scheduleRepeating(15*1000);
		//refresh();
		
	}

	
}
