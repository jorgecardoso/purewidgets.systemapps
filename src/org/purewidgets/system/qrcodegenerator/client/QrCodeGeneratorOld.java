/**
 * 
 */
package org.purewidgets.system.qrcodegenerator.client;

import java.util.ArrayList;

import org.purewidgets.client.application.PublicDisplayApplication;
import org.purewidgets.client.application.PublicDisplayApplicationLoadedListener;
import org.purewidgets.shared.Log;
import org.purewidgets.shared.widgetmanager.Callback;
import org.purewidgets.shared.widgetmanager.WidgetManager;
import org.purewidgets.shared.widgetmanager.WidgetOption;
import org.purewidgets.shared.widgets.Application;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class QrCodeGeneratorOld implements PublicDisplayApplicationLoadedListener, EntryPoint {
	
	
	private TabPanel tabPanelApplications;
	
	private ListBox qrSizes;
	
	@Override
	public void onModuleLoad() {
//		sightingService = GWT.create(SightingService.class);
//		((ServiceDefTarget) sightingService).setServiceEntryPoint("/sighting");
		
		PublicDisplayApplication.load(this, "QRCodeGenerator", false);
		WidgetManager.get().setAutomaticInputRequests(false);
		
	}
	
	
	protected void refreshApplications() {
		Log.debug(this, "Asking server for list of applications");
		final String placeId = "DefaultPlace";
		PublicDisplayApplication.getServerCommunicator().getApplicationsList(placeId, new Callback<ArrayList<Application>> () {

			@Override
			public void onSuccess(ArrayList<Application> applicationList) {
				QrCodeGeneratorOld.this.onApplicationList(placeId, applicationList);
			}

			@Override
			public void onFailure(Throwable exception) {
				Log.warn(QrCodeGeneratorOld.this, "Could not get application list: " + exception.getMessage());
			}
			
		});
		
		//Log.debug(this, "Asking server for list of applications");
		//WidgetManager.get().getApplicationsList("DefaultPlace");	
	}

	protected void refreshWidgets() {
		if ( null != this.tabPanelApplications ) {
			int selected = tabPanelApplications.getTabBar().getSelectedTab();
			Log.debug(this, "Selected Tab: " + selected);
			final String currentApplicationId = tabPanelApplications.getTabBar().getTabHTML(selected);
			Log.debug(this, "Asking server for list of widgets for application: " + currentApplicationId );
			
			
			PublicDisplayApplication.getServerCommunicator().getWidgetsList("DefaultPlace", currentApplicationId,
					new Callback<ArrayList<org.purewidgets.shared.widgets.Widget>>() {

						@Override
						public void onSuccess(
								ArrayList<org.purewidgets.shared.widgets.Widget> widgetList) {
							QrCodeGeneratorOld.this.onWidgetsList("DefaultPlace", currentApplicationId, widgetList);	
						}

						@Override
						public void onFailure(Throwable exception) {
							Log.warn(QrCodeGeneratorOld.this, "Could not list of widgets from server: " + exception.getMessage());
						}
				
			});

		}

		
	}
	

	private void onWidgetsList(
			String placeId, String applicationId, ArrayList<org.purewidgets.shared.widgets.Widget> widgetList) {
		Log.debug(this, "Received widget list" + widgetList.toString());
		
		if ( null != widgetList && widgetList.size() > 0 ) {
			
			ArrayList<String> widgetIds = new ArrayList<String>();
			for (org.purewidgets.shared.widgets.Widget widget : widgetList) {
				widgetIds.add(widget.getWidgetId());
			}
			//String applicationId = widgetList.get(0).getApplicationId();
			Log.debug(this, "Received widgets for application: " + applicationId);
			
			/* 
			 * Get the tab that holds this application
			 */
			VerticalPanel panel = null;
			for (int i = 0; i < tabPanelApplications.getWidgetCount(); i++ ) {
				String tabName = tabPanelApplications.getTabBar().getTabHTML(i);
				if ( tabName.equals(applicationId) ) {
					panel = (VerticalPanel) tabPanelApplications.getWidget(i);
					break;
				}
			}
			
			if ( null != panel) {
				Log.debug(this, panel.toString());
				
				/*
				 * Delete widgets that no longer exist
				 */
				int i = 0;
				while ( i < panel.getWidgetCount() ) {
					String widgetName = panel.getWidget(0).getTitle();
					
					if ( !widgetIds.contains(widgetName) ) {
						panel.remove(i);
					} else {
						// only increment if we haven't deleted anything because if we did, indexed may have changed
						i++;
					}
				}
				
				/*
				 * Add the new widgets
				 */
				
				for ( org.purewidgets.shared.widgets.Widget widget : widgetList ) {
					boolean exists = false;
					for (i = 0; i < panel.getWidgetCount(); i++ ) {
						String existingWidgetName = panel.getWidget(i).getTitle();
						if ( existingWidgetName.equals(widget.getWidgetId()) ) {
							exists = true;
						}
					}
					
					if ( !exists ) {
						VerticalPanel v = new VerticalPanel();
					
						v.add(new Label("Widget id: " + widget.getWidgetId()) );
						v.add(new Label("Short description:" + widget.getShortDescription()) );
						
						for ( WidgetOption option : widget.getWidgetOptions() ) {
							Log.debug(this,"Adding " + widget.getWidgetId() + " to panel");
							//panel.add(this.getHtmlWidget(widget));
							String data = "http://pw.jorgecardoso.org/qrcodeinteractor/index.html?place=" + widget.getPlaceId() + 
									"&app="+widget.getApplicationId() + "&widget=" + widget.getWidgetId()
									+"&type="+widget.getControlType() + "&opid=" + option.getWidgetOptionId();
							
							String unencodedUrl = data;
							
							data = com.google.gwt.http.client.URL.encode(data);
							data = data.replaceAll(";", "%2F");
							data = data.replaceAll("/", "%2F");
							data = data.replaceAll(":", "%3A");
							data = data.replaceAll("\\?", "%3F");
							data = data.replaceAll("&", "%26");
						    data = data.replaceAll("\\=", "%3D");
						    data = data.replaceAll("\\+", "%2B");
						    data = data.replaceAll("\\$", "%24");
						    data = data.replaceAll(",", "%2C");
						    data = data.replaceAll("#", "%23");
							  
						    
							String url = "https://chart.googleapis.com/chart?cht=qr&chs="+qrSizes.getValue(qrSizes.getSelectedIndex())+"&chl="+data;
							Image im = new Image(url);
						
							v.add(new Label("Reference code: " + option.getReferenceCode()));
							v.add(new Label("Encoded url: " +data));
							v.add(new Label("Unencoded url: " + unencodedUrl));
							v.add(im);
						}
						panel.add(v);
						
					}
				}
			}
		}
	}
	
	
	
	private void onApplicationList(String placeId, ArrayList<Application> applicationList) {
		Log.debug(this, "Received applications: " + applicationList);
		
		/*
		 * Create a temporary list just with the app names
		 */
		ArrayList<String> applicationIds = new ArrayList<String>();
		for ( Application app: applicationList ) {
			applicationIds.add( app.getApplicationId() );
		}

		
		
		
		/*
		 * Delete applications that no longer exist  
		 */
		int i = 0;
		while ( i < tabPanelApplications.getWidgetCount() ) {
			String tabName = tabPanelApplications.getTabBar().getTabHTML(i);
			if ( !applicationIds.contains(tabName) ) {
				tabPanelApplications.remove(i);
			} else {
				// only increment if we haven't deleted anything because if we did, indexed may have changed
				i++;
			}
		}
	
		/*
		 * Add new applications
		 */
		for ( String appName : applicationIds ) {
			boolean exists = false;
			for (i = 0; i < tabPanelApplications.getWidgetCount(); i++ ) {
				String tabName = tabPanelApplications.getTabBar().getTabHTML(i);
				if ( tabName.equals(appName) ) {
					exists = true;
				}
			}
			
			if ( !exists ) {
				VerticalPanel p = new VerticalPanel();
				
				tabPanelApplications.add( p, appName );
			}
		}
		
		/*
		 * Set the selected tab
		 */
		int selected = tabPanelApplications.getTabBar().getSelectedTab();
		if ( selected < 0 ) {
			this.tabPanelApplications.getTabBar().selectTab(0);
		}
		refreshWidgets();
	}


	@Override
	public void onApplicationLoaded() {

		
		
		qrSizes = new ListBox();
		for (int i = 50; i < 550; i+= 50) {
			qrSizes.addItem(i+"x"+i);
		}
		qrSizes.setSelectedIndex(3);
		
		RootPanel.get("qrsize").add(new Label("QR Code size: "));
		RootPanel.get("qrsize").add(qrSizes);
		
		
		
		tabPanelApplications = new TabPanel();
		tabPanelApplications.getTabBar().addHandler(new ClickHandler() {
	        @Override
	        public void onClick(ClickEvent event) {
	        	Log.debug(this, "Clicked");
	        	refreshWidgets();
	            
	        }
	    }, ClickEvent.getType());
		RootPanel.get("features").add(tabPanelApplications);
		
		
		this.refreshApplications();
		
	}
	
	




}
