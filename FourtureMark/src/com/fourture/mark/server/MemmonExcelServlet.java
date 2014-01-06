package com.fourture.mark.server;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
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
import com.fourture.mark.Memmon;
import com.fourture.mark.PMF;

@SuppressWarnings("serial")
public class MemmonExcelServlet extends HttpServlet {
	private static final Logger _logger = Logger
			.getLogger(MemmonExcelServlet.class.getName());



	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		OutputStream out = null;
		try {

			resp.setContentType("application/vnd.ms-excel");

			resp.setHeader("Content-Disposition",
					"attachment; filename=Memmon.xls");

			String param1 = req.getParameter("param1");
			
			WritableWorkbook w = Workbook
					.createWorkbook(resp.getOutputStream());
			WritableSheet s = w.createSheet("Demo", 0);

			
			// register result
			PersistenceManager pm;
			pm = PMF.get().getPersistenceManager();
			
			Query query;
			
			pm.currentTransaction().begin();
			if ( param1 != null){
				query = pm.newQuery(com.fourture.mark.Memmon.class, "param1==param");
				query.declareParameters("String param");
			}
			else {
				query = pm.newQuery(com.fourture.mark.Memmon.class );
			}
			query.setOrdering("timestamp");

			Collection allObjects = (Collection) query.execute( "OB201101");
			
			// pm.deletePersistentAll( allObjects);
			pm.currentTransaction().commit();

			Iterator iter = allObjects.iterator();

			Memmon memmon;
			int i=0;
			while (iter.hasNext()) {
				memmon = (Memmon) iter.next();
				s.addCell(new Label(0, i, memmon.getId().toString()));
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");      
				s.addCell(new Label(1, i, memmon.getTimestamp() == null ? "" : sdf.format( memmon.getTimestamp())));
				sdf = new SimpleDateFormat("HH:mm");      
				s.addCell(new Label(2, i, memmon.getTimestamp() == null ? "" : sdf.format( memmon.getTimestamp())));
				
				//s.addCell( new jxl.write.DateT);
				s.addCell(new Label(3, i, memmon.getIpAddress()));
				s.addCell(new Label(4, i, memmon.getHostname()));
				s.addCell(new Label(5, i, memmon.getProcessName()));
				s.addCell(new Label(6, i, memmon.getProcessID()));
				s.addCell(new Label(7, i, memmon.getMemoryUsage()));
		
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
