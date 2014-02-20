package org.at.web.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.at.libvirt.Libvirt;
import org.at.settings.Host;
import org.at.settings.HostsSettings;
import org.json.JSONArray;
import org.json.JSONObject;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;

/**
 * Servlet implementation class VLanInfo
 */
@WebServlet("/VLanInfo")
public class VLanInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = new PrintWriter(response.getOutputStream());
		response.setContentType("application/json");

		
		/* Creo una lista delle macchine configurate nell'Hypervisor */
		ArrayList<String> domainsName= new ArrayList<String>();
		try {
			final HostsSettings s = new HostsSettings();
			for(Host host : s.getHosts()) {
				Connect conn = Libvirt.getHypervisorConnection(host);
				
				//stopped machines
				final String[] domains_inactive_name = conn.listDefinedDomains();
				for(int i=0; i<domains_inactive_name.length; i++){
					if(!domainsName.contains(domains_inactive_name[i])){
						domainsName.add(domains_inactive_name[i]);
					}
				}
				
				//active machine and state
				final int[] ids=conn.listDomains();
				Domain d=null;
				for(int i=0; i< ids.length; i++){
					d=conn.domainLookupByID(ids[i]);
					if(!domainsName.contains(d.getName())){
						domainsName.add(d.getName());
					}
				}
				conn.close();
				
			}
		}catch(LibvirtException e){e.printStackTrace();}
		finally{
			System.gc();
		}
		
		JSONObject vlan = new JSONObject();
		JSONArray machinesarray= new JSONArray();

		for (int i=0; i<domainsName.size(); i++) {
			machinesarray.put(new JSONObject().put("name", domainsName.get(i)));
		}
		vlan.put("machine", machinesarray);
		
		String json = vlan.toString();
		out.println(json);
		out.close();
	}

}
