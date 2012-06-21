/**
 * 
 */
package org.purewidgets.system.qrcodegenerator.client;

import org.purewidgets.client.application.PublicDisplayApplication;
import org.purewidgets.client.application.PublicDisplayApplicationLoadedListener;
import org.purewidgets.client.im.WidgetManager;
import org.purewidgets.system.qrcodegenerator.client.ui.UiType;
import org.purewidgets.system.qrcodegenerator.client.ui.main.MainScreenUi;

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
public class QrCodeGenerator implements EntryPoint, PublicDisplayApplicationLoadedListener {

	private MainScreenUi mainScreen;
	private UiType uiType;

	@Override
	public void onModuleLoad() {
		PublicDisplayApplication.load(this, "PlaceInteractionWebpage", false);
		
		WidgetManager.get().setAutomaticInputRequests(false);
		
		if ( Window.Location.getPath().contains("index.html") ) {
			this.uiType = UiType.Desktop;
		} else {
			this.uiType = UiType.Smartphone;
		}
		
		
		
		this.mainScreen = new MainScreenUi(this.uiType);
		RootPanel.get().add(this.mainScreen);
		
	}
	
	
	@Override
	public void onApplicationLoaded() {
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
			int indexOfDash = historyToken.indexOf("%-%");
		    if (  indexOfDash < 0 ) { // no dash: this is a place name
				this.mainScreen.showApplicationList( historyToken );
			} else  {
				String placeName = historyToken.substring(0, indexOfDash);
				String applicationName = historyToken.substring(indexOfDash+3);
				this.mainScreen.showWidgets(placeName, applicationName);
			}
		}
		
		
	}
}
