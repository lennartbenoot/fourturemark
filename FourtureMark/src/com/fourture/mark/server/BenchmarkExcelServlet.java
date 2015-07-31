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

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.fourture.mark.Mark;
import com.fourture.mark.PMF;

@SuppressWarnings("serial")
public class BenchmarkExcelServlet extends HttpServlet {
	private static final Logger _logger = Logger
			.getLogger(BenchmarkExcelServlet.class.getName());



	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		OutputStream out = null;
		try {

			resp.setContentType("application/vnd.ms-excel");

			resp.setHeader("Content-Disposition",
					"attachment; filename=SecurexTestResults.xls");

			String param1 = req.getParameter("param1");
			String startdate = req.getParameter("startdate");
			String enddate = req.getParameter("enddate");
			
			if ( (startdate == null) ||  ("".equals( startdate)))
				startdate = "01/05/2013";
			
			WritableWorkbook w = Workbook
					.createWorkbook(resp.getOutputStream());
			WritableSheet s = w.createSheet("Demo", 0);

			
			// register result
			PersistenceManager pm;
			pm = PMF.get().getPersistenceManager();
			
			Query query;
			
			pm.currentTransaction().begin();
			Collection allObjects;
			
//			if ( (param1 == null) ||  ("".equals( param1))){
//				query = pm.newQuery(com.fourture.mark.Mark.class, "param1==param");
//				query.setOrdering("timestamp");
//				query.declareParameters("String param");
//				allObjects = (Collection) query.execute( param1);
//			}
//			else {
			if (enddate == null ){
				query = pm.newQuery(com.fourture.mark.Mark.class );
				query.setOrdering("timestamp");
				query.setFilter( "timestamp > param");
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date startdateD = sdf.parse( startdate);
				query.declareParameters("java.util.Date param");
				allObjects = (Collection) query.execute( startdateD);
			}
			else {
				query = pm.newQuery(com.fourture.mark.Mark.class );
				query.setOrdering("timestamp");
				query.setFilter( "timestamp > param && timestamp < param2");
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date startdateD = sdf.parse( startdate);
				Date enddateD = sdf.parse( enddate);
				query.declareParameters("java.util.Date param, java.util.Date param2");
				allObjects = (Collection) query.execute( startdateD, enddateD);				
								
			}
			
			
			// pm.deletePersistentAll( allObjects);
			pm.currentTransaction().commit();

			Iterator iter = allObjects.iterator();

			Mark mark;
			int i=0;
			while (iter.hasNext()) {
				mark = (Mark) iter.next();
				s.addCell(new Label(0, i, mark.getId().toString()));
				s.addCell(new Label(1, i, mark.getTimestamp() == null ? "" : mark.getTimestamp().toString()));
				s.addCell(new Label(2, i, mark.getIpAddress()));
				s.addCell(new Label(3, i, mark.getParam1()));
				s.addCell(new Label(4, i, mark.getParam2()));
				s.addCell(new Label(5, i, mark.getParam3()));
				s.addCell( new Label(6, i, mark.getBrowserName()));
				s.addCell( new Label(7, i, mark.getBrowserVersion()));
				s.addCell( new Label(8, i, mark.getPlatform()));
				s.addCell( new Label(9, i, mark.getPlatformVersion()));			
				s.addCell( new Label(10, i, mark.getScreenResolution()));
				s.addCell(new Label(11, i, String.valueOf( mark.getScore1())));
						
				i++;
			}

			w.write();
			w.close();

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
