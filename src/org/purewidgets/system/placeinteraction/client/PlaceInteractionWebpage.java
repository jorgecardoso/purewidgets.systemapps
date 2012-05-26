/**
 * 
 */
package org.purewidgets.system.placeinteraction.client;

import org.purewidgets.client.application.PublicDisplayApplication;
import org.purewidgets.client.application.PublicDisplayApplicationLoadedListener;
import org.purewidgets.shared.widgetmanager.WidgetManager;
import org.purewidgets.system.placeinteraction.client.ui.UiType;
import org.purewidgets.system.placeinteraction.client.ui.main.MainScreenUi;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author "Jorge C. S. Cardoso"
 * 
 */
public class PlaceInteractionWebpage implements EntryPoint, PublicDisplayApplicationLoadedListener {

	private MainScreenUi mainScreen;
	private UiType uiType;

	@Override
	public void onModuleLoad() {
		if ( Window.Navigator.getUserAgent().toLowerCase().contains("iphone") && !Window.Location.getPath().contains("mobile.html")) {
			
			Window.open("mobile.html?"+Window.Location.getQueryString()+Window.Location.getHash(), "_self", "");
			
		} else if ( Window.Navigator.getUserAgent().toLowerCase().contains("android") 
				&& Window.Navigator.getUserAgent().toLowerCase().contains("mobile") 
				&& !Window.Location.getPath().contains("android.html") ) {
			
			Window.open("android.html?"+Window.Location.getQueryString()+Window.Location.getHash(), "_self", ""); 
		}
		PublicDisplayApplication.load(this, "PlaceInteractionWebpage", false);
		
		
		
		if ( Window.Location.getPath().contains("index.html") ) {
			this.uiType = UiType.Desktop;
		} else {
			this.uiType = UiType.Smartphone;
		}
		
		
		
		this.mainScreen = new MainScreenUi(this.uiType);
		RootPanel.get().add(this.mainScreen);
		this.loadJanRain();
	}
	
	// A Java method using JSNI
	
	native void loadJanRain() /*-{
	  $wnd.loadJanRain(); // $wnd is a JSNI synonym for 'window'
	}-*/;
	
	native void setTokenUrl() /*-{
	  $wnd.setTokenUrl(); // $wnd is a JSNI synonym for 'window'
	}-*/;
	
	@Override
	public void onApplicationLoaded() {
		WidgetManager.get().setAutomaticInputRequests(false);
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();
				show(historyToken);
			}
		});
		
		this.show(History.getToken());
	}
	

	/**
	 * Show a different screen based on the history token
	 * @param historyToken
	 */
	protected void show(String historyToken) {
		if (null == historyToken || historyToken.length() == 0) {
			this.mainScreen.showPlaceList();
		} else {
			int indexOfDash = historyToken.indexOf("-");
		    if (  indexOfDash < 0 ) { // no dash: this is a place name
				this.mainScreen.showApplicationList( historyToken );
			} else  {
				String placeName = historyToken.substring(0, indexOfDash);
				String applicationName = historyToken.substring(indexOfDash+1);
				this.mainScreen.showWidgets(placeName, applicationName);
			}
		}
		
		this.setTokenUrl();
	}
}
