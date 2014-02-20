package org.at.web.settings;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.at.settings.ControllerSettings;
import org.json.JSONObject;

/**
 * Servlet implementation class GetControllerSettings
 */
@WebServlet("/GetControllerSettings")
public class GetControllerSettings extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = new PrintWriter(response.getOutputStream());
		response.setContentType("application/json");
		
		ControllerSettings controller = new ControllerSettings();
		JSONObject json;
		if(controller.getHostname()!=null)
		{
			json = new JSONObject()
				.put("hostname",controller.getHostname())
				.put("port", controller.getPort())
				.put("ui_url", "http://"+controller.getHostname()+":8080/ui/index.html");
		} else {
			json = new JSONObject()
				.put("hostname", "Non configurato")
				.put("port", "Non configurato")
				.put("ui_url", "Non configurato");
		}
		
		out.println(json);
		out.close();
		
	}

}
