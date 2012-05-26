package org.purewidgets.system.qrcodeinteractor.client.ui.popup;



import org.purewidgets.system.qrcodeinteractor.client.ui.UiType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopupUi extends PopupPanel implements HasText {

	@UiTemplate("PopupUiDesktop.ui.xml")
	interface PopupUiDesktopUiBinder extends UiBinder<Widget, PopupUi> {}
	private static PopupUiDesktopUiBinder desktopUiBinder = GWT.create(PopupUiDesktopUiBinder.class);

	private UiType uiType;
	@UiField Label messageLabel;
	
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

	@Override
	public String getText() {
		return this.messageLabel.getText();
	}

	@Override
	public void setText(String text) {
		this.messageLabel.setText(text);
	}

}
