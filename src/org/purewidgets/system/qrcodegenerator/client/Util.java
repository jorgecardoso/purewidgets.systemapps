package org.purewidgets.system.qrcodegenerator.client;

import org.purewidgets.client.im.InteractionManagerService;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.im.Widget;

import com.google.gwt.core.client.GWT;

public class Util {
	public static String APP_ID = "QRCodeGenerator";
	public static String TOKEN_SEPARATOR = "%25-%25";
	
	private static String systemAppsUrl;
	
	private static InteractionManagerService interactionManager;
	
	public static void setIM(InteractionManagerService im) {
		interactionManager = im;
	}
	
	public static InteractionManagerService getIM() {
		return interactionManager;
	}
	public static String getQrCode(Widget widget, WidgetOption option, String qrCodeSize) {
		//TODO: This URL should be in some config setting
	
	String data = "http://"+ com.google.gwt.user.client.Window.Location.getHost() +"/placeinteraction/index.html#" + widget.getPlaceId() + 
			TOKEN_SEPARATOR +widget.getApplicationId() + TOKEN_SEPARATOR + widget.getWidgetId()
			+TOKEN_SEPARATOR + option.getWidgetOptionId();
	
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

	/**
	 * @return the systemAppsUrl
	 */
	public static String getSystemAppsUrl() {
		return systemAppsUrl;
	}

	/**
	 * @param systemAppsUrl the systemAppsUrl to set
	 */
	public static void setSystemAppsUrl(String systemAppsUrl) {
		systemAppsUrl = systemAppsUrl;
	}
}
