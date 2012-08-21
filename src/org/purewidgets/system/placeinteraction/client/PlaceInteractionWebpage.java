/**
 * 
 */
package org.purewidgets.system.placeinteraction.client;


import org.purewidgets.client.application.PDApplication;
import org.purewidgets.client.application.PDApplicationLifeCycle;
import org.purewidgets.client.im.InteractionManagerService;
import org.purewidgets.client.im.WidgetManager;
import org.purewidgets.client.storage.LocalStorage;
import org.purewidgets.shared.logging.Log;
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
public class PlaceInteractionWebpage implements EntryPoint {
	private static final boolean PRODUCTION = false;
	
	private static final String PRODUCTION_IM = "http://pw-interactionmanager.appspot.com";
	private static final String TEST_IM = "http://pw-interactionmanager-test.appspot.com";
	
	public static final String TOKEN_SEPARATOR = "%-%";

	public static String APP_ID = "PlaceInteractionWebpage";
	
	private MainScreenUi mainScreen;
	private UiType uiType;
	private static InteractionManagerService interactionManager;
	
	@Override
	public void onModuleLoad() {
		if ( Window.Navigator.getUserAgent().toLowerCase().contains("iphone") && !Window.Location.getPath().contains("mobile.html")) {
			
			Window.open("mobile.html?"+Window.Location.getQueryString()+Window.Location.getHash(), "_self", "");
			
		} else if ( Window.Navigator.getUserAgent().toLowerCase().contains("android") 
				&& Window.Navigator.getUserAgent().toLowerCase().contains("mobile") 
				&& !Window.Location.getPath().contains("android.html") ) {
			
			Window.open("android.html?"+Window.Location.getQueryString()+Window.Location.getHash(), "_self", ""); 
		}
		
		
		if ( Window.Location.getPath().contains("index.html") ) {
			this.uiType = UiType.Desktop;
		} else {
			this.uiType = UiType.Smartphone;
		}
		
		String interactionManagerServer = com.google.gwt.user.client.Window.Location.getParameter("imurl");
		if ( null == interactionManagerServer ) {
			if ( PRODUCTION ) {
				interactionManagerServer = PRODUCTION_IM;
			} else {
				interactionManagerServer = TEST_IM;
			}
		}
		
		interactionManager = new InteractionManagerService(interactionManagerServer, 
				new LocalStorage(APP_ID));
	
		this.mainScreen = new MainScreenUi(this.uiType);
		RootPanel.get().add(this.mainScreen);
		
		
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();
				show(historyToken);
			}
		});
		
		this.show(History.getToken());
		
		this.loadJanRain();
	}
	
	public static InteractionManagerService getIM() {
		return interactionManager;
	}
	// A Java method using JSNI
	
	native void loadJanRain() /*-{
	  $wnd.loadJanRain(); 
	}-*/;
	
	native void setTokenUrl() /*-{
	  $wnd.setTokenUrl(); 
	}-*/;
	

	/**
	 * Show a different screen based on the history token
	 * @param historyToken
	 */
	protected void show(String historyToken) {
		if (null == historyToken || historyToken.length() == 0) {
			this.mainScreen.showPlaceList();
		} else {
			
			String []tokens = historyToken.split(TOKEN_SEPARATOR);
			
			for ( String s : tokens ) {
				Log.debug(this, s);
			}
			
			switch ( tokens.length ) {
			case 0:  
				this.mainScreen.showPlaceList();
				break;
			case 1: // only place specified
				this.mainScreen.showApplicationList( tokens[0] );
				break;
			case 2: //place and app
				this.mainScreen.showWidgets(tokens[0], tokens[1]);
				break;
			case 3: // place, app, and widget
				this.mainScreen.showWidget(tokens[0], tokens[1], tokens[2], null);
				break;
			case 4: // place, app, widget, and option
				this.mainScreen.showWidget(tokens[0], tokens[1], tokens[2], tokens[3]);
				break;				
			}
			
		}
		
		this.setTokenUrl();
	}
}
