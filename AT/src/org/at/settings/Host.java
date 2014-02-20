package org.at.settings;

public class Host {
	private String name;
	private String hostname;
	private long port;
	
	public Host(String name, String hostname, long port) {
		this.name = name;
		this.hostname = hostname;
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

}
