package org.purewidgets.system.filearchive;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class Test extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String f = " <form action=\"" + blobstoreService.createUploadUrl("filearchive/upload")
				+ "\" method=\"post\" enctype=\"multipart/form-data\">";
		f += "<input type=\"text\" name=\"foo\">";
		f += "<input type=\"file\" name=\"myFile\">";
		f += "<input type=\"submit\" value=\"Submit\">";
		f += "</form>";
		res.getWriter().print(f);
	}
}