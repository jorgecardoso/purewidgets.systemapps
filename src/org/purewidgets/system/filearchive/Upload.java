package org.purewidgets.system.filearchive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class Upload extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,
			IOException {

		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);

		String uploadId = blobs.keySet().iterator().next();

		if (null == uploadId) {
			res.setContentType("text/html");
			res.getWriter().print("oops");
			return;
		}

		List<BlobKey> blobKeys = blobs.get(uploadId);

		if (blobKeys == null || blobKeys.size() < 1) {
			res.setContentType("text/html");
			res.getWriter().print("oops");
		} else {
			String blobKey = blobKeys.get(0).getKeyString();
			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
			syncCache.put(uploadId, blobKey);

			res.setContentType("text/html");
			res.getWriter().print("<html><head></head><body>");
			res.getWriter().print(req.getServerName() + "/serve?blob-key=" + blobKey);
			res.getWriter().print("</body></html>");
			// res.sendRedirect("/serveurl?blob-key=" +
			// blobKeys.get(0).getKeyString());
		}
	}
}