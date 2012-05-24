package org.purewidgets.system.placeinteraction.client;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * The client side stub for the RPC service.
 */

public interface SightingService extends RemoteService {
	
	
	void sighting(String s, String date);
}
