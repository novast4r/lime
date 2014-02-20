package org.at.web.networking;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.at.floodlight.VirtualNetworkController;
import org.at.libvirt.Libvirt;

/**
 * Servlet implementation class AttachMachine
 */
@WebServlet("/AttachMachine")
public class AttachMachine extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		PrintWriter out = new PrintWriter(response.getOutputStream());
		response.setContentType("application/json");
		
		final String vlan = request.getParameter("vlan");
		final String machine = request.getParameter("machine");
		final VirtualNetworkController controller = new VirtualNetworkController();
		final Libvirt libvirt = new Libvirt();
		
		final String json = controller.attachToVirtualNetwork(
				vlan, "1", libvirt.getMacAddressByName(machine));
		
		out.println(json);
		
		out.close();
	}

}
