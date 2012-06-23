package org.purewidgets.system.qrcodeinteractor.client;

import org.purewidgets.client.im.InteractionManager;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.im.Widget;

public class Util {
	public static String APP_ID = "QRCodeInteractor";
	
	private static InteractionManager interactionManager;
	
	public static void setIM(InteractionManager im) {
		interactionManager = im;
	}
	
	public static InteractionManager getIM() {
		return interactionManager;
	}
	
}
