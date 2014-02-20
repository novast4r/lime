package org.at.web.settings;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.at.settings.Host;
import org.at.settings.HostsSettings;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class GetHypervisorSettings
 */
@WebServlet("/GetHypervisorSettings")
public class GetHypervisorSettings extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = new PrintWriter(response.getOutputStream());
		response.setContentType("application/json");
		
		JSONArray hypervisorList = new JSONArray();
		
		HostsSettings settings = new HostsSettings();
		for (Host h:settings.getHosts())
		{
			JSONObject hypervisor = new JSONObject()
			.put("hostname",h.getHostname())
			.put("username", h.getName())
			.put("port", h.getPort());
			hypervisorList.put(hypervisor);
		}
		
		out.println(hypervisorList);
		out.close();
	}

}
