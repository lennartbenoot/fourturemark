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

import com.mincko.googleutil.GoogleUtil;

@SuppressWarnings("serial")
public class WebSiteKickServlet extends HttpServlet {
	private static final Logger _logger = Logger
			.getLogger(WebSiteKickServlet.class.getName());

	/**
	 * 
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		try {
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
			resp.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER");
			resp.setHeader("Access-Control-Max-Age", "1728000");			
			
			// get IP address
			String addr = req.getRemoteAddr();
			String lang = req.getParameter("lang");
			String debug = req.getParameter("debug"); // if debug is enabled, page will not redirect
			if (lang == null)
				lang = "nl";
			String useragent = req.getHeader("User-Agent");
			ParseUserAgent p = new ParseUserAgent(useragent);

			resp.setContentType("text/javascript");

			// IE Browser Check
			if (p.getBrowserName().equals("MSIE")) {

				if (p.getBrowserVersion() < 9.0f)
					resp.getOutputStream().println(createFunction(lang, "IE"));

				else if (p.getBrowserVersion() == 11.0f)
					resp.getOutputStream().println(createFunction(lang, "IE11"));
				
				else {
					String ret = generateBrowsenOKCode( debug);
					resp.getOutputStream().print(  ret);
				}
				
			}
			// Firefox Browser < 6 Check
			else if ((p.getBrowserName().equals("Firefox"))
					&& (p.getBrowserVersion() < 6.0f)) {

				resp.getOutputStream().println(createFunction(lang, "FF"));
			} 
			//else if ((p.getBrowserName().equals("Firefox"))
			//		&& (p.getBrowserVersion() == 16.0f)) {
			//
			//	resp.getOutputStream().println(createFunction(lang, "FF16"));
			//}
			else {
				String ret = generateBrowsenOKCode( debug);
				resp.getOutputStream().print(  ret);
			}

			resp.getOutputStream().flush();
			resp.getOutputStream().close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// GreetingServiceImpl.log = GreetingServiceImpl.log + " " +
			// e.toString();
		}
	}
	
	public String generateBrowsenOKCode( String debug){
		
		String ret = "function websitekick( param1, param2, param3) {" 
			+ generateBenchmarkServletHtml()
			+ (debug == null ?  "window.location.href = 'https://www.mysecurexhrservices.eu';" : "alert('redirect not called due to debug mode');")
			+ " return true;}";
		
		return ret;
		
	}

	public String createFunction(String lang, String actualCase) {

		String message = "";

		if ("nl".equals(lang)) {
			if ("IE".equals(actualCase))
				message = GoogleUtil
						.readGoogleDocCached("1yTe5vfEfM8tVaZ43l5LG1-CFVCauc6DtnIYqNtdlGvY");
			if ("IE11".equals(actualCase))
				message = GoogleUtil
						.readGoogleDocCached("1mviHpZaB9jsEBpBJ0wKAPpBPGHMpbEYPND8k2lbnJZ0");
			if ("FF".equals(actualCase))
				message = GoogleUtil
						.readGoogleDocCached("1Q1gT4xM3sQsWR3JxE0xcYpZ3tLXhs9Rnl8PNOjR0EMw");
			if ("FF16".equals(actualCase))
				message = GoogleUtil
						.readGoogleDocCached("18SXX86EE4f7q-576e1nJN_ET_gfZ1ZN_yKDy4H4KrXk");
		}
		if ("fr".equals(lang)) {
			if ("IE".equals(actualCase))
				message = GoogleUtil
						.readGoogleDocCached("1ifi3bjxqmvqZenpUf_jIbFrUnQnAAl06h0VWpYhacwc");
			if ("IE11".equals(actualCase))
				message = GoogleUtil
						.readGoogleDocCached("1IAJIqSSmPNAZ77uyuLHunh176WqMsg8FqHBFjZPL7jQ");
			if ("FF".equals(actualCase))
				message = GoogleUtil
						.readGoogleDocCached("1AN1ZrRvcJj-oA4e0g-8pJTeewVnzKT2AUdhLueG7ppY");
			if ("FF16".equals(actualCase))
				message = GoogleUtil
						.readGoogleDocCached("1RX0za0OUXTBLxb49-lVISZ1NHg6CoDtU_Va7IKwxasM");
		}

		String str = "";
		str = "function websitekick( param1, param2, param3) {\n"
				+ generateBenchmarkServletHtml()
				+ "if  (document.getElementById('websitekickmodal') != null ) return false;\n"
				+ "if ( param1 == null) {alert('Please specify param1'); return false; }\n"
				+ "if ( param2 == null) {alert('Please specify param2'); return false; }\n"
				+ "if ( param3 == null) {alert('Please specify param3'); return false; }\n"
				+ "var divTag = document.createElement('div');\n"
				+ "divTag.id = 'websitekickmodal';\n"
				+ "function hidediv(){  document.getElementById(\"websitekickmodal\").style.visibility = \"hidden\";}\n"

				+ "if(!window.ActiveXObject){\n"
				+ "	divTag.setAttribute(\"style\",\"font-family: Verdana, sans-serif; padding-left:20px; color: black; font-size:small; margin-left:auto; margin-right:auto; float:center; display:block; width:640px; border:1px solid #000000; background-color:#FFFFFF;\");\n"
				+ "} else {\n"
				+ "	divTag.style.setAttribute(\"cssText\", \"font-family: Verdana, sans-serif; padding-left:20px; color: black; font-size:small; margin-left:auto; margin-right:auto; float:center; display:block; width:640px; border:1px solid #000000; background-color:#FFFFFF;\");\n"
				+ "}\n"
				+ " divTag.innerHTML = '<br><div style=\"Float:right;\"><img src=\"https://fourturemark.appspot.com/images/logo_secu.gif\"></div><div style=\"margin-left: auto; margin-right: auto;text-align: left;\">"
				+ message + "</div><br>';\n"
				+ "divTag.style.visibility = 'hidden';\n"
				+ " document.body.appendChild(divTag);" + " return false; }\n";

		return str;
	}

	public String generateBenchmarkServletHtml(){
//	return "";
		return 
		"var screenResolution = screen.width + \" X \" + screen.height;\n" +
		"var colorDepth = screen.colorDepth;\n" +
		"var javaScriptEnabled = \"Yes\";\n" +
		"var xmlhttp; \n" +
		"var xdr; function xdronload() { if (xdr.responseText == 'true') window.location.href = 'https://www.mysecurexhrservices.eu'; else document.getElementById('websitekickmodal').style.visibility = 'visible';  } \n" +
		" if (window.XMLHttpRequest) { \n" +
		"	xmlhttp=new XMLHttpRequest(); }\n" +
		" else \n" +
		"{ \n" +
		"	xmlhttp=new ActiveXObject(\"Microsoft.XMLHTTP\");\n" +
		"}\n"   +
		" if ( navigator.appName == 'Microsoft Internet Explorer')\n {" +
		"		xdr = new XDomainRequest();\n" +
		"		xdr.open(\"POST\",\"http://fourturemark.appspot.com/cron/benchmarkServlet?param1=\"+param1+\"&param2=\"+param2+\"&param3=\"+param3+\"&screenResolution=\"+screenResolution+\"&colorDepth=\"+colorDepth+\"&score1=0&score2=0&score3=0\",false);\n" +
		//"		xdr.open(\"POST\",\"http://localhost:8888/cron/benchmarkServlet?param1=\"+param1+\"&param2=\"+param2+\"&param3=\"+param3+\"&screenResolution=\"+screenResolution+\"&colorDepth=\"+colorDepth+\"&score1=0&score2=0&score3=0\",false);\n" +
		"		xdr.onload = xdronload;" +
		"		xdr.send();\n" +		
		"}\n" +
		" else if ( navigator.appName == 'Netscape'){\n" +
		"	xmlhttp.open(\"GET\",\"http://fourturemark.appspot.com/cron/benchmarkServlet?param1=\"+param1+\"&param2=\"+param2+\"&param3=\"+param3+\"&screenResolution=\"+screenResolution+\"&colorDepth=\"+colorDepth+\"&score1=0&score2=0&score3=0\", false);\n"
		//"	xmlhttp.open(\"GET\",\"http://localhost:8888/cron/benchmarkServlet?param1=\"+param1+\"&param2=\"+param2+\"&param3=\"+param3+\"&screenResolution=\"+screenResolution+\"&colorDepth=\"+colorDepth+\"&score1=0&score2=0&score3=0\",false);\n"
		+ "	xmlhttp.send();\n"
		+ " if (document.getElementById('websitekickmodal') != null ) document.getElementById('websitekickmodal').style.visibility = 'visible';"
		+ "}\n" + 
		"else {\n" +
		"	xmlhttp.open(\"GET\",\"http://fourturemark.appspot.com/cron/benchmarkServlet?param1=\"+param1+\"&param2=\"+param2+\"&param3=\"+param3+\"&screenResolution=\"+screenResolution+\"&colorDepth=\"+colorDepth+\"&score1=0&score2=0&score3=0\", true);\n"
		//"xmlhttp.open(\"GET\",\"http://localhost:8888/cron/benchmarkServlet?param1=\"+param1+\"&param2=\"+param2+\"&param3=\"+param3+\"&screenResolution=\"+screenResolution+\"&colorDepth=\"+colorDepth+\"&score1=0&score2=0&score3=0\",false);\n"
		+ "xmlhttp.send();\n"
		+ "}";
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
