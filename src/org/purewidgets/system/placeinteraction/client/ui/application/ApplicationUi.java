package org.purewidgets.system.placeinteraction.client.ui.application;

import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.im.Application;
import org.purewidgets.system.placeinteraction.client.ui.UiType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationUi extends Composite implements HasClickHandlers {

	public static final String ICON_NAME = "icon.svg";
	
	/*interface Style extends CssResource {
	    String onScreen();
	    String offScreen();
	    
	 }	*/
	
	@UiTemplate("ApplicationUiDesktop.ui.xml")
	interface ApplicationUiDesktopUiBinder extends UiBinder<Widget, ApplicationUi> {	}
	private static ApplicationUiDesktopUiBinder desktopUiBinder = GWT.create(ApplicationUiDesktopUiBinder.class);
	
	@UiTemplate("ApplicationUiSmartphone.ui.xml")
	interface ApplicationUiSmartphoneUiBinder extends UiBinder<Widget, ApplicationUi> {	}
	private static ApplicationUiSmartphoneUiBinder smartphoneUiBinder = GWT.create(ApplicationUiSmartphoneUiBinder.class);
	
	
	//@UiField Style style;
	
	@UiField Image icon;
	@UiField Label name;
	
	@UiField
	Image onscreenIcon;
	
	@UiField
	Panel mainPanel;
	
	private UiType uiType;

	

	private Application application;


	private boolean iconErrorThrown;

	

	public ApplicationUi( UiType uiType, Application application) {
		initWidget(this.getUiBinder(uiType).createAndBindUi(this));
		this.uiType = uiType;
		
		this.setApplication(application);
		
	}
	
	private UiBinder<Widget, ApplicationUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		
		case Desktop:
			return desktopUiBinder;
			
		case Smartphone:
			return smartphoneUiBinder;
			
		default:
			return desktopUiBinder;
		}
	}

	

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	/**
	 * @return the application
	 */
	public Application getApplication() {
		return application;
	}

	/**
	 * @param application the application to set
	 */
	public void setApplication(Application application) {
		this.application = application;
		this.name.setText(this.application.getApplicationName());
		String url = this.application.getApplicationBaseUrl();
	

		this.icon.setUrl(url + ICON_NAME);
		
		
		if ( application.isOnScreen() ) {
			this.onscreenIcon.setUrl("/placeinteraction/onscreenicon.png");
		} else {
			this.onscreenIcon.removeFromParent();
		}
	}
	
	@UiHandler("icon")
	void handleImageError(ErrorEvent e) {
		
		/*
		 * The first we get an error, we try to load our own default icon.
		 * We only do this the first time because, in the event that our own icon
		 * is not found we don't want to be caught in a loop
		 */
		if ( !iconErrorThrown ) {
			Log.warn(this, "Could not load icon, using default icon.");
   	  		icon.setUrl( GWT.getHostPageBaseURL() + ICON_NAME );
		}
   	  	iconErrorThrown = true;
	 }

}
