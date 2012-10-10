package org.purewidgets.system.placeinteraction.client.ui.popup;

import org.purewidgets.system.placeinteraction.client.ui.UiType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopupUi extends PopupPanel{

	@UiTemplate("PopupUiDesktop.ui.xml")
	interface PopupUiDesktopUiBinder extends UiBinder<Widget, PopupUi> {}
	private static PopupUiDesktopUiBinder desktopUiBinder = GWT.create(PopupUiDesktopUiBinder.class);

	private UiType uiType;

	@UiField 
	Label messageApplicationName;
	
	@UiField 
	Label messagePlaceName;
	
	@UiField
	DeckPanel deckPanelMain;
	
	@UiField
	Image imgApplicationIcon;
			
	public PopupUi(UiType uiType) {
		this.uiType = uiType;
		setWidget(this.getUiBinder(uiType).createAndBindUi(this));
		super.removeStyleName(super.getStylePrimaryName());
	}
	
	private UiBinder<Widget, PopupUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		
		case Desktop:
			return desktopUiBinder;

		default:
			return desktopUiBinder;
		}
	}
	
	public void setIcon(String url) {
		this.imgApplicationIcon.setUrl(url);
		
	}
	public void showProgressIndicator() {
		this.deckPanelMain.showWidget(0);
	}
	
	public void showInfo() {
		this.deckPanelMain.showWidget(1);
	}
	
	public void showError() {
		this.deckPanelMain.showWidget(2);
	}

	public void setPlaceName(String text) {
		this.messagePlaceName.setText(text);
	}

	public void setApplicationName(String text) {
		this.messageApplicationName.setText(text);
	}

}
