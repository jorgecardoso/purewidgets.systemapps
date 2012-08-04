/**
 * 
 */
package org.purewidgets.system.qrcodegenerator.client;

import org.purewidgets.client.im.InteractionManagerService;
import org.purewidgets.client.storage.LocalStorage;
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
public class QrCodeGenerator implements EntryPoint {

	private MainScreenUi mainScreen;
	private UiType uiType;

	@Override
	public void onModuleLoad() {
			
		
		if ( Window.Location.getPath().contains("index.html") ) {
			this.uiType = UiType.Desktop;
		} else {
			this.uiType = UiType.Smartphone;
		}
		
		String interactionManagerServer = com.google.gwt.user.client.Window.Location.getParameter("imurl");
		if ( null == interactionManagerServer ) {
			interactionManagerServer = "http://pw-interactionmanager.appspot.com";
		}
		
		Util.setIM(new InteractionManagerService(interactionManagerServer, 
				new LocalStorage(Util.APP_ID)) );
		
		
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
