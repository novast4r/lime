package org.at.settings;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerSettings {

	private String hostname = null;
	private long port = 0;
	
	public ControllerSettings() {
		Database db = new Database();
		db.connect();
		ResultSet rs = db.selectController();
		try {
			if(rs.next()) {
				hostname = rs.getString("ip");
				port = Long.valueOf(rs.getString("port"));
			}
		rs.close();
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		} finally {
			db.disconnect();
		}
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public long getPort() {
		return port;
	}

	public void setPort(long port) {
		this.port = port;
	}
	
	public void applySettings() {
		Database db = new Database();
		db.connect();
		db.deleteController();
		db.insertController(hostname, String.valueOf(port));
		db.disconnect();
	}
}
