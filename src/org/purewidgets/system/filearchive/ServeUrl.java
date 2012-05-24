package org.purewidgets.system.filearchive;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * Serves the serve url, i.e., serves the url that can be used to retrieve the file, as jsonp. 
 * To request this url the caller must specify the 'uploadId' url parameter
 * 
 * @author "Jorge C. S. Cardoso"
 *
 */
public class ServeUrl extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String uploadId = req.getParameter("uploadId");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		String blobKey = (String) syncCache.get(uploadId);

		res.setContentType("text/html");
		res.getWriter().print(
				req.getParameter("callback") + "(\"http://" + req.getServerName()
						+ "/filearchive/serve?blob-key=" + blobKey + "\")");
		// blobstoreService.serve(blobKey, res);
	}
}
