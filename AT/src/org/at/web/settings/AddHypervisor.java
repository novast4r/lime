package org.at.web.settings;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.at.settings.Host;
import org.at.settings.HostsSettings;
import org.json.JSONObject;


@WebServlet("/AddHypervisor")
public class AddHypervisor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String ip 	= request.getParameter("ip");
		String port = request.getParameter("port");
		
		Host host2add=new Host(username, ip, Long.valueOf(port));
		HostsSettings hostsettings = new HostsSettings();
		List<Host> hosts = null;
		hosts=hostsettings.getHosts();
		boolean find=false;
		JSONObject message=new JSONObject();
		for(int i=0; i< hosts.size(); i++){
			if (hosts.get(i).getHostname().equals(ip))
				find=true;
		}
		if(!find){
			message.put("status", "hypervisor aggiunto alla lista");
			hostsettings.addNewHost(host2add);
		}
		else
			message.put("status", "hypervisor gia' presente nella lista");
		PrintWriter out =new PrintWriter(response.getWriter());
		response.setContentType("application/json");
		out.println(message);
	}
	
}
