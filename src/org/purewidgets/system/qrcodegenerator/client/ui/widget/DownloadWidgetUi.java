package org.purewidgets.system.qrcodegenerator.client.ui.widget;


import org.purewidgets.system.qrcodegenerator.client.Util;
import org.purewidgets.system.qrcodegenerator.client.ui.UiType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class DownloadWidgetUi extends Composite {

	
	@UiTemplate("DownloadWidgetDesktop.ui.xml")
	interface DownloadUiDesktopUiBinder extends UiBinder<Widget, DownloadWidgetUi> {	}
	private static DownloadUiDesktopUiBinder desktopUiBinder = GWT.create(DownloadUiDesktopUiBinder.class);


	/*
	 * The ui type we will generate
	 */
	private UiType uiType;
	
	@UiField Panel mainPanel;
	@UiField Image qrCodeImage;
	@UiField Label descriptionLabel;

	 
	/* 
	 * Indicates whether we should load the widget icon. This is determined according to the
	 * template being used.
	 */
	private boolean loadWidgetIcon;
	
	private org.purewidgets.shared.widgets.Widget pureWidget;

	public DownloadWidgetUi(UiType uiType, org.purewidgets.shared.widgets.Widget widget) {
		this.uiType = uiType;
		this.pureWidget = widget;
		initWidget(this.getUiBinder(uiType).createAndBindUi(this));
		
		this.initUi();
	}
	
	private void initUi() {
		String description = this.pureWidget.getLongDescription();
		if ( null == description || description.trim().length() == 0) {
			description = this.pureWidget.getShortDescription();
		}
		this.descriptionLabel.setText(description);
	
		this.qrCodeImage.setUrl(Util.getQrCode(this.pureWidget, this.pureWidget.getWidgetOptions().get(0), "300x300"));
	}

	private UiBinder<Widget, DownloadWidgetUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		default:
			return desktopUiBinder;
		}
	}
}
