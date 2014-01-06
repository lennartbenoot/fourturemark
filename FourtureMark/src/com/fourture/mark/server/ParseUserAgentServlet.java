package com.fourture.mark.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fourture.mark.ParseUserAgent;

@SuppressWarnings("serial")
public class ParseUserAgentServlet extends HttpServlet {
	private static final Logger _logger = Logger.getLogger(ParseUserAgentServlet.class
			.getName());


	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		try {
			
			// get IP address
			String addr = req.getRemoteAddr(); 
			 
			String useragent = req.getHeader("User-Agent");
			ParseUserAgent p = new ParseUserAgent( useragent);

			//resp.setContentType("application/json");
			resp.setContentType("text/html");
			
			resp.getOutputStream().println("{");
			resp.getOutputStream().println("\"browserName\": \""+ p.getBrowserName() + "\",");
			resp.getOutputStream().println("\"browserVersion\": \""+ p.getBrowserVersion() + "\",");
			resp.getOutputStream().println("\"platform\": \""+ p.getPlatform() + "\",");
			resp.getOutputStream().println("\"platformVersion\": \""+ p.getPlatformVersion() + "\",");
			resp.getOutputStream().println("\"device\": \""+ p.getDevice() + "\",");
			resp.getOutputStream().println("\"javascriptEnabled\": \"null\",");
			resp.getOutputStream().println("\"screenResolution\": \"null\",");
			resp.getOutputStream().println("\"colorDepth\": \"null\",");
			resp.getOutputStream().println("\"flashVersion\": \"null\",");
			resp.getOutputStream().println("\"ip\": \""+ addr + "\"");
			
			resp.getOutputStream().println("}");
			
			resp.getOutputStream().flush();
			resp.getOutputStream().close();	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//GreetingServiceImpl.log = GreetingServiceImpl.log + " " + e.toString();
		}
	}
	
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
