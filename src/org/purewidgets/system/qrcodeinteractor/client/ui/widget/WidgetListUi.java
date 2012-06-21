package org.purewidgets.system.qrcodeinteractor.client.ui.widget;

import java.util.ArrayList;
import java.util.HashMap;

import org.purewidgets.client.application.PublicDisplayApplication;
import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.im.Application;
import org.purewidgets.system.qrcodeinteractor.client.ui.UiType;

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

public class WidgetListUi extends Composite  {
	public static final String ICON_NAME = "icon_64.png";
	
	interface Style extends CssResource {
	    String center();
	    String over();
	    String widgetsPanel();
	  }
	
	
	@UiTemplate("WidgetListSmartphone.ui.xml")
	interface WidgetListSmartphoneUiBinder extends UiBinder<Widget, WidgetListUi> {}
	private static WidgetListSmartphoneUiBinder smartphoneUiBinder = GWT.create(WidgetListSmartphoneUiBinder.class);
		
	
	/*
	 * The ui type we will generate
	 */
	private UiType uiType;
	
	
	@UiField HTMLPanel mainPanel;
	@UiField SpanElement spanApplicationName;
	@UiField Image applicationIcon;
	
	/** 
	 * The tabpanel that holds one tab for each different widget short description
	 */
	@UiField TabPanel tabPanel;
	@UiField StackPanel stackPanel;
	@UiField Label labelNoWidgetFound;
	@UiField Style style;
	

	/*
	 * The timer to trigger requests to get the list of widgets of the application.
	 */
	private Timer timerWidgets;

	private String placeName;
	private String applicationName;
	private String widgetName;
	private String optionName;

	/*
	 * Indicates whether we should load the application icon. This is determined according to the
	 * template being used.
	 */
	private boolean loadApplicationIcon;
	
	private HashMap<String, FlowPanel> panelsMap;


	private boolean iconErrorThrown;
	
	public WidgetListUi( UiType uiType, String placeName, String applicationName, String widgetName, String optionName ) {
		this.uiType = uiType;
		initWidget(this.getUiBinder(uiType).createAndBindUi(this));
		
		this.placeName = placeName;
		this.applicationName = applicationName;
		this.widgetName = widgetName;
		this.optionName = optionName;
		
		this.spanApplicationName.setInnerText(this.applicationName);
		
		panelsMap = new HashMap<String, FlowPanel>();
		
		
		
		if ( this.loadApplicationIcon ) {
			
			PublicDisplayApplication.getServerCommunicator().getApplication(placeName, applicationName, new AsyncCallback<Application>(){
	
				@Override
				public void onSuccess(Application application) {
					if ( null != application ) {
						WidgetListUi.this.applicationIcon.setUrl(application.getApplicationBaseUrl() + ICON_NAME);
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
		
		/*
		 * Remove the panel that is not used by this template
		 */
		switch (this.uiType) {
		case Desktop:
			this.stackPanel.removeFromParent();
			this.stackPanel = null;
			break;
		case Smartphone:
			this.tabPanel.removeFromParent();
			this.tabPanel = null;
			break;
		}
	}
	
	private UiBinder<Widget, WidgetListUi> getUiBinder(UiType uiType) {
		this.loadApplicationIcon = true;
		
		switch ( uiType ) {

		default:
			this.loadApplicationIcon = false;
			return smartphoneUiBinder;
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
	
	public void start() {

//		timerWidgets = new Timer() {
//			@Override
//			public void run() {
//				refreshWidgets();
//			}
//		};
//		timerWidgets.scheduleRepeating(15 * 1000);
		this.refreshWidgets();
	}
	
	public void stop() {
		if ( null != this.timerWidgets ) {
			this.timerWidgets.cancel();
			this.timerWidgets = null;
		}
	}

	
	protected void refreshWidgets() {
		final String placeName = this.placeName;
		final String applicationName = this.applicationName;
		final String widgetName = this.widgetName;
		final String optionName = this.optionName;
		PublicDisplayApplication.getServerCommunicator().getWidgetsList(this.placeName, this.applicationName, 
				new AsyncCallback<ArrayList<org.purewidgets.shared.im.Widget>>() {

					@Override
					public void onSuccess(
							ArrayList<org.purewidgets.shared.im.Widget> widgetList) {
						WidgetListUi.this.onWidgetsList(placeName, applicationName, widgetName, optionName, widgetList);
						
					}

					@Override
					public void onFailure(Throwable exception) {
						Log.warn(WidgetListUi.this, "Could not get list of widgets from server: " + exception.getMessage());
						
					}
			
		});
	}
	
	private void setPanelVisible(boolean visible) {
		if ( null != this.tabPanel ) {
			this.tabPanel.setVisible(visible);
		}
		if ( null != this.stackPanel ) {
			this.stackPanel.setVisible(visible);
		}
	}
	
	private void addTab(Widget w, String tabName) {
		if ( null != this.tabPanel ) {
			this.tabPanel.add(w, tabName);
		}
		if ( null != this.stackPanel ) {
			this.stackPanel.add(w, tabName);
		}
	}
	
	private int getSelectedTab() {
		if ( null != this.tabPanel ) {
			return this.tabPanel.getTabBar().getSelectedTab();
		}
		if ( null != this.stackPanel ) {
			return this.stackPanel.getSelectedIndex();
		}
		return -1;
	}
	
	private void selectTab(int index) {
		if ( null != this.tabPanel ) {
			this.tabPanel.selectTab(index);
		}
		if ( null != this.stackPanel ) {
			this.stackPanel.showStack(index);
		}
	}

	
	private void onWidgetsList(String placeId, String applicationId, String widgetId, String optionId,
			ArrayList<org.purewidgets.shared.im.Widget> widgetList) {
		Log.debug(this, "Received widget list" + widgetList.toString());

		/*
		 * Sort the widgets by id
		 */
		//Collections.sort(widgetList);
		
		if ( null == widgetList || widgetList.size() == 0 ) {
			
			//this.mainPanel.clear();
			this.labelNoWidgetFound.setVisible(true);
			this.setPanelVisible(false);
			return;
			
		} else {
			this.setPanelVisible(true);
			this.labelNoWidgetFound.setVisible(false);
			//this.mainPanel.add(this.tabPanel);
		}
			
		if (null != widgetList) {	
			
			// String applicationId = this.currentApplicationId;
			// //widgetList.get(0).getApplicationId();
			Log.debug(this, "Received widgets for application: " + applicationId);
			
			

			for (org.purewidgets.shared.im.Widget widget: widgetList) {
				if ( widget.getWidgetId().equals(widgetId) ) {
					Log.debug(this, "Found widget: " + widget.getWidgetId() );
					this.stackPanel.add( this.getHtmlWidget(widget, optionId) );
					break;
				}
			} 
		

		}

	}
	

	Widget getHtmlWidget(org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		Widget toReturn = null;
		Log.error(this, publicDisplayWidget.getControlType());
		if (publicDisplayWidget.getControlType().equals(
				org.purewidgets.shared.im.Widget.CONTROL_TYPE_ENTRY)) {
			toReturn = getEntryWidget(publicDisplayWidget, optionId);
		} else if (publicDisplayWidget
				.getControlType()
				.equals(org.purewidgets.shared.im.Widget.CONTROL_TYPE_IMPERATIVE_SELECTION)) {
			toReturn = getImperativeWidget(publicDisplayWidget, optionId);
		} else if (publicDisplayWidget.getControlType().equals(
				org.purewidgets.shared.im.Widget.CONTROL_TYPE_DOWNLOAD)) {
			toReturn =  getDownloadWidget(publicDisplayWidget, optionId);
		} else if ( publicDisplayWidget.getControlType().equals(
				org.purewidgets.shared.im.Widget.CONTROL_TYPE_UPLOAD) ) {
			toReturn =  getUploadWidget(publicDisplayWidget, optionId);
		}
		if ( null != toReturn ) {
			//toReturn.setStyleName("widget");
			toReturn.getElement().setPropertyString("id", publicDisplayWidget.getWidgetId());
		}
		return toReturn;
	}

	Widget getUploadWidget(org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		return new UploadWidgetUi(this.uiType, publicDisplayWidget, optionId);
	}
	
	Widget getEntryWidget(org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		return new EntryWidgetUi(this.uiType, publicDisplayWidget, optionId);
//		WidgetOption option = publicDisplayWidget.getWidgetOptions().get(0);
//
//		FlowPanel flowPanel = new FlowPanel();
//		// for ( WidgetOption wo : widgetOptions ) {
//		Label label = new Label(publicDisplayWidget.getShortDescription() + " ["
//				+ option.getReferenceCode() + "]");
//		flowPanel.add(label);
//		TextBox textBox = new TextBox();
//		flowPanel.add(textBox);
//		Button btn = new Button("Submit");
//		flowPanel.add(btn);
//		btn.addClickHandler(new EntryClickHandler(publicDisplayWidget.getPlaceId(), publicDisplayWidget.getApplicationId(), publicDisplayWidget.getWidgetId(), publicDisplayWidget.getWidgetOptions(), textBox));
//		// }
//
//		//flowPanel.getElement().setPropertyString("id", publicDisplayWidget.getWidgetId());
//		return flowPanel;
	}

	Widget getImperativeWidget(
			org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		ArrayList<WidgetOption> widgetOptions = publicDisplayWidget.getWidgetOptions();
		
		if (null != widgetOptions) {
			if (widgetOptions.size() == 1) {
				return getSingleOptionImperativeWidget(publicDisplayWidget, optionId);
			} else {
				return getMultipleOptionImperativeWidget(publicDisplayWidget, optionId);
			}
		} else {
			return null;
		}
	}

	
	Widget getMultipleOptionImperativeWidget(
			org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		return new ImperativeMultipleOptionWidgetUi(this.uiType, publicDisplayWidget, optionId);
//		ArrayList<WidgetOption> widgetOptions = publicDisplayWidget.getWidgetOptions();
//		
//		VerticalPanel panel = new VerticalPanel();
//
//		panel.add(new Label(publicDisplayWidget.getShortDescription()));
//		
//		ListBox listbox = new ListBox();
//		//listbox.addStyleName(style.list());
//		listbox.setVisibleItemCount(Math.min(4, widgetOptions.size()));
//		for (WidgetOption wo : widgetOptions) {
//			listbox.addItem(wo.getShortDescription() + " [" + wo.getReferenceCode() + "]");
//		}
//		
//		
//		panel.add(listbox);
//		
//		Button button = new Button("Send");
//		button.addClickHandler(new MultipleOptionImperativeClickHandler(publicDisplayWidget.getPlaceId(), publicDisplayWidget.getApplicationId(), publicDisplayWidget.getWidgetId(), publicDisplayWidget.getWidgetOptions(), listbox));
//		
//		panel.add(button);
//		
//		return panel;
	}

	Widget getSingleOptionImperativeWidget(
			org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		return new ImperativeSingleOptionWidgetUi(this.uiType, publicDisplayWidget, optionId);
//		ArrayList<WidgetOption> widgetOptions = publicDisplayWidget.getWidgetOptions();
//		WidgetOption option = publicDisplayWidget.getWidgetOptions().get(0);
//
//		FlowPanel flowPanel = new FlowPanel();
//		Label label = new Label(publicDisplayWidget.getLongDescription());
//		flowPanel.add(label);
//
//		Button btn = new Button(publicDisplayWidget.getShortDescription() + " ["
//				+ option.getReferenceCode() + "]");
//		flowPanel.add(btn);
//		btn.addClickHandler(new ImperativeClickHandler( publicDisplayWidget.getPlaceId(), publicDisplayWidget.getApplicationId(), publicDisplayWidget.getWidgetId(), publicDisplayWidget.getWidgetOptions()) );
//		//flowPanel.getElement().setPropertyString("id", publicDisplayWidget.getWidgetId());
//		return flowPanel;
	}

	Widget getDownloadWidget(org.purewidgets.shared.im.Widget publicDisplayWidget, String optionId) {
		return new DownloadWidgetUi(this.uiType, publicDisplayWidget, optionId);
//		WidgetOption option = publicDisplayWidget.getWidgetOptions().get(0);
//
//		FlowPanel flowPanel = new FlowPanel();
//		//flowPanel.add(new Label("Download: "));
//		Anchor a = new Anchor(publicDisplayWidget.getShortDescription() + " ["
//				+ option.getReferenceCode() + "]", publicDisplayWidget.getContentUrl());
//
//		flowPanel.add(a);
//
//		//flowPanel.getElement().setPropertyString("id", publicDisplayWidget.getWidgetId());
//		return flowPanel;
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
