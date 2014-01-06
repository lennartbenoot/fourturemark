package com.fourture.mark.server;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fourture.mark.Memmon;
import com.fourture.mark.MemmonVisit;
import com.fourture.mark.PMF;
import com.fourture.mark.ParseUserAgent;

@SuppressWarnings("serial")
public class MemmonServlet extends HttpServlet {
	private static final Logger _logger = Logger.getLogger(MemmonServlet.class
			.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String memmonStrP = req.getParameter("p");
		String memmonStrU = req.getParameter("u");

		PersistenceManager pm;
		pm = PMF.get().getPersistenceManager();

		// Script data
		if (memmonStrP != null) {
			String memmonA[] = memmonStrP.split(",");

			Memmon memmon = new Memmon();

			java.util.Date now = Calendar.getInstance().getTime();
			memmon.setTimestamp(now);
			memmon.setIpAddress(req.getRemoteAddr());
			memmon.setHostname(memmonA[0]);
			memmon.setProcessName(memmonA[1]);
			memmon.setProcessID(memmonA[2]);
			String memUsageStr = memmonA[5].replaceAll("\\.", "");
			memmon.setMemoryUsage(memUsageStr);

			pm.makePersistent(memmon);
		}

		// Browser plugin data
		if (memmonStrU != null) {
			MemmonVisit visit = new MemmonVisit();

			java.util.Date now = Calendar.getInstance().getTime();
			visit.setTimestamp(now);
			visit.setIpAddress(req.getRemoteAddr());
			visit.setUrl(memmonStrU);

			pm.makePersistent(visit);
		}
		pm.close();

	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
