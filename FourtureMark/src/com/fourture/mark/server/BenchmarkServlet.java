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
public class BenchmarkServlet extends HttpServlet {
	private static final Logger _logger = Logger
			.getLogger(BenchmarkServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		try {
			
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
			resp.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER");
			resp.setHeader("Access-Control-Max-Age", "1728000");
			
			// save results
			Mark mark = new Mark();

			String useragent = req.getHeader("User-Agent");
			ParseUserAgent p = new ParseUserAgent( useragent);
			
			mark.setUserAgentString(useragent);
			
			mark.setClient((String) req.getParameter("client"));
			mark.setParam1((String) req.getParameter("param1"));
			mark.setParam2((String) req.getParameter("param2"));
			mark.setParam3((String) req.getParameter("param3"));
			
			if ( req.getParameter("browserName") == null) 
				mark.setBrowserName( p.getBrowserName());
			else
				mark.setBrowserName((String) req.getParameter("browserName"));
			if ( req.getParameter("browserVersion") == null)
				mark.setBrowserVersion( Float.toString( p.getBrowserVersion()) );
			else
				mark.setBrowserVersion((String) req.getParameter("browserVersion"));

			mark.setColorDepth((String) req.getParameter("colorDepth"));
			mark.setDevice((String) req.getParameter("device"));
			mark.setFlashVersion((String) req.getParameter("flashVersion"));
			
			if (req.getParameter("ipAddress") == null)
				mark.setIpAddress( req.getRemoteAddr() );
			else
				mark.setIpAddress((String) req.getParameter("ipAddress"));
			mark.setJavascriptEnabled( "Yes".equals( req.getParameter("javascriptEnabled")));
					
			if ( req.getParameter("platform") == null) 
				mark.setPlatform( p.getPlatform());
			else
				mark.setPlatform((String) req.getParameter("platform"));
			
			if ( req.getParameter("platformVersion") == null) 
				mark.setPlatformVersion( p.getPlatformVersion());
			else
				mark.setPlatformVersion((String) req.getParameter("platformVersion"));
			
			mark.setScreenResolution((String) req.getParameter("screenResolution"));

			// score
			String score1_S = (String) req.getParameter("score1");
			mark.setScore1(Math.round(Float.parseFloat(score1_S)));
			java.util.Date now = Calendar.getInstance().getTime();
			mark.setTimestamp(now);

			// score
			String score2_S = (String) req.getParameter("score2");
			mark.setScore2(Math.round(Float.parseFloat(score2_S)));
			
			// register result
			PersistenceManager pm;
			pm = PMF.get().getPersistenceManager();

			pm.makePersistent(mark);
			pm.close();
			
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
