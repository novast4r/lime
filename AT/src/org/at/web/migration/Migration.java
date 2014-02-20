package org.at.web.migration;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.at.libvirt.LiveMigration;


/**
 * Servlet implementation class Migration
 */
@WebServlet("/Migration")
public class Migration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		PrintWriter out = new PrintWriter(response.getOutputStream());
		response.setContentType("application/json");
		String name  = request.getParameter("vmname");
		String srcip = request.getParameter("srcip");
		String dstip = request.getParameter("dstip");
		
		LiveMigration m = new LiveMigration(name, srcip, dstip);
		request.getSession().setAttribute("liveMigration", m);
		
		m.migrate();
		
		out.println("{}");
		out.close();
	}

}
