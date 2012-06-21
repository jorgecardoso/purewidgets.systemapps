package org.purewidgets.system.placeinteraction.client;

import java.util.ArrayList;

import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.system.placeinteraction.client.ui.popup.PopupUi;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.ListBox;

public class MultipleOptionImperativeClickHandler extends BaseClickHandler {

	private ListBox listbox;
	
	public MultipleOptionImperativeClickHandler(String placeName, String applicationName, String widgetId, ArrayList<WidgetOption> widgetOptions, ListBox listbox,
			PopupUi popup) {
		super(placeName, applicationName, widgetId, widgetOptions, popup);
		this.listbox = listbox;
	}
	
	
	@Override
	public void onClick(ClickEvent event) {
		WidgetOption referenceCode;
		int selected = listbox.getSelectedIndex();
		if ( selected >= 0 ) {
			referenceCode = this.widgetOptions.get(selected);
			this.sendInput(referenceCode, "");
		}
	}

}
