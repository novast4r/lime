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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class NetworkInfo
 */
@WebServlet("/NetworkInfo")
public class NetworkInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = new PrintWriter(response.getOutputStream());
		response.setContentType("application/json");
		
		/* Recupero informazioni sulla rete dal controller */
		VirtualNetworkController controller = new VirtualNetworkController();
		if(controller.getCurrentSettings().getHostname()==null) {
			/* Nessun controller settato */
			out.println("{\"vlan\":[]}");
			out.close();
			return;
		}
		JSONArray network = new JSONArray(controller.getVirtualNetworks());

		
		/* Recupero le impostazioni degli hypervisors */
		Libvirt libvirt = new Libvirt();
		
		/* Costruzione dell'oggetto json di risposta */
		JSONObject jresponse = new JSONObject().put("vlan", new JSONArray());
		JSONArray vlanarray = jresponse.getJSONArray("vlan");

		for(int i=0; i<network.length(); i++) {
			JSONObject vlan = new JSONObject();
			vlan.put("name", network.getJSONObject(i).get("name"));
			vlan.put("gateway",network.getJSONObject(i).get("gateway"));
			
			JSONArray vmarray = new JSONArray();
			
			for(int j=0; j<network.getJSONObject(i).getJSONArray("mac").length(); j++) {
				String mac= network.getJSONObject(i).getJSONArray("mac").getString(j);
				JSONObject vmachine = new JSONObject()
					.put("name", libvirt.getNameByMacAddress(mac))
					.put("mac", mac);
				
				vmarray.put(vmachine);
			}
			vlan.put("vmachine", vmarray);
			vlanarray.put(vlan);
		}		

		String json = jresponse.toString();
		out.println(json);
		out.close();
		System.gc();
	}

}
