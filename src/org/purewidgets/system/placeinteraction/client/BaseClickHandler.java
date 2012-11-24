/**
 * 
 */
package org.purewidgets.system.placeinteraction.client;

import java.util.ArrayList;

import org.purewidgets.client.im.WidgetManager;
import org.purewidgets.client.im.json.InputResponseJson;
import org.purewidgets.shared.logging.Log;
import org.purewidgets.shared.im.WidgetInput;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.system.placeinteraction.client.ui.popup.PopupUi;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author "Jorge C. S. Cardoso"
 * 
 */
public abstract class BaseClickHandler implements ClickHandler {

	private Timer timer;
	private PopupUi popup;

	protected String placeName;
	protected String applicationName;
	protected String widgetId;
	protected ArrayList<WidgetOption> widgetOptions;
	
	public BaseClickHandler(String placeName, String applicationName, String widgetId, ArrayList<WidgetOption> widgetOptions,
			PopupUi popup) {
		this.placeName = placeName;
		this.applicationName = applicationName;
		this.widgetId = widgetId;
		this.widgetOptions = widgetOptions;
		
		
		this.popup = popup;
		
		timer = new Timer() {
			@Override
			public void run() {
				timerExpired();
			}
		};
		
	}

	@Override
	public abstract void onClick(ClickEvent event);

	protected void sendInput(WidgetOption widgetOption, String input) {

	

		Log.debug(this, "Sending input: " + input);
		//popup.setText("Sending input from user " + UserInfo.getUsername()+".");
		popup.showProgressIndicator();
		popup.show();
		//popup.setWidth("256px");
		popup.setSize("256px", "350px");
		popup.center();
		//popup.getElement().getStyle().setTop(value, unit)
		//popup.setPopupPosition(Window.getClientWidth()/2-popup.getOffsetWidth()/2, 50);
		

//		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd'T'hh:mm:ss");
//		Date d = new Date();
		
		ArrayList<String>parameters = new ArrayList<String>();
		parameters.add(input);
		WidgetInput widgetInput = new WidgetInput();
		widgetInput.setUserId(UserInfo.getIdentity());
		widgetInput.setNickname(UserInfo.getUsername());
		widgetInput.setInputMechanism("PlaceInteractionWebpage:"+Navigator.getUserAgent());
		widgetInput.setParameters(parameters);
		widgetInput.setWidgetId(this.widgetId);
		widgetInput.setWidgetOptionId(widgetOption.getWidgetOptionId());
		widgetInput.setReferenceCode(widgetOption.getReferenceCode());
		
		
		PlaceInteractionWebpage.getIM().sendWidgetInput(this.placeName, this.applicationName, PlaceInteractionWebpage.APP_ID,
				widgetInput, 
				new AsyncCallback<InputResponseJson>() {

					@Override
					public void onSuccess(InputResponseJson returnValue) {
						Log.debug(this, "Sent! " +returnValue);
						popup.showInfo();
						popup.setIcon(returnValue.getApplication().getApplicationBaseUrl()+"icon.svg");
						popup.setApplicationName(returnValue.getApplication().getApplicationId());
						popup.setPlaceName(returnValue.getPlace().getPlaceName());
						String info;
						if ( returnValue.getApplication().isOnScreen() ) {
							info = " (Check the display to see the result.)";
						} else {
							info = " (Application is not on screen. You may have to wait a bit...)";
						}
						
						popup.setAdditionalInfo(info);

						timer.schedule(6000);
						
					}

					@Override
					public void onFailure(Throwable exception) {
						Log.debug(this, "An error ocurred.");
						popup.showError();
						//popup.setText("\n Oops, an error ocurred!");
						timer.schedule(6000);
						
					}
			
		});

	}

	void timerExpired() {
		if (null != popup && popup.isShowing()) {
			popup.hide();
		}
	}
}
