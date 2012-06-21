/**
 * 
 */
package org.purewidgets.system.placeinteraction.client;

import java.util.ArrayList;

import org.purewidgets.client.im.WidgetManager;
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
		popup.setText("Sending input from user " + UserInfo.getUsername()+".");
		popup.show();
		
		popup.setPopupPosition(Window.getClientWidth()/2-popup.getOffsetWidth()/2, 50);
		

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
		
		
		WidgetManager.get().sendWidgetInput(this.placeName, this.applicationName, widgetInput, 
				new AsyncCallback<WidgetInput>() {

					@Override
					public void onSuccess(WidgetInput returnValue) {
						Log.debug(this, "Sent!");
						popup.setText(popup.getText() + "\n Sent!");
						timer.schedule(2000);
						
					}

					@Override
					public void onFailure(Throwable exception) {
						Log.debug(this, "An error ocurred.");
						popup.setText(popup.getText() + "\n Oops, an error ocurred!");
						timer.schedule(5000);
						
					}
			
		});

	}

	void timerExpired() {
		if (null != popup && popup.isShowing()) {
			popup.hide();
		}
	}
}
