package org.at.floodlight;

import org.at.settings.ControllerSettings;


public class VirtualNetworkController {
	private final String baseurl;
	private final String networkurl;
	private final ControllerSettings settings;

	public VirtualNetworkController() {
		settings = new ControllerSettings();
		baseurl = "http://" + settings.getHostname() + ":" + String.valueOf(settings.getPort()); 
		networkurl = "/networkService/v1.1/tenants/default/networks";
	}
	
	public ControllerSettings getCurrentSettings() {
		return settings;
	}
	
	/* Virtual Networks Management */
	public String getVirtualNetworks() {
		final String url = baseurl + networkurl;
		return RestRequest.get(url);
	}
	
	public String addVirtualNetwork(String id, String name, String gateway) {
		final String url  = baseurl + networkurl + "/" + id;
		final String data = "{\"network\":{"+
				(gateway!=null? "\"gateway\":\""+gateway+"\"," : "") +
				"\"name\":\""+name+"\"}}";
		return RestRequest.put(url, data);
	}
	
	public String removeVirtualNetwork(String id, String name, String gateway) {
		final String url  = baseurl + networkurl + "/" + id;
		final String data = "{\"network\":{"+
				(gateway!=null? "\"gateway\":\""+gateway+"\"," : "") +
				"\"name\":\""+name+"\"}}";
		return RestRequest.delete(url,data);
	}
	
	public String attachToVirtualNetwork(String networkId, String port, String mac) {
		final String url = baseurl + networkurl + "/" + networkId + "/ports/"+port+"/attachment";
		final String data = "{\"attachment\":{\"id\":\""+networkId+"\",\"mac\": \""+mac+"\"}}";
		return RestRequest.put(url,data);
	}
	
	public String detachFromVirtualNetwork(String networkId, String port, String mac) {
		final String url = baseurl + networkurl + "/" + networkId + "/ports/"+port+"/attachment";
		final String data = "{\"attachment\":{\"id\":\""+networkId+"\",\"mac\": \""+mac+"\"}}";
		return RestRequest.delete(url,data);
	}
	
	/* Test application */
	public static void main(String argv[]) {
		VirtualNetworkController controller = new VirtualNetworkController();
//
//		controller.addVirtualNetwork("mynet", "mynet", null);
//		controller.attachToVirtualNetwork("mynet", "1", "52:54:00:6e:b2:86");
//		controller.attachToVirtualNetwork("mynet", "1", "52:54:00:b5:ea:f6");
		System.out.println(controller.getVirtualNetworks());
		//controller.detachFromVirtualNetwork("mynet", "1", "52:54:00:b5:ea:f6");
	}
	
}
