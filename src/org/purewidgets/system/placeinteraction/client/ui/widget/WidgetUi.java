package org.purewidgets.system.placeinteraction.client.ui.widget;

import java.util.ArrayList;
import java.util.HashMap;

import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.im.Application;
import org.purewidgets.shared.im.WidgetParameter;
import org.purewidgets.system.placeinteraction.client.PlaceInteractionWebpage;
import org.purewidgets.system.placeinteraction.client.ui.UiType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class WidgetUi extends Composite  {
	public static final String ICON_NAME = "icon_64.png";
	
	interface Style extends CssResource {
	    String center();
	    String over();
	    String widgetsPanel();
	  }
	
	@UiTemplate("WidgetDesktop.ui.xml")
	interface WidgetDesktopUiBinder extends UiBinder<Widget, WidgetUi> {}
	private static WidgetDesktopUiBinder desktopUiBinder = GWT.create(WidgetDesktopUiBinder.class);
	
	@UiTemplate("WidgetSmartphone.ui.xml")
	interface WidgetSmartphoneUiBinder extends UiBinder<Widget, WidgetUi> {}
	private static WidgetSmartphoneUiBinder smartphoneUiBinder = GWT.create(WidgetSmartphoneUiBinder.class);
		
	
	/*
	 * The ui type we will generate
	 */
	private UiType uiType;
	
	
	@UiField HTMLPanel mainPanel;
	@UiField SpanElement spanApplicationName;
	@UiField Image applicationIcon;
	

	@UiField Label labelNoWidgetFound;
	@UiField HTML goBackPanel;
	@UiField Style style;
	



	private String placeName;
	private String applicationName;
	private String widgetId;
	private String optionId;
	

	/*
	 * Indicates whether we should load the application icon. This is determined according to the
	 * template being used.
	 */
	private boolean loadApplicationIcon;
	



	private boolean iconErrorThrown;
	
	public WidgetUi( UiType uiType, String placeName, String applicationName, String widgetId, String optionId ) {
		this.uiType = uiType;
		initWidget(this.getUiBinder(uiType).createAndBindUi(this));
		
		this.placeName = placeName;
		this.applicationName = applicationName;
		this.widgetId = widgetId;
		this.optionId = optionId;
		
		this.spanApplicationName.setInnerText(this.applicationName);
		
	
		
		this.goBackPanel.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				WidgetUi.this.goBackPanel.addStyleName(WidgetUi.this.style.over());	
			}
		});
		
		this.goBackPanel.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				WidgetUi.this.goBackPanel.removeStyleName(WidgetUi.this.style.over());
			}
		});
		
		this.goBackPanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(WidgetUi.this.placeName+PlaceInteractionWebpage.TOKEN_SEPARATOR+WidgetUi.this.applicationName);
			}
			
		});
		
		if ( this.loadApplicationIcon ) {
			
			PlaceInteractionWebpage.getIM().getApplication(placeName, applicationName, PlaceInteractionWebpage.APP_ID, new AsyncCallback<Application>(){
	
				@Override
				public void onSuccess(Application application) {
					if ( null != application ) {
						WidgetUi.this.applicationIcon.setUrl(application.getApplicationBaseUrl() + ICON_NAME);
					}
				}
	
				@Override
				public void onFailure(Throwable exception) {
				}
				
			}); 
		} else {
			/*
			 * If we don't show the icon, then remove the element from the dom
			 */
			this.applicationIcon.removeFromParent();
		}
		
		this.refreshWidgets();
	}
	
	private UiBinder<Widget, WidgetUi> getUiBinder(UiType uiType) {
		this.loadApplicationIcon = true;
		
		switch ( uiType ) {
		case Desktop:
			return desktopUiBinder;
		case Smartphone:
			this.loadApplicationIcon = false;
			return smartphoneUiBinder;
		default:
			return desktopUiBinder;
		}
	}

	@UiHandler("applicationIcon")
	void handleImageError(ErrorEvent e) {
		/*
		 * The first we get an error, we try to load our own default icon.
		 * We only do this the first time because, in the event that our own icon
		 * is not found we don't want to be caught in a loop
		 */
		if ( !iconErrorThrown ) {
			Log.warn(this, "Could not load icon, using default icon.");
			applicationIcon.setUrl( GWT.getHostPageBaseURL() + ICON_NAME );
		}
   	  	iconErrorThrown = true;
	 }
	
	

	
	protected void refreshWidgets() {
		final String placeName = this.placeName;
		final String applicationName = this.applicationName;
		PlaceInteractionWebpage.getIM().getWidgetsList(this.placeName, this.applicationName, PlaceInteractionWebpage.APP_ID,
				new AsyncCallback<ArrayList<org.purewidgets.shared.im.Widget>>() {

					@Override
					public void onSuccess(
							ArrayList<org.purewidgets.shared.im.Widget> widgetList) {
						WidgetUi.this.onWidgetsList(placeName, applicationName, widgetList);
						
					}

					@Override
					public void onFailure(Throwable exception) {
						Log.warn(WidgetUi.this, "Could not get list of widgets from server: " + exception.getMessage());
						
					}
			
		});
	}
	
	
	private void onWidgetsList(String placeId, String applicationId,
			ArrayList<org.purewidgets.shared.im.Widget> widgetList) {
		Log.debug(this, "Received widget list" + widgetList.toString());

		/*
		 * Sort the widgets by id
		 */
		//Collections.sort(widgetList);
		
		if ( null == widgetList || widgetList.size() == 0 ) {
			
			//this.mainPanel.clear();
			this.labelNoWidgetFound.setVisible(true);
			this.labelNoWidgetFound.setText("Not found");
			return;
			
		} else {
			
			this.labelNoWidgetFound.setVisible(false);
			//this.mainPanel.add(this.tabPanel);
		}
			
		if (null != widgetList) {	

			/*
			 * Find and add the  widget.
			 */
			for (org.purewidgets.shared.im.Widget widget : widgetList) {
				
				if ( widget.getWidgetId().equals( this.widgetId ) ) {
					this.mainPanel.add(WidgetUi.getHtmlWidget(this.uiType, widget, this.optionId));
		
				}
			}
			
		}

	}
	

	public static Widget getHtmlWidget(UiType uiType, org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		Widget toReturn = null;
		Log.error(WidgetUi.class.getName(), publicDisplayWidget.getControlType());
		if (publicDisplayWidget.getControlType().equals(
				org.purewidgets.shared.im.Widget.CONTROL_TYPE_ENTRY)) {
			toReturn = getEntryWidget(uiType, publicDisplayWidget, optionId);
		} else if (publicDisplayWidget
				.getControlType()
				.equals(org.purewidgets.shared.im.Widget.CONTROL_TYPE_IMPERATIVE_SELECTION)) {
			toReturn = getImperativeWidget(uiType, publicDisplayWidget, optionId);
		} else if (publicDisplayWidget.getControlType().equals(
				org.purewidgets.shared.im.Widget.CONTROL_TYPE_DOWNLOAD)) {
			toReturn =  getDownloadWidget(uiType, publicDisplayWidget, optionId);
		} else if ( publicDisplayWidget.getControlType().equals(
				org.purewidgets.shared.im.Widget.CONTROL_TYPE_UPLOAD) ) {
			toReturn =  getUploadWidget(uiType, publicDisplayWidget, optionId);
		} else if ( publicDisplayWidget.getControlType().equals(
				org.purewidgets.shared.im.Widget.CONTROL_TYPE_PRESENCE) ) {
			toReturn =  getPresenceWidget(uiType, publicDisplayWidget, optionId);
		}
		if ( null != toReturn ) {
			//toReturn.setStyleName("widget");
			String id = publicDisplayWidget.getWidgetId();
			if ( null != publicDisplayWidget.getWidgetParameters() && publicDisplayWidget.getWidgetParameters().size() > 0 ) {
				for ( WidgetParameter wp : publicDisplayWidget.getWidgetParameters() ) {
					if ( wp.getName().equals(	org.purewidgets.shared.im.Widget.SORT_ORDER_PARAMETER_NAME ) ) {
						id = wp.getValue();
					}
				}
			}
			toReturn.getElement().setPropertyString("id", id);
		}
		return toReturn;
	}

	static Widget getPresenceWidget(UiType uiType, org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		return new PresenceWidgetUi(uiType, publicDisplayWidget);
	}
	
	static Widget getUploadWidget(UiType uiType, org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		return new UploadWidgetUi(uiType, publicDisplayWidget);
	}
	
	static Widget getEntryWidget(UiType uiType, org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		return new EntryWidgetUi(uiType, publicDisplayWidget);
	}

	static Widget getImperativeWidget(UiType uiType, 
			org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		ArrayList<WidgetOption> widgetOptions = publicDisplayWidget.getWidgetOptions();
		
		if (null != widgetOptions) {
			if (widgetOptions.size() == 1) {
				return getSingleOptionImperativeWidget(uiType, publicDisplayWidget, optionId);
			} else {
				return getMultipleOptionImperativeWidget(uiType, publicDisplayWidget, optionId);
			}
		} else {
			return null;
		}
	}

	
	static Widget getMultipleOptionImperativeWidget(UiType uiType, 
			org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		return new ImperativeMultipleOptionWidgetUi(uiType, publicDisplayWidget, optionId);

	}

	static Widget getSingleOptionImperativeWidget(UiType uiType, 
			org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		return new ImperativeSingleOptionWidgetUi(uiType, publicDisplayWidget);

	}

	static Widget getDownloadWidget(UiType uiType, org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		return new DownloadWidgetUi(uiType, publicDisplayWidget);

	}

	/**
	 * @return the placeName
	 */
	public String getPlaceName() {
		return placeName;
	}

	
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	
	
}
