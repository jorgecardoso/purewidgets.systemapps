package org.purewidgets.system.qrcodeinteractor.client;

import java.util.ArrayList;

import org.purewidgets.shared.widgetmanager.WidgetOption;
import org.purewidgets.system.qrcodeinteractor.client.ui.popup.PopupUi;


import com.google.gwt.event.dom.client.ClickEvent;

public class ImperativeClickHandler extends  BaseClickHandler {
	private String referenceCode;
	
	
	public ImperativeClickHandler(String placeName, String applicationName, String widgetId, ArrayList<WidgetOption> widgetOptions,
			PopupUi popup) {
		super(placeName, applicationName, widgetId, widgetOptions, popup);
		this.referenceCode = referenceCode;
		
	}
	
	
	@Override
	public void onClick(ClickEvent event) {
		
		
		//this.sendInput("tag." + this.referenceCode);
		this.sendInput(widgetOptions.get(0), "");
	}

	
}
