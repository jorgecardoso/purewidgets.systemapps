package org.purewidgets.system.filearchive;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.purewidgets.shared.logging.Log;


import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

/**
 * Creates the temporary upload url and serves it as jsonp
 */
public class ServePostUrl extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String uploadUrl = blobstoreService.createUploadUrl("/filearchive/upload");
		
		//uploadUrl.replace("", req.getParameter("callback"));
		Log.info(this, "Serving upload URL: " + uploadUrl);
		
		res.setContentType("text/html");
		res.getWriter().print(
				req.getParameter("callback") + "(\"" + uploadUrl
						+ "\")");
	}
}
