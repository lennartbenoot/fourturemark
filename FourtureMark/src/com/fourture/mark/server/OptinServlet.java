package com.fourture.mark.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.fourture.mark.Mark;
import com.fourture.mark.PMF;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;

@SuppressWarnings("serial")
public class OptinServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(OptinServlet.class
			.getName());

	public boolean firstrun = true;
	public String header_nl = "";
	public String header_fr = "";
	public String popup_nl = "";
	public String popup_fr = "";
	public Boolean active = true; 
	public HashMap<String, HashMap<String, String>> inclusionList = new HashMap<String, HashMap<String, String>>();
	private static final Logger log = Logger.getLogger(OptinServlet.class.getName());
	
	
	private String readFile(String filename) {
		String str = "";
		String requestUrl = "http://fourturemark.appspot.com/"+filename;
		//String requestUrl = "http://localhost:8888/" + filename;
		try {
			URL url = new URL(requestUrl.toString());
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				str = str + inputLine + "\n";
			}
			in.close();

			return str;
		} catch (IOException e) {
			e.printStackTrace();
			return "Error reading files";
		}
	}

	private String readFile2(String filename) {
		String str = "";
		Random rand = new Random();
		String requestUrl = "http://fourturemark.appspot.com/" + filename + "?rand=" + rand.nextInt();
		//String requestUrl = "http://localhost:8888/" + filename;
		try {
			URL url = new URL(requestUrl.toString());
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				inputLine = forHTML(inputLine);
				str = str + inputLine;
			}
			in.close();

			str = str.replace("/n", "");

			return str;
		} catch (IOException e) {
			e.printStackTrace();
			return "Error reading files";
		}
	}

	public String forHTML(String aText) {
		aText = aText.replace("è", "&egrave;");
		aText = aText.replace("é", "&eacute;");
		aText = aText.replace("à", "&agrave;");
		aText = aText.replace("\'", "&#39;");
		aText = aText.replace("’", "&#39;");
		aText = aText.replace("–", "&#45;");

		return aText;
	}

	public void readInclusionList() throws IOException {
		String str1 = "";
		inclusionList = new HashMap<String, HashMap<String, String>>();

		String resourceId = "0ArgYPVDBv3F1dEwtY2VyOE5JTTZfNEhJTzVSbG9sT3c";

		Random rand = new Random();
		String requestUrl = "https://spreadsheets.google.com/pub?key="
				+ resourceId + "&hl=nl&output=csv&rand=" + rand.nextInt();
		URL url = new URL(requestUrl);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				url.openStream()));
		String inputLine;

		// read header
		inputLine = in.readLine();
		String[] tokens;
		if ( inputLine.contains("Deactivate"))
			active = false;
		else
			active = true;

		while ((inputLine = in.readLine()) != null) {

			if (inputLine.equals(""))
				continue;
			tokens = inputLine.split(",");

			HashMap<String, String> run;
			run = inclusionList.get(tokens[0]);
			if (run == null)
				run = new HashMap<String, String>();

			run.put(tokens[1], "");
			inclusionList.put(tokens[0], run);
		}

	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		try {
			
			MemcacheService  cache = MemcacheServiceFactory.getMemcacheService();
						
			String reloaded = "";
			
			String param1 = (String) req.getParameter("param1"); //test
			String param2 = (String) req.getParameter("param2"); //klant
			String param3 = (String) req.getParameter("param3"); //user
			String lang = (String) req.getParameter("lang");
			String refresh = (String) req.getParameter("refresh");

			// default is true, but following code can move this to false.
			boolean showOptin = true;
			
			// detect case where servlet is reloaded but values are in context
			if ( firstrun == true) {
				
				if ( cache.get("header_fr") != null )
				{
					firstrun = false;
					header_fr = (String)cache.get("header_fr");
					header_nl = (String)cache.get("header_nl");
					popup_nl = (String)cache.get("popup_nl");
					popup_fr = (String)cache.get("popup_fr");
					inclusionList = (HashMap<String, HashMap<String, String>>) cache.get("inclusionList");
					active = (Boolean) cache.get("active");
					//reloaded = "alert('Reloaded from Context');";
				}
			}
			
			if ("true".equals(refresh)) {
				log.info("Initiating refresh...");
				firstrun = true;
				showOptin = false;
			}

			if ("norun".equals(param1))
				showOptin = false;
		
			
			// safety valve: if one of the headers == "", reload
			if ( "".equals( header_fr) || "".equals( header_nl) || "".equals( popup_nl) || "".equals( popup_fr) )
				firstrun = true;
			
			if (firstrun) {
				header_fr = header_nl = readFile("header.html");
				popup_nl = readFile2("popup_nl.html");
				popup_fr = readFile2("popup_fr.html");
				header_fr = header_fr.replace("${popup}", popup_fr);
				header_nl = header_nl.replace("${popup}", popup_nl);
				readInclusionList();
				
				cache.put("header_nl", header_nl);
				cache.put("header_fr", header_fr);
				cache.put("popup_nl", popup_nl);
				cache.put("popup_fr", popup_fr);
				cache.put("inclusionList", inclusionList);
				cache.put("active", active);
				
				firstrun = false;
				log.info( "Refresh executed correctly");
			}

			// check if inclusion is active, if so, check if user should be
			// measured or not
			HashMap<String, String> run = inclusionList.get(param1);
			if (run != null) {
				// inclusion is active for this run
				if (run.get(param3) == null)
					showOptin = false;
			}

			// check if user is already measured or not
			PersistenceManager pm;
			pm = PMF.get().getPersistenceManager();

			Query query = pm.newQuery("select from com.fourture.mark.Mark "
					+ "where param1 == param1Param && param3 == param3Param "
					+ "parameters String param1Param, String param3Param "
					);

			List<Mark> results = (List<Mark>) query.execute( param1, param3);
			if (!results.isEmpty())
				showOptin = false;
			
			pm.close();

			resp.setContentType("text/html");

			// write document
			if ("alwaysrun".equals(param1)) showOptin = true;
			
			// final overwriting parameter
			if (active == false) showOptin = false;
			
			if (showOptin)
				if ("fr".equals(lang))
					resp.getOutputStream().print( reloaded + header_fr);
				else
					resp.getOutputStream().print( reloaded + header_nl);

			reloaded = "";
			
			resp.getOutputStream().flush();
			resp.getOutputStream().close();
		} catch (Exception e) {
			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			
			log.severe( e.getMessage() + stacktrace);
			
			// fully reset
			firstrun = true;
			MemcacheServiceFactory.getMemcacheService().clearAll();
			
			e.printStackTrace();
			resp.setContentType("text/html");
			resp.getOutputStream().flush();
			resp.getOutputStream().close();
		}

	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
