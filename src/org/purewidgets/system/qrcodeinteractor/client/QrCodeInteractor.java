/**
 * 
 */
package org.purewidgets.system.qrcodeinteractor.client;

import java.util.ArrayList;
import java.util.Date;

import org.purewidgets.client.im.InteractionManager;
import org.purewidgets.client.im.WidgetManager;
import org.purewidgets.client.storage.LocalStorage;
import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.im.WidgetInput;

import org.purewidgets.system.qrcodeinteractor.client.ui.UiType;
import org.purewidgets.system.qrcodeinteractor.client.ui.main.MainScreenUi;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;


/**
 * @author "Jorge C. S. Cardoso"
 * 
 */
public class QrCodeInteractor implements EntryPoint {

	String placeId;
	String applicationId;
	String widgetId;
	String controlType;
	String optionId;

	private MainScreenUi mainScreen;
	private UiType uiType;
	
	
	
	// A Java method using JSNI
	
		native void loadJanRain() /*-{
		  $wnd.loadJanRain(); // $wnd is a JSNI synonym for 'window'
		}-*/;
		
		native void setTokenUrl() /*-{
		  $wnd.setTokenUrl(); // $wnd is a JSNI synonym for 'window'
		}-*/;
	
	@Override
	public void onModuleLoad() {
		
		this.uiType = UiType.Smartphone;
		this.mainScreen = new MainScreenUi(this.uiType);
		RootPanel.get().add(this.mainScreen);
		this.loadJanRain();
		


		Util.setIM(new InteractionManager("http://pw-interactionmanager.appspot.com", 
				new LocalStorage(Util.APP_ID)) );
	
		placeId = com.google.gwt.user.client.Window.Location.getParameter("place");
		applicationId = com.google.gwt.user.client.Window.Location.getParameter("app");
		widgetId = com.google.gwt.user.client.Window.Location.getParameter("widget");
		controlType = com.google.gwt.user.client.Window.Location.getParameter("type");
		optionId = com.google.gwt.user.client.Window.Location.getParameter("opid");
		
		
		this.mainScreen.showWidgets(placeId, applicationId, widgetId, optionId);
		
	/*	if (controlType
				.equals(org.purewidgets.shared.widgets.Widget.CONTROL_TYPE_IMPERATIVE_SELECTION)) {
			Log.info(this, "Processing imperative widget");
			doImperative();
		} else if (controlType
				.equals(org.purewidgets.shared.widgets.Widget.CONTROL_TYPE_ENTRY)) {
			Log.info(this, "Processing entry widget");
			doEntry();
		} else {
			Log.info(this, "Could not understand widget type");
			
		}
		*/
	}

	

	
}
