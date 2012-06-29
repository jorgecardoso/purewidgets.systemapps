package org.purewidgets.system.qrcodeinteractor.client;

import org.purewidgets.client.im.InteractionManagerService;

public class Util {
	public static String APP_ID = "QRCodeInteractor";
	
	private static InteractionManagerService interactionManager;
	
	public static void setIM(InteractionManagerService im) {
		interactionManager = im;
	}
	
	public static InteractionManagerService getIM() {
		return interactionManager;
	}
	
}
