package org.purewidgets.system.placeinteraction.client.ui.widget;

import org.purewidgets.client.widgets.ReferenceCodeFormatter;
import org.purewidgets.shared.im.WidgetParameter;
import org.purewidgets.shared.widgets.Download;
import org.purewidgets.system.placeinteraction.client.ImperativeClickHandler;
import org.purewidgets.system.placeinteraction.client.ui.UiType;
import org.purewidgets.system.placeinteraction.client.ui.popup.PopupUi;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class DownloadWidgetUi extends Composite {

	
	@UiTemplate("DownloadWidgetDesktop.ui.xml")
	interface DownloadUiDesktopUiBinder extends UiBinder<Widget, DownloadWidgetUi> {	}
	private static DownloadUiDesktopUiBinder desktopUiBinder = GWT.create(DownloadUiDesktopUiBinder.class);

	
	@UiTemplate("DownloadWidgetSmartphone.ui.xml")
	interface DownloadUiSmartphoneUiBinder extends UiBinder<Widget, DownloadWidgetUi> {	}
	private static DownloadUiSmartphoneUiBinder smartphoneUiBinder = GWT.create(DownloadUiSmartphoneUiBinder.class);
	
	
	/*
	 * The ui type we will generate
	 */
	private UiType uiType;
	
	@UiField Panel mainHorizontalPanel;
	@UiField Image iconImage;
	@UiField Label descriptionLabel;
	@UiField Button actionButton;
	 
	/* 
	 * Indicates whether we should load the widget icon. This is determined according to the
	 * template being used.
	 */
	private boolean loadWidgetIcon;
	
	private org.purewidgets.shared.im.Widget pureWidget;

	public DownloadWidgetUi(UiType uiType, org.purewidgets.shared.im.Widget widget) {
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
		this.actionButton.setText(this.pureWidget.getShortDescription() + " " + ReferenceCodeFormatter.format(this.pureWidget.getWidgetOptions().get(0).getReferenceCode()) );
		
		if ( this.loadWidgetIcon ) {
			String url = this.pureWidget.getWidgetOptions().get(0).getIconUrl();
			if (null != url) {
				this.iconImage.setUrl(url);
			} else {
				this.iconImage.removeFromParent();
			}
		} else {
			this.iconImage.removeFromParent();
		}
		
		this.actionButton.addClickHandler(new ImperativeClickHandler( this.pureWidget.getPlaceId(), this.pureWidget.getApplicationId(), 
				this.pureWidget.getWidgetId(), this.pureWidget.getWidgetOptions(), new PopupUi(this.uiType)) );
		
		this.actionButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for ( WidgetParameter parameter : DownloadWidgetUi.this.pureWidget.getWidgetParameters() ) {
					if ( parameter.getName().equals(Download.CONTENT_URL_PARAMETER_NAME) ) {
						Window.open(parameter.getValue(), "_blank", "");
						break;
					}
				}
				
				
			}
			
		});
	}

	private void updateUi() {
		String description = this.pureWidget.getLongDescription();
		if ( null == description || description.trim().length() == 0) {
			description = this.pureWidget.getShortDescription();
		}
		this.descriptionLabel.setText(description);
		this.actionButton.setText(this.pureWidget.getShortDescription() + " " + ReferenceCodeFormatter.format(this.pureWidget.getWidgetOptions().get(0).getReferenceCode()) );
		
		if ( this.loadWidgetIcon ) {
			String url = this.pureWidget.getWidgetOptions().get(0).getIconUrl();
			if (null != url) {
				this.iconImage.setUrl(url);
			} else {
				this.iconImage.removeFromParent();
			}
		} 
		
	}
	
	private UiBinder<Widget, DownloadWidgetUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		case Desktop:
			this.loadWidgetIcon = true;
			return desktopUiBinder;
		case Smartphone:
			this.loadWidgetIcon = false;
			return smartphoneUiBinder;
		default:
			return desktopUiBinder;
		}
	}

	public void update(org.purewidgets.shared.im.Widget widget) {
		this.pureWidget = widget;
		this.updateUi();
		
	}


	
}
