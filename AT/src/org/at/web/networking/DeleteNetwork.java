package org.at.web.networking;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.at.floodlight.VirtualNetworkController;

/**
 * Servlet implementation class DeleteNetwork
 */
@WebServlet("/DeleteNetwork")
public class DeleteNetwork extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		PrintWriter out = new PrintWriter(response.getOutputStream());
		response.setContentType("application/json");

		final String netname = request.getParameter("netname");
		final VirtualNetworkController controller = new VirtualNetworkController();		
		
		final String json = controller.removeVirtualNetwork(netname, netname, null);
		out.println(json);
		out.close();

	}

}
