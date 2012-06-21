package org.purewidgets.system.qrcodegenerator.client.ui.widget;

import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.system.qrcodegenerator.client.Util;
import org.purewidgets.system.qrcodegenerator.client.ui.UiType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ImperativeMultipleOptionWidgetUi extends Composite {

	
	@UiTemplate("ImperativeMultipleOptionWidgetDesktop.ui.xml")
	interface ImperativeMultipleOptionUiDesktopUiBinder extends UiBinder<Widget, ImperativeMultipleOptionWidgetUi> {	}
	private static ImperativeMultipleOptionUiDesktopUiBinder desktopUiBinder = GWT.create(ImperativeMultipleOptionUiDesktopUiBinder.class);
	
	
	/*
	 * The ui type we will generate
	 */
	private UiType uiType;
	
	@UiField VerticalPanel mainPanel;
	
	@UiField Label descriptionLabel;

	
	/*
	 * Indicates whether we should load the widget icon. This is determined according to the
	 * template being used.
	 */
	private boolean loadWidgetIcon;
	
	private org.purewidgets.shared.im.Widget pureWidget;

	public ImperativeMultipleOptionWidgetUi(UiType uiType, org.purewidgets.shared.im.Widget widget) {
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
		
		this.mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE); 
		
		for (WidgetOption wo : this.pureWidget.getWidgetOptions() ) {
			Image img = new Image(Util.getQrCode(this.pureWidget, wo, "300x300"));
			Label lbl = new Label(wo.getShortDescription());
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			hPanel.add(img);
			hPanel.add(lbl);
			mainPanel.add(hPanel);
		}
		
//		this.actionButton.addClickHandler(new MultipleOptionImperativeClickHandler(this.pureWidget.getPlaceId(), this.pureWidget.getApplicationId(), 
//				this.pureWidget.getWidgetId(), this.pureWidget.getWidgetOptions(), this.optionsListBox, new PopupUi(this.uiType)));
////		
	}

	private UiBinder<Widget, ImperativeMultipleOptionWidgetUi> getUiBinder(UiType uiType) {
		switch ( uiType ) {
		
		
		default:
			return desktopUiBinder;
		}
	}
	

	

}
