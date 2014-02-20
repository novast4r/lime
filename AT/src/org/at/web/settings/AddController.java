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


@WebServlet("/AddController")
public class AddController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ControllerSettings controllersettings= new ControllerSettings();
		controllersettings.setHostname(request.getParameter("ip"));
		controllersettings.setPort(Long.valueOf(request.getParameter("port")));
		controllersettings.applySettings();
		JSONObject message=new JSONObject();
		message.put("status", "controller modificato");
		PrintWriter out =new PrintWriter(response.getWriter());
		response.setContentType("application/json");
		out.println(message);
	}

}
