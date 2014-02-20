package org.at.settings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HostsSettings {
	private List<Host> hosts;
	
	public HostsSettings() {
		hosts = new ArrayList<Host>();
		Database db = new Database();
		db.connect();
		ResultSet rs = db.selectAllHost();
		try {
			while(rs.next()) {
				String name = rs.getString("nome");
				String hostname = rs.getString("ip");
				long port = Long.valueOf(rs.getString("port"));
				hosts.add(new Host(name, hostname, port));
			}
			rs.close();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Host> getHosts() {
		return hosts;
	}
	
	public Host getHostByHostname(String hostname) {
		for(Host h : hosts)
			if(h.getHostname().equals(hostname))
				return h;
		return null;
	}
	
	public void addNewHost(Host h) {
		Database db = new Database();
		db.connect();
		db.insertHost(h.getName(),h.getHostname(), String.valueOf(h.getPort()));
		db.disconnect();
	}
	
	public void deleteHostByHostname(String hostname) {
		Database db = new Database();
		db.connect();
		db.deleteHost(hostname);
		db.disconnect();
	}
}
