package org.at.web.migration;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.at.libvirt.LiveMigration;
import org.json.JSONObject;

/**
 * Servlet implementation class MigrationStatus
 */
@WebServlet("/MigrationStatus")
public class MigrationStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		PrintWriter out = new PrintWriter(response.getOutputStream());
		response.setContentType("application/json");
		
		LiveMigration m = (LiveMigration)request.getSession().getAttribute("liveMigration");
		long[] stats = m.getDomainJobInfo();
		
		JSONObject state = new JSONObject()
			.put("state", String.valueOf(m.getState()))
			.put("processed", String.valueOf(stats[0]))
			.put("remaining", String.valueOf(stats[1]))
			.put("total", String.valueOf(stats[2]));
		
		out.println(state.toString());
		out.close();
	}

}
