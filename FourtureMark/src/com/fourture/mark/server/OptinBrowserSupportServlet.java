package com.fourture.mark.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fourture.mark.ParseUserAgent;

@SuppressWarnings("serial")
public class OptinBrowserSupportServlet extends HttpServlet {
	private static final Logger _logger = Logger.getLogger(OptinBrowserSupportServlet.class
			.getName());


	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		try {
			
			// get IP address
			String addr = req.getRemoteAddr(); 
			 
			String useragent = req.getHeader("User-Agent");
			ParseUserAgent p = new ParseUserAgent( useragent);

			//resp.setContentType("application/json");
			resp.setContentType("text/javascript");
			
			if ( p.getBrowserName().contains("IE")) {
				
				resp.getOutputStream().println( "var divTag = document.createElement('div');");
				resp.getOutputStream().println( "divTag.id = 'modal';");
				resp.getOutputStream().println( "function hidediv(){  document.getElementById(\"modal\").style.visibility = \"hidden\"; }");
				
				resp.getOutputStream().println( "if(!window.ActiveXObject){");
				resp.getOutputStream().println( "	   divTag.setAttribute(\"style\",\"font-family: Verdana, sans-serif; font-weight: bold; padding-left:20px; color: white; font-size:small; display:block; position:absolute; top:0; right:0; width:640; border:1px solid #fffdfc; background-color:#df8283;\");");
				resp.getOutputStream().println( "} else {");
				resp.getOutputStream().println( "divTag.style.setAttribute(\"cssText\", \"font-family: Verdana, sans-serif; font-weight: bold; padding-left:20px; color: white; font-size:x-small; display:block; position:absolute; top:0; right:0; width:640; border:1px solid #fffdfc; background-color:#df8283;\");");
				resp.getOutputStream().println( "}");
				
				resp.getOutputStream().println(" divTag.innerHTML = '<br>Your browser is no longer supported ( " +p.getBrowserName()+ " " + p.getBrowserVersion() + "). Click <a style=\"color:white;\" href=\"BrowserSupport.html\">here</a> for more information on supported browsers and how to install them.<br><br> <a style=\"color:white;\" href=\"JavaScript:hidediv();\">Close</a><br><br>';");
				resp.getOutputStream().println(" document.body.appendChild(divTag);");
			}
			
			resp.getOutputStream().flush();
			resp.getOutputStream().close();	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//GreetingServiceImpl.log = GreetingServiceImpl.log + " " + e.toString();
		}
	}
	
	
//	<style type="text/css">
//	  
//	  div.topright{
//	    display:block;
//	    position:absolute;
//	    top:0;
//	    right:0;
//	    width:350px;		
//	  }
//	</style>
	
	
	
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
