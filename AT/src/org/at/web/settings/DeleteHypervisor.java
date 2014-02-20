package org.at.web.settings;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.at.settings.HostsSettings;
import org.json.JSONObject;


@WebServlet("/DeleteHypervisor")
public class DeleteHypervisor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//get parameter
		String hostname=request.getParameter("hostname");
		
		//delete host
		HostsSettings hostssettings=new HostsSettings();
		hostssettings.deleteHostByHostname(hostname);
		
		//print the responsehypervisor
		PrintWriter out = new PrintWriter(response.getOutputStream());
		response.setContentType("application/json");
		JSONObject json = new JSONObject();
   		json.put("status"," eliminato con successo" );
        
        out.println(json);
		out.close();
		
	}

	
}
