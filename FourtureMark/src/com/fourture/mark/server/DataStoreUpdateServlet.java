package com.fourture.mark.server;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fourture.mark.Mark;
import com.fourture.mark.PMF;
import com.fourture.mark.ParseUserAgent;

@SuppressWarnings("serial")
public class DataStoreUpdateServlet extends HttpServlet {
	
	private static final Logger _logger = Logger
			.getLogger(DataStoreUpdateServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		OutputStream out = null;
		try {

			resp.setContentType("text/html");
			
			String param1 = req.getParameter("param1");
			String startdate = req.getParameter("startdate");
			String enddate = req.getParameter("enddate");
			String update = req.getParameter("update");
			
			if ( (startdate == null) ||  ("".equals( startdate)))
				startdate = "01/05/2013";
			if ( (enddate == null) ||  ("".equals( enddate)))
				enddate = "31/12/2023";

			// register result
			PersistenceManager pm;
			pm = PMF.get().getPersistenceManager();
			
			Query query;
			
			pm.currentTransaction().begin();
			Collection allObjects;
			
			query = pm.newQuery(com.fourture.mark.Mark.class );
			query.setOrdering("timestamp");
			query.setFilter( "timestamp > param && timestamp < param2");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date date = sdf.parse( startdate);
			Date endDate = sdf.parse( enddate);
			query.declareParameters("java.util.Date param, java.util.Date param2");
			allObjects = (Collection) query.execute( date, endDate);

			pm.currentTransaction().commit();

			Iterator iter = allObjects.iterator();

			Mark mark;
			int i=0;
			while (iter.hasNext()) {
				mark = (Mark) iter.next();
				
				ParseUserAgent p = new ParseUserAgent( mark.getUserAgentString());
					
				boolean different = false;
				if  (!mark.getBrowserName().equals( p.getBrowserName())) different = true;
				if  (!mark.getBrowserVersion().equals( String.valueOf(p.getBrowserVersion()))) different = true;
				if  (!mark.getPlatform().equals( p.getPlatform())) different = true;
				if  (!mark.getPlatformVersion().equals( p.getPlatformVersion())) different = true;
				
				resp.getWriter().append( 
						(different ? "<font color=\"red\">" : "") + 
						different +
						mark.getTimestamp() + ":" + 
						mark.getBrowserName() + ":" + 
						mark.getBrowserVersion() + ":" + 
						mark.getPlatform() + ":" + 
						mark.getPlatformVersion() + ":" +
						" ---> " +
						p.getBrowserName() + ":" +
						p.getBrowserVersion() + ":" + 
						p.getPlatform() + ":" + 
						p.getPlatformVersion() + ":" +
						"(" + mark.getUserAgentString() + ")" +
						(different ? "</font>" : "" ) + 
						"<br>");
				
				if ( (different) && "true".equals( update)) {
					mark.setBrowserName( p.getBrowserName());
					mark.setBrowserVersion( String.valueOf( p.getBrowserVersion()));
					mark.setPlatform( p.getPlatform());
					mark.setPlatformVersion( p.getPlatformVersion());
					
					pm.currentTransaction().begin();
					pm.makePersistent( mark);
					pm.currentTransaction().commit();
				}
			}

			//pm.currentTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
