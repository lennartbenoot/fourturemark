package com.fourture.mark.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.Label;

import com.fourture.mark.Mark;
import com.fourture.mark.PMF;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

@SuppressWarnings("serial")
public class QueryMark extends HttpServlet {

	private static final Logger logger = Logger.getLogger(QueryMark.class
			.getName());

	
	private static final Logger log = Logger.getLogger(QueryMark.class.getName());
	public HashMap<String, HashMap<String, String>> headerTranslation = new HashMap<String, HashMap<String, String>>();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		try {
			
			String lang = (String) req.getParameter("lang");
			String condStr = "";
			Map params = new HashMap();
			
			//set 1
			String p1 = (String) req.getParameter("p1"); //param
			String v1 = (String) req.getParameter("v1"); //value
			
			//set 2
			String p2 = (String) req.getParameter("p2"); //param
			String v2 = (String) req.getParameter("v2"); //value
			
			//set 3
			String p3 = (String) req.getParameter("p3"); //param
			String v3 = (String) req.getParameter("v3"); //value
				
			//building the condition and parameters
			if(p1!=null){
				condStr = condStr +  p1 + "== param1 ";
				params.put("param1", v1);
			}
			if(p2!=null){
				condStr = condStr + " && "+ p2 + " == param2 ";
				params.put("param2",v2);
			}
			if(p3!=null){
				condStr = condStr +" && "+ p3 + " == param3 ";
				params.put("param3", v3);
			}
			
			// check if user is already measured or not
			PersistenceManager pm;
			pm = PMF.get().getPersistenceManager();
			//Query query = pm.newQuery(com.fourture.mark.Mark.class);
			Query query = pm.newQuery(com.fourture.mark.Mark.class, condStr);
//			query.setOrdering( "timestamp descending");

			//List<Mark> results = (List<Mark>) query.execute();
			List<Mark> results = (List<Mark>) query.executeWithMap(params);
			String tableRows,tableHeader;
			tableHeader = tableRows  = ""; 
			int count = 0; 
			if(results.isEmpty()){
				resp.getOutputStream().print("no_result_found");
			}
			else{
				Iterator iter = results.iterator();
				Mark mark;
				resp.getOutputStream().print("<table class='queryResult'>");
				ArrayList<String> fieldList = getFieldList();
				while (iter.hasNext()) {
					mark = (Mark) iter.next();
					String cssClass = "table_rowodd";
					if(count%2==0) cssClass = "table_roweven";
					tableRows+="<tr class='"+ cssClass +"'>";
					Iterator iterField = fieldList.iterator();
					while (iterField.hasNext()) {
						String mname = (String) iterField.next();
						System.out.println( mname);
						Method m = mark.getClass().getDeclaredMethod (mname);
						Object o = m.invoke(mark);
						
						if(count==0){
							String headerText = mname.substring(3);
							if(headerTranslation.get(mname)!=null)
								headerText = headerTranslation.get(mname).get(lang) != null ? headerTranslation.get(mname).get(lang) : mname.substring(3); 
							tableHeader += "<th class='table_th'>" + headerText + "</th>";
						}
						if ( o != null)
							tableRows += "<td>"+  o.toString() +"</td>";
						else
							tableRows += "<td></td>";
						
					}
					count++;
					tableRows+="</tr>";
				}
				resp.getOutputStream().print("<thead><tr>"+tableHeader+"</td><thead>"); 
				resp.getOutputStream().print("<tbody>"+tableRows+"</tbody>");
				resp.getOutputStream().print("</table>"); 
			}
			
			pm.close();
			resp.setContentType("text/html");
			resp.getOutputStream().flush();
			resp.getOutputStream().close();
		} catch (Exception e) {
			
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			
			log.severe( e.getMessage() + stacktrace);
			
			e.printStackTrace();
			resp.setContentType("text/html");
			resp.getOutputStream().flush();
			resp.getOutputStream().close();
		}

	}
	//a list of method, to show, & ordering
	public ArrayList getFieldList(){
		ArrayList<String> fieldList = new ArrayList<String>();
		
		fieldList.add("getTimestamp");
		fieldList.add("getBrowserName");
		/*translating the field header*/
		HashMap<String, String> langField = new HashMap();
		langField.put("en","Browser Name");
		langField.put("fr","Navigateur");
		langField.put("nl","Browser");
		headerTranslation.put(fieldList.get(fieldList.size()-1), langField);
		
		fieldList.add("getBrowserVersion");
		fieldList.add("getParam1");
		fieldList.add("getParam2");
		fieldList.add("getParam3");
		fieldList.add("getScore1");
		fieldList.add("getScore2");
		fieldList.add("getPlatform");
		fieldList.add("getPlatformVersion");
		fieldList.add("getIpAddress");
		fieldList.add("getDevice");
		fieldList.add("isJavascriptEnabled");
		fieldList.add("getFlashVersion");
		fieldList.add("getScreenResolution");
		fieldList.add("getColorDepth");
		fieldList.add("getClient");
		return fieldList;
	}
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}

