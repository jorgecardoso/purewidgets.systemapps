package org.purewidgets.system.qrcodegenerator.client;

import org.purewidgets.shared.widgetmanager.WidgetOption;
import org.purewidgets.shared.widgets.Widget;

public class Util {
	public static String getQrCode(Widget widget, WidgetOption option, String qrCodeSize) {
		//TODO: This URL should be in some config setting
	String data = "http://pw-systemapps.appspot.com/qrcodeinteractor/index.html?place=" + widget.getPlaceId() + 
			"&app="+widget.getApplicationId() + "&widget=" + widget.getWidgetId()
			+"&type="+widget.getControlType() + "&opid=" + option.getWidgetOptionId();
	
	String unencodedUrl = data;
	
	data = com.google.gwt.http.client.URL.encode(data);
	data = data.replaceAll(";", "%2F");
	data = data.replaceAll("/", "%2F");
	data = data.replaceAll(":", "%3A");
	data = data.replaceAll("\\?", "%3F");
	data = data.replaceAll("&", "%26");
    data = data.replaceAll("\\=", "%3D");
    data = data.replaceAll("\\+", "%2B");
    data = data.replaceAll("\\$", "%24");
    data = data.replaceAll(",", "%2C");
    data = data.replaceAll("#", "%23");
	  
    
	String url = "https://chart.googleapis.com/chart?cht=qr&chs="+qrCodeSize+"&chl="+data;
	return url;
	}
}
