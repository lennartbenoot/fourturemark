package com.fourture.mark.server;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fourture.mark.Mark;
import com.fourture.mark.PMF;
import com.fourture.mark.ParseUserAgent;

@SuppressWarnings("serial")
public class GCFServlet extends HttpServlet {
	private static final Logger _logger = Logger
			.getLogger(GCFServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		try {
			
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
			resp.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER");
			resp.setHeader("Access-Control-Max-Age", "1728000");
			
			String useragent = req.getHeader("User-Agent");
			ParseUserAgent p = new ParseUserAgent( useragent);
			
			if (useragent.contains("chromeframe")) 
				resp.getOutputStream().print("true");
			else
				resp.getOutputStream().print("false");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// GreetingServiceImpl.log = GreetingServiceImpl.log + " " +
			// e.toString();
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
