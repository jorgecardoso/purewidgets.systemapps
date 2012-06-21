package org.purewidgets.system.qrcodeinteractor.client.ui.main;

import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.im.Application;
import org.purewidgets.system.qrcodeinteractor.client.ui.UiType;
import org.purewidgets.system.qrcodeinteractor.client.ui.login.LoginUi;
import org.purewidgets.system.qrcodeinteractor.client.ui.widget.WidgetListUi;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainScreenUi extends Composite    {

	@UiTemplate("MainScreenUiSmartphone.ui.xml")
	interface MainScreenUiSmartphoneUiBinder extends UiBinder<Widget, MainScreenUi> {	}
	private static MainScreenUiSmartphoneUiBinder smartPhoneUiBinder = GWT.create(MainScreenUiSmartphoneUiBinder.class);
	
	
	private UiType uiType;

	@UiField HTMLPanel features;
	
	@UiField LoginUi loginUi;
	

	
	private WidgetListUi widgetListUi;
	
	public MainScreenUi( UiType uiType ) {
		this.uiType = uiType;
		initWidget(this.getUiBinder(uiType).createAndBindUi(this));
	}
	@UiFactory LoginUi makeLoginUi() { // method name is insignificant
	    return new LoginUi(this.uiType);
	}
	
	private UiBinder<Widget, MainScreenUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		

		default:
			return smartPhoneUiBinder;
		}
	}


	/**
	 * Show the widget list for a given application
	 */
	public void showWidgets( String placeName, String applicationName, String widgetName, String optionName) {
		Log.debug(this, "Showing widgets");
		
		if ( null == this.widgetListUi || !this.widgetListUi.getPlaceName().equals(placeName) || !this.widgetListUi.getApplicationName().equals(applicationName) ) {
			this.widgetListUi = new WidgetListUi(this.uiType, placeName, applicationName, widgetName, optionName); 
		}
		//RootPanel.get("features").clear();
		//RootPanel.get("features").add(this.widgetListUi);
		this.widgetListUi.start();
		this.features.clear();
		this.features.add(this.widgetListUi);
	}
	

}
