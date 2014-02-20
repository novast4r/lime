package org.at.web.dashboard;

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
 * Servlet implementation class HypervisorInfo
 */
@WebServlet("/HypervisorInfo")
public class HypervisorInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		PrintWriter out = new PrintWriter(response.getOutputStream());
		response.setContentType("application/json");
		
		HostsSettings hostssettings = new HostsSettings();
		
		JSONArray hypervisorList=null;
		try {
			hypervisorList = new JSONArray();
			for(Host host : hostssettings.getHosts()) {
				
				final Connect conn = Libvirt.getHypervisorConnection(host);
				if(conn==null) continue;
				
				//stopped machines
				final String[] domains_inactive_name = conn.listDefinedDomains();

				//active machine and state
				final int[] ids=conn.listDomains();
				final ArrayList<String> domains_active_name = new ArrayList<String>();
				final ArrayList<String> domains_active_state = new ArrayList<String>();
				Domain d=null;
				for(int i=0; i< ids.length; i++){
					d=conn.domainLookupByID(ids[i]);
					domains_active_name.add(d.getName());
					String st;
					int x=d.isActive();
					if(x==1)
						st="running";
					else if(x==0)
						st="stopped";
					else
						st="error";
					domains_active_state.add(st);
					d.free();
				}
				
				//composizione oggetti json
				//hypervisorList (array of hypervisor)
				//hypervisor(name of hypervisor,machines)
				//machines (name o machines,state)

				JSONObject hypervisor = new JSONObject()
				.put("ip",host.getName()+"@"+host.getHostname());
				JSONArray machines = new JSONArray();
				JSONObject vm_state=null;
				//running domains
				for(int k=0; k< domains_active_name.size();k++){
					vm_state= new JSONObject()
					.put("name", domains_active_name.get(k))
					.put("status", domains_active_state.get(k));
					machines.put(vm_state);
				}
				//stopped domains
				for(int j=0; j< domains_inactive_name.length; j++){
					vm_state = new JSONObject()
					.put("name", domains_inactive_name[j])
					.put("status", "stopped");
					machines.put(vm_state);
				}
				hypervisor.put("machines", machines);
				hypervisorList.put(hypervisor);
				
				if(conn.close()!=0) System.out.println("errore chiusura connessione");
			}

		} catch (LibvirtException e) {e.printStackTrace();}
		finally
		{
			System.gc();
		}
		
		out.println(hypervisorList);
		out.close();
	}

}
